package top.chengdongqing.weui.ui.screens.demo.indexedlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.indexedlist.WeIndexedList
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun IndexedListScreen() {
    WeScreen(
        title = "IndexedList",
        description = "索引列表",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        WeIndexedList(cities)
    }
}