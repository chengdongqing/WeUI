package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.enum.DragAnchors
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.swipeaction.SwipeActionItem
import top.chengdongqing.weui.ui.components.swipeaction.SwipeActionStyle
import top.chengdongqing.weui.ui.components.swipeaction.SwipeActionType
import top.chengdongqing.weui.ui.components.swipeaction.WeSwipeAction
import top.chengdongqing.weui.ui.components.swipeaction.rememberAnchoredDraggableState
import top.chengdongqing.weui.utils.rememberToggleState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeActionScreen() {
    WeScreen(
        title = "SwipeAction",
        description = "滑动操作",
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        val options = remember {
            listOf(
                SwipeActionItem(
                    type = SwipeActionType.PLAIN,
                    label = "喜欢",
                    icon = Icons.Outlined.FavoriteBorder
                ),
                SwipeActionItem(
                    type = SwipeActionType.WARNING,
                    label = "收藏",
                    icon = Icons.Outlined.StarOutline
                ),
                SwipeActionItem(
                    type = SwipeActionType.DANGER,
                    label = "删除",
                    icon = Icons.Outlined.Delete
                )
            )
        }

        WeSwipeAction(
            startOptions = options.slice(0..1),
            endOptions = options
        ) {
            Text(text = "文字按钮（左右滑动）", color = MaterialTheme.colorScheme.onPrimary)
        }
        WeSwipeAction(
            startOptions = options,
            endOptions = options,
            style = SwipeActionStyle.ICON,
            height = 70.dp
        ) {
            Text(text = "图标按钮（左右滑动）", color = MaterialTheme.colorScheme.onPrimary)
        }

        val options1 = remember { options.slice(1..2) }
        val (anchor, toggleAnchor) = rememberToggleState(
            defaultValue = DragAnchors.End,
            reverseValue = DragAnchors.Center,
        )
        val swipeActionState = rememberAnchoredDraggableState(
            initialValue = anchor,
            endActionCount = options1.size
        )
        val coroutineScope = rememberCoroutineScope()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "切换状态", type = ButtonType.PLAIN) {
                coroutineScope.launch {
                    swipeActionState.state.animateTo(toggleAnchor())
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeSwipeAction(
                startOptions = options1,
                endOptions = options1,
                swipeActionState = swipeActionState
            ) {
                Text(text = "变量控制", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}