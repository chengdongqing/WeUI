package top.chengdongqing.weui.feature.demos.components.indexedlist

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import kotlin.math.roundToInt

@Composable
fun BoxScope.IndexBar(listState: LazyListState, groups: Map<Char, List<String>>) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    var heightPerIndex by remember { mutableFloatStateOf(0f) }
    val dpHeightPerIndex = remember(heightPerIndex) {
        with(density) { heightPerIndex.toDp() }
    }
    val indexes = remember { ('A'..'Z').toList() + '#' }
    var current by remember { mutableStateOf<Pair<Char, Int>?>(null) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.TopEnd)
            .clickableWithoutRipple { },
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
                            val title = indexes[index]
                            current = title to index
                            scrollToIndex(coroutineScope, title, groups, listState)
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                indexes.forEachIndexed { index, title ->
                    IndexBarItem(
                        title,
                        index,
                        current,
                        groups,
                        listState
                    ) {
                        current = it
                    }
                }
            }

            current?.let { (title, index) ->
                DrawIndicator(title, index, dpHeightPerIndex)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun IndexBarItem(
    title: Char,
    index: Int,
    current: Pair<Char, Int>?,
    groups: Map<Char, List<String>>,
    listState: LazyListState,
    setCurrent: (Pair<Char, Int>?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val selected = title == current?.first

    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                },
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title.toString(),
            color = if (selected) Color.White else MaterialTheme.colorScheme.onPrimary,
            fontSize = 11.sp,
            modifier = Modifier
                .pointerInteropFilter { motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            setCurrent(title to index)
                            scrollToIndex(coroutineScope, title, groups, listState)
                            true
                        }

                        MotionEvent.ACTION_UP -> {
                            coroutineScope.launch {
                                delay(300)
                                setCurrent(null)
                            }
                            true
                        }

                        else -> false // 对于其他事件，返回false表示未消费事件
                    }
                }
        )
    }
}

@Composable
private fun BoxScope.DrawIndicator(title: Char, index: Int, dpHeightPerIndex: Dp) {
    val color = MaterialTheme.colorScheme.background

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

private fun scrollToIndex(
    coroutineScope: CoroutineScope,
    title: Char,
    groups: Map<Char, List<String>>,
    listState: LazyListState
) {
    coroutineScope.launch {
        val index = calculateIndexForTitle(title, groups, listState.layoutInfo.totalItemsCount)
        if (index >= 0) {
            listState.scrollToItem(index)
        }
    }
}

private fun calculateIndexForTitle(
    title: Char,
    groups: Map<Char, List<String>>,
    totalCount: Int
): Int {
    if (title == '#') return totalCount - 1
    var cumulativeIndex = 0
    groups.forEach { (groupTitle, items) ->
        if (groupTitle == title) return cumulativeIndex
        cumulativeIndex += items.size + 1
    }
    return -1
}