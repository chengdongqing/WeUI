package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.tabview.WeTabView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabViewScreen() {
    WeScreen(
        title = "TabView",
        description = "选项卡视图",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        val options = remember {
            List(10) { "Tab ${it + 1}" }
        }

        WeTabView(options) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (index + 1).toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 60.sp
                )
            }
        }
    }
}