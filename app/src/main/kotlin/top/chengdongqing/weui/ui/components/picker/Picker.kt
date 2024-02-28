package top.chengdongqing.weui.ui.components.picker

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.popup.WePopup
import kotlin.math.roundToInt

@Composable
fun WePicker(
    visible: Boolean,
    title: String? = null,
    range: Array<List<String>>,
    value: Array<Int>,
    onColumnChange: ((column: Int, value: Int, fullValue: Array<Int>) -> Unit)? = null,
    onChange: (Array<Int>) -> Unit,
    onCancel: () -> Unit
) {
    val localValue = remember(visible) {
        value.copyOf()
    }

    WePopup(
        visible, title = title,
        enterTransition = fadeIn() + slideInVertically { it / 2 },
        exitTransition = fadeOut() + slideOutVertically { it / 3 },
        onClose = onCancel
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val indicatorColor = if (isSystemInDarkTheme()) Color(0xff202020) else Color(0xFFF7F7F7)

            Box(modifier = Modifier
                .height(280.dp)
                .drawBehind {
                    // 指示栏
                    drawRoundRect(
                        color = indicatorColor,
                        topLeft = Offset(0f, size.height / 2 - 56.dp.toPx() / 2),
                        size = Size(size.width, 56.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                }
            ) {
                // 数据列表
                Row(verticalAlignment = Alignment.CenterVertically) {
                    range.forEachIndexed { index, options ->
                        PickerColumn(
                            modifier = Modifier.weight(1f),
                            options = options,
                            index = localValue[index]
                        ) {
                            localValue[index] = it
                            onColumnChange?.invoke(index, it, localValue)
                        }
                    }
                }
                // 遮罩
                PickerMask()
            }
            Spacer(modifier = Modifier.height(56.dp))
            Row {
                WeButton(text = "取消", type = ButtonType.PLAIN, width = 144.dp) {
                    onCancel()
                }
                Spacer(modifier = Modifier.width(20.dp))
                WeButton(text = "确定", width = 144.dp) {
                    onChange(localValue)
                    onCancel()
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PickerColumn(
    modifier: Modifier,
    options: List<String>,
    index: Int,
    onChange: (Int) -> Unit
) {
    val itemHeight = 56.dp
    val listState = rememberLazyListState(index)
    val snapFlingBehavior = rememberSnapFlingBehavior(listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                if (it != index) {
                    onChange(it)
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = (280.dp - itemHeight) / 2),
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = snapFlingBehavior
    ) {
        items(options.size) {
            val item = options[it]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item, color = MaterialTheme.colorScheme.onPrimary, fontSize = 17.sp)
            }
        }
    }
}

@Composable
private fun PickerMask() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Brush.verticalGradient(
                        colors = if (isSystemInDarkTheme()) {
                            listOf(
                                Color(25, 25, 25, (25 * 0.95).roundToInt()),
                                Color(25, 25, 25, (25 * 0.6).roundToInt())
                            )
                        } else {
                            listOf(
                                Color(255, 255, 255, (255 * 0.95).roundToInt()),
                                Color(255, 255, 255, (255 * 0.6).roundToInt())
                            )
                        }
                    )
                )
        )
        Box(modifier = Modifier.height(56.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Brush.verticalGradient(
                        colors = if (isSystemInDarkTheme()) {
                            listOf(
                                Color(25, 25, 25, (25 * 0.6).roundToInt()),
                                Color(25, 25, 25, (25 * 0.95).roundToInt())
                            )
                        } else {
                            listOf(
                                Color(255, 255, 255, (255 * 0.6).roundToInt()),
                                Color(255, 255, 255, (255 * 0.95).roundToInt())
                            )
                        }
                    )
                )
        )
    }
}