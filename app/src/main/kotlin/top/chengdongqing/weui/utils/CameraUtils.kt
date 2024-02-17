package top.chengdongqing.weui.utils

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(
    navController: NavController,
    permissions: List<String> = emptyList(),
    content: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(buildList {
        add(Manifest.permission.CAMERA)
        if (permissions.isNotEmpty()) {
            addAll(permissions)
        }
    }) { res ->
        if (res.values.any { value -> !value }) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(permissionState) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (permissionState.allPermissionsGranted) {
            SetupFullscreen()
            content()
        }
    }
}