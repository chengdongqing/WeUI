package top.chengdongqing.weui.feature.demos.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.searchbar.WeSearchBar

@Composable
fun SearchBarScreen() {
    WeScreen(title = "SearchBar", description = "搜索栏") {
        var value by remember { mutableStateOf("") }

        WeSearchBar(value) {
            value = it
        }
    }
}