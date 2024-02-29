package top.chengdongqing.weui.ui.screens.demo.filebrowser

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.ui.components.actionsheet.ActionSheetOptions
import top.chengdongqing.weui.ui.components.actionsheet.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.dialog.DialogOptions
import top.chengdongqing.weui.ui.components.dialog.rememberWeDialog
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.popup.WePopup
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import top.chengdongqing.weui.utils.calculateFileSize
import top.chengdongqing.weui.utils.deleteFile
import top.chengdongqing.weui.utils.format
import top.chengdongqing.weui.utils.formatFileSize
import java.io.File

@Composable
internal fun FileListItem(
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
    val (visible, setVisible) = remember { mutableStateOf(false) }
    FileDetailsPopup(visible, file) {
        setVisible(false)
    }

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    actionSheet.show(ActionSheetOptions(menus) {
                        when (it) {
                            0 -> {
                                setVisible(true)
                            }

                            1 -> {
                                dialog.show(DialogOptions(title = "确定删除吗？") {
                                    if (!deleteFile(File(file.path))) {
                                        toast.show(ToastOptions("删除失败", ToastIcon.FAIL))
                                    } else {
                                        onDeleted()
                                    }
                                })
                            }
                        }
                    })
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
            painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "下一级",
            tint = MaterialTheme.colorScheme.secondary
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

        WePairItem(label = "位置", value = file.path)
        WePairItem(label = "大小", value = formatFileSize(size))
        WePairItem(label = "时间", value = file.lastModified)
        WePairItem(label = "可读", value = originalFile.canRead().format())
        WePairItem(label = "可写", value = originalFile.canWrite().format())
        WePairItem(label = "隐藏", value = originalFile.isHidden.format())
    }
}