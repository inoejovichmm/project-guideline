package com.google.research.guideline.util.permissions

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.research.guideline.R


/** Helper for checking that required runtime permissions have been granted.  */
class RequiredPermissionsHelper internal constructor(private val activity: Activity) {
    /**
     * Checks that the given runtime permissions have been granted, and if not will request that the
     * user grant the permissions.
     *
     *
     * If the user has explicitly denied the permissions then a dialog will be shown that directs
     * the user to the application settings and the current activity will be finished.
     *
     *
     * This must be called in or before `onCreate()` of the calling activity/fragment.
     *
     * @param caller the activity or fragment making this call
     * @param requiredPermissions the runtime permissions that are required
     * @param successRunnable will be run immediately if the permissions are already granted, or run
     * after the user grants the required permissions
     */
    fun checkRequiredPermissions(
        caller: ActivityResultCaller, requiredPermissions: Array<String>, successRunnable: Runnable
    ) {
        if (hasRequiredPermissions(requiredPermissions)) {
            successRunnable.run()
            return
        }
        val permissionsLauncher =
            caller.registerForActivityResult<Array<String>, Map<String, Boolean>>(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { grantResults: Map<String, Boolean> ->
                onPermissionGrantResults(
                    grantResults,
                    successRunnable
                )
            }
        permissionsLauncher.launch(requiredPermissions)
    }

    private fun hasRequiredPermissions(requiredPermissions: Array<String>): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(
                    TAG,
                    "Permission is denied: $permission"
                )
                return false
            }
        }
        return true
    }

    private fun onPermissionGrantResults(
        grantResults: Map<String, Boolean>, successRunnable: Runnable
    ) {
        if (!grantResults.containsValue(false)) {
            successRunnable.run()
        } else {
            AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.permissions_required_dialog_title))
                .setMessage(activity.getString(R.string.permissions_required_dialog_message))
                .setPositiveButton(
                    activity.getString(R.string.dialog_settings)
                ) { dialog: DialogInterface, _: Int ->
                    launchAppSettings()
                    dialog.dismiss()
                }
                .setNegativeButton(
                    activity.getString(R.string.dialog_cancel)
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .setOnDismissListener { activity.finish() }
                .show()
        }
    }

    private fun launchAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", activity.packageName,  /* fragment= */null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
    }

    companion object {
        private const val TAG = "PermissionsHelper"
    }
}
