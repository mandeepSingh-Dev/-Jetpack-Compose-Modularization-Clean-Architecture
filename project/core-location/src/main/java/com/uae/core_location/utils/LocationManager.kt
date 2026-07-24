package com.uae.core_location.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException


class LocationMManager(
    val context: Context,
    val onLocationResultUpdate : ((Location?) -> Unit)? = null,
    val onLastLocationResultUpdate: ((Location?) -> Unit)? = null,
) {





    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null

    var currentLocationn: Location? = null


    companion object{
    fun checkLocationPermissions(context: Context): Boolean {
        val isAnyLocationGranted = (ActivityCompat.checkSelfPermission(
            context, LocationConstants.LOCATION_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
            context,
            LocationConstants.LOCATION_PERMISSIONS[1]
        ) == PackageManager.PERMISSION_GRANTED)

        Log.d("kfnbkfnb", isAnyLocationGranted.toString())
        return isAnyLocationGranted
    }


        private val _startLocationService = MutableSharedFlow<Boolean>()
        val startLocationService = _startLocationService.asSharedFlow()

        suspend fun triggerLocationBackgroundService(isTrigger : Boolean){
            _startLocationService.emit(isTrigger)
        }

    }

    @SuppressLint("MissingPermission")
    fun initLocationSDK(
        isLocationResultCallback: Boolean = false,
        shouldFetchLocation: Boolean = true
    ) {

        initLocationManager()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        initLocationRequest()

        if (shouldFetchLocation) {
            fetchLocation()
        }
        if (isLocationResultCallback) {
            enableLocationResultCallback()
        }
    }

    fun initLocationManager() {

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    fun initLocationRequest(){
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LocationConstants.LOCATION_CALLBACK_REQUEST_INTERVAL,
        ).build()
    }

    @SuppressLint("MissingPermission")
    fun enableLocationResultCallback() {


        locationRequest?.let {
            fusedLocationProviderClient?.requestLocationUpdates(
                it,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getCurrentLocation(onLocation : (Location?) -> Unit, onError : (String) -> Unit): Location? {

        return if(!checkLocationPermissions(context)){
            onError("Location permissions are not granted")
            null
        }else {
            if (checkLocationProviders()) {

                val lastLocation = fusedLocationProviderClient?.lastLocation?.await()
                if (lastLocation != null) {
                    if (lastLocation.time < 2 * 60 * 1000) { Log.d("TAG", "last location valid and returning last location")

                        onLocation(lastLocation)
                        Log.d("fkbnfjkbf", lastLocation.toString() + " 1")
                        lastLocation
                    } else {
                        Log.d("TAG", "last location is expired and returning current location")
                        currentLocationn = fusedLocationProviderClient?.getCurrentLocation(Priority.PRIORITY_LOW_POWER, null)?.await()
                        Log.d("fkbnfjkbf", currentLocationn.toString())
                        onLocation(currentLocationn)
                        currentLocationn
                    }
                } else {
                    Log.d("TAG", "last location is null and returning current location")
                    currentLocationn = fusedLocationProviderClient?.getCurrentLocation(Priority.PRIORITY_LOW_POWER, null)?.await()
                    Log.d("fkbnfjkbf", currentLocationn.toString() + " 0> 2")
                    onLocation(currentLocationn)
                    currentLocationn
                }
            } else {
//                requestLocationProviders(onSuccess = {})
                onError("Location providers are not enabled.")
                null
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun fetchLocation() {

//        if (!checkLocationPermissions(context)) {
//            onRequestLocationPermissions()
//        } else {
//            setCurrentLocation()
//        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Location? = suspendCancellableCoroutine { continuation ->
        if (checkLocationPermissions(context) && checkLocationProviders()) {
            fusedLocationProviderClient?.lastLocation?.addOnSuccessListener {
                if (continuation.isActive) {
                    continuation.resume(it, null)
                }
            }?.addOnFailureListener {
                if (continuation.isActive) {
                    continuation.resumeWithException(it)
                }
            }?.addOnCanceledListener {
                if (continuation.isActive) {
                    continuation.resumeWithException(Exception("Failed to get Location"))
                }
            }
        } else {
            continuation.resumeWithException(LocationPermissionsExceptions("Permissions or Location providers are not satisfied"))
        }
    }


    /**Function to refresh current location so that In future Last location gives current location.*/
    @SuppressLint("MissingPermission")
    fun triggerCurrentLocationRefresh() {
        if (checkLocationPermissions(context) && checkLocationProviders()) {
            fusedLocationProviderClient?.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        }
    }


    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {

        if (checkLocationProviders()) {

            fusedLocationProviderClient?.lastLocation?.addOnSuccessListener {lastLocation ->
                try {
                    onLastLocationResultUpdate?.invoke(lastLocation)
                } catch (e: Exception) {
                }
            }

             fusedLocationProviderClient?.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            )
                ?.addOnSuccessListener { nLocation ->
                    try {
                        Log.d("fvmklvmvjfjfkd", nLocation.latitude.toString())
                        onLocationResultUpdate?.invoke(nLocation)
                    } catch (e: Exception) {
                        Log.d("mvkmvkfvmf", e.message.toString())
                    }

//                setAndStoreNewLocation(nLocation)

                }?.addOnFailureListener {
                    null
                }
        } else {
            Log.d("fvkbnkjfnvf", "getCurrentLocation ELSE")
//            requestLocationProviders()

        }
    }

    fun checkLocationProviders(): Boolean {

        if(locationManager == null) {
            initLocationManager()
        }
        val isGpsProvider = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkProvider = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGpsProvider == true || isNetworkProvider == true

    }


     fun requestLocationProviders(getPendingIntent : (PendingIntent) -> Unit) {

        Log.d("fvkbnkjfnvf", "providers")

         if(locationRequest == null){
             initLocationRequest()
         }
        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!).build()

        val settingClient = LocationServices.getSettingsClient(context)
        val locationSettingTask = settingClient.checkLocationSettings(locationSettingsRequest)

        locationSettingTask
            .addOnSuccessListener {
                setCurrentLocation()
                Log.d("fvkbnkjfnvf", "addOnSuccessListener")
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        Log.d("fvkbnkjfnvf", "addOnFailureListener")

                        val resolution = exception.resolution

                        getPendingIntent(resolution)
//                        onRequestLocationProvidersCallback(resolution)

//                        r.launch(IntentSenderRequest.Builder(exception.resolution).build())
                    } catch (e: Exception) {
                        Log.d("fbkmfkvbfk", e.message.toString())
                        Log.d("fvkbnkjfnvf", "addOnFailureListener EXCEPTION")

                    }
                }
            }
    }

    fun onRequestProviders() {
        Log.d("flvnfjkvnfv", checkLocationProviders().toString() + "    locationProviders")
        setCurrentLocation()
    }

    fun onLocationRequestPermissions() {
        Log.d(
            "flvnfjkvnfv",
            checkLocationProviders().toString() + "    onLocationRequestPermissions "
        )

        setCurrentLocation()

    }


    var debounceTime = 2000L
    var time = 0L

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)


            val currentTime = System.currentTimeMillis()

            Log.d("fkvmkfvmnkf", "${currentTime.toString()} + ${(currentTime - time)} current-time + ${debounceTime}" )

            val differenceTime = (currentTime - time)
            if(differenceTime > debounceTime) {
                time = currentTime
                Log.d("fvlmfkvmf", locationResult.lastLocation?.latitude.toString() + " latitude")
                Log.d("fvlmfkvmf", locationResult.lastLocation?.longitude.toString() + " longitude")

                locationResult.locations.first()

                onLocationResultUpdate?.invoke(locationResult.lastLocation)

                setAndStoreNewLocation(locationResult.lastLocation)
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }
    }

    fun calculateDistance(newLocation: Location?, oldLocation: Location?): Float? {

        val distance = oldLocation?.let { newLocation?.distanceTo(it) }
        return distance?.div(1000)
    }


    /**Set and Store New Location Only If distance between new and old-locally-stored location is specified KM   */
    fun setAndStoreNewLocation(newLoc: Location?) {
        /*  //Fetched old location stored locally.
          val oldLocationStr = appPreferences.getString(LocationConstants.LOCATION)
          val oldLocationModel = oldLocationStr.fromJson<LocationModel>()

          val newLocation = newLoc

          //converting locationModel to android.Location object.
          val oldLocation = Location("")
          oldLocation.latitude = oldLocationModel?.latitude ?: 0.0
          oldLocation.longitude = oldLocationModel?.longitude ?: 0.0

          //Calculating distance between old and new current location.
          val distanceKM = calculateDistance(newLocation,oldLocation ) ?: 0f

          *//*If distance between old and new current location is  >= 10 km then set old  *//*
        if(distanceKM >= LocationConstants.LOCATION_DISTANCE_KM) {
            saveLocation(newLocation)
        }

        currentLocationn = newLocation
        onLocationResultUpdate(currentLocationn)

*/
    }


    var addressJob : Job? = null

    suspend fun getAddressFromLatLng(location: Location?) = with(Dispatchers.IO) {
       return@with suspendCancellableCoroutine<String> { continuation ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Geocoder(context).getFromLocation(
                    location?.latitude!!,
                    location.longitude,
                    1,
                    object : GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isNotEmpty()) {
                                continuation.resume(addresses.first().getAddressLine(0), null)
                            } else {
                                continuation.resumeWithException(Exception("Error"))
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            super.onError(errorMessage)
                            continuation.resumeWithException(Exception(errorMessage))
                        }
                    })
            } else {
                try {
                    val addresses = Geocoder(context).getFromLocation(
                        location?.latitude!!,
                        location.longitude,
                        1
                    )
                    if (!addresses.isNullOrEmpty()) {
                        continuation.resume(addresses.first().getAddressLine(0), null)
                    } else {
                        continuation.resumeWithException(Exception("Error"))
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(Exception(e.message))
                }
            }
        }
    }

    fun release(){
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        // Null out references
        fusedLocationProviderClient = null
        locationManager = null
        locationRequest = null }
    }

