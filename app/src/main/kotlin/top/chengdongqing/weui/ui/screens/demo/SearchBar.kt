package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.searchbar.WeSearchBar

@Composable
fun SearchBarScreen() {
    WeScreen(title = "SearchBar", description = "搜索栏") {
        val value = remember {
            mutableStateOf("")
        }

        WeSearchBar(value.value) {
            value.value = it
        }
    }
}