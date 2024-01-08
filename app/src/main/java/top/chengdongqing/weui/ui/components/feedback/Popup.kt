package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun WePopup(
    visible: Boolean,
    onClose: () -> Unit,
    title: String? = null,
    content: @Composable () -> Unit
) {
    var localVisible by remember {
        mutableStateOf(visible)
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(300)
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
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                title?.also {
                                    Text(text = it, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Box(modifier = Modifier.padding(16.dp)) {
                                content()
                            }
                        }
                    }
                }
            }
        }
    }
}
