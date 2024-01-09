package top.chengdongqing.weui.ui.views.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.search.WeSearchBar

@Composable
fun SearchBarPage() {
    Page(title = "SearchBar", description = "搜索栏") {
        val value = remember {
            mutableStateOf("")
        }

        WeSearchBar(value.value) {
            value.value = it
        }
    }
}