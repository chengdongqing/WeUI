package top.chengdongqing.weui.ui.components.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerLoadingAnimation(
    isActive: Boolean = false,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1500
): Modifier {
    if (isActive) {
        return this
    } else {
        return composed {
            val shimmerColors = ShimmerAnimationData(
                isLightMode = !isSystemInDarkTheme()
            ).getColors()

            val transition = rememberInfiniteTransition(label = "")
            val translateAnimation by transition.animateFloat(
                initialValue = 0f,
                targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ShimmerLoadingAnimation"
            )

            this.background(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(x = translateAnimation - widthOfShadowBrush, y = 0.0f),
                    end = Offset(x = translateAnimation, y = angleOfAxisY)
                )
            )
        }
    }
}

private data class ShimmerAnimationData(
    private val isLightMode: Boolean
) {
    fun getColors(): List<Color> {
        return if (isLightMode) {
            val color = Color.White
            listOf(
                color.copy(0.3f),
                color.copy(0.5f),
                color.copy(1.0f),
                color.copy(0.5f),
                color.copy(0.3f),
            )
        } else {
            val color = Color.Black
            listOf(
                color.copy(0.0f),
                color.copy(0.3f),
                color.copy(0.5f),
                color.copy(0.3f),
                color.copy(0.0f),
            )
        }
    }
}