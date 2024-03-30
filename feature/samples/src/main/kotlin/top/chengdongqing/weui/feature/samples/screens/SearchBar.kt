package top.chengdongqing.weui.feature.samples.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.searchbar.WeSearchBar

@Composable
fun SearchBarScreen() {
    WeScreen(
        title = "SearchBar",
        description = "搜索栏",
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        var value by remember { mutableStateOf("") }

        WeSearchBar(value) {
            value = it
        }
    }
}