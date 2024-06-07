package ar.edu.utn.frba.foody.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun LocationScreen(navController: NavController)
{
    val context = LocalContext.current

    var Direccion by remember { mutableStateOf("") }
    var Nro by remember { mutableStateOf("") }
    var Localidad by remember { mutableStateOf("") }

    var userLocation by remember { mutableStateOf(LatLng(0.toDouble(),0.toDouble())) }


    var locationText by remember { mutableStateOf("No location obtained :(") }


    RequestPermissions(context)


    getLastUserLocation(
        context,
        onGetLastLocationSuccess = {
            userLocation = LatLng(it.first,it.second)
        },
        onGetLastLocationFailed = { },
        onGetLastLocationIsNull = { }
    )


    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize())


    IconButton(onClick = { navController.navigate(AppScreens.SignUp_Screen.route) }) {
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
                //uiSettings = MapUiSettings(zoomControlsEnabled = false),
                cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(userLocation, 3f)}
            ){
                Marker(
                    state = rememberMarkerState(position = userLocation),
                    draggable = false,
                    title = "Your position",
                    snippet = "longitud:${userLocation.longitude}/latitud:${userLocation.latitude}"
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                TextField(
                    modifier = Modifier.weight(2f),
                    value = Direccion, onValueChange = { Direccion = it },
                    label = { Text(text = "Direccion", modifier = Modifier.padding(start = 16.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = Nro, onValueChange = { Nro = it },
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
                value = Localidad, onValueChange = { Localidad = it },
                label = { Text(text = "Localidad", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {navController.navigate(AppScreens.SignUp_Screen.route)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save & Continue", fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreviewLocation(){
    val navController = rememberNavController()
    LocationScreen(navController = navController)
}

// SECCION DE FUNCIONES PARA VALIDACION DE PERMISOS

lateinit var fusedLocationProviderClient: FusedLocationProviderClient

@Composable
fun RequestPermissions(context: Context)
{
    RequestLocationPermission(
        onPermissionGranted = {
            getLastUserLocation(
                context,
                onGetLastLocationSuccess = {
                    // Accion cuando se consigue las coordenadas
                },
                onGetLastLocationFailed = { exception ->
                    // Accion cuando no se consigue las coordenadas
                },
                onGetLastLocationIsNull = {
                    getCurrentLocation(
                        context,
                        onGetCurrentLocationSuccess = {
                            // Accion cuando se consigue las coordenadas
                        },
                        onGetCurrentLocationFailed = {
                            // Accion cuando no se consigue las coordenadas
                        }
                    )
                }
            )
        },
        onPermissionDenied = {
            // Callback cuando los permisos son denegados
        },
        onPermissionsRevoked = {
            // Callback cuando los permisos son revocados
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(key1 = permissionState) {
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getLastUserLocation(
    context: Context,
    onGetLastLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetLastLocationFailed: (Exception) -> Unit,
    onGetLastLocationIsNull: () -> Unit
) {
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (areLocationPermissionsGranted(context)) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onGetLastLocationSuccess(Pair(it.latitude, it.longitude))
                }?.run {
                    onGetLastLocationIsNull()
                }
            }
            .addOnFailureListener { exception ->
                onGetLastLocationFailed(exception)
            }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true
) {
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    if (areLocationPermissionsGranted(context)) {
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }?.run {
            }
        }.addOnFailureListener { exception ->
            onGetCurrentLocationFailed(exception)
        }
    }
}

fun areLocationPermissionsGranted(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}



