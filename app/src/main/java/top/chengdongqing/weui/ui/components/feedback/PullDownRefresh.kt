package top.chengdongqing.weui.ui.components.feedback

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.basic.WeLoading

private const val threshold = 250f

@Composable
fun WePullDownRefresh(
    onRefresh: (() -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val offsetY = remember { mutableStateOf(0f) }
    val tips by remember {
        derivedStateOf {
            deriveTips(offsetY.value, isRefreshing.value)
        }
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (offsetY.value > threshold && !isRefreshing.value) {
                            offsetY.value = threshold
                            isRefreshing.value = true
                            onRefresh {
                                isRefreshing.value = false
                            }
                        } else {
                            offsetY.value = 0f
                        }
                    }
                ) { _, dragAmount ->
                    if (!isRefreshing.value) {
                        val dampingFactor = calculateDampingFactor(offsetY.value)
                        offsetY.value += dragAmount * dampingFactor
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
            if (isRefreshing.value) {
                WeLoading()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(tips)
        }

        val offsetDp = with(LocalDensity.current) {
            offsetY.value.coerceAtLeast(0f).toDp()
        }
        Box(
            modifier = Modifier.offset(y = animateDpAsState(offsetDp).value)
        ) {
            content()
        }
    }

    LaunchedEffect(isRefreshing.value) {
        if (!isRefreshing.value) {
            offsetY.value = 0f
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
