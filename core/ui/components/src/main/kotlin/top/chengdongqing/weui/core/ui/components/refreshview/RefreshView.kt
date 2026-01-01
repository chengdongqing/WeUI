package top.chengdongqing.weui.core.ui.components.refreshview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.loading.WeLoading

@Composable
fun WeRefreshView(
    modifier: Modifier = Modifier,
    onRefresh: (suspend () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshingTips = getRefreshingTips(isRefreshing, state)

    val handleRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            onRefresh?.invoke()
            isRefreshing = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 刷新的标识
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WeLoading(isRotating = isRefreshing)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                refreshingTips,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .pullToRefresh(isRefreshing, state, onRefresh = handleRefresh)
                .graphicsLayer {
                    val positionalThreshold = PullToRefreshDefaults.PositionalThreshold.roundToPx()
                    translationY = state.distanceFraction * positionalThreshold
                }
        ) {
            content()
        }
    }
}

private fun getRefreshingTips(isRefreshing: Boolean, state: PullToRefreshState): String {
    return when {
        isRefreshing -> "刷新中..."
        state.distanceFraction > 1.0f -> "释放立即刷新"
        state.distanceFraction > 0f -> "继续下拉执行刷新"
        else -> "刷新中..."
    }
}