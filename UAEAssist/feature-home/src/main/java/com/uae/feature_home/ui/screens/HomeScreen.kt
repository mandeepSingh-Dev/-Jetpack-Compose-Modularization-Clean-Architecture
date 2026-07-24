package com.uae.feature_home.ui.screens

import com.uae.core.navigation.AuthScreens
import com.uae.core.navigation.ChatWithUsScreens
import com.uae.core.navigation.HomeScreens
import com.uae.core.navigation.ProfileScreens
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.ButtonsDialog
import com.uae.core_common.components.HomeHeaderView
import com.uae.core_common.components.UIRequirePermissions
import com.uae.core_common.components.bottomPagingLoader
import com.uae.core_common.components.clickableDeb
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.showSnackBarWithAction
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.extenstions.toComposeColor
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.bg_color_1
import com.uae.core_common.theme.red
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.utils.AsyncImageWithLoader
import com.uae.core_common.utils.AsyncProfileImageWithLoader
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_location.utils.LocationConstants
import com.uae.core_location.utils.LocationMManager
import com.uae.feature_home.remote.model.response.CategoryListingResponse
import com.uae.feature_home.ui.events.HomeScreenEvent
import com.uae.feature_home.ui.viewmodel.HomeViewModel
import com.uae.feature_profile.remote.model.response.ProfileResponse
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.uae.core.navigation.LocationScreens
import com.uae.core_common.components.AttachLifecycleObserver
import com.uae.core_common.components.GradientButton
import com.uae.core_common.utils.toJson
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_5
import com.zodiaq.ui.theme.Shape_8
import kotlinx.coroutines.launch


val permissions = arrayOf(
//    Manifest.permission.CAMERA,
    Manifest.permission.POST_NOTIFICATIONS,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val drawerWidth = LocalConfiguration.current.screenWidthDp * (2.5 / 3.0)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val categoriesListPaging = homeViewModel.categoriesPagingData.collectAsLazyPagingItems()

    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }


    AttachLifecycleObserver() {event ->
        when(event){
            Lifecycle.Event.ON_RESUME -> {
                homeViewModel.getLastAcceptedAssistance()
            }
            else -> Unit
        }
    }


    LaunchedEffect(categoriesListPaging.loadState.refresh) {
        if (categoriesListPaging.loadState.refresh is LoadState.NotLoading) {
            homeViewModel.updateState { it?.copy(isRefreshing = false) }
        }
        if (categoriesListPaging.loadState.refresh is LoadState.Error) {
            (categoriesListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                homeViewModel.updateState { it?.copy(isRefreshing = false) }
            }
        }
        if (categoriesListPaging.loadState.hasError) {
            (categoriesListPaging.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }

    val requestLocationProviderLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) @androidx.annotation.RequiresPermission(
            allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]
        )
        {
//        If after accept or reject provider from location Provider dialogue then check for locationProvider and call function.
            coroutineScope.launch {
                LocationMManager.triggerLocationBackgroundService(isTrigger = true)
            }
        }

    val locationManager = retain { LocationMManager(context) }
    UIRequirePermissions(
        permissions = permissions,
        onPermissionGranted = { grantedPermissions ->

            Log.d("kfbnjmfm b", grantedPermissions.toString())
            //If contains any location permissions then check for location providers
            if (grantedPermissions.contains(LocationConstants.LOCATION_PERMISSIONS.getOrNull(0)) || grantedPermissions.contains(
                    LocationConstants.LOCATION_PERMISSIONS.getOrNull(1)
                )
            ) {
                val isLocationEnabled = locationManager.checkLocationProviders()
                Log.d("kfbnjmfm b",isLocationEnabled.toString() + "enab")

                if (isLocationEnabled) {
                    coroutineScope.launch {
                        LocationMManager.triggerLocationBackgroundService(isTrigger = true)
                    }
                } else {
                    locationManager.requestLocationProviders(getPendingIntent = { pendingIntent ->
                        val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                        requestLocationProviderLauncher.launch(intentSenderRequest)
                    })
                }
            }
        },
        onPermissionDenied = { notGrantedPermissions ->

            var permissionsDisclaimer: String? = "Please allow "

            val list = mutableListOf<String>()
            if (notGrantedPermissions.contains(LocationConstants.LOCATION_PERMISSIONS.getOrNull(0))
                && notGrantedPermissions.contains(LocationConstants.LOCATION_PERMISSIONS.getOrNull(1))
            ) {
                list.add("Location")
            }
            if (notGrantedPermissions.contains(Manifest.permission.POST_NOTIFICATIONS)) {
                list.add("Notification")
            }
            permissionsDisclaimer += "${list.joinToString(", ")} permissions"

            coroutineScope.launch {
                snackbarHostState.showSnackBarWithAction(
                    message = permissionsDisclaimer,
                    actionLabel = "Settings",
                    onAction = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    })
            }
        },
        manualLaunchPermissionsOnClick = { trigger -> })


//    locationMManager = retain {
//        LocationMManager(
//            context = context,
//            onRequestLocationProvidersCallback = { pendingIntent ->
//                requestLocationProviderLauncher.launch(
//                    IntentSenderRequest.Builder(pendingIntent.intentSender).build()
//                )
//            },
//            onRequestLocationPermissions = {
//                requestLocationPermissionLauncher.launch(LocationConstants.LOCATION_PERMISSIONS.toTypedArray())
//            }
//        )
//    }


    ObserveUiEvent(homeViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUiEvent.NavigateTo -> {
                backStack.add(uIEvent.routeNavKey)
            }

            is CommonUiEvent.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is HomeScreenEvent.LogoutSuccess -> {
                backStack.removeAll(backStack)
                backStack.add(AuthScreens.LoginScreen)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(drawerWidth.dp),
            ) {
                DrawerLayout(uiState?.userData, onClick = {
                    homeViewModel.onEvent(CommonUiEvent.NavigateTo(it))
                }, onLogout = {
                    showLogoutDialog = true
                })
            }
        },
        modifier = Modifier
            .navigationBarsPadding()
    ) {

        BoxCommon(
            isLoading = categoriesListPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false,
            isAppearanceLightStatusBars = false
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                HomeHeaderView(onClickMenu = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }, onClickNotification = {
                    homeViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.NotificationScreen))
                })

                PullToRefreshBox(
                    isRefreshing = (uiState?.isRefreshing ?: false),
                    onRefresh = {
                        homeViewModel.updateState {
                            it?.copy(isRefreshing = true)
                        }
                        categoriesListPaging.refresh()
                        homeViewModel.getLastAcceptedAssistance()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        contentPadding = PaddingValues(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    )
                    {

                        if (categoriesListPaging.itemCount > 0) {
                            items(categoriesListPaging.itemCount) { index ->
                                val categoryData = categoriesListPaging.get(index)

                                CategoryCard(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clickableDeb {
                                            homeViewModel.performCategoryAction(categoryData)
                                        }, categoryData
                                )
                            }
                            item {
                                bottomPagingLoader(
                                    isVisible = categoriesListPaging.loadState.append is LoadState.Loading || categoriesListPaging.loadState.append is LoadState.Error,
                                    isRetry = categoriesListPaging.loadState.append is LoadState.Error,
                                    onRetry = {
                                        categoriesListPaging.retry()
                                    })
                            }

                        } else {
                            if (categoriesListPaging.loadState.refresh is LoadState.NotLoading) {
                                item {
                                    emptyView(modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }

                AnimatedVisibility(uiState?.trackAssistanceData != null) {
                    GradientButton(
                        onClick = {
                            homeViewModel.onEvent(CommonUiEvent.NavigateTo(LocationScreens.TrackAssistanceScreen(uiState?.trackAssistanceData.toJson())))
                        },
                        text = "Track Assistance",
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                    )

                }
            }
        }
    }

    ButtonsDialog(
        showDialog = showLogoutDialog,
        title = "Logout?",
        description = "Are you sure\nyou want to logout?",
        onPositiveClick = {
            showLogoutDialog = false
            homeViewModel.logout()
        },
        onNegativeClick = {
            showLogoutDialog = false
        },
        onDismiss = {
            showLogoutDialog = false
        }
    )
}


@Composable
fun CategoryCard(
    modifier: Modifier, categoryData: CategoryListingResponse.CategoryData?
) {

    Box(modifier = modifier) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    color = categoryData?.bgColor?.toComposeColor() ?: theme_color_1,
                    shape = Shape_15
                )
        ) {
            Image(
                painter = painterResource(R.drawable.dots),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImageWithLoader(image = categoryData?.imgSrc, modifier = Modifier.size(40.dp))
//            androidx.compose.foundation.Image(
//                painter = ,
//                contentDescription = null,
//                modifier = Modifier.size(35.dp)
//            )
            Text(
                categoryData?.name ?: "",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun DrawerLayout(
    userData: ProfileResponse.UserData?,
    onClick: (NavKey) -> Unit,
    onLogout: () -> Unit,
) {

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxHeight()
            .background(color = bg_color_1)
            .padding(15.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = theme_color_1, shape = Shape_15)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncProfileImageWithLoader(
                    image = userData?.profilePic, modifier = Modifier.size(50.dp),
                    placeholder = R.drawable.profile_white
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${userData?.firstName?.replaceFirstChar { it.uppercase() }} ${userData?.lastName?.replaceFirstChar { it.uppercase() }}",
                        color = Color.White
                    )
                    Text("Customer Id: ${userData?.userId}", color = Color.White, fontSize = 10.sp)
                }

                Box(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.White, Shape_8)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickableDebAnim {
                                onClick(ProfileScreens.ProfileSetUpScreen)
                            }
                    )
                }
            }

            DrawerItemCard(
                modifier = Modifier.padding(top = 20.dp),
                icon = R.drawable.assistance_request_icon,
                title = "My Assistance"
            ) {
                onClick(HomeScreens.AssistanceScreen)
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.location_icon,
                title = "My Location"
            ) {
                onClick(LocationScreens.MyLocationScreen)
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.call,
                title = "Emergency Contacts"
            ) {
                onClick(HomeScreens.EmergencyContactsScreen)
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.chat,
                title = "Chat With Us"
            ) {
                onClick(ChatWithUsScreens.ChatWithUsScreen)
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.terms_conditions,
                title = "Terms & Conditions"
            ) {
                onClick(HomeScreens.TermsConditionsScreen(1))
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.terms_conditions,
                title = "Privacy Policy"
            ) {
                onClick(HomeScreens.TermsConditionsScreen(2))
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.chat,
                title = "About Us"
            ) {
                onClick(HomeScreens.TermsConditionsScreen(3))
            }
            DrawerItemCard(
                modifier = Modifier.padding(top = 10.dp),
                icon = R.drawable.faq,
                title = "FAQs"
            ) {
                onClick(HomeScreens.FaqScreen)
            }
        }


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = Shape_5,
                    shadow = Shadow(radius = 10.dp, color = Color.Black.copy(0.05f))
                )
                .clip(Shape_5)
                .background(color = Color.White, shape = Shape_5)
                .clickableDebAnim {
                    onLogout()
                }
                .padding(horizontal = 10.dp, vertical = 10.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Logout", color = red)
                Image(
                    painter = painterResource(R.drawable.baseline_logout_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = red)
                )
            }
        }
    }
}

@Composable
fun DrawerItemCard(
    modifier: Modifier, icon: Int, title: String,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_5,
                shadow = Shadow(radius = 10.dp, color = Color.Black.copy(0.05f))
            )
            .clickableDebAnim {
                onClick()
            }
            .background(color = Color.White, shape = Shape_5)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(icon), contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color.Gray)
        )
        Text(title, color = Color.Gray)
    }
}