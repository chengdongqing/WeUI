package top.chengdongqing.weui.core.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 带回弹感的滚动效果
 */
class BounceOverscrollEffect(
    private val scope: CoroutineScope,
    private val orientation: Orientation = Orientation.Vertical
) : OverscrollEffect {
    private val animatable = Animatable(0f)
    private var animationJob: Job? = null

    override val node: DelegatableNode = object : Modifier.Node(), DrawModifierNode {
        override fun ContentDrawScope.draw() {
            drawIntoCanvas {
                if (orientation == Orientation.Vertical) {
                    it.translate(0f, animatable.value)
                } else {
                    it.translate(animatable.value, 0f)
                }
            }
            drawContent()
        }
    }

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {
        if (source == NestedScrollSource.UserInput) {
            animationJob?.cancel()
        }

        // 提取当前维度的增量
        val deltaAmount = if (orientation == Orientation.Vertical) delta.y else delta.x
        val oldOffset = animatable.value

        // 如果当前已经处于拉伸状态 (oldOffset != 0)，优先处理回正
        if (source == NestedScrollSource.UserInput && oldOffset != 0f) {
            val potentialNewOffset = oldOffset + deltaAmount * 0.45f

            // 判定是否跨越了原点（即从拉伸状态回到了正常滚动状态）
            return if ((oldOffset > 0 && potentialNewOffset <= 0) || (oldOffset < 0 && potentialNewOffset >= 0)) {
                // 同步归零
                scope.launch { animatable.snapTo(0f) }

                // 计算回到 0 点消耗了多少 delta
                // 消耗量 = (0 - oldOffset) / 阻尼
                val consumedToReachZero = -oldOffset / 0.45f
                val remainingDeltaAfterZero = deltaAmount - consumedToReachZero

                // 将穿过 0 点后剩下的位移交给组件滚动
                val newDelta = if (orientation == Orientation.Vertical)
                    delta.copy(y = remainingDeltaAfterZero) else delta.copy(x = remainingDeltaAfterZero)

                performScroll(newDelta)
                delta // 整体视为已消费
            } else {
                // 还没回到 0 点，继续拉伸/收缩
                scope.launch { animatable.snapTo(potentialNewOffset) }
                delta
            }
        }

        // 正常滚动逻辑
        val consumedByComponent = performScroll(delta)
        val consumedAmount =
            if (orientation == Orientation.Vertical) consumedByComponent.y else consumedByComponent.x
        val remaining = deltaAmount - consumedAmount

        // 边界触发拉伸
        if (source == NestedScrollSource.UserInput && remaining != 0f) {
            val newOffset = oldOffset + remaining * 0.45f
            scope.launch { animatable.snapTo(newOffset) }
            return delta
        }

        return consumedByComponent
    }

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity
    ) {
        if (animatable.value == 0f) {
            performFling(velocity)
        }

        animationJob = scope.launch {
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    override val isInProgress: Boolean
        get() = animatable.value != 0f
}

/**
 * 创建并记住一个带回弹感的滚动效果
 */
@Composable
fun rememberBounceOverscrollEffect(
    orientation: Orientation = Orientation.Vertical
): BounceOverscrollEffect {
    val scope = rememberCoroutineScope()
    return remember {
        BounceOverscrollEffect(scope, orientation)
    }
}