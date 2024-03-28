package top.chengdongqing.weui.core.ui.components.refreshview

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.filter
import top.chengdongqing.weui.core.ui.components.loading.WeLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeRefreshView(
    modifier: Modifier = Modifier,
    onRefresh: (suspend () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val refreshState = rememberPullToRefreshState()
    val refreshingTips = getRefreshingTips(refreshState, refreshState.positionalThreshold)

    LaunchedEffect(refreshState) {
        snapshotFlow { refreshState.isRefreshing }.filter { it }.collect {
            onRefresh?.invoke()
            refreshState.endRefresh()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection)
    ) {
        // 刷新的标识
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WeLoading(isRotating = refreshState.isRefreshing)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                refreshingTips,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp
            )
        }

        val animatedRefreshOffsetY by animateIntAsState(
            targetValue = refreshState.verticalOffset.toInt(),
            label = "ScrollViewRefreshOffsetYAnimation"
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset {
                    IntOffset(x = 0, y = animatedRefreshOffsetY)
                }
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun getRefreshingTips(state: PullToRefreshState, positionalThresholdPx: Float): String {
    return when {
        state.isRefreshing -> "刷新中..."
        state.verticalOffset > positionalThresholdPx -> "释放立即刷新"
        state.verticalOffset > 0 -> "继续下拉执行刷新"
        else -> "刷新中..."
    }
}