package com.uae.feature_home.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.ButtonsDialog
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.StarRatingBar
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.grey_4
import com.uae.core_common.theme.grey_5
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.feature_home.ui.events.RateUsScreenEvents
import com.uae.feature_home.ui.viewmodel.RateUseViewModel
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.zodiaq.ui.theme.Shape_12

@Composable
fun RateUsScreen(
    requestId: String?,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    rateUseViewModel: RateUseViewModel = hiltViewModel()
) {
    val uiState by rateUseViewModel.uiState.collectAsStateWithLifecycle()

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(requestId) {
        rateUseViewModel.updateState { state ->
            state?.copy(ratingRequestBody = state.ratingRequestBody?.copy(requestId = requestId))
        }
    }
    ObserveUiEvent(rateUseViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is RateUsScreenEvents.RatingAddedSuccessfully -> {
                showSuccessDialog = true
            }

        }
    }

    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false,
        modifier = Modifier.blur(radius = if (showSuccessDialog) 20.dp else 0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            HeaderView(title = "Rate Us"){
                backStack.removeLastOrNull()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.uae_logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    "How was you experience?", textAlign = TextAlign.Center,
                    fontFamily = POPPINS_MEDIUM,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    "Your feedback help us improve our assistance and serve you better",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp),
                    color = grey_4
                )


                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .border(width = 1.dp, color = grey_5, shape = Shape_12)
                        .background(color = Color.White, Shape_12)
                        .padding(10.dp)
                ) {

                    Text(
                        "Rate the assistance you received", textAlign = TextAlign.Center,
                        fontFamily = POPPINS_MEDIUM,
                        modifier = Modifier.padding(top = 15.dp)
                    )

                    StarRatingBar(
                        rating = uiState?.ratingRequestBody?.rating?.toFloat() ?: 0f,
                        onRatingChanged = {
                            rateUseViewModel.updateState { state ->
                                state?.copy(ratingRequestBody = state.ratingRequestBody?.copy(rating = it.toInt()))
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 5.dp)
                    )

                }

                Text(
                    "Write your Review (Optional)",
                    fontFamily = POPPINS_MEDIUM,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .border(width = 1.dp, color = grey_5, shape = Shape_12)
                        .background(color = Color.White, Shape_12)
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
                            value = uiState?.ratingRequestBody?.comment ?: "",
                            onValueChange = {
                                rateUseViewModel.updateState { state ->
                                    state?.copy(
                                        ratingRequestBody = state?.ratingRequestBody?.copy(
                                            comment = it
                                        )
                                    )
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
                                if (uiState?.ratingRequestBody?.comment.isNullOrEmpty()) {
                                    Text("Tell us what you think", color = grey_5)
                                }
                                box()
                            },
                        )
                    }
                }
            }

            GradientButton(
                onClick = {
                    keyboardController?.hide()
                    rateUseViewModel.addRating()
                },
                text = "Submit",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }

    ButtonsDialog(
        showDialog = showSuccessDialog,
        title = "Thank You",
        description = "Your feedback has been submitted successfully. Thank you for sharing your experience with the assistance provided.",
        showBothButtons = false,
        positiveBtnText = "Ok",
        onPositiveClick = {
            showSuccessDialog = false
            backStack.removeLastOrNull()
        },
        onDismiss = {
            showSuccessDialog = false
        })


}