package top.chengdongqing.weui.ui.views.media.filebrowser

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.R
import top.chengdongqing.weui.extensions.clickableWithoutRipple
import top.chengdongqing.weui.ui.components.basic.LoadMoreType
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
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
private fun FileBrowser(folderPath: String) {
    // 是否加载中
    var loading by remember { mutableStateOf(true) }
    // 已进入的所有文件夹
    val folders = remember { mutableStateListOf(folderPath) }
    // 当前文件夹下所有文件/文件夹
    val files by produceState<List<FileItem>>(initialValue = emptyList(), folders.size) {
        if (loading) {
            delay(300)
        } else {
            loading = true
        }
        value = buildFiles(folders.last())
        loading = false
    }

    // 处理权限
    SetupPermission()
    // 处理返回
    BackHandler(folders.size > 1) {
        folders.removeAt(folders.lastIndex)
    }

    Column {
        NavigationBar(folders)
        Spacer(modifier = Modifier.height(20.dp))
        FileList(files, loading) {
            folders.add(it)
        }
    }
}

@Composable
private fun FileList(
    files: List<FileItem>,
    loading: Boolean,
    navigateToFolder: (String) -> Unit
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
        } else {
            if (files.isNotEmpty()) {
                items(files) { item ->
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
                        }
                    )
                }
            } else {
                item {
                    WeLoadMore(type = LoadMoreType.ALL_LOADED)
                }
            }
        }
    }
}

@Composable
private fun FileListItem(
    file: FileItem,
    onFolderClick: () -> Unit,
    onFileClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickableWithoutRipple {
            if (file.isDirectory) {
                onFolderClick()
            } else {
                onFileClick()
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
                    color = FontColo1,
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
                size = formatFileSize(file.path),
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