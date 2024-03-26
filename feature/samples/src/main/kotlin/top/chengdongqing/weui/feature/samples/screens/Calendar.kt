package top.chengdongqing.weui.feature.samples.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.samples.components.WeCalendar
import top.chengdongqing.weui.feature.samples.components.rememberCalendarState

@Composable
fun CalendarScreen() {
    WeScreen(
        title = "Calendar",
        description = "日历",
        padding = PaddingValues(0.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val calendarState = rememberCalendarState()

        WeCalendar(calendarState)
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "回到今天", type = ButtonType.PLAIN) {
            calendarState.toToday()
        }
    }
}