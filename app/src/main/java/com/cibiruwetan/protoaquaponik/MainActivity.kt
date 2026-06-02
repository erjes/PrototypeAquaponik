package com.cibiruwetan.protoaquaponik

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibiruwetan.protoaquaponik.service.NotificationHelper
import com.cibiruwetan.protoaquaponik.ui.screen.MainPage
import com.cibiruwetan.protoaquaponik.ui.theme.AquaponikTheme
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.ensureAlertChannel(this)
        setContent {
            AquaponikTheme {
                RequestNotificationPermission()
                val sharedViewModel: SharedViewModel = viewModel()
                MainPage(sharedViewModel = sharedViewModel)
            }
        }
    }

    @Composable
    fun RequestNotificationPermission() {
        val context = LocalContext.current
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (!isGranted) {
//                    Toast.makeText("")
                }
            }

            LaunchedEffect(Unit) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
