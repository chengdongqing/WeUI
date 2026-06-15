package top.chengdongqing.weui.feature.samples.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.WeSearchBar
import top.chengdongqing.weui.core.ui.theme.WeTheme

@Composable
fun SearchBarScreen(onBack: () -> Unit) {
    WeScreen(
        title = "SearchBar",
        description = "搜索栏",
        containerColor = WeTheme.colorScheme.surface,
        onBack = onBack
    ) {
        var value by remember { mutableStateOf("") }

        WeSearchBar(value) {
            value = it
        }
    }
}