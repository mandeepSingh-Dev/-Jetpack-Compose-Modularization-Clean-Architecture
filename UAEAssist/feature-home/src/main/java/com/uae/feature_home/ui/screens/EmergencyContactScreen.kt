package com.uae.feature_home.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.ButtonsDialog
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.PhoneNumberField
import com.uae.core_common.components.TextFieldOuterLabel
import com.uae.core_common.components.clickableAnim
import com.uae.core_common.components.clickableDeb
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.openDialPad
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_SEMI_BOLD
import com.uae.core_common.theme.brush4
import com.uae.core_common.theme.grey_8
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.showToast
import com.uae.feature_home.remote.model.requestBody.AddContactRequestBody
import com.uae.feature_home.remote.model.response.ContactsResponse
import com.uae.feature_home.ui.events.EmergencyContactEvents
import com.uae.feature_home.ui.viewmodel.EmergencyContactViewModel
import com.uae.feature_home.utils.ContactItemAction
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.zodiaq.ui.theme.Shape_10
import kotlinx.coroutines.launch



typealias ShowContactDialogFlag = Pair<Boolean, ContactsResponse.ContactsData?>
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    emergencyContactViewModel: EmergencyContactViewModel = hiltViewModel(),
    backStack : NavBackStack<NavKey> = LocalBackStackNav.current
) {


    val context = LocalContext.current
    val uiState by emergencyContactViewModel.uiState.collectAsStateWithLifecycle()

    var showContactDialog by rememberSaveable { mutableStateOf<ShowContactDialogFlag>(ShowContactDialogFlag(false, null))}
    var showDeleteContactDialog by rememberSaveable { mutableStateOf<String?>(null)}

    val sheetState = rememberModalBottomSheetState()

    val coroutineScope = rememberCoroutineScope()

    ObserveUiEvent(emergencyContactViewModel.uiEvent) {uIEvent ->
        when(uIEvent){
            is CommonUiEvent.ShowSuccessMessage -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.message)
            }
            is EmergencyContactEvents.ContactAdded -> {
                showContactDialog = ShowContactDialogFlag(false, null)
                coroutineScope.launch {
                    sheetState.hide()
                }
                emergencyContactViewModel.getContacts()
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.message)
            }

            is EmergencyContactEvents.ContactDeleted -> {

            }
            is CommonUiEvent.ShowError -> {
                context.showToast(message = uIEvent.error)
            }

        }
    }
    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        isAppearanceLightStatusBars = false
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderView(title = "Emergency Contacts", onBack = {
                backStack.removeLastOrNull()
            })


            PullToRefreshBox(isRefreshing = uiState?.isRefreshing ?: false, onRefresh = {
                emergencyContactViewModel.getContacts(isRefresh = true)
            }, modifier = Modifier.fillMaxWidth().weight(1f)) {

                if(uiState?.contactsList.isNullOrEmpty() && uiState?.isLoading == false){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        emptyView()
                    }
                }else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        items(
                            uiState?.contactsList ?: emptyList(),
                            key = { it?.id ?: "" }) { contact ->
                            ContactCard(
                                modifier = Modifier.animateItem(),
                                contactsData = contact
                            ) { action ->
                                when (action) {
                                    ContactItemAction.CALL -> {
                                        contact?.phone?.let {
                                            openDialPad(context = context, phoneNumber = it)
                                        }
                                    }

                                    ContactItemAction.EDIT -> {
                                        showContactDialog = ShowContactDialogFlag(true, contact)
                                    }

                                    ContactItemAction.DELETE -> {
                                        contact?.id?.let { id ->
                                            showDeleteContactDialog = id
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .clip(Shape_10)
                .dropShadow(
                    shape = Shape_10,
                    shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp)
                )
                .background(brush = brush4(), shape = Shape_10)
                .clickableDebAnim {
                    showContactDialog = ShowContactDialogFlag(true,null)
                }
                .padding(10.dp)) {
            Image(
                painter = painterResource(com.uae.feature_home.R.drawable.baseline_add_24),
                contentDescription = null
            )
        }
    }

    AddContactsDialog(
        show = showContactDialog.first,
        contactsData = showContactDialog.second,
        sheetState= sheetState,
        onDismiss = {
            showContactDialog = ShowContactDialogFlag(false,null)
        },
        onAdd = {
            emergencyContactViewModel.addContact(it)
        },
        onEdit = {
            emergencyContactViewModel.editContact(it)
        },
        isAddInProgress = uiState?.isContactAddInProgress ?: false)


    ButtonsDialog(
        showDialog = !showDeleteContactDialog.isNullOrEmpty(),
        onDismiss = {
            showDeleteContactDialog = null
        },
        title = "Delete Contact",
        description = "Do you want to delete this contact?",
        onPositiveClick = {
            showDeleteContactDialog?.let {
                emergencyContactViewModel.deleteContact(id = it)
            }
            showDeleteContactDialog = null
        },
        onNegativeClick = {
            showDeleteContactDialog = null
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddContactsDialog(
    show: Boolean = true,
    contactsData: ContactsResponse.ContactsData?,
    onDismiss: () -> Unit,
    onAdd: (AddContactRequestBody) -> Unit = {},
    onEdit: (AddContactRequestBody) -> Unit = {},
    isAddInProgress: Boolean = false,
    sheetState: SheetState
) {


    val keyboardController = LocalSoftwareKeyboardController.current

    if (show) {

        val coroutineScope = rememberCoroutineScope()


        var fullName by rememberSaveable(contactsData?.fullName) { mutableStateOf(contactsData?.fullName) }
        var phone by rememberSaveable(contactsData?.phone) { mutableStateOf(contactsData?.phone) }
        val state = rememberKomposeCountryCodePickerState(
            defaultCountryCode = "IN"
        )
        LaunchedEffect(state.countryCode) {
//        loginViewModel.updateState { loginState ->
//            loginState?.copy(loginRequestBody = loginState.loginRequestBody?.copy(countryCode = state.getCountryPhoneCode()))
//        }
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = grey_8,
            dragHandle = null
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickableAnim{
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                            onDismiss()
                        }
                        .dropShadow(
                            Shape_10,
                            shadow = Shadow(color = Color.Black.copy(0.2f), radius = 10.dp)
                        )
                        .background(color = theme_color_1, shape = CircleShape)
                        .padding(3.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_close_24),
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Add Contact", fontFamily = POPPINS_SEMI_BOLD, fontSize = 16.sp)


                    TextFieldOuterLabel(
                        value = fullName ?: "",
                        onValueChange = {
                            fullName = it
                        },
                        hint = "Enter full name",
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
                            .padding(top = 15.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )


                    PhoneNumberField(
                        modifier = Modifier.padding(top = 15.dp),
                        onPhone = {
                            if (it?.isDigitsOnly() == true) {
                                phone = it
                            }
                        },
                        state = state,
                        phone = phone,
                    )

                    AnimatedVisibility(isAddInProgress) {
                        LoadingIndicator(modifier = Modifier.size(30.dp))
                    }
                    AnimatedVisibility(!isAddInProgress) {
                        GradientButton(
                            onClick = {
                                keyboardController?.hide()
                                if(contactsData == null){
                                    onAdd(AddContactRequestBody(
                                        countryCode = state.getCountryPhoneCode(),
                                        fullName = fullName,
                                        phone = phone,
                                    ))
                                }else{
                                    onEdit(AddContactRequestBody(
                                        countryCode = state.getCountryPhoneCode(),
                                        fullName = fullName,
                                        phone = phone,
                                        id = contactsData.id

                                    ))
                                }

                            },
                            horizontalPadding = 40.dp,
                            text = if (contactsData !=null ) "Edit" else "Add",
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }
    }

}


@Composable
fun ContactCard(modifier: Modifier = Modifier, contactsData: ContactsResponse.ContactsData?,
                onClick : (ContactItemAction) -> Unit){


    Column(modifier = modifier.fillMaxWidth()
        .dropShadow(Shape_10, shadow = Shadow(radius = 10.dp, color = Color.Black.copy(alpha = 0.1f)))
        .background(Color.White, Shape_10)
        .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Text(contactsData?.fullName ?: "")
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("${contactsData?.countryCode} ${contactsData?.phone}", modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Image(painter = painterResource(R.drawable.phone_2), contentDescription = null, modifier = Modifier.size(30.dp).clickableDeb{
                    onClick(ContactItemAction.CALL)
                })
                Image(painter = painterResource(R.drawable.edit_1), contentDescription = null, modifier = Modifier.size(30.dp).clickableDeb{
                    onClick(ContactItemAction.EDIT)
                })
                Image(painter = painterResource(R.drawable.delete), contentDescription = null, modifier = Modifier.size(30.dp).clickableDeb{
                    onClick(ContactItemAction.DELETE)
                })
            }
        }
    }
}