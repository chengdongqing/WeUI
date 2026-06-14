package top.chengdongqing.weui.animation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.theme.GreenPrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun FibonacciScreen(onBack: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "sphere")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "angle"
    )
    val pointCount = 500

    WeScreen(
        title = "Fibonacci",
        description = "斐波那契数列",
        onBack = onBack
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2
            val sphereRadius = min(width, height) * 0.4f

            // 黄金分割常数
            val phi = PI * (3.0 - sqrt(5.0))

            for (i in 0 until pointCount) {
                // 斐波那契分布核心公式
                val y = 1 - (i / (pointCount - 1.0)) * 2.0
                val radiusAtY = sqrt(1 - y * y)
                val theta = phi * i + (angle * PI / 180f) // 加上旋转偏移

                val x = cos(theta) * radiusAtY
                val z = sin(theta) * radiusAtY

                // 3D 转 2D 投影
                val perspective = 1.0 / (1.0 - z * 0.5)
                val drawX = (x * sphereRadius * perspective + centerX).toFloat()
                val drawY = (y * sphereRadius * perspective + centerY).toFloat()

                // 绘制点
                drawCircle(
                    color = GreenPrimary,
                    radius = 4f * perspective.toFloat(),
                    center = Offset(drawX, drawY)
                )
            }
        }
    }
}