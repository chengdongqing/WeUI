package top.chengdongqing.weui.ui.screens.demo.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.PrimaryColor
import java.time.ZoneId

@Composable
fun ClockScreen() {
    WeScreen(title = "Clock", description = "时钟", padding = PaddingValues(0.dp)) {
        val timeZones = remember {
            listOf(
                TimeZoneItem("上海（中国）", "Asia/Shanghai", BorderColor),
                TimeZoneItem("莫斯科（俄罗斯）", "Europe/Moscow", Color.Black),
                TimeZoneItem("阿姆斯特丹（荷兰）", "Europe/Amsterdam", PrimaryColor),
                TimeZoneItem("圣保罗（巴西）", "America/Sao_Paulo", Color.Yellow),
                TimeZoneItem("洛杉矶（美国）", "America/Los_Angeles", Color.Magenta),
                TimeZoneItem("悉尼（澳大利亚）", "Australia/Sydney", Color.Cyan)
            )
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            items(timeZones) {
                WeClock(ZoneId.of(it.zoneId), it.color)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = it.name, color = FontColor1, fontSize = 14.sp)
            }
        }
    }
}

private data class TimeZoneItem(
    val name: String,
    val zoneId: String,
    val color: Color
)