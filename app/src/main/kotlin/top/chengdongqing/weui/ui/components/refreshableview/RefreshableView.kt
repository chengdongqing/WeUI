package top.chengdongqing.weui.ui.components.refreshableview

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.theme.FontSecondaryColorLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeRefreshableView(
    modifier: Modifier = Modifier,
    onRefresh: (suspend () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val scrollState = rememberPullToRefreshState()
    val tips = getRefreshTips(scrollState, scrollState.positionalThreshold)

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isRefreshing }.collect {
            if (it) {
                onRefresh?.invoke()
                scrollState.endRefresh()
            }
        }
    }

    Box(modifier = modifier.nestedScroll(scrollState.nestedScrollConnection)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WeLoading(isRotating = scrollState.isRefreshing)
            Spacer(modifier = Modifier.width(8.dp))
            Text(tips, color = FontSecondaryColorLight, fontSize = 14.sp)
        }

        val animatedOffsetY by animateIntAsState(
            targetValue = scrollState.verticalOffset.toInt(),
            label = "ScrollViewOffsetYAnimation"
        )
        Box(
            modifier = Modifier.offset {
                IntOffset(x = 0, y = animatedOffsetY)
            }
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun getRefreshTips(state: PullToRefreshState, positionalThresholdPx: Float): String {
    return when {
        state.isRefreshing -> "刷新中..."
        state.verticalOffset > positionalThresholdPx -> "释放立即刷新"
        state.verticalOffset > 0 -> "继续下拉执行刷新"
        else -> ""
    }
}

@Composable
fun rememberReachBottom(listState: LazyListState, onReachBottom: suspend () -> Unit): Boolean {
    val loadingMore = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { index ->
            if (index == listState.layoutInfo.totalItemsCount - 1) {
                loadingMore.value = true
                onReachBottom()
                loadingMore.value = false
            }
        }
    }

    return loadingMore.value
}