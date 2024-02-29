package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun LoadMoreScreen() {
    WeScreen(title = "LoadMore", description = "加载更多") {
        WeLoadMore(type = LoadMoreType.LOADING)
        WeLoadMore(type = LoadMoreType.EMPTY_DATA)
        WeLoadMore(type = LoadMoreType.ALL_LOADED)
    }
}