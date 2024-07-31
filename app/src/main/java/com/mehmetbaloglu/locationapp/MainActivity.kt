package com.mehmetbaloglu.locationapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mehmetbaloglu.locationapp.ui.theme.LocationAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    LocationApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun LocationApp(viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context) //izinler varsa true yoksa false döndürür
    locationDisplay(locationUtils, viewModel, context = context)
}


@Composable
fun locationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
) {

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                //we have access to location
            } else {
                val rationaleRequire = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleRequire) {
                    Toast.makeText(
                        context,
                        "Location Permission Required for this feature to work",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Location Permission Required.Please enable it from settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Location Not Available !")

        Button(onClick = {
            if (locationUtils.hasLocationPermission(context)) {
                //permission already granted

            } else {
                //request location permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }

    }

}