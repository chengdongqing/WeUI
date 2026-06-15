package top.chengdongqing.weui.feature.basic.ui

import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.DragAnchor
import top.chengdongqing.weui.core.ui.components.SwipeActionItem
import top.chengdongqing.weui.core.ui.components.SwipeActionStyle
import top.chengdongqing.weui.core.ui.components.SwipeActionType
import top.chengdongqing.weui.core.ui.components.ToastState
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.WeSwipeAction
import top.chengdongqing.weui.core.ui.components.rememberSwipeActionState
import top.chengdongqing.weui.core.ui.components.rememberToastState
import top.chengdongqing.weui.core.ui.theme.WeTheme

@Composable
fun SwipeActionScreen(onBack: () -> Unit) {
    WeScreen(
        title = "SwipeAction",
        description = "滑动操作",
        onBack = onBack
    ) {
        val options = remember {
            listOf(
                SwipeActionItem(
                    type = SwipeActionType.Plain,
                    label = "喜欢",
                    icon = Icons.Outlined.FavoriteBorder
                ),
                SwipeActionItem(
                    type = SwipeActionType.Warning,
                    label = "收藏",
                    icon = Icons.Outlined.StarOutline
                ),
                SwipeActionItem(
                    type = SwipeActionType.Danger,
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
private fun LabelStyleDemo(
    options: List<SwipeActionItem>,
    toast: ToastState
) {
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
        Text(
            text = "文字按钮（左右滑动）",
            color = WeTheme.colorScheme.textPrimary
        )
    }
}

@Composable
private fun IconStyleDemo(
    options: List<SwipeActionItem>,
    toast: ToastState
) {
    WeSwipeAction(
        startOptions = options,
        endOptions = options,
        style = SwipeActionStyle.Icon,
        height = 70.dp,
        onStartTap = {
            toast.show("你点击了左边的${options[it].label}")
        },
        onEndTap = {
            toast.show("你点击了右边的${options[it].label}")
        }
    ) {
        Text(
            text = "图标按钮（左右滑动）",
            color = WeTheme.colorScheme.textPrimary
        )
    }
}

@Composable
private fun ControllableDemo(
    options: List<SwipeActionItem>,
    toast: ToastState
) {
    val currentOptions = remember { options.slice(1..2) }
    val swipeActionState = rememberSwipeActionState(
        initialValue = DragAnchor.End,
        endActionCount = currentOptions.size
    )
    val coroutineScope = rememberCoroutineScope()

    WeButton(
        text = "切换状态",
        type = ButtonType.Plain
    ) {
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
        startOptions = currentOptions,
        endOptions = currentOptions,
        swipeActionState = swipeActionState,
        onStartTap = {
            toast.show("你点击了左边的${currentOptions[it].label}")
        },
        onEndTap = {
            toast.show("你点击了右边的${currentOptions[it].label}")
        }
    ) {
        Text(
            text = "变量控制",
            color = WeTheme.colorScheme.textPrimary
        )
    }
}