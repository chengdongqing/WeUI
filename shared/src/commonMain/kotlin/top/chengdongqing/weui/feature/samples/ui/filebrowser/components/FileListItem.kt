package top.chengdongqing.weui.feature.samples.ui.filebrowser.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.core.ui.components.ActionSheetItem
import top.chengdongqing.weui.core.ui.components.WeCardListItem
import top.chengdongqing.weui.core.ui.components.WePopup
import top.chengdongqing.weui.core.ui.components.rememberActionSheetState
import top.chengdongqing.weui.core.ui.components.rememberDialogState
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.util.calculateFileSize
import top.chengdongqing.weui.util.format
import top.chengdongqing.weui.util.formatFileSize
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_arrow_right
import weui_kmp.shared.generated.resources.ic_folder

@Composable
internal fun FileListItem(
    file: FileItem,
    onFolderClick: () -> Unit,
    onFileClick: () -> Unit,
    onDelete: () -> Unit
) {
    val actionSheet = rememberActionSheetState()
    val dialog = rememberDialogState()

    val menus = remember {
        listOf(
            ActionSheetItem("详情"),
            ActionSheetItem("删除", color = Color.Red)
        )
    }

    // 详情弹窗
    val (detailsVisible, setDetailsVisible) = remember { mutableStateOf(false) }
    FileDetailsPopup(detailsVisible, file) {
        setDetailsVisible(false)
    }

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    actionSheet.show(menus) {
                        when (it) {
                            0 -> {
                                setDetailsVisible(true)
                            }

                            1 -> {
                                dialog.show(title = "确定删除吗？") {
                                    onDelete()
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
                color = WeTheme.colorScheme.textPrimary,
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
                    color = WeTheme.colorScheme.textSecondary,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            painterResource(Res.drawable.ic_arrow_right),
            contentDescription = "下一级",
            tint = WeTheme.colorScheme.textSecondary
        )
    }
}

@Composable
private fun FileThumbnail(file: FileItem) {
    Box(
        modifier = Modifier.size(42.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            file.isDirectory -> {
                Image(
                    painter = painterResource(Res.drawable.ic_folder),
                    contentDescription = "文件夹",
                    modifier = Modifier.matchParentSize()
                )
            }

            file.isVisualMedia -> {
                VisualMediaThumbnail(file)
            }

            else -> {
                FileDefaultIcon(file.iconRes)
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
        error = painterResource(file.iconRes),
        placeholder = painterResource(file.iconRes)
    )
}

@Composable
private fun FileDefaultIcon(iconRes: DrawableResource) {
    Image(
        painter = painterResource(iconRes),
        contentDescription = "文件",
        modifier = Modifier.size(38.dp)
    )
}

@Composable
private fun FileDetailsPopup(
    visible: Boolean,
    file: FileItem,
    onClose: () -> Unit
) {
    WePopup(
        visible,
        title = file.name,
        padding = PaddingValues(20.dp),
        onClose = onClose
    ) {
        val totalSize by produceState(initialValue = 0L) {
            value = calculateFileSize(file.path)
        }

        WeCardListItem(label = "位置", value = file.path)
        WeCardListItem(label = "大小", value = formatFileSize(totalSize))
        WeCardListItem(label = "时间", value = file.lastModified)
        WeCardListItem(label = "可读", value = file.isReadable.format())
        WeCardListItem(label = "可写", value = file.isWriteable.format())
        WeCardListItem(label = "隐藏", value = file.isHidden.format())
    }
}