package top.chengdongqing.weui.ui.screens.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.rate.WeRate
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun RateScreen() {
    WeScreen(title = "Rate", description = "评分") {
        var value by remember { mutableFloatStateOf(4f) }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            item {
                DemoItem("基本用法") {
                    WeRate(value) {
                        value = it
                    }
                }
            }
            item {
                DemoItem("设置数量") {
                    WeRate(value, count = 8) {
                        value = it
                    }
                }
            }
            item {
                DemoItem("支持半星") {
                    WeRate(value, allowHalf = true) {
                        value = it
                    }
                }
            }
            item {
                DemoItem("不可修改") {
                    WeRate(value)
                }
            }
        }
    }
}

@Composable
private fun DemoItem(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(4.dp))
            .padding(vertical = 18.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        content()
    }
}

@Preview
@Composable
private fun PreviewRate() {
    RateScreen()
}