package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.basic.WeScrollView
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun ScrollViewPage() {
    WePage(title = "ScrollView", description = "可滚动视图") {
        val coroutineScope = rememberCoroutineScope()

        WeScrollView(onRefresh = {
            coroutineScope.launch {
                delay(3000)
                it()
            }
        }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "下拉触发刷新", color = PrimaryColor)
            }
        }
    }
}
