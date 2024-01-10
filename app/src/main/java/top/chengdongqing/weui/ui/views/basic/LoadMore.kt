package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.LoadMoreType
import top.chengdongqing.weui.ui.components.basic.WeLoadMore

@Composable
fun LoadMorePage() {
    Page(title = "LoadMore", description = "加载更多") {
        Column {
            WeLoadMore(type = LoadMoreType.LOADING)
            WeLoadMore(type = LoadMoreType.EMPTY_DATA)
            WeLoadMore(type = LoadMoreType.ALL_LOADED)
        }
    }
}