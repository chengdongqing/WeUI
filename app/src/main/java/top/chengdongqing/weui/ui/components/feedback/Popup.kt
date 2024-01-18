package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import top.chengdongqing.weui.utils.clickableWithoutRipple

/**
 * 从底部弹出的半屏弹窗
 *
 * @param visible 是否显示
 * @param onClose 关闭事件
 * @param padding 内边距
 * @param title 标题
 * @param content 内容
 */
@Composable
fun WePopup(
    visible: Boolean,
    onClose: () -> Unit,
    padding: PaddingValues = PaddingValues(12.dp),
    title: String? = null,
    content: @Composable () -> Unit
) {
    var localVisible by remember {
        mutableStateOf(visible)
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(350)
        }
        localVisible = visible
    }

    if (visible || localVisible) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickableWithoutRipple {
                        onClose()
                    },
                contentAlignment = Alignment.BottomStart
            ) {
                AnimatedVisibility(
                    visible = visible && localVisible,
                    enter = slideIn(
                        animationSpec = tween(300),
                        initialOffset = { IntOffset(0, it.height) }),
                    exit = slideOut(
                        animationSpec = tween(300),
                        targetOffset = { IntOffset(0, it.height) })
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(Color.White)
                            .clickableWithoutRipple { }
                            .padding(padding)
                    ) {
                        Column {
                            title?.let {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = it, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            content()
                        }
                    }
                }
            }
        }
    }
}
