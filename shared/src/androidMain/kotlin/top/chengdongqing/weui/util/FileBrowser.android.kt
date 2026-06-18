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
import top.chengdongqing.weui.feature.samples.data.model.AndroidFileNode
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Files.walkFileTree
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.atomic.AtomicLong

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun RequestStoragePermission(content: @Composable ((FileNode) -> Unit)) {
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
        val rootPath = Environment.getExternalStorageDirectory().path
        content(
            AndroidFileNode(
                id = rootPath,
                name = "内部存储",
                isDirectory = true,
                file = File(rootPath)
            )
        )
    }
}

actual suspend fun calculateFileSize(fileNode: FileNode): Long = withContext(Dispatchers.IO) {
    val size = AtomicLong(0)
    val node = fileNode as AndroidFileNode
    val path = Paths.get(node.id)

    if (Files.exists(path)) {
        walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                size.addAndGet(attrs.size())
                return FileVisitResult.CONTINUE
            }

            override fun visitFileFailed(file: Path, exc: IOException): FileVisitResult {
                // 忽略无法访问的文件/目录
                return FileVisitResult.CONTINUE
            }
        })
    }
    size.get()
}

fun Context.getFileProviderUri(file: File): Uri {
    return FileProvider.getUriForFile(this, "$packageName.provider", file)
}