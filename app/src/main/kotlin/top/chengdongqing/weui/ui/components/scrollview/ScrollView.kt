package top.chengdongqing.weui.ui.components.scrollview

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.theme.FontSecondaryColorLight

private const val threshold = 250f

@Composable
fun WeScrollView(
    onRefresh: (suspend () -> Unit)? = null,
    onReachBottom: (suspend () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val tips by remember {
        derivedStateOf {
            deriveTips(offsetY, isRefreshing)
        }
    }

    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val dampingFactor = calculateDampingFactor(offsetY)
                offsetY += available.y * dampingFactor

                scope.launch {
                    onRefresh?.invoke()
                }
                return super.onPreScroll(available, source)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // 上拉加载更多逻辑
                if (available.y < 0) {
                    if (!isLoadingMore) {
                        scope.launch {
                            isLoadingMore = true
                            onReachBottom?.invoke()
                            isLoadingMore = false
                        }
                    }
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    Box(modifier = Modifier.nestedScroll(scrollConnection)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WeLoading(isRotating = isRefreshing)
            Spacer(modifier = Modifier.width(8.dp))
            Text(tips, color = FontSecondaryColorLight, fontSize = 14.sp)
        }

        val offsetDp = with(LocalDensity.current) {
            offsetY.coerceAtLeast(0f).toDp()
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