package top.chengdongqing.weui.feature.samples.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeCalendar
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.rememberCalendarState
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.localDate
import kotlin.time.Clock

@Composable
fun CalendarScreen(onBack: () -> Unit) {
    val calendarState = rememberCalendarState()
    val today = remember { Clock.System.localDate }

    WeScreen(
        title = "Calendar",
        description = "日历",
        padding = PaddingValues(0.dp),
        containerColor = WeTheme.colorScheme.surface,
        onBack = onBack
    ) {
        WeCalendar(calendarState)
        Spacer(modifier = Modifier.height(20.dp))

        if (calendarState.currentMonth != today) {
            WeButton(text = "回到今天", type = ButtonType.Plain) {
                calendarState.toToday()
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}