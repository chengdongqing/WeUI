package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.WePullDownRefresh
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun PullDownRefreshPage() {
    WePage(title = "PullDownRefresh", description = "下拉刷新") {
        val coroutineScope = rememberCoroutineScope()

        WePullDownRefresh(onRefresh = {
            coroutineScope.launch {
                delay(3000)
                it()
            }
        }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "下拉触发刷新", color = PrimaryColor)
            }
        }
    }
}

@Composable
fun LazyColumnWithLoadMore(
    listState: LazyListState,
    loadMore: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }

    LazyColumn(state = listState) {
        content()

        item {
            if (isScrolledToEnd(listState) && !isLoading.value) {
                isLoading.value = true
                WeLoadMore()
                loadMore()
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { isScrolledToEnd(listState) }
            .filter { it }
            .collect {
                if (!isLoading.value) {
                    isLoading.value = true
                    loadMore()
                }
            }
    }
}

private fun isScrolledToEnd(state: LazyListState): Boolean {
    val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull()
    val totalItemCount = state.layoutInfo.totalItemsCount
    return lastVisibleItem != null && lastVisibleItem.index >= totalItemCount - 1
}
