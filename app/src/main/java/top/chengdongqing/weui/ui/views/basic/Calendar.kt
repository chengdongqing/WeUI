package top.chengdongqing.weui.ui.views.basic

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.basic.WeCalendar
import top.chengdongqing.weui.ui.components.basic.WePage

@Composable
fun CalendarPage() {
    WePage(title = "Calendar", description = "日历") {
        WeCalendar()
    }
}