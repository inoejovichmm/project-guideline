package com.google.research.guideline.guidance

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.research.guideline.engine.NativeEngineFragment
import com.google.research.guideline.util.permissions.RequiredPermissionsHelper

/** Main Guideline activity.  */
class GuidanceActivity : AppCompatActivity() {
    private val permissionsHelper = RequiredPermissionsHelper(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsHelper.checkRequiredPermissions(this, REQUIRED_PERMISSIONS) {
            createFragment()
        }
    }

    private fun createFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, NativeEngineFragment())
            .commitNow()
    }

    override fun onStop() {
        super.onStop()
        // Finish the activity so it will be re-created from scratch the next time.
        finish()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}