package top.chengdongqing.weui.feature.samples.filebrowser

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.samples.filebrowser.navigation.FileBrowserNavHost

@Composable
fun FileBrowserScreen() {
    WeScreen(
        title = "FileBrowser",
        description = "文件浏览器",
        padding = PaddingValues(horizontal = 16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        scrollEnabled = false
    ) {
        RequestStoragePermission {
            val navController = rememberNavController()
            val rootPath = remember { Environment.getExternalStorageDirectory().path }
            val folders = rememberSaveable(
                saver = listSaver(
                    save = { list -> list.toList() },
                    restore = { list -> list.toMutableStateList() }
                )
            ) {
                mutableStateListOf(rootPath)
            }

            Column {
                NavigationBar(navController, folders)
                Spacer(modifier = Modifier.height(20.dp))
                FileBrowserNavHost(navController, folders, rootPath)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun RequestStoragePermission(content: @Composable () -> Unit) {
    var hasPermission by remember { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    ) {
        hasPermission = it.all { item -> item.value }
    }

    val checkPermission = {
        hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            permissionState.allPermissionsGranted
        }
    }
    LaunchedEffect(Unit) { checkPermission() }
    val openSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        checkPermission()
    }

    if (!hasPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WeButton(text = "授予文件管理权限", width = 200.dp, type = ButtonType.PLAIN) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                openSettingsLauncher.launch(intent)
            }
        } else {
            WeButton(text = "授予文件读写权限", width = 200.dp, type = ButtonType.PLAIN) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    } else {
        content()
    }
}