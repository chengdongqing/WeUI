package top.chengdongqing.weui.ui.views.media.filebrowser

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.basic.KeyValueRow
import top.chengdongqing.weui.ui.components.basic.LoadMoreType
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.ActionSheetItem
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.WePopup
import top.chengdongqing.weui.ui.components.feedback.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.feedback.rememberWeDialog
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.utils.calculateFileSize
import top.chengdongqing.weui.utils.deleteFile
import top.chengdongqing.weui.utils.format
import top.chengdongqing.weui.utils.formatFileSize
import top.chengdongqing.weui.utils.formatTime
import java.io.File

@Composable
fun FileBrowserPage() {
    WePage(
        title = "FileBrowser",
        description = "文件浏览器",
        backgroundColor = Color.White
    ) {
        FileBrowser(Environment.getExternalStorageDirectory().path)
    }
}

@Composable
private fun FileBrowser(rootFolderPath: String) {
    val coroutineScope = rememberCoroutineScope()
    val folders = remember { mutableStateListOf(rootFolderPath) }
    val children = rememberFolderChildren(folders)
    var files by children.first
    var loading by children.second

    Column {
        NavigationBar(folders)
        Spacer(modifier = Modifier.height(20.dp))
        FileList(
            files,
            loading,
            navigateToFolder = {
                files = emptyList()
                folders.add(it)
            }
        ) {
            coroutineScope.launch {
                loading = true
                files = buildFiles(folders.last())
                delay(300)
                loading = false
            }
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
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            File(item.path)
                        )
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "*/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(intent)
                    },
                    onDeleted
                )
            }
        }
    }
}

@Composable
private fun FileListItem(
    file: FileItem,
    onFolderClick: () -> Unit,
    onFileClick: () -> Unit,
    onDeleted: () -> Unit
) {
    val actionSheet = rememberWeActionSheet()
    val dialog = rememberWeDialog()
    val toast = rememberWeToast()

    val menus = remember {
        listOf(
            ActionSheetItem("详情"),
            ActionSheetItem("删除", color = Color.Red)
        )
    }

    // 详情弹窗
    val (visible, setVisible) = remember {
        mutableStateOf(false)
    }
    FileDetailsPopup(visible, file) {
        setVisible(false)
    }

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    actionSheet.show(options = menus) {
                        when (it) {
                            0 -> {
                                setVisible(true)
                            }

                            1 -> {
                                dialog.show("确定删除吗？") {
                                    if (!deleteFile(File(file.path))) {
                                        toast.show("删除失败", ToastIcon.FAIL)
                                    } else {
                                        onDeleted()
                                    }
                                }
                            }
                        }
                    }
                }) {
                    if (file.isDirectory) {
                        onFolderClick()
                    } else {
                        onFileClick()
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (file.isDirectory) {
                Image(
                    painter = painterResource(id = R.drawable.ic_folder),
                    contentDescription = "文件夹",
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Image(
                    painter = painterResource(id = file.iconId),
                    contentDescription = "文件",
                    modifier = Modifier.size(38.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                color = FontColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row {
                Text(
                    text = buildString {
                        append(file.lastModified)
                        append(" | ")
                        if (file.isDirectory) {
                            append("${file.childrenCount}项")
                        } else {
                            append(file.size)
                        }
                    },
                    color = FontColor1,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "下一级"
        )
    }
}

@Composable
private fun FileDetailsPopup(visible: Boolean, file: FileItem, onClose: () -> Unit) {
    WePopup(
        visible,
        title = file.name,
        padding = PaddingValues(20.dp),
        swipeable = true,
        onClose = onClose
    ) {
        val originalFile = remember { File(file.path) }
        val size by produceState(initialValue = 0L) {
            value = calculateFileSize(originalFile)
        }

        KeyValueRow(label = "位置", value = file.path)
        KeyValueRow(label = "大小", value = formatFileSize(size))
        KeyValueRow(label = "时间", value = file.lastModified)
        KeyValueRow(label = "可读", value = originalFile.canRead().format())
        KeyValueRow(label = "可写", value = originalFile.canWrite().format())
        KeyValueRow(label = "隐藏", value = originalFile.isHidden.format())
    }
}

@Composable
private fun rememberFolderChildren(folders: MutableList<String>): Pair<MutableState<List<FileItem>>, MutableState<Boolean>> {
    // 是否加载中
    val loading = remember { mutableStateOf(true) }
    // 当前文件夹下所有文件/文件夹
    val files = remember { mutableStateOf<List<FileItem>>(emptyList()) }

    // 处理权限
    SetupPermission()
    // 处理返回
    BackHandler(folders.size > 1) {
        folders.removeAt(folders.lastIndex)
    }

    // 处理文件夹子项加载
    LaunchedEffect(folders.size) {
        if (loading.value) {
            delay(300)
        } else {
            loading.value = true
        }
        files.value = buildFiles(folders.last())
        loading.value = false
    }

    return Pair(files, loading)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SetupPermission() {
    val permissions = remember {
        buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                addAll(
                    listOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                )
            }
        }
    }
    val permissionState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }
}

private suspend fun buildFiles(filePath: String): List<FileItem> = withContext(Dispatchers.IO) {
    File(filePath).listFiles()?.filter { !it.isHidden }
        ?.sortedWith(compareBy {
            !it.isDirectory
        })?.map { file ->
            FileItem(
                name = file.name,
                path = file.path,
                isDirectory = file.isDirectory,
                size = formatFileSize(file),
                lastModified = formatTime(file.lastModified()),
                childrenCount = file.listFiles()?.filter { !it.isHidden }?.size ?: 0,
                iconId = getFileCategoryIcon(file)
            )
        } ?: emptyList()
}

private fun getFileCategoryIcon(file: File): Int {
    val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)?.let {
        when {
            it.startsWith("image") -> R.drawable.ic_picture
            it.startsWith("video") -> R.drawable.ic_video
            it.startsWith("audio") -> R.drawable.ic_music
            it.startsWith("text")
                    || it.endsWith("pdf")
                    || it.endsWith("msword")
                    || it.endsWith("vnd.ms-excel")
                    || it.endsWith("vnd.ms-powerpoint")
            -> R.drawable.ic_document

            else -> R.drawable.ic_file
        }
    } ?: when (file.extension) {
        "apk" -> R.drawable.ic_apk
        "mp3", "flac", "aac", "wav" -> R.drawable.ic_music
        else -> {
            R.drawable.ic_file
        }
    }
}

private data class FileItem(
    val name: String,
    val path: String,
    val size: String,
    val isDirectory: Boolean,
    val lastModified: String,
    val childrenCount: Int,
    @DrawableRes val iconId: Int
)