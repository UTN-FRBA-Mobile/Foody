package ar.edu.utn.frba.foody.ui.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.google.maps.*
import com.google.maps.android.compose.*
import com.google.maps.model.GeocodingResult
import kotlinx.coroutines.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LocationGoogleScreen(
    context: ComponentActivity,
    navController: NavController,
    viewModel: OrderViewModel,
    origin: String,
    id: String
) {
    var direccion by remember { mutableStateOf(if(origin != "sign_up") viewModel.user.address.street!! else "") }
    var nro by remember { mutableStateOf(if(origin != "sign_up") viewModel.user.address.number.toString() else "") }
    var localidad by remember { mutableStateOf(if(origin != "sign_up") viewModel.user.address.location!! else "") }
    var region by remember { mutableStateOf(if(origin != "sign_up") viewModel.user.address.country!! else "") }
    var address: Address.AddressInfo

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    var locationRequired by remember { mutableStateOf(false) }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 30f)
    }
    val cameraPositionState by remember { mutableStateOf(cameraPosition) }


    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location in p0.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(currentLocation, 15f)
                }
                if (locationRequired) {
                    startLocationUpdates(
                        fusedLocationClient,
                        this
                    ) // "this" hace referencia al LocationCallback
                    locationRequired = false // Marca como que ya se activaron las actualizaciones
                }
            }
        }
    }

    LaunchedEffect(locationRequired) {
        if (locationRequired) {
            startLocationUpdates(fusedLocationClient, locationCallback)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }


    val launchMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMaps ->
        val areGranted = permissionMaps.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates(fusedLocationClient, locationCallback)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    val apiKey = try {
        val applicationInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        applicationInfo.metaData.getString("com.google.android.geo.API_KEY")
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }

    val geoApiContext = remember {
        GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()
    }


    suspend fun geocodeAddress(address: String): LatLng? {
        return withContext(Dispatchers.IO) {
            try {
                val results: Array<GeocodingResult> =
                    GeocodingApi.geocode(geoApiContext, address).await()
                if (results.isNotEmpty()) {
                    val location = results[0].geometry.location
                    LatLng(location.lat, location.lng)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()
    if(origin !="sign_up" && currentLocation.latitude==0.0 && currentLocation.longitude==0.0){
        val fullAddress = "$direccion $nro, $localidad, $region"
        coroutineScope.launch {
            val location = geocodeAddress(fullAddress)
            if (location != null) {
                stopLocationUpdates()
                currentLocation = location
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(currentLocation, 15f)
            }
        }
    }
    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    IconButton(onClick = {
        when (origin) {
            "sign_up" -> navController.navigate(AppScreens.SignUp_Screen.route)
            "profile" -> navController.navigate(AppScreens.Profile_Screen.route)
            "payment" -> navController.navigate(AppScreens.Payment.route)
        }
    }) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.go_back),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 50.dp),
        verticalArrangement = Arrangement.Bottom
    )
    {
        item {
            GoogleMap(
                modifier = Modifier.height(550.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = currentLocation),
                    title = "You",
                    snippet = "You're here!!!"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Your location ${currentLocation.latitude}/${currentLocation.longitude}")
                /*Button(onClick = {
                    if (permissions.all {
                            ContextCompat.checkSelfPermission(context, it) ==
                                    PackageManager.PERMISSION_GRANTED
                        }) {
                        locationRequired = true

                       // startLocationUpdates(fusedLocationClient, locationCallback)
                    } else {
                        launchMultiplePermissions.launch(permissions)
                    }
                }) {
                    Text("Get your location")
                }

                 */
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TextField(
                    modifier = Modifier.weight(2f),
                    value = direccion, onValueChange = { direccion = it },
                    label = {
                        Text(
                            text = "Calle",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                TextField(
                    modifier = Modifier.weight(1f),
                    value = nro, onValueChange = { nro = it },
                    label = { Text(text = "Altura", modifier = Modifier.padding(start = 16.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = localidad, onValueChange = { localidad = it },
                label = { Text(text = "Localidad", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = region, onValueChange = { region = it },
                label = { Text(text = "Region", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val fullAddress = "$direccion $nro, $localidad, $region"
                    coroutineScope.launch {
                        val location = geocodeAddress(fullAddress)
                        if (location != null) {
                            stopLocationUpdates()
                            currentLocation = location
                            cameraPositionState.position =
                                CameraPosition.fromLatLngZoom(currentLocation, 15f)
                        }
                    }
                }
            ) {
                Text("Find")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    address = Address.AddressInfo(
                        street = direccion,
                        number = if (nro == "") 0 else nro.toInt(),
                        location = localidad,
                        country = region,
                        latitude = currentLocation.latitude,
                        longitude = currentLocation.longitude
                    )
                    //coroutineScope.launch {
                    //val location = geocodeAddress(address.calle)
                    if (viewModel.isEmptyAddress(address)) {
                        Toast.makeText(
                            navController.context,
                            "Address Incomplete",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //stopLocationUpdates()
                        //currentLocation = location
                        //cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
                        Log.d("LocationGoogleScreen", "Dirección: ${address.street}")
                        Log.d("LocationGoogleScreen", "Número: ${address.number}")
                        Log.d("LocationGoogleScreen", "Localidad: ${address.location}")
                        Log.d("LocationGoogleScreen", "Región: ${address.country}")
                        // Guardar la dirección en la variable
                        viewModel.updateAddress(address)

                        // Navegar a SignUpScreen
                        when (origin) {
                            "sign_up" -> navController.navigate(AppScreens.SignUp_Screen.route)
                            "profile" -> navController.navigate(AppScreens.Profile_Screen.route)
                            "payment" -> navController.navigate(AppScreens.Payment.route)
                            // add other cases if needed
                        }
                    }
                    // }
                }
            ) {
                Text("Save & Continue")
            }
        }
    }
}


private var locationUpdatesJob: Job? = null

@SuppressLint("MissingPermission")
internal fun startLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(3000)
        .setMaxUpdateDelayMillis(100)
        .build()

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
    // Cancelar actualizaciones después de un tiempo determinado
    locationUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
        delay(10000) // Detener las actualizaciones después de 10 segundos (ajusta este valor según sea necesario)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}

private fun stopLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    fusedLocationClient.removeLocationUpdates(locationCallback)
}

private fun stopLocationUpdates() {
    locationUpdatesJob?.cancel()
}


