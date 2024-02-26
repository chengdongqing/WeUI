package top.chengdongqing.weui.ui.components.digitalkeyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.theme.BackgroundColorLight
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeDigitalKeyboard(
    allowDecimal: Boolean = true,
    isEmpty: Boolean = true,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onKeyClick: (String) -> Unit
) {
    var widthPerItem by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val popupPositionProvider = remember {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                return IntOffset(0, windowSize.height)
            }
        }
    }

    Popup(popupPositionProvider) {
        Column {
            WeDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColorLight)
                    .padding(8.dp)
                    .onSizeChanged {
                        widthPerItem = density.run { (it.width.toDp() - 8.dp * 3) / 4 }
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DigitalGrid(widthPerItem, allowDecimal, onKeyClick)
                ActionBar(widthPerItem, isEmpty, onBack, onConfirm)
            }
        }
    }
}

@Composable
private fun ActionBar(
    widthPerItem: Dp,
    isEmpty: Boolean,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .width(widthPerItem)
                .height(50.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .clickable {
                    onBack()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Backspace, contentDescription = "回退")
        }
        Box(
            modifier = Modifier
                .width(widthPerItem)
                .height((50 * 3 + 8 * 2).dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (isEmpty) PrimaryColor.copy(0.4f) else PrimaryColor)
                .clickable(enabled = !isEmpty) { onConfirm() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "确定", color = Color.White, fontSize = 17.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RowScope.DigitalGrid(
    widthPerItem: Dp,
    allowDecimal: Boolean,
    onClick: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3
    ) {
        repeat(9) { index ->
            val value = (index + 1).toString()
            KeyItem(
                key = value,
                modifier = Modifier.weight(1f)
            ) {
                onClick(value)
            }
        }
        KeyItem(
            key = "0",
            modifier = Modifier.width(widthPerItem * 2 + 8.dp)
        ) {
            onClick("0")
        }
        KeyItem(
            key = if (allowDecimal) "." else "",
            modifier = Modifier.weight(1f),
            clickable = allowDecimal
        ) {
            onClick(".")
        }
    }
}

@Composable
private fun KeyItem(
    key: String,
    modifier: Modifier,
    clickable: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .clickable(enabled = clickable) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = key,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}