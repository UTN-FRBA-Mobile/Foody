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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.dataClasses.AddressViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.model.GeocodingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LocationGoogleScreen(context: ComponentActivity,
                         navController:NavController,
                         viewModel: AddressViewModel,
                         origin: String
) {
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    var locationRequired by remember { mutableStateOf(false) }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 30f)
    }
    var cameraPositionState by remember { mutableStateOf(cameraPosition) }


    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location in p0.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
                }
                if (locationRequired) {
                    startLocationUpdates(fusedLocationClient, this) // "this" hace referencia al LocationCallback
                    locationRequired = false // Marca como que ya se activaron las actualizaciones
                }
            }
        }
    }
    var direccion by remember { mutableStateOf("") }
    var nro by remember { mutableStateOf("") }
    var localidad by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var address:Address.AddressInfo


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
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
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
                val results: Array<GeocodingResult> = GeocodingApi.geocode(geoApiContext, address).await()
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

    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize())
    IconButton(onClick = {
        when (origin) {
            "sign_up" -> navController.navigate(AppScreens.SignUp_Screen.route)
            "profile" -> navController.navigate(AppScreens.Profile_Screen.route)
        }
    }) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.go_back),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }

    LazyColumn (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 50.dp),
        verticalArrangement = Arrangement.Bottom)
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

            Row (
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
            Row (
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
                            text = "Direccion",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = nro, onValueChange = { nro = it },
                    label = { Text(text = "Nro", modifier = Modifier.padding(start = 16.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
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
                        calle = direccion,
                        numero = nro.toInt(),
                        localidad = localidad,
                        region = region,
                        latitud = currentLocation.latitude,
                        longitud = currentLocation.longitude
                    )
                    coroutineScope.launch {
                        val location = geocodeAddress(address.calle)
                        if (location != null) {
                            stopLocationUpdates()
                            currentLocation = location
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
                            Log.d("LocationGoogleScreen", "Dirección: ${address.calle}")
                            Log.d("LocationGoogleScreen", "Número: ${address.numero}")
                            Log.d("LocationGoogleScreen", "Localidad: ${address.localidad}")
                            Log.d("LocationGoogleScreen", "Región: ${address.region}")
                            // Guardar la dirección en la variable
                            viewModel.updateAddress(address)

                            // Navegar a SignUpScreen
                            when (origin) {
                                "sign_up" -> navController.navigate(AppScreens.SignUp_Screen.route)
                                "profile" -> navController.navigate(AppScreens.Profile_Screen.route)
                                // add other cases if needed
                            }
                        }
                    }
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


