package top.chengdongqing.weui.ui.screens.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.chengdongqing.weui.ui.components.contextmenu.WeContextMenu
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun ContextMenuScreen() {
    WeScreen(
        title = "ContextMenu",
        description = "上下文菜单",
        scrollEnabled = false
    ) {
        val menus = remember {
            listOf("菜单1", "菜单2", "菜单3", "菜单4")
        }

        WeContextMenu(menus)
    }
}