package top.chengdongqing.weui.ui.components.swipeaction

import androidx.compose.ui.graphics.Color
import top.chengdongqing.weui.ui.theme.DangerColorLight
import top.chengdongqing.weui.ui.theme.PlainColorLight
import top.chengdongqing.weui.ui.theme.WarningColorLight

enum class DragAnchors {
    Start,
    Center,
    End
}

enum class SwipeActionStyle {
    LABEL,
    ICON
}

enum class SwipeActionType(val color: Color) {
    PLAIN(PlainColorLight),
    WARNING(WarningColorLight),
    DANGER(DangerColorLight)
}