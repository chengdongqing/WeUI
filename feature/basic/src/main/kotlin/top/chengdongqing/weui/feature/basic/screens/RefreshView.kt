package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.refreshview.WeRefreshView
import top.chengdongqing.weui.core.ui.components.refreshview.rememberReachBottom
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun RefreshViewScreen() {
    WeScreen(title = "RefreshView", description = "可刷新视图", scrollEnabled = false) {
        val listState = rememberLazyListState()
        val listItems = remember {
            mutableStateListOf<String>().apply {
                addAll(List(30) { "${it + 1}" })
            }
        }
        val isLoadingMore = rememberReachBottom(listState) {
            delay(2000)
            listItems.addAll(List(30) { index -> "${listItems.size + index + 1}" })
        }

        WeRefreshView(onRefresh = {
            delay(2000)
            listItems.clear()
            listItems.addAll(List(30) { "${it + 1}" })
        }) {
            WeCardList(state = listState) {
                items(listItems, key = { it }) {
                    WeCardListItem(label = "第${it}行")
                }
                if (isLoadingMore) {
                    item {
                        WeLoadMore()
                    }
                }
            }
        }
    }
}