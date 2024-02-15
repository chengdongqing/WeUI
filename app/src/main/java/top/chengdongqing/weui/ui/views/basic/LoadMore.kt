package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.page.WePage

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