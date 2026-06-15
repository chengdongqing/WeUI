package top.chengdongqing.weui.feature.feedback.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import top.chengdongqing.weui.core.ui.components.WeCardListItem
import top.chengdongqing.weui.core.ui.components.WeContextMenu
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.cardList
import top.chengdongqing.weui.core.ui.components.rememberContextMenuState
import top.chengdongqing.weui.core.ui.components.rememberDialogState
import top.chengdongqing.weui.core.ui.components.weContextMenu

@Composable
fun ContextMenuScreen(onBack: () -> Unit) {
    WeScreen(
        title = "ContextMenu",
        description = "上下文菜单",
        scrollEnabled = false,
        onBack = onBack
    ) {
        val menus = remember {
            listOf("标为未读", "置顶该聊天", "不显示该聊天", "删除该聊天")
        }
        val dialog = rememberDialogState()
        val contextMenuState = rememberContextMenuState()

        LazyColumn(modifier = Modifier.cardList()) {
            items(30) { index ->
                Box(
                    modifier = Modifier
                        .weContextMenu { position ->
                            contextMenuState.show(position, menus, index)
                        }
                ) {
                    WeCardListItem(label = "第${index + 1}项")
                }
            }
        }

        WeContextMenu(contextMenuState) { listIndex, menuIndex ->
            dialog.show(
                title = "你点击了第${listIndex + 1}项的“${menus[menuIndex]}”",
                onCancel = null
            )
        }
    }
}
