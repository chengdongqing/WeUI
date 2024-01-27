package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.basic.LoadMoreType
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.basic.WePage

@Composable
fun LoadMorePage() {
    WePage(title = "LoadMore", description = "加载更多") {
        Column {
            WeLoadMore(type = LoadMoreType.LOADING)
            WeLoadMore(type = LoadMoreType.EMPTY_DATA)
            WeLoadMore(type = LoadMoreType.ALL_LOADED)
        }
    }
}