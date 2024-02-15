package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.badge.WeBadge
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.page.WePage

@Composable
fun BadgePage() {
    WePage(title = "Badge", description = "徽章") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            WeBadge {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge("8", size = 20.dp) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge("New", size = 20.dp, alignment = Alignment.BottomEnd) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge(alignment = Alignment.TopStart, size = 5.dp) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge(
                "8",
                size = 20.dp,
                color = Color(0xFFFF6700),
                alignment = Alignment.BottomStart
            ) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge("New", size = 20.dp, alignment = Alignment.CenterEnd) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge(alignment = Alignment.CenterEnd) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeBadge(alignment = Alignment.CenterStart) {
                WeButton(text = "按钮")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "Java开发工程师")
                WeBadge("New", 20.dp, alignment = Alignment.Center)
            }
        }
    }
}