package top.chengdongqing.weui.feature.form.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.rate.WeRate
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun RateScreen() {
    WeScreen(
        title = "Rate",
        description = "评分",
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var value by remember { mutableFloatStateOf(4f) }

        DemoItem("基本用法") {
            WeRate(value) {
                value = it
            }
        }
        DemoItem("设置数量") {
            WeRate(value, count = 8) {
                value = it
            }
        }
        DemoItem("支持半星") {
            WeRate(value, allowHalf = true) {
                value = it
            }
        }
        DemoItem("不可修改") {
            WeRate(value)
        }
    }
}

@Composable
private fun DemoItem(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(4.dp))
            .padding(vertical = 18.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}