package top.chengdongqing.weui.basic.ui

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.components.loading.LoadMoreType
import top.chengdongqing.weui.components.loading.WeLoadMore

@Composable
fun LoadMoreScreen(onBack: () -> Unit) {
    WeScreen(
        title = "LoadMore",
        description = "加载更多",
        onBack = onBack
    ) {
        WeLoadMore(type = LoadMoreType.Loading)
        WeLoadMore(type = LoadMoreType.EmptyData)
        WeLoadMore(type = LoadMoreType.AllLoaded)
    }
}