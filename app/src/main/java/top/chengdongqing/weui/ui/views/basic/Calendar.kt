package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.basic.WeCalendar
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.basic.rememberCalendarState
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun CalendarPage() {
    WePage(title = "Calendar", description = "日历") {
        val calendarState = rememberCalendarState()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeCalendar(calendarState)
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "回到今天", type = ButtonType.PLAIN) {
                calendarState.toToday()
            }
        }
    }
}