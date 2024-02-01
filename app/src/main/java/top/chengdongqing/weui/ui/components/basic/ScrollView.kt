package top.chengdongqing.weui.ui.components.basic

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColor1

private const val threshold = 250f

@Composable
fun WeScrollView(
    onRefresh: (() -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    val tips by remember {
        derivedStateOf {
            deriveTips(offsetY.floatValue, isRefreshing.value)
        }
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (offsetY.floatValue > threshold && !isRefreshing.value) {
                            offsetY.floatValue = threshold
                            isRefreshing.value = true
                            onRefresh {
                                offsetY.floatValue = 0f
                                isRefreshing.value = false
                            }
                        } else {
                            offsetY.floatValue = 0f
                        }
                    }
                ) { _, dragAmount ->
                    if (!isRefreshing.value) {
                        val dampingFactor = calculateDampingFactor(offsetY.floatValue)
                        offsetY.floatValue += dragAmount * dampingFactor
                    }
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WeLoading(isRotating = isRefreshing.value)
            Spacer(modifier = Modifier.width(8.dp))
            Text(tips, color = FontColor1, fontSize = 14.sp)
        }

        val offsetDp = with(LocalDensity.current) {
            offsetY.floatValue.coerceAtLeast(0f).toDp()
        }
        Box(
            modifier = Modifier.offset(
                y = animateDpAsState(
                    offsetDp,
                    label = "PullDownAnimation"
                ).value
            )
        ) {
            content()
        }
    }
}

private fun deriveTips(offset: Float, isRefreshing: Boolean): String {
    return when {
        isRefreshing -> "刷新中..."
        offset > threshold -> "释放立即刷新"
        offset > 0 -> "继续下拉执行刷新"
        else -> ""
    }
}

private fun calculateDampingFactor(offset: Float, maxDampingDistance: Float = 500f): Float {
    return if (offset < maxDampingDistance) {
        1f - (offset / maxDampingDistance)
    } else {
        0.01f
    }
}
