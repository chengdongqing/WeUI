package top.chengdongqing.weui.ui.screens.feedback

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import top.chengdongqing.weui.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.ui.components.contextmenu.rememberContextMenuState
import top.chengdongqing.weui.ui.components.dialog.DialogOptions
import top.chengdongqing.weui.ui.components.dialog.rememberWeDialog
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.toIntOffset

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
        val dialog = rememberWeDialog()
        val contextMenuState = rememberContextMenuState { listIndex, menuIndex ->
            dialog.show(
                DialogOptions(
                    title = "您点击的是第${listIndex + 1}项的“${menus[menuIndex]}”",
                    onCancel = null
                )
            )
        }

        WeCardList {
            items(30) { index ->
                var offset by remember { mutableStateOf(Offset.Zero) }
                Box(modifier = Modifier
                    .onGloballyPositioned {
                        offset = it.positionInParent()
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            contextMenuState.show((offset + it).toIntOffset(), menus, index)
                        })
                    }) {
                    WeCardListItem(label = "第${index + 1}项")
                }
            }
        }
    }
}