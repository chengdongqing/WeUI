package top.chengdongqing.weui.ui.views.demo.indexedlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.data.cities
import top.chengdongqing.weui.ui.components.indexedlist.WeIndexedList
import top.chengdongqing.weui.ui.components.page.WePage

@Composable
fun IndexedListPage() {
    WePage(
        title = "IndexedList",
        description = "索引列表",
        padding = PaddingValues(0.dp)
    ) {
        WeIndexedList(cities)
    }
}