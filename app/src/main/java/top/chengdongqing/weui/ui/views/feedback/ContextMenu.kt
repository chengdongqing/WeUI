package top.chengdongqing.weui.ui.views.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.WeContextMenu

@Composable
fun ContextMenuPage() {
    WePage(title = "ContextMenu", description = "上下文菜单") {
        val menus = remember {
            listOf("菜单1", "菜单2", "菜单3", "菜单4")
        }

        WeContextMenu(menus)
    }
}