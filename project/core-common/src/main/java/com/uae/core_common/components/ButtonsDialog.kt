package com.uae.core_common.components

import com.uae.core_common.theme.POPPINS_SEMI_BOLD
import com.uae.core_common.theme.theme_color_12
import com.uae.core_common.theme.theme_color_17
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zodiaq.ui.theme.Shape_12


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonsDialog(
    showDialog: Boolean = false,
    showLoader: Boolean = false,
    title: String? = null,
    description: String? = null,
    descriptionColor: Color = Color.Unspecified,
    descriptionFontStyle: FontStyle? = null,
    descriptionFontFamily: FontFamily? = null,
    showDescriptionLoader: Boolean = false,
    onDismiss: () -> Unit,
    showBothButtons: Boolean = true,
    positiveBtnText : String? = null,
    negativeBtnText : String? = null,
    properties : DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit = {},
) {
    if (showDialog) {

        Dialog(
            onDismissRequest = onDismiss,
            properties = properties
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .background(color = Color.White, Shape_12)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {

                Text(
                    title ?: "", fontFamily = POPPINS_SEMI_BOLD, fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
                if (!showDescriptionLoader) {
                    Text(
                        description ?: "",
                        textAlign = TextAlign.Center,
                        color = descriptionColor,
                        fontFamily = descriptionFontFamily,
                        fontStyle = descriptionFontStyle,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }else{
                    LoadingIndicator(
                        color = theme_color_12,
                        modifier = Modifier.size(40.dp)
                    )
                }
                if (!showLoader) {

                    if (showBothButtons) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        ) {
                            GradientButton(
                                onClick = onPositiveClick, text = positiveBtnText ?: "Yes",
                                verticalPadding = 10.dp,
                                horizontalPadding = 22.dp,
                                bgColor = Color.Transparent,
                                fontColor = theme_color_17,
                                isBorder = true
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                            GradientButton(
                                onClick = onNegativeClick, text = negativeBtnText ?: "No",
                                verticalPadding = 10.dp,
                                horizontalPadding = 22.dp, bgColor = Color.Transparent,
                                fontColor = theme_color_17,
                                isBorder = true
                            )
                        }

                    }
                    else {
                        GradientButton(
                            onClick = onPositiveClick, text = positiveBtnText ?: "Ok",
                            verticalPadding = 10.dp,
                            horizontalPadding = 35.dp,
                            bgColor = Color.Transparent,
                            fontColor = theme_color_17,
                            isBorder = true,
                            modifier = Modifier

                                .padding(top = 20.dp)
                        )
                    }
                } else {
                    LoadingIndicator(
                        color = theme_color_12,
                        modifier = Modifier.size(40.dp)
                    )
                }

            }
        }

    }

}