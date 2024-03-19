package top.chengdongqing.weui.feature.demos.components.dropcard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
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
    animationSpec: AnimationSpec<DropCardAnimationState> = tween(durationMillis = 300),
    onDrop: (T) -> Unit,
    content: @Composable BoxScope.(T) -> Unit
) {
    val animatedItems = remember {
        mutableStateMapOf<T, Animatable<DropCardAnimationState, AnimationVector2D>>()
    }

    items.forEach { item ->
        if (!animatedItems.containsKey(item)) {
            animatedItems[item] = Animatable(
                DropCardAnimationState(0f, 0.5f),
                cardAnimationStateConverter
            )
        }
    }
    val keysToRemove = animatedItems.keys - items.toSet()
    keysToRemove.forEach { key ->
        animatedItems.remove(key)
    }

    Box {
        items.reversed().forEachIndexed { index, item ->
            val isInTopThree = index > items.lastIndex - 3
            val current = if (items.lastIndex >= 3) index - 3 else index
            val animatedItem = animatedItems[item]!!

            LaunchedEffect(item) {
                if (isInTopThree) {
                    launch {
                        animatedItem.animateTo(
                            targetValue = DropCardAnimationState(
                                offsetY = 64f - current * 32f,
                                scale = 1f - 0.05f * (2 - current)
                            ),
                            animationSpec = animationSpec
                        )
                    }
                }
            }

            CardItem(
                key = item,
                modifier = modifier
                    .offset(y = animatedItem.value.offsetY.dp)
                    .scale(animatedItem.value.scale),
                onDrop = {
                    onDrop(item)
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
    onDrop: () -> Unit,
    content: @Composable () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val targetOffset = (screenWidth * 3)
    val animatedOffset = remember(key) {
        Animatable(Offset(0f, 0f), offsetConverter)
    }
    val coroutineScope = rememberCoroutineScope()

    if (abs(animatedOffset.value.x) < targetOffset - 50f) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    translationX = animatedOffset.value.x
                    translationY = animatedOffset.value.y
                    rotationZ = (animatedOffset.value.x / screenWidth * 12).coerceIn(-40f, 40f)
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragCancel = {
                            coroutineScope.launch {
                                animatedOffset.snapTo(Offset(0f, 0f))
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (abs(animatedOffset.targetValue.x) < abs(targetOffset) / 4) {
                                    animatedOffset.animateTo(Offset(0f, 0f), tween(400))
                                } else {
                                    val endValue = if (animatedOffset.targetValue.x > 0) {
                                        targetOffset.toFloat()
                                    } else {
                                        -targetOffset.toFloat()
                                    }
                                    animatedOffset.animateTo(
                                        Offset(endValue, animatedOffset.value.y),
                                        tween(200)
                                    )
                                    onDrop()
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        coroutineScope.launch {
                            animatedOffset.snapTo(
                                Offset(
                                    animatedOffset.targetValue.x + dragAmount.x,
                                    animatedOffset.targetValue.y + dragAmount.y
                                )
                            )
                        }
                    }
                }
                .clip(RoundedCornerShape(16.dp))
        ) {
            content()
        }
    }
}