package com.uae.core_common.components

import com.uae.core_common.R
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.POPPINS_SEMI_BOLD
import com.uae.core_common.theme.theme_color_8
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageOptionsSelector(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCamera: () -> Unit,
    onGallery: () -> Unit,
) {

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            onCamera()
        }
    }

    if (showDialog) {
        ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp)
            ) {
                Text(
                    "Choose Option",
                    fontFamily = POPPINS_SEMI_BOLD,
                    fontSize = 18.sp,
                    color = theme_color_8
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickableDebAnim {
                            val isGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED
                           if(isGranted){
                               onCamera()
                           }else{
                               permissionLauncher.launch(Manifest.permission.CAMERA)
                           }
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.outline_camera),
                            contentDescription = null
                        )
                        Text("Camera", fontFamily = POPPINS_MEDIUM)
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickableDebAnim {
                            onGallery()
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.outline_image),
                            contentDescription = null
                        )
                        Text("Gallery", fontFamily = POPPINS_MEDIUM)
                    }
                }
            }
        }
    }
}