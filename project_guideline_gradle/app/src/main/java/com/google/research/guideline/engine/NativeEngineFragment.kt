package com.google.research.guideline.engine

import android.annotation.SuppressLint
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.FatalException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.google.protobuf.util.Durations
import com.google.research.guideline.R
import com.google.research.guideline.proto.AudioSystemOptions
import com.google.research.guideline.proto.ClearanceZoneOptions
import com.google.research.guideline.proto.FrameBasedOccupancyMapOptions
import com.google.research.guideline.proto.FrameBasedPointCloudOptions
import com.google.research.guideline.proto.GuidanceSystemOptions
import com.google.research.guideline.proto.GuidelineAggregatorOptions
import com.google.research.guideline.proto.GuidelineEngineConfig
import com.google.research.guideline.proto.LegacySoundPackOptions
import com.google.research.guideline.proto.LegacySoundPackOptions.LegacySoundPackType
import com.google.research.guideline.proto.LocalTemporalRegressionBasedGuidelineAggregatorOptions
import com.google.research.guideline.proto.OccupancyMapOptions
import com.google.research.guideline.proto.PointCloudOptions
import java.util.Objects
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/** Fragment for the Guideline Native Engine.  */
class NativeEngineFragment : Fragment() {
    private val started = AtomicBoolean()
    private val glSurfaceViewRenderer: GLSurfaceView.Renderer = GLSurfaceViewRenderer()
    private var engine: GuidelineNativeEngine? = null
    private var glSurfaceView: GLSurfaceView? = null
    private var partialWakeLock: WakeLock? = null
    private var arcoreInstallRequested = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.native_engine_layout, container,  /* attachToRoot= */false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val configBuilder: GuidelineEngineConfig.Builder = GuidelineEngineConfig.newBuilder()
        configBuilder.setGuidanceSystemOptions(
            GuidanceSystemOptions.newBuilder()
                .setEagerStopThreshold(EAGER_STOP_THRESHOLD)
                .setEnableObstacleDetection(true)
                .setObstacleOnlyMode(ENABLE_OBSTACLE_ONLY_MODE)
                .setCameraHeightMeters(CAMERA_HEIGHT_METERS)
        )
        configBuilder.setOccupancyMapOptions(
            OccupancyMapOptions.newBuilder()
                .setFrameBasedOccupancyMapOptions(
                    FrameBasedOccupancyMapOptions.newBuilder()
                        .setOccupancyThreshold(OBSTACLE_OCCUPANCY_THRESHOLD.toFloat())
                        .setClearanceZoneOptions(
                            ClearanceZoneOptions.newBuilder()
                                .setWidth(2 * CLEARANCE_ZONE_HALF_WIDTH_METERS)
                                .setDepth(CLEARANCE_ZONE_DEPTH_METERS) // The bottom/top parameters need to be relative to camera height.
                                .setBottom(CLEARANCE_ZONE_BOTTOM_METERS - CAMERA_HEIGHT_METERS)
                                .setTop(CLEARANCE_ZONE_TOP_METERS - CAMERA_HEIGHT_METERS)
                        )
                )
        )
        configBuilder.setGuidelineAggregatorOptions(
            GuidelineAggregatorOptions.newBuilder()
                .setLocalTemporalRegressionBasedGuidelineAggregatorOptions(
                    LocalTemporalRegressionBasedGuidelineAggregatorOptions.getDefaultInstance()
                )
        )
        configBuilder.setPointCloudOptions(
            PointCloudOptions.newBuilder()
                .setFrameBasedPointCloudOptions(
                    FrameBasedPointCloudOptions.newBuilder()
                        .setSubsampleStep(POINT_CLOUD_SUBSAMPLE_STEP)
                )
        )
        configBuilder.setAudioSystemOptions(
            AudioSystemOptions.newBuilder()
                .setLegacySoundPackOptions(
                    LegacySoundPackOptions.newBuilder()
                        .setType(LegacySoundPackType.V4_2)
                        .setWarningThresholdMeters(SOUND_LANE_WIDTH_METERS)
                        .setMaxRotationDegrees(SOUND_MAX_ROTATION_DEGREES)
                        .setSensitivityCurvature(SOUND_SENSITIVITY_CURVATURE)
                )
        )
        engine = GuidelineNativeEngine.create(requireContext(), configBuilder.build())
        val powerManager = requireContext().getSystemService(PowerManager::class.java)
        partialWakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "guideline:partial")
        glSurfaceView = view.findViewById(R.id.gl_surface_view)
        glSurfaceView?.apply {
            preserveEGLContextOnPause = true
            setEGLContextClientVersion(2)
            setEGLConfigChooser(true)
            setRenderer(glSurfaceViewRenderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
            setWillNotDraw(false)
        }
    }

    @SuppressLint("WakelockTimeout")
    override fun onStart() {
        super.onStart()
        partialWakeLock!!.acquire()
    }

    override fun onStop() {
        super.onStop()
        partialWakeLock!!.release()
    }

    override fun onResume() {
        super.onResume()
        try {
            val installStatus: ArCoreApk.InstallStatus = ArCoreApk.getInstance()
                .requestInstall(requireActivity(), !arcoreInstallRequested)

            if (installStatus == ArCoreApk.InstallStatus.INSTALL_REQUESTED) {
                arcoreInstallRequested = true
                return
            }
        } catch (e: UnavailableDeviceNotCompatibleException) {
            Log.e(TAG, "Device incompatible with ARCore", e)
            Toast.makeText(context, R.string.arcore_incompatible_error, Toast.LENGTH_LONG).show()
            return
        } catch (e: UnavailableUserDeclinedInstallationException) {
            Log.e(TAG, "ARCore installation declined", e)
            Toast.makeText(context, R.string.arcore_declined_error, Toast.LENGTH_LONG).show()
            return
        } catch (e: FatalException) {
            Log.e(TAG, "Failed to install ARCore", e)
            Toast.makeText(context, R.string.arcore_fatal_error, Toast.LENGTH_LONG).show()
            return
        }
        engine!!.start()
        glSurfaceView!!.onResume()
        started.set(true)
    }

    override fun onPause() {
        super.onPause()
        started.set(false)
        engine!!.stop()
        glSurfaceView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (engine != null) {
            engine!!.destroy()
            engine = null
        }
    }

    private inner class GLSurfaceViewRenderer : GLSurfaceView.Renderer {
        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            GLES20.glClearColor( /* red= */0.1f,  /* green= */
                0.1f,  /* blue= */
                0.1f,  /* alpha= */
                1.0f
            )
            engine!!.onGlSurfaceCreated()
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            GLES20.glViewport( /* x= */0,  /* y= */0, width, height)
            engine!!.onGlViewportChanged(width, height)
        }

        override fun onDrawFrame(gl: GL10) {
            // Clears screen to notify driver it should not load any pixels from previous frame.
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            if (!started.get()) {
                return
            }
            engine!!.onGlDrawFrame()
        }
    }

    companion object {
        private const val TAG = "NativeEngineFragment"

        // Obstacle-only mode will only detect obstacles and not guideline navigation.
        private const val ENABLE_OBSTACLE_ONLY_MODE = false

        // Stop signal given after this time if no line detected in camera frame.
        private val EAGER_STOP_THRESHOLD = Durations.fromMillis(750)

        // Estimated height of camera above ground (approx. waist height).
        private const val CAMERA_HEIGHT_METERS = 1.0f
        private const val CLEARANCE_ZONE_HALF_WIDTH_METERS = 2
        private const val CLEARANCE_ZONE_DEPTH_METERS = 4

        // The bottom/top are expressed in meters above the ground.
        private const val CLEARANCE_ZONE_BOTTOM_METERS = 0.8f
        private const val CLEARANCE_ZONE_TOP_METERS = 2.0f
        private const val OBSTACLE_OCCUPANCY_THRESHOLD = 10
        private const val POINT_CLOUD_SUBSAMPLE_STEP = 4
        private const val SOUND_LANE_WIDTH_METERS = 4.0f
        private const val SOUND_MAX_ROTATION_DEGREES = 30.0f
        private const val SOUND_SENSITIVITY_CURVATURE = 0.4f
    }
}
