package top.chengdongqing.weui.feature.samples.filebrowser.filelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import top.chengdongqing.weui.core.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.core.ui.components.actionsheet.rememberActionSheetState
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.dialog.rememberDialogState
import top.chengdongqing.weui.core.ui.components.popup.WePopup
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.utils.calculateFileSize
import top.chengdongqing.weui.core.utils.deleteFile
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.formatFileSize
import top.chengdongqing.weui.feature.samples.filebrowser.R
import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem
import java.io.File
import top.chengdongqing.weui.core.ui.theme.R as ThemeR

@Composable
internal fun FileListItem(
    file: FileItem,
    onFolderClick: () -> Unit,
    onFileClick: () -> Unit,
    onDeleted: () -> Unit
) {
    val actionSheet = rememberActionSheetState()
    val dialog = rememberDialogState()
    val toast = rememberToastState()

    val menus = remember {
        listOf(
            ActionSheetItem("详情"),
            ActionSheetItem("删除", color = Color.Red)
        )
    }

    // 详情弹窗
    val (visible, setVisible) = remember { mutableStateOf(false) }
    FileDetailsPopup(visible, file) {
        setVisible(false)
    }

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    actionSheet.show(menus) {
                        when (it) {
                            0 -> {
                                setVisible(true)
                            }

                            1 -> {
                                dialog.show(title = "确定删除吗？") {
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
        FileThumbnail(file)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                color = MaterialTheme.colorScheme.onPrimary,
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
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            painterResource(id = ThemeR.drawable.ic_arrow_right),
            contentDescription = "下一级",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun FileThumbnail(file: FileItem) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            file.isDirectory -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_folder),
                    contentDescription = "文件夹",
                    modifier = Modifier.matchParentSize()
                )
            }

            file.isVisualMedia -> {
                VisualMediaThumbnail(file)
            }

            else -> {
                FileDefaultIcon(file.iconId)
            }
        }
    }
}

@Composable
private fun VisualMediaThumbnail(file: FileItem) {
    AsyncImage(
        model = file.path,
        contentDescription = null,
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(6.dp)),
        contentScale = ContentScale.Crop,
        error = painterResource(file.iconId),
        placeholder = painterResource(file.iconId)
    )
}

@Composable
private fun FileDefaultIcon(iconId: Int) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = "文件",
        modifier = Modifier.size(38.dp)
    )
}

@Composable
private fun FileDetailsPopup(visible: Boolean, file: FileItem, onClose: () -> Unit) {
    WePopup(
        visible,
        title = file.name,
        padding = PaddingValues(20.dp),
        onClose = onClose
    ) {
        val originalFile = remember { File(file.path) }
        val size by produceState(initialValue = 0L) {
            value = calculateFileSize(originalFile)
        }

        WeCardListItem(label = "位置", value = file.path)
        WeCardListItem(label = "大小", value = formatFileSize(size))
        WeCardListItem(label = "时间", value = file.lastModified)
        WeCardListItem(label = "可读", value = originalFile.canRead().format())
        WeCardListItem(label = "可写", value = originalFile.canWrite().format())
        WeCardListItem(label = "隐藏", value = originalFile.isHidden.format())
    }
}
