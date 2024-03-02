package top.chengdongqing.weui.ui.components.dropcard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun <T> WeDropCard(
    items: List<T>,
    modifier: Modifier = Modifier,
    onSwiped: (Int, T) -> Unit,
    content: @Composable BoxScope.(T) -> Unit
) {
    val animatedOffsets = remember(items.size) { items.map { Animatable(0f) } }
    val coroutineScope = rememberCoroutineScope()

    Box {
        items.reversed().forEachIndexed { index, item ->
            // 判断当前卡片是否在顶部三张卡片中
            val isInTopThree = index > items.lastIndex - 3
            val current = if (items.lastIndex >= 3) index - 3 else index

            LaunchedEffect(items.size) {
                if (isInTopThree) {
                    // 动画到指定的偏移量
                    animatedOffsets[index].animateTo(
                        targetValue = (64 - current * 32f),
                        animationSpec = tween(800)
                    )
                } else {
                    // 不在顶部的卡片，设置一个默认偏移量
                    animatedOffsets[index].animateTo(
                        targetValue = 0f,
                        animationSpec = tween(800)
                    )
                }
            }

            CardItem(
                key = item,
                modifier = modifier
                    .then(
                        if (isInTopThree) {
                            Modifier
                                .offset(y = animatedOffsets[index].value.dp)
                                .scale(1 - 0.05f * (2 - current))
                        } else {
                            Modifier.scale(0.5f)
                        }
                    ),
                onSwiped = {
                    onSwiped(index, item)
                    coroutineScope.launch {
                        animatedOffsets[index].snapTo(0f)
                    }
                }
            ) {
                content(item)
            }
        }
    }
}

@Composable
private fun <T> CardItem(
    key: T,
    modifier: Modifier,
    onSwiped: () -> Unit,
    content: @Composable () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val swipeOffset = (screenWidth.value * 3)
    val swipeX = remember(key) { Animatable(0f) }
    val swipeY = remember(key) { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    if (abs(swipeX.value) < swipeOffset - 50f) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    translationX = swipeX.value
                    translationY = swipeY.value
                    rotationZ = (swipeX.value / screenWidth.value * 12).coerceIn(-40f, 40f)
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragCancel = {
                            coroutineScope.launch {
                                swipeX.snapTo(0f)
                                swipeY.snapTo(0f)
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (abs(swipeX.targetValue) < abs(swipeOffset) / 4) {
                                    launch {
                                        swipeX.animateTo(0f, tween(400))
                                    }
                                    launch {
                                        swipeY.animateTo(0f, tween(400))
                                    }
                                } else {
                                    if (swipeX.targetValue > 0) {
                                        swipeX.animateTo(swipeOffset, tween(200))
                                    } else {
                                        swipeX.animateTo(-swipeOffset, tween(200))
                                    }
                                    onSwiped()
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        coroutineScope.apply {
                            launch { swipeX.snapTo(swipeX.targetValue + dragAmount.x) }
                            launch { swipeY.snapTo(swipeY.targetValue + dragAmount.y) }
                        }
                    }
                }
                .clip(RoundedCornerShape(16.dp))
        ) {
            content()
        }
    }
}