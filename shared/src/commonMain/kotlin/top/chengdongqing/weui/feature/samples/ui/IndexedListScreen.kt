package top.chengdongqing.weui.feature.samples.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.indexedlist.WeIndexedList
import top.chengdongqing.weui.feature.samples.data.provider.CityDataProvider

@Composable
fun IndexedListScreen(onBack: () -> Unit) {
    WeScreen(
        title = "IndexedList",
        description = "索引列表",
        padding = PaddingValues(0.dp),
        scrollEnabled = false,
        onBack = onBack
    ) {
        WeIndexedList(labels = CityDataProvider.cities)
    }
}