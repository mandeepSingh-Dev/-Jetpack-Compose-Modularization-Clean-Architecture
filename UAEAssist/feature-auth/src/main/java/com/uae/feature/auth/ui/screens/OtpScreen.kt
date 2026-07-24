package com.uae.feature.auth.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.OTPInputTextFields
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_BOLD
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.POPPINS_REGULAR
import com.uae.core_common.theme.POPPINS_SEMI_BOLD
import com.uae.core_common.theme.light_blue
import com.uae.core_common.theme.theme_color_2
import com.uae.core_common.utils.Constants
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.extensions.showToast
import com.uae.feature.auth.ui.viewmodel.OTPViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OtpScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    otpViewModel: OTPViewModel = hiltViewModel(),
    body: LoginRequestBody?
) {

    val context = LocalContext.current

    val uiState by otpViewModel.uiState.collectAsStateWithLifecycle()

    var hasCompleted by rememberSaveable { mutableStateOf(false) }

    val otpValues = remember { mutableStateListOf<String>("", "", "", "", "", "") }

    val isButtonEn by remember {
        derivedStateOf {
            otpValues.all { it.isNotEmpty() }
        }
    }

    LaunchedEffect(Unit) {

        otpViewModel.updateState { state ->
            state?.copy(loginRequestBody = body)
        }
        snapshotFlow { otpValues.toList() }.collectLatest {
            if (it.all { it.isNotEmpty() }) {
                val finalOtp = otpValues.joinToString("")
//                otpViewModel.verifyOTP(finalOtp)
//                onDone(finalOtp)
            }
        }
    }

    ObserveUiEvent(otpViewModel.uiEvent) { uIEvent ->
        when(uIEvent){
            is CommonUiEvent.ShowSuccessMessage -> {
                context.showToast(message = uIEvent.message)
            }
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
            is CommonUiEvent.NavigateTo -> {
                backStack.add(uIEvent.routeNavKey)
            }
        }
    }


    BoxCommon(uiState?.isLoading ?: false) {

        Image(painter = painterResource(com.uae.core_common.R.drawable.background), contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

        Column(modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.uae_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                        .size(80.dp)
                )



                Text("OTP Verification", fontFamily = POPPINS_BOLD, fontSize = 20.sp, modifier = Modifier.padding(top = 40.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                        .background(shape = Shape_15, color = light_blue)
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                ) {
                    Text("Enter 6-digit OTP sent to your mobile number.", fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("${uiState?.loginRequestBody?.countryCode} ${uiState?.loginRequestBody?.phone}", fontFamily = POPPINS_MEDIUM,fontSize = 13.sp)
                        Text("Change", color = theme_color_2, fontFamily = POPPINS_MEDIUM,fontSize = 13.sp)
                    }
                }

                OTPInputTextFields(
                    otpValues = otpValues,
                    onOtpInputComplete = {
                        if (!hasCompleted) {
                            hasCompleted = true
//                        isButtonEnabled = true
//                                    onDone(otpValues.toString())
                        }
                    },
                    onUpdateOtpValuesByIndex = { index, newValue ->
//                    isButtonEnabled = false
                        otpValues[index] = newValue
                    },
                    otpLength = 6,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 15.dp, start = 45.dp, end = 45.dp)
                        .background(shape = Shape_10, color = light_blue.copy(alpha = 0.4f))
                        .padding(vertical = 3.dp, horizontal = 5.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.group_10117), contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                    Text("For your security please do not\nshare this OTP with anyone", fontSize = 10.sp,
                        modifier = Modifier)
                }

                GradientButton(
                    onClick = {
                        otpViewModel.verifyOTP(otpValues.joinToString(""))
                    },
                    enabled = isButtonEn,
                    text = "Verify OTP",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 20.dp,
                            start = 35.dp, end = 35.dp
                        )
                )

                TimerWithResend(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                ) {
                    otpViewModel.login()
                }

                Spacer(modifier = Modifier.weight(1f))

            }

            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                contentAlignment = Alignment.Center) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(com.uae.core_common.R.drawable.secured),
                        contentDescription = null
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text("Secure & Trusted", fontSize = 12.sp, fontFamily = POPPINS_MEDIUM)
                        Text("Your data is protected with\ntop level security", fontSize = 12.sp)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimerWithResend(
    modifier: Modifier = Modifier,
    onResendClick: () -> Unit
) {

    var timerThresholdValue = Constants.OTP_RESEND_TIME
    var timer by rememberSaveable { mutableStateOf(timerThresholdValue) }
    var isResendVisibile by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(timer) {
        delay(1000)
        if (timer > 0) {
            timer--
        } else {
            isResendVisibile = true
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
//        if (!isResendVisibile) {
//            LoadingIndicator(modifier = Modifier.size(20.dp), color = theme_color_1)
//        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(!isResendVisibile) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Image(
                        painter = painterResource(R.drawable.time),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )

                    Text(
                        "Resend OTP in ",
                        fontFamily = POPPINS_REGULAR,
                        color = Color.Black,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        "${String.format("00:%02d", timer)}",
                        fontFamily = POPPINS_REGULAR,
                        color = theme_color_2,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                    )
                }

            }
            AnimatedVisibility(isResendVisibile) {
                resendText{
                    timer = timerThresholdValue
                    isResendVisibile = false
                    onResendClick()
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun resendText(onClick: () -> Unit) {
    Text(
        "Resend",
        fontFamily = POPPINS_SEMI_BOLD,
        color = theme_color_2,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickableDebAnim {
                onClick()
            })
}






