package com.uae.feature_chat_with_us.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.TextFieldOuterLabel
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.grey_1
import com.uae.core_common.theme.grey_5
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.feature_chat_with_us.ui.viewmodels.CreateTicketViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.zodiaq.ui.theme.Shape_15


@Composable
@Preview
fun CreateTickerScreen(snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
                       backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
                       createTicketViewModel : CreateTicketViewModel = hiltViewModel()
) {

    val uiState by createTicketViewModel.uiState.collectAsStateWithLifecycle()

    ObserveUiEvent(createTicketViewModel.uiEvent) {uiEvent ->
        when(uiEvent){
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uiEvent.error.toString())
            }
        }
    }

    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        isAppearanceLightNavigationBars = false,
        isAppearanceLightStatusBars = false
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {

            HeaderView(title = "Create New Ticket", onBack = {
                backStack.removeLastOrNull()
            })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    "Please provide the details of your issue. Our team will get back to you as soon as possible",
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
                )

                TextFieldOuterLabel(
                    value = uiState?.createTickerRequestBody?.title ?: "",
                    label = "Title",
                    isBorder = true,
                    onValueChange = {
                        createTicketViewModel.updateState { state ->
                            state?.copy(createTickerRequestBody = state.createTickerRequestBody.copy(title = it))
                        }
                    },
                    hint = "Enter ticket title",
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.document),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 30.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    )
                )


                Text("Description", color = Color.Black, fontFamily = POPPINS_MEDIUM,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp).padding(top = 20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 5.dp)
                        .dropShadow(
                            shape = Shape_15,
                            shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 5.dp)
                        )
                        .border(width = 1.dp, color = grey_1, shape = Shape_15)
                        .background(color = Color.White, Shape_15)
                        .padding(20.dp)
                )
                {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.delete), contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        BasicTextField(
                            value = uiState?.createTickerRequestBody?.description ?: "",
                            onValueChange = {
                                createTicketViewModel.updateState { state ->
                                    state?.copy(createTickerRequestBody = state.createTickerRequestBody.copy(description = it))
                                }
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            keyboardActions = KeyboardActions(onDone = {}),
                            minLines = 6,
                            maxLines = 6,
                            decorationBox = { box ->
                                if ("".isNullOrEmpty()) {
                                    Text("Enter ticker description", color = grey_5)
                                }
                                box()
                            },
                        )
                    }
                }


                AttachImageSection()
            }

            GradientButton(onClick = {
                createTicketViewModel.createTicket()
            },
                text = "Submit",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 5.dp))
        }

    }

}

@Composable
fun AttachImageSection() {

    Text("Attach Image (Optional)", color = Color.Black, fontFamily = POPPINS_MEDIUM, modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp))

//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        modifier = Modifier.systemBarsPadding(),
//        contentPadding = PaddingValues(horizontal = 20.dp),
//        horizontalArrangement = Arrangement.spacedBy(10.dp),
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        item {
//            Box(
//                modifier = Modifier
//                    .aspectRatio(1f)
//                    .drawBehind {
//                        drawRoundRect(
//                            color = blue_sky,
//                            style = Stroke(
//                                width = 2.dp.toPx(),
//                                pathEffect = PathEffect.dashPathEffect(
//                                    floatArrayOf(10f, 10f) // dash length, gap length
//                                )
//                            ),
//                            cornerRadius = CornerRadius(12.dp.toPx())
//                        )
//                    },
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = painterResource(ai.os.feature_chat_with_us.R.drawable.round_add_24),
//                    contentDescription = null,
//                    colorFilter = ColorFilter.tint(color = blue_sky)
//                )
//            }
//        }
//
//    }
}