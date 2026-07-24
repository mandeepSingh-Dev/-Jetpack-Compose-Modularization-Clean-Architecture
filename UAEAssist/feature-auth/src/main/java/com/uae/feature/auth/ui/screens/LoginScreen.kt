package com.uae.feature.auth.ui.screens

import com.uae.core.navigation.AuthScreens
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.PhoneNumberField
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.feature.auth.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.uae.core_common.theme.POPPINS_SEMI_BOLD
import com.uae.core_common.theme.theme_color_2
import com.uae.core_common.utils.ObserveUiEvent
 import com.uae.feature.auth.ui.events.LoginScreenEvent
import com.uae.feature.auth.ui.viewmodel.LoginViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uae.core.navigation.HomeScreens
import com.uae.core_common.utils.toJson
import com.zodiaq.ui.theme.Shape_20

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel(),
                snackBarHostState : SnackbarHostState = LocalSnackBarHostState.current,
                backStack: NavBackStack<NavKey> = LocalBackStackNav.current) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    val state = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "IN"
    )

    LaunchedEffect(state.countryCode){
        loginViewModel.updateState { loginState ->
            loginState?.copy(loginRequestBody = loginState.loginRequestBody?.copy(countryCode = state.getCountryPhoneCode()))
        }
    }

    ObserveUiEvent(loginViewModel.uiEvent) {uiEvent ->
        when(uiEvent){
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uiEvent.error.toString())
            }
            is LoginScreenEvent.LoginSuccess -> {
                backStack.add(AuthScreens.OTPScreen(uiState?.loginRequestBody.toJson()))
            }
        }
    }
    BoxCommon(isLoading = uiState?.isLoading ?: false) {

        Image(painter = painterResource(com.uae.core_common.R.drawable.background), contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.systemBarsPadding()
            .verticalScroll(rememberScrollState())) {
            Text("Log in",
                fontFamily = POPPINS_SEMI_BOLD,
                fontSize = 17.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            Text(stringResource(R.string.welcome_back),)

            Image(
                painter = painterResource(com.uae.core_common.R.drawable.uae_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 20.dp)
                    .size(120.dp)
            )

            PhoneNumberField(modifier = Modifier.padding(horizontal = 20.dp),
                onPhone = {
                    if(it?.isDigitsOnly() == true){
                        loginViewModel.updateState { state ->
                            state?.copy(loginRequestBody = state.loginRequestBody?.copy(phone = it))
                        }
                    }
                },
                state = state,
                phone = uiState?.loginRequestBody?.phone,
               )

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp).padding(top = 8.dp))
            {
                Checkbox(
                    checked = uiState?.isTermsConditionsChecked ?: false,
                    onCheckedChange = {
                        loginViewModel.updateState { state ->
                            state?.copy(isTermsConditionsChecked = it)
                        }
                    },
                    colors = CheckboxDefaults.colors().copy(checkedBorderColor = theme_color_2,
                        checkedBoxColor = theme_color_2,
                        uncheckedBorderColor = theme_color_2),
                 )
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black,
                        fontSize = 12.sp,
                        )) {
                        append("I accept ")
                    }
                    withLink(
                        link = LinkAnnotation.Clickable(
                            tag = "Terms",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = theme_color_2,
                                    fontSize = 12.sp,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            linkInteractionListener = { link ->
                                loginViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.TermsConditionsScreen(1)))
                            }
                        )
                    ){
                        append("Terms & Conditions ")
                    }
                    withStyle(style = SpanStyle(color = Color.Black,
                        fontSize = 12.sp,
                        )) {
                        append("and ")
                    }
                    withLink(
                        link = LinkAnnotation.Clickable(
                            tag = "Privacy_Policy",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = theme_color_2,
                                    fontSize = 12.sp,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            linkInteractionListener = { link ->
                                loginViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.TermsConditionsScreen(2)))
                            }
                        )
                    ){
                        append("Privacy Policy ")
                    }
                })
            }

            GradientButton(onClick = {
                keyboardController?.hide()
                loginViewModel.login()
            },
                text = "Login",
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp,
                    start = 35.dp, end = 35.dp))



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

@Preview
@Composable
fun TextField1() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .dropShadow(
                shape = Shape_20,
                shadow = Shadow(radius = 10.dp, color = Color.Black.copy(alpha = 0.6f))
            )
            .background(color = Color.White, shape = Shape_20)
    ) {

        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null
        )

        BasicTextField(
            value = "sdafsafsa", onValueChange = {},
            modifier = Modifier.weight(1f),
            decorationBox = {
                Text("Hello", color = Color.Black)
            })

    }
}

