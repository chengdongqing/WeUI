package top.chengdongqing.weui.feature.demos.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.demos.data.CityDataProvider
import top.chengdongqing.weui.ui.components.indexedlist.WeIndexedList

@Composable
fun IndexedListScreen() {
    WeScreen(
        title = "IndexedList",
        description = "索引列表",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        WeIndexedList(CityDataProvider.cities)
    }
}