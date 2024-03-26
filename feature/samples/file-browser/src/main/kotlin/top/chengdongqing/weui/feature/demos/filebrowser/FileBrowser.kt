package top.chengdongqing.weui.feature.samples.filebrowser

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.openFile
import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem
import java.io.File

@Composable
fun FileBrowserScreen(fileViewModel: FileViewModel = viewModel()) {
    WeScreen(
        title = "FileBrowser",
        description = "文件浏览器",
        containerColor = MaterialTheme.colorScheme.surface,
        scrollEnabled = false
    ) {
        RequestStoragePermission {
            FileBrowser(fileViewModel, Environment.getExternalStorageDirectory().path)
        }
    }
}

@Composable
private fun FileBrowser(fileViewModel: FileViewModel, rootFolderPath: String) {
    val folders = remember { mutableStateListOf(rootFolderPath) }

    BackHandler(folders.size > 1) {
        folders.removeAt(folders.lastIndex)
    }
    LaunchedEffect(folders.size) {
        if (fileViewModel.loading) {
            delay(300)
        }
        fileViewModel.refresh(folders.last())
    }

    Column {
        NavigationBar(folders)
        Spacer(modifier = Modifier.height(20.dp))
        FileList(
            fileViewModel.fileList,
            fileViewModel.loading,
            navigateToFolder = {
                fileViewModel.fileList = emptyList()
                folders.add(it)
            }
        ) {
            fileViewModel.refresh(folders.last())
        }
    }
}

@Composable
private fun FileList(
    files: List<FileItem>,
    loading: Boolean,
    navigateToFolder: (String) -> Unit,
    onDeleted: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (loading) {
            item {
                WeLoadMore()
            }
        } else if (files.isEmpty()) {
            item {
                WeLoadMore(type = LoadMoreType.ALL_LOADED)
            }
        }
        if (files.isNotEmpty()) {
            items(files, key = { it.path }) { item ->
                FileListItem(
                    item,
                    onFolderClick = {
                        navigateToFolder(item.path)
                    },
                    onFileClick = {
                        context.openFile(File(item.path), item.mimeType ?: "*/*")
                    },
                    onDeleted
                )
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