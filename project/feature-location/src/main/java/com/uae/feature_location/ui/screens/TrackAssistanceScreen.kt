package com.uae.feature_location.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.AttachLifecycleObserver
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.extenstions.openDialPad
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_SEMI_BOLD
import com.uae.core_common.theme.gray_light_2
import com.uae.core_common.theme.grey_2
import com.uae.core_common.utils.AsyncProfileImageWithLoader
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_network.data.model.TrackAssistanceData
import com.uae.feature_location.ui.TrackAssistanceScreenEvents
import com.uae.feature_location.ui.viewmodels.TrackAssistanceViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import androidx.core.graphics.createBitmap
import com.uae.core.navigation.HomeScreens


@Composable
fun TrackAssistanceScreen(
    trackAssistanceData: TrackAssistanceData?,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    trackAssistanceViewModel: TrackAssistanceViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    LaunchedEffect(trackAssistanceData) {
        trackAssistanceViewModel.updateState { state ->
            state?.copy(trackAssistanceData = trackAssistanceData)
        }
        trackAssistanceData?.staff?.id?.let { staffId ->
            trackAssistanceViewModel.startSocket(staffId = staffId)
        }
    }

    AttachLifecycleObserver() { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                trackAssistanceViewModel.getLastAcceptedAssistance()
            }

            else -> Unit
        }
    }

    val uiState by trackAssistanceViewModel.uiState.collectAsStateWithLifecycle()

    val density = LocalDensity.current

    var parentBoxHeight by remember { mutableStateOf(0) }
    var detailsBoxHeight by remember { mutableStateOf(0) }

    val mapViewHeight by remember {
        derivedStateOf {
            with(density) { (parentBoxHeight - detailsBoxHeight).toDp() }
        }
    }


    var isCollapsed by rememberSaveable() { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()


    Log.d("knvknvfd", mapViewHeight.toString())


    ObserveUiEvent(trackAssistanceViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is TrackAssistanceScreenEvents.AssistanceResolved -> {
                backStack.removeLastOrNull()
                uiState?.trackAssistanceData?.request?.id?.let {
                    backStack.add(
                        HomeScreens.RateUsScreen(requestId = it)
                    )
                }
            }
        }
    }

    BoxCommon(
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false,
        modifier = Modifier
            .fillMaxWidth()
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        )
        {


            HeaderView(title = "Track Assistance") {
                backStack.removeLastOrNull()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onSizeChanged {
                        parentBoxHeight = it.height
                    })
            {

                GoogleMapScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mapViewHeight),
                    currentLatLng = uiState?.currentLatLng,
                    staffLatLng = LatLng(
                        uiState?.trackAssistanceData?.staff?.latitude ?: 0.0,
                        uiState?.trackAssistanceData?.staff?.longitude ?: 0.0
                    )
                )


                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
                {

                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .dropShadow(
                                shape = Shape_10,
                                shadow = Shadow(color = Color.Black.copy(0.6f), radius = 10.dp)
                            )
                            .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                            .background(color = gray_light_2)
                            .clickable {
                                isCollapsed = !isCollapsed
                            }
                            .padding(horizontal = 30.dp, vertical = 7.dp)
                    ) {
                        Image(
                            painter = if (isCollapsed) painterResource(R.drawable.baseline_keyboard_arrow_up_24) else painterResource(
                                R.drawable.baseline_keyboard_arrow_down_24
                            ),
                            contentDescription = null
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .onSizeChanged {
                                detailsBoxHeight = it.height
                            }
                            .background(color = gray_light_2)
                            .padding(15.dp))
                    {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isCollapsed = !isCollapsed
                                },
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncProfileImageWithLoader(
                                image = trackAssistanceData?.staff?.profilePic,
                                modifier = Modifier.size(25.dp),
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    "${trackAssistanceData?.staff?.firstName ?: ""} ${trackAssistanceData?.staff?.lastName ?: ""}",
                                    fontFamily = POPPINS_SEMI_BOLD
                                )
                                Text("${trackAssistanceData?.staff?.countryCode ?: ""} ${trackAssistanceData?.staff?.phone ?: ""}")
                            }
                            Image(
                                painter = painterResource(R.drawable.phone_circle),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(
                                        CircleShape
                                    )
                                    .size(25.dp)
                                    .clickableDebAnim {
                                        trackAssistanceData?.staff?.phone?.let {
                                            openDialPad(context = context, it)
                                        }
                                    }
                            )
                        }





                        AnimatedVisibility(!isCollapsed, modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            )
                            {

                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = grey_2,
                                    modifier = Modifier
                                        .padding(vertical = 15.dp)
                                )

                                Text(
                                    "# ${trackAssistanceData?.request?.category?.name}",
                                    fontFamily = POPPINS_SEMI_BOLD
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.address),
                                        modifier = Modifier.size(25.dp),
                                        contentDescription = null
                                    )
                                    Text("24, Block B, Sector 2, Noida, Uttar Pradesh, India")
                                }

                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = grey_2,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                )


                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.distance),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp)
                                        )
                                        Text("10 m")

                                    }
                                    Row(
                                        modifier = Modifier,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.time_circle),
                                            contentDescription = (null),
                                            modifier = Modifier.size(25.dp)
                                        )
                                        Text("1 min")
                                    }
                                }
                            }
                        }


                        AnimatedVisibility(uiState?.isStaffArrivedAtLocation ?: false || uiState?.trackAssistanceData?.request?.arrivalStatus?.toInt() == 1) {
                            GradientButton(
                                onClick = {
                                    trackAssistanceViewModel.confirmStaffArrival(uiState?.trackAssistanceData)
                                },
                                text = "Confirm Assistance Arrival",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 5.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun GoogleMapScreen(
    modifier: Modifier,
    currentLatLng: LatLng?,
    staffLatLng: LatLng? = null
) {


    val context = LocalContext.current

    val staffLocation = if(staffLatLng?.latitude == 0.0 && staffLatLng.longitude == 0.0) null else staffLatLng

    val cameraPositionState = rememberCameraPositionState()
    val markerState = remember { MarkerState() }
    val staffMarkerState = remember { MarkerState() }

    var mapLoaded by remember { mutableStateOf(false) }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            mapLoaded = true
        },
        modifier = modifier
    ) {
        Marker(state = markerState)
        if(mapLoaded) {
            staffLocation?.let {
                Marker(
                    state = staffMarkerState,
                    icon =  context.bitmapDescriptorFromVector(R.drawable.map_pin,100,100),
                    title = "Current Location"
                )
            }
        }

    }

    LaunchedEffect(currentLatLng, mapLoaded, staffLatLng) {
        Log.d("fkbfkbnf", staffLatLng.toString())
        if (mapLoaded && currentLatLng != null) {
            markerState.position = currentLatLng
            staffLocation?.let {
                staffMarkerState.position = staffLocation
            }

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
            )


        }
    }
}

fun Context.bitmapDescriptorFromVector(
    @DrawableRes drawableId: Int,
    width: Int,
    height: Int
): BitmapDescriptor {

    val drawable = ContextCompat.getDrawable(this, drawableId)!!

    val bitmap = createBitmap(width, height)

    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}