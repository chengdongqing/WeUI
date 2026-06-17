package top.chengdongqing.weui.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun RequestStoragePermission(content: @Composable (() -> Unit)) {
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
            WeButton(
                text = "授予文件管理权限",
                width = 200.dp,
                type = ButtonType.Plain
            ) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                openSettingsLauncher.launch(intent)
            }
        } else {
            WeButton(
                text = "授予文件读写权限",
                width = 200.dp,
                type = ButtonType.Plain
            ) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    } else {
        content()
    }
}

actual fun getStorageRootPath(): String = Environment.getExternalStorageDirectory().path

actual suspend fun calculateFileSize(filePath: String): Long = withContext(Dispatchers.IO) {
    fun loadFileSize(file: File): Long =
        when {
            file.isFile -> {
                // 如果是文件，直接返回其大小
                file.length()
            }

            file.isDirectory -> {
                // 如果是目录，递归计算所有子文件和子目录的大小
                val children = file.listFiles()
                var totalSize: Long = 0
                if (children != null) {
                    for (child in children) {
                        totalSize += loadFileSize(child)
                    }
                }
                totalSize
            }

            else -> 0
        }

    loadFileSize(File(filePath))
}

fun Context.getFileProviderUri(file: File): Uri {
    return FileProvider.getUriForFile(this, "$packageName.provider", file)
}