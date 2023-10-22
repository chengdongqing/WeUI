package top.chengdongqing.weui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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

@Composable
fun WeDialog(
    visible: Boolean,
    title: String,
    content: String? = null,
    okText: String = "确定",
    cancelText: String = "取消",
    okColor: Color = Color(0xFF576B95),
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
                            color = Color(0f, 0f, 0f, 0.55f),
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
                                Text(text = cancelText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
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
fun WeDialogHolder(
    title: String,
    content: String? = null,
    okText: String = "确定",
    cancelText: String = "取消",
    okColor: Color = Color(0xFF576B95),
    onOk: (MutableState<Boolean>) -> Unit,
    onCancel: ((MutableState<Boolean>) -> Unit)? = null,
    holder: @Composable (MutableState<Boolean>) -> Unit
) {
    val visible = remember { mutableStateOf(false) }

    holder(visible)

    WeDialog(
        visible = visible.value,
        title = title,
        content = content,
        okText = okText,
        cancelText = cancelText,
        okColor = okColor,
        onOk = {
            onOk(visible)
        },
        onCancel = if (onCancel != null) {
            {
                onCancel(visible)
            }
        } else null
    )
}
