package com.uae.core_common.components

import com.uae.core_common.theme.POPPINS_BOLD
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.theme.theme_color_12
import com.uae.core_common.theme.theme_color_6
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zodiaq.ui.theme.Shape_20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerCustomDialog(
    shouldShow: Boolean,
    datePickerState: DatePickerState,
    title: String?,
    onDismiss: () -> Unit = {},
    onDone : (Long?) -> Unit = {},
    onCancel : () -> Unit = {},
) {


    Log.d("fkvnkfnvf",datePickerState.displayMode.toString())

    if (shouldShow) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(12.dp)
        ) {
            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = onDismiss) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(
                            shape = Shape_20,
                           color = Color.White
                        )
                        .innerShadow(
                            shape = Shape_20,
                            shadow = Shadow(
                                radius = 10.dp,
                                spread = 5.dp,
                                brush = Brush.linearGradient(colors = listOf(Color.Black,
                                    theme_color_1
                                ))
                            )
                        )
                        .padding(vertical = 10.dp, horizontal = 2.dp)
                ) {
                    Column {
                        DatePicker(
                            state = datePickerState,
                            showModeToggle = false,
                            colors = DatePickerDefaults.colors().copy(
                                containerColor = Color.Transparent,
                                yearContentColor = Color.Black,  // year text
                                selectedYearContainerColor = theme_color_1, // selected year bg
                                disabledSelectedYearContainerColor = theme_color_6,
                                // also set the normal year container background by using the "selected" colors when appropriate,
                                // and tweak dividerColor if needed:
                                dividerColor = theme_color_1
                            ),
                            title = {
                                Text(
                                    title ?: "Select Date",
                                    fontFamily = POPPINS_BOLD,
                                    fontSize = 14.sp,
                                    style = TextStyle(
                                        color = theme_color_12
                                    ),
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)

                                )
                            }
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .fillMaxWidth().padding(vertical = 6.dp, horizontal = 10.dp)) {
                            GradientButton(onClick = onCancel, text = "Cancel",
                                modifier = Modifier.weight(1f).padding(horizontal = 5.dp))
                            GradientButton(onClick = {
                                onDone(datePickerState.selectedDateMillis)
                            }, text = "Done",
                                modifier = Modifier.weight(1f).padding(horizontal = 5.dp))
                        }
                    }
                }
            }
        }
    }
}