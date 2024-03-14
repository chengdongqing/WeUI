package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import top.chengdongqing.weui.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.refreshableview.WeRefreshableView
import top.chengdongqing.weui.ui.components.refreshableview.rememberReachBottom
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun RefreshableScreen() {
    WeScreen(title = "RefreshableView", description = "可刷新视图", scrollEnabled = false) {
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

        WeRefreshableView(onRefresh = {
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