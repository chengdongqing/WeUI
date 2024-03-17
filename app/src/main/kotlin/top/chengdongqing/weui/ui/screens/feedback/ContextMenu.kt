package top.chengdongqing.weui.ui.screens.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import top.chengdongqing.weui.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.ui.components.contextmenu.contextMenu
import top.chengdongqing.weui.ui.components.contextmenu.rememberContextMenuState
import top.chengdongqing.weui.ui.components.dialog.rememberDialogState
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun ContextMenuScreen() {
    WeScreen(
        title = "ContextMenu",
        description = "上下文菜单",
        scrollEnabled = false
    ) {
        val menus = remember {
            listOf("标为未读", "置顶该聊天", "不显示该聊天", "删除该聊天")
        }
        val dialog = rememberDialogState()
        val contextMenuState = rememberContextMenuState { listIndex, menuIndex ->
            dialog.show(
                title = "你点击了第${listIndex + 1}项的“${menus[menuIndex]}”",
                onCancel = null
            )
        }

        WeCardList {
            items(30) { index ->
                Box(
                    modifier = Modifier
                        .contextMenu { position ->
                            contextMenuState.show(position, menus, index)
                        }
                ) {
                    WeCardListItem(label = "第${index + 1}项")
                }
            }
        }
    }
}