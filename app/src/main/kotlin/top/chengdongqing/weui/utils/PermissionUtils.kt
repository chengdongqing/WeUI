package top.chengdongqing.weui.utils

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestMediaPermission(
    onRevoked: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    ) { res ->
        if (res.values.any { value -> !value }) {
            onRevoked?.invoke()
        }
    }

    LaunchedEffect(permissionState) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (permissionState.allPermissionsGranted) {
        content()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(
    extraPermissions: List<String> = emptyList(),
    onRevoked: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(buildList {
        add(Manifest.permission.CAMERA)
        if (extraPermissions.isNotEmpty()) {
            addAll(extraPermissions)
        }
    }) { res ->
        if (res.values.any { value -> !value }) {
            onRevoked?.invoke()
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