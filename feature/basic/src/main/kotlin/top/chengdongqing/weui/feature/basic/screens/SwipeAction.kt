package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.DragAnchor
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.swipeaction.SwipeActionItem
import top.chengdongqing.weui.core.ui.components.swipeaction.SwipeActionStyle
import top.chengdongqing.weui.core.ui.components.swipeaction.SwipeActionType
import top.chengdongqing.weui.core.ui.components.swipeaction.WeSwipeAction
import top.chengdongqing.weui.core.ui.components.swipeaction.rememberSwipeActionState
import top.chengdongqing.weui.core.ui.components.toast.ToastState
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState

@Composable
fun SwipeActionScreen() {
    WeScreen(
        title = "SwipeAction",
        description = "滑动操作"
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
        val toast = rememberToastState()

        LabelStyleDemo(options, toast)
        Spacer(modifier = Modifier.height(40.dp))
        IconStyleDemo(options, toast)
        Spacer(modifier = Modifier.height(40.dp))
        ControllableDemo(options, toast)
    }
}

@Composable
private fun LabelStyleDemo(options: List<SwipeActionItem>, toast: ToastState) {
    WeSwipeAction(
        startOptions = options.slice(0..1),
        endOptions = options,
        onStartTap = {
            toast.show("你点击了左边的${options[it].label}")
        },
        onEndTap = {
            toast.show("你点击了右边的${options[it].label}")
        }
    ) {
        Text(text = "文字按钮（左右滑动）", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun IconStyleDemo(options: List<SwipeActionItem>, toast: ToastState) {
    WeSwipeAction(
        startOptions = options,
        endOptions = options,
        style = SwipeActionStyle.ICON,
        height = 70.dp,
        onStartTap = {
            toast.show("你点击了左边的${options[it].label}")
        },
        onEndTap = {
            toast.show("你点击了右边的${options[it].label}")
        }
    ) {
        Text(text = "图标按钮（左右滑动）", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun ControllableDemo(options: List<SwipeActionItem>, toast: ToastState) {
    val options1 = remember { options.slice(1..2) }
    val swipeActionState = rememberSwipeActionState(
        initialValue = DragAnchor.End,
        endActionCount = options1.size
    )
    val coroutineScope = rememberCoroutineScope()

    WeButton(text = "切换状态", type = ButtonType.PLAIN) {
        coroutineScope.launch {
            val value = if (swipeActionState.draggableState.currentValue == DragAnchor.End) {
                DragAnchor.Center
            } else {
                DragAnchor.End
            }
            swipeActionState.draggableState.animateTo(value)
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    WeSwipeAction(
        startOptions = options1,
        endOptions = options1,
        swipeActionState = swipeActionState,
        onStartTap = {
            toast.show("你点击了左边的${options1[it].label}")
        },
        onEndTap = {
            toast.show("你点击了右边的${options1[it].label}")
        }
    ) {
        Text(text = "变量控制", color = MaterialTheme.colorScheme.onPrimary)
    }
}