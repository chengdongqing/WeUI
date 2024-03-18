package top.chengdongqing.weui.utils

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestMediaPermission(
    extraPermissions: List<String> = emptyList(),
    onRevoked: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            addAll(
                listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
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