package com.uae.feature_profile.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.DatePickerCustomDialog
import com.uae.core_common.components.DropDownTextField
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.ImageOptionsSelector
import com.uae.core_common.components.TextFieldOuterLabel
import com.uae.core_common.components.clickableDeb
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.grey_1
import com.uae.core_common.theme.grey_2
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.theme.theme_color_2
import com.uae.core_common.theme.theme_color_3
import com.uae.core_common.utils.AsyncProfileImageWithLoader
import com.uae.core_common.utils.DateFormats
import com.uae.core_common.utils.Gender
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.createFolder
import com.uae.core_common.utils.localeUtils.convertToDateFormat
import com.uae.core_common.utils.showToast
import com.uae.feature_profile.ui.viewmodel.ProfileSetupViewModel
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.zodiaq.ui.theme.Shape_10
import java.io.File
import java.util.Date


@Composable
@Preview
fun ProfileSetUpScreen(profileSetupViewModel: ProfileSetupViewModel = hiltViewModel(),
                       snackBarHostState : SnackbarHostState = LocalSnackBarHostState.current,
                       backStack: NavBackStack<NavKey> = LocalBackStackNav.current) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    var gender by rememberSaveable { mutableIntStateOf(Gender.MALE.type) }
    var showDOBDialog by remember { mutableStateOf(false) }

    val uiState by profileSetupViewModel.uiState.collectAsStateWithLifecycle()

    val dobPickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    var showImageSelectorDialog by remember { mutableStateOf(false) }
    var fileUri: Uri? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        var fileName = "profile_image_${System.currentTimeMillis()}"
        if (!fileName.isNullOrEmpty()) {
            val folderFile = File(context.cacheDir, fileName).createFolder()
            val file = File(folderFile, fileName)
            fileUri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", file
            )
        }
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                fileUri?.let { it1 ->
                    profileSetupViewModel.updateState { state ->
                        state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(profilePic = fileUri.toString()))
                    }
                }
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {

                profileSetupViewModel.updateState { state ->
                    state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(profilePic = uri.toString()))
                }
//                profileSetupViewModel.updateState { state ->
//                    state.copy()
//                }
//                profileViewModel.updateState { state ->
//                    state?.copy(userDetails = state?.userDetails?.copy(photo = uri.toString()))
//                }
//                profileViewModel.uploadItem(galleryMediaItemSelection?.copy(uri = uri.toString()))
            }
        }

//    val showDOBDialog by

    ObserveUiEvent(profileSetupViewModel.uiEvent) { uiEvent ->
        when(uiEvent){
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uiEvent.error.toString())
            }
            is CommonUiEvent.ShowSuccessMessage -> {
                context.showToast(message = uiEvent.message)
            }
            is CommonUiEvent.NavigateTo -> {
                backStack.removeLastOrNull()
                backStack.add(uiEvent.routeNavKey)
            }
        }
    }


    BoxCommon(
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false,
        isLoading = uiState?.isLoading ?: false) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            HeaderView(title = "Set Profile", onBack = {
                backStack.removeLastOrNull()
            })

            Column(modifier = Modifier.weight(1f)
                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(5.dp)) {

                Spacer(modifier = Modifier.height(25.dp))

                editImageIcon(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(
                        start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp
                    ),
                    image = uiState?.profileSetupRequestBody?.profilePic,
//                    showEditIcon = uiState?.isEditable ?: false,
                    onClick = {
                            showImageSelectorDialog = true
                    })

                TextFieldOuterLabel(
                    value = uiState?.profileSetupRequestBody?.firstName ?: "",
                    onValueChange = {
                        if (!it.contains(" ")) {
                            profileSetupViewModel.updateState { state ->
                                state?.copy(profileSetupRequestBody = state?.profileSetupRequestBody?.copy(firstName = it))
                            }
                         }
                    },
                    hint = "First Name",
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.user),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp,),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    )
                )
                TextFieldOuterLabel(
                    value = uiState?.profileSetupRequestBody?.lastName ?: "",
                    onValueChange = {
                        if (!it.contains(" ")) {
                            profileSetupViewModel.updateState { state ->
                                state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(lastName = it))
                            }
                        }
                    },
                    hint = "Last Name",
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.user),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    )
                )
                TextFieldOuterLabel(
                    value = uiState?.profileSetupRequestBody?.email ?: "",
                    onValueChange = {
                        profileSetupViewModel.updateState { state ->
                            state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(email = it))
                        }
                    },
                    hint = "Email Address",
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.email),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
                )


                Text("Gender",color = Color.Black, fontFamily = POPPINS_MEDIUM, fontSize = 13.sp,
                    modifier = Modifier.padding(top = 15.dp, start = 20.dp, end = 20.dp))

                GenderLayout(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 5.dp, start = 20.dp, end = 20.dp),
                    gender = uiState?.profileSetupRequestBody?.gender ?: Gender.MALE.type,
                    onChange = {
                        profileSetupViewModel.updateState { state ->
                            state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(gender = it))
                        }
                    }
                )

                TextFieldOuterLabel(
                    value = uiState?.profileSetupRequestBody?.birthDate ?: "",
                    onValueChange = { value ->
//                        profileViewModel.updateState { state ->
//                            state?.copy(userDetails = state.userDetails?.copy(name = value))
//                        }
                    },
                    isRequired = true,
                    readOnly = true,
                    label = "Date of Birth",
                    leadingIcon = {
                        Image(painter = painterResource(R.drawable.calender), contentDescription = null)
                    },
                    hint = "DD/MM/YYYY",
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onDone = {}),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 20.dp, end = 20.dp)
                        .clickableDeb{
                            showDOBDialog = true
                        }

                )

                DropDownTextField(
                    list = uiState?.bloodGroups ?: emptyList(),
                    getText = { t ->
                        uiState?.bloodGroups?.find { it?.id == t?.id }?.name.toString()
                    },
                    leadingIcon = R.drawable.calender,
                    hint = "Blood Group",
                    value = uiState?.selectedBloodGroup?.name ?: "",
                    onSelect = { condition ->
                        profileSetupViewModel.updateState { state ->
                            state?.copy(selectedBloodGroup = condition)
                        }
                    },
                    trailingIcon = R.drawable.baseline_arrow_back_24,
//                    enabled = uiState?.isEditable ?: false,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                )

                DropDownTextField(
                    list = uiState?.medicalCondition ?: emptyList(),
                    getText = { t ->
                        uiState?.medicalCondition?.find { it?.id == t?.id }?.name.toString()
                    },
                    leadingIcon = R.drawable.calender,
                    hint = "Medical Complications",
                    value =uiState?.selectedMedicalCondition?.name ?: "",
                    onSelect = { condition ->
                        profileSetupViewModel.updateState { state ->
                            state?.copy(selectedMedicalCondition = condition)
                        }
                    },
//                    enabled = uiState?.isEditable ?: false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.fillMaxWidth().height(35.dp))
            }



            GradientButton(
                onClick = {
                    keyboardController?.hide()
                    profileSetupViewModel.performUpdateProfile()
                },
                text = "Submit",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp).padding(bottom = 10.dp)
            )
        }

    }

    DatePickerCustomDialog(
        shouldShow = showDOBDialog,
        title = "Date of Birth",
        onDismiss = {
            showDOBDialog = false
        },
        datePickerState = dobPickerState,
        onDone = {
            val dob = it ?: Date().time

            profileSetupViewModel.updateState { state ->
                val formattedDate = dob.convertToDateFormat(DateFormats.DATE_FORMAT_8) ?: ""
                state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(birthDate = formattedDate))
            }
            showDOBDialog = false
        },
        onCancel = {
            showDOBDialog = false
        })
    ImageOptionsSelector(showDialog = showImageSelectorDialog, onDismiss = {
        showImageSelectorDialog = false
    }, onCamera = {
        showImageSelectorDialog = false
        fileUri?.let {
            takePictureLauncher.launch(it)
        }
    }, onGallery = {
        showImageSelectorDialog = false
        galleryLauncher.launch("image/*")
    })

}


@Composable
fun GenderLayout(
    modifier: Modifier = Modifier,
    gender: Int,
    onChange: (Int) -> Unit
) {

    LaunchedEffect(gender) {
        Log.d("fkbnfknbf", (gender == Gender.MALE.type).toString())
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GenderItem(
            title = "Male",
            icon = if(gender == Gender.MALE.type) R.drawable.male_selected else R.drawable.male,
            isSelected = gender == Gender.MALE.type,
            onClick = {onChange(Gender.MALE.type)})
        GenderItem(
            title = "Female",
            icon = if(gender == Gender.FEMALE.type) R.drawable.female_selected else R.drawable.female,
            isSelected = gender == Gender.FEMALE.type,
            onClick = {onChange(Gender.FEMALE.type)})
        GenderItem(
            title = "Other",
            icon = if(gender == Gender.OTHER.type) R.drawable.other_selected else R.drawable.other,
            isSelected = gender == Gender.OTHER.type,
            onClick = {onChange(Gender.OTHER.type)})
    }
}

@Composable
fun RowScope.GenderItem(title: String, icon: Int, isSelected: Boolean, onClick: () -> Unit) {


    val borderWidth by animateDpAsState(if (isSelected) 1.dp else 0.dp)
    val shadowAlpha by animateFloatAsState(if (isSelected) 0.1f else 0.05f)
    val textColor by animateColorAsState(if (isSelected) theme_color_3 else grey_1)

    Box(
        modifier = Modifier
            .weight(1f)
            .dropShadow(
                shape = Shape_10,
                shadow = Shadow(radius = 10.dp, color = Color.Black.copy(alpha = shadowAlpha))
            )
            .clip(Shape_10)
            .background(color = Color.White, shape = Shape_10)
            .border(width = borderWidth, color = if(isSelected) theme_color_2 else Color.Transparent, shape = Shape_10)
            .clickable{
                onClick()
            }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(icon), contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(title, color = textColor)
        }
    }
}

@Composable
fun editImageIcon(
    modifier: Modifier = Modifier, image: Any?, showEditIcon: Boolean = true, onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .size(80.dp)
            .clickableDebAnim {
                onClick()
            }) {


        AsyncProfileImageWithLoader(
            image = image, modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .dropShadow(
                    shape = CircleShape, shadow = Shadow(color = theme_color_1, radius = 10.dp)
                )
                .background(color = grey_2, shape = CircleShape)
        )
        AnimatedVisibility(
            showEditIcon, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.camera_),
                contentDescription = null,
                modifier = Modifier
            )
        }
    }
}

