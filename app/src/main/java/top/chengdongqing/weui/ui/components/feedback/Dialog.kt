package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.LinkColor

/**
 * 对话框
 *
 * @param visible 是否显示
 * @param title 标题
 * @param content 内容
 * @param okText 确定按钮文字
 * @param cancelText 取消按钮文字
 * @param okColor 确定按钮颜色
 * @param onOk 确定事件
 * @param onCancel 取消事件
 */
@Composable
fun WeDialog(
    visible: Boolean,
    title: String,
    content: String? = null,
    okText: String = "确定",
    cancelText: String = "取消",
    okColor: Color = LinkColor,
    onOk: () -> Unit,
    onCancel: (() -> Unit)? = null
) {
    if (visible) {
        Dialog(
            onDismissRequest = {
                onCancel?.invoke()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth(0.8f)
                    .background(Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 32.dp,
                                bottom = if (content != null) 16.dp else 0.dp,
                                start = 24.dp,
                                end = 24.dp
                            ),
                        color = FontColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (content != null) {
                        Text(
                            text = content,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            color = FontColo1,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Divider(thickness = 0.5.dp, color = Color(0f, 0f, 0f, 0.1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (onCancel != null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clickable(onClick = onCancel),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cancelText,
                                    color = FontColor,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Divider(
                                modifier = Modifier
                                    .width(0.5.dp)
                                    .height(56.dp),
                                color = Color(0f, 0f, 0f, 0.1f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clickable(onClick = onOk),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = okText,
                                color = okColor,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberWeDialog(
    okText: String = "确定",
    cancelText: String = "取消",
    okColor: Color = LinkColor,
    closeOnAction: Boolean = true
): WeDialogState {
    val visibleState = remember { mutableStateOf(false) }
    val titleState = remember { mutableStateOf("") }
    val contentState = remember { mutableStateOf<String?>(null) }
    val onOkState = remember { mutableStateOf<(() -> Unit)?>(null) }
    val onCancelState = remember { mutableStateOf<(() -> Unit)?>(null) }

    fun close() {
        visibleState.value = false
    }

    WeDialog(
        visible = visibleState.value,
        title = titleState.value,
        content = contentState.value,
        okText = okText,
        cancelText = cancelText,
        okColor = okColor,
        onOk = {
            onOkState.value?.invoke()
            if (closeOnAction) {
                close()
            }
        },
        onCancel = if (onCancelState.value != null) {
            {
                onCancelState.value?.invoke()
                if (closeOnAction) {
                    close()
                }
            }
        } else null
    )

    return WeDialogState(visibleState.value, { title, content, onOk, onCancel ->
        titleState.value = title
        contentState.value = content
        onOkState.value = onOk
        onCancelState.value = onCancel
        visibleState.value = true
    }, {
        close()
    })
}

class WeDialogState(
    val visible: Boolean,
    private val showDialog: (title: String, content: String?, onOk: (() -> Unit)?, onCancel: (() -> Unit)?) -> Unit,
    private val hideDialog: () -> Unit
) {
    fun open(
        title: String,
        content: String? = null,
        onOk: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = {}
    ) {
        showDialog(title, content, onOk, onCancel)
    }

    fun hide() {
        hideDialog()
    }
}
