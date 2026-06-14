package top.chengdongqing.weui.basic.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.delay
import top.chengdongqing.weui.components.WeCardListItem
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.components.cardList
import top.chengdongqing.weui.components.loading.WeLoadMore
import top.chengdongqing.weui.components.refreshview.WeRefreshView
import top.chengdongqing.weui.components.refreshview.rememberLoadMoreState
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RefreshViewScreen(onBack: () -> Unit) {
    WeScreen(
        title = "RefreshView",
        description = "可刷新视图",
        scrollEnabled = false,
        onBack = onBack
    ) {
        val listState = rememberLazyListState()
        val listItems = remember {
            mutableStateListOf<String>().apply {
                addAll(List(30) { "${it + 1}" })
            }
        }
        val loadMoreState = rememberLoadMoreState {
            delay(2000.milliseconds)
            listItems.addAll(List(30) { index -> "${listItems.size + index + 1}" })
        }

        WeRefreshView(
            modifier = Modifier.nestedScroll(loadMoreState.nestedScrollConnection),
            onRefresh = {
                delay(2000.milliseconds)
                listItems.clear()
                listItems.addAll(List(30) { "${it + 1}" })
            }
        ) {
            LazyColumn(state = listState, modifier = Modifier.cardList()) {
                items(listItems, key = { it }) {
                    WeCardListItem(label = "第${it}行")
                }
                item {
                    if (loadMoreState.isLoadingMore) {
                        WeLoadMore(listState = listState)
                    }
                }
            }
        }
    }
}