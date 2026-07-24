package com.uae.core_common.components

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle

@Composable
fun UIRequirePermissions(
    permissions: Array<String>,
    onPermissionGranted: (List<String>) -> Unit,
    onPermissionDenied: (List<String>) -> Unit,
    manualLaunchPermissionsOnClick: ((() -> Unit) -> Unit)
) {
    val context = LocalContext.current

    Log.d("fkbnfjkbnf", permissions.toString())

    var grantState by rememberSaveable {
        mutableStateOf(
            permissions.all {
                ContextCompat.checkSelfPermission(context, it) ==
                        PackageManager.PERMISSION_GRANTED
            }
        )
    }

    Log.d("PERM", "Initial grantState = $grantState")

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        Log.d("fkbnfkbnf", result.toString())
        grantState = !result.containsValue(false)

        if (!grantState) {
            val notGrantedPermissions = result.filter { !it.value }.map { it.key }
            onPermissionDenied(notGrantedPermissions)
        } else {
            val grantedPermissions = result.filter { !it.value }.map { it.key }
            onPermissionGranted(grantedPermissions)
        }
    }

    LaunchedEffect(Unit) {
//        if (grantState) {
//            // Permissions already granted
//            onPermissionGranted()
//        } else {
//            // Request permissions now
//            launcher.launch(permissions)
//        }
    }


    AttachLifecycleObserver() {event ->
        when(event){
            Lifecycle.Event.ON_START -> {
                val isAllGranted = permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
                val grantedPermissions = permissions.filter { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }

                if (isAllGranted) {
                    // Permissions already granted
                    onPermissionGranted(grantedPermissions)
                } else {
                    // Request permissions now
                    launcher.launch(permissions)
                }
            }
            else -> Unit
        }
    }
    // Save launcher for manual trigger
    LaunchedEffect(Unit) {
        manualLaunchPermissionsOnClick {
            Log.d("fkbnkfbnf", "manual click")
            val isAllGranted = permissions.all {
                ContextCompat.checkSelfPermission(context, it) ==
                        PackageManager.PERMISSION_GRANTED
            }
            val grantedPermissions = permissions.filter { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }

            Log.d("fbknfbnf", isAllGranted.toString())
            if (isAllGranted) {
                // Permissions already granted
                onPermissionGranted(grantedPermissions)
            } else {
                // Request permissions now
                launcher.launch(permissions)
            }
//            launcher.launch(permissions)
        }
    }
}
