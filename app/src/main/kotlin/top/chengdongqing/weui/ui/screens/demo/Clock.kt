package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.clock.WeClock
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.BorderColorLight
import top.chengdongqing.weui.ui.theme.PrimaryColor
import java.time.ZoneId

@Composable
fun ClockScreen() {
    WeScreen(
        title = "Clock",
        description = "时钟",
        padding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        val timeZoneList = remember {
            listOf(
                TimeZoneItem("上海（中国）", "Asia/Shanghai", BorderColorLight),
                TimeZoneItem("莫斯科（俄罗斯）", "Europe/Moscow", Color.Black),
                TimeZoneItem("阿姆斯特丹（荷兰）", "Europe/Amsterdam", PrimaryColor),
                TimeZoneItem("圣保罗（巴西）", "America/Sao_Paulo", Color.Yellow),
                TimeZoneItem("洛杉矶（美国）", "America/Los_Angeles", Color.Magenta),
                TimeZoneItem("悉尼（澳大利亚）", "Australia/Sydney", Color.Cyan)
            )
        }

        repeat(timeZoneList.size) { index ->
            val item = timeZoneList[index]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                WeClock(ZoneId.of(item.zoneId), item.color)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private data class TimeZoneItem(
    val name: String,
    val zoneId: String,
    val color: Color
)