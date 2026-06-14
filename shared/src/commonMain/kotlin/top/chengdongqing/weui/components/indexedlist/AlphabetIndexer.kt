package top.chengdongqing.weui.components.indexedlist

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.theme.WeTheme
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun <T> BoxScope.AlphabetIndexer(
    groups: Map<Char, List<T>>,
    onSelected: (initial: Char) -> Unit
) {
    val density = LocalDensity.current
    var heightPerIndex by remember { mutableFloatStateOf(0f) }
    val dpHeightPerIndex = with(density) { heightPerIndex.toDp() }
    val indexes = remember { ('A'..'Z').toList() + '#' }
    var current by remember { mutableStateOf<Pair<Char, Int>?>(null) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.TopEnd),
        contentAlignment = Alignment.Center
    ) {
        Box {
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 4.dp)
                    .onGloballyPositioned { layoutCoordinates ->
                        heightPerIndex = layoutCoordinates.size.height / indexes.size.toFloat()
                    }
                    .pointerInput(indexes, groups, heightPerIndex) {
                        detectVerticalDragGestures(
                            onDragEnd = { current = null }
                        ) { change, _ ->
                            val index = (change.position.y / heightPerIndex)
                                .roundToInt()
                                .coerceIn(indexes.indices)
                            val initial = indexes[index]
                            current = initial to index

                            onSelected(initial)
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                indexes.forEachIndexed { index, initial ->
                    IndexBarItem(
                        initial = initial,
                        index = index,
                        current = current,
                        setCurrent = { current = it }
                    ) {
                        onSelected(initial)
                    }
                }
            }

            current?.let { (title, index) ->
                Indicator(title, index, dpHeightPerIndex)
            }
        }
    }
}

@Composable
private fun IndexBarItem(
    initial: Char,
    index: Int,
    current: Pair<Char, Int>?,
    setCurrent: (Pair<Char, Int>?) -> Unit,
    onTap: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val selected = initial == current?.first

    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                if (selected) {
                    WeTheme.colorScheme.primary
                } else {
                    Color.Transparent
                },
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial.toString(),
            color = if (selected) Color.White else WeTheme.colorScheme.textPrimary,
            fontSize = 11.sp,
            modifier = Modifier
                .pointerInput(Unit) {
                    awaitEachGesture {
                        // 等待 ACTION_DOWN
                        awaitFirstDown()
                        setCurrent(initial to index)
                        onTap()

                        // 等待 ACTION_UP 或其他中断（如滑出）
                        waitForUpOrCancellation()

                        coroutineScope.launch {
                            delay(300.milliseconds)
                            setCurrent(null)
                        }
                    }
                }
        )
    }
}

@Composable
private fun BoxScope.Indicator(
    title: Char,
    index: Int,
    dpHeightPerIndex: Dp
) {
    val color = WeTheme.colorScheme.background

    Box(
        modifier = Modifier
            .size(60.dp)
            .align(Alignment.TopStart)
            .offset(
                x = (-60).dp,
                y = (-30 + dpHeightPerIndex.value * index + dpHeightPerIndex.value / 2).dp
            )
            .drawWithCache {
                val circlePath = Path().apply {
                    addOval(Rect(Offset(0f, 0f), Size(size.width, size.height)))
                }
                val trianglePath = Path().apply {
                    moveTo(
                        size.width - 14.dp.toPx(),
                        size.height / 2 - 25.dp.toPx()
                    )
                    lineTo(size.width + 16.dp.toPx(), size.height / 2) // 尖角顶点
                    lineTo(
                        size.width - 14.dp.toPx(),
                        size.height / 2 + 25.dp.toPx()
                    )
                    close()
                }

                onDrawBehind {
                    drawPath(circlePath, color)
                    drawPath(trianglePath, color)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title.toString(), color = Color.White, fontSize = 30.sp)
    }
}