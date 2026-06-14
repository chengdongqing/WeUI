package top.chengdongqing.weui.animation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.isActive
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.theme.GreenPrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadialParticleScreen(onBack: () -> Unit) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val animState = remember { BallAnimationState() }

    LaunchedEffect(canvasSize) {
        if (canvasSize.width > 0) {
            animState.initBalls(canvasSize.width.toFloat(), canvasSize.height.toFloat())
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameNanos { _ ->
                animState.update()
            }
        }
    }

    WeScreen(
        title = "RadialParticle",
        description = "径向粒子",
        onBack = onBack
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .onSizeChanged { canvasSize = it }
                .background(Color.White)
        ) {
            animState.balls.forEach { ball ->
                drawCircle(
                    color = GreenPrimary,
                    radius = ball.radius,
                    center = Offset(ball.x, ball.y)
                )
            }
        }
    }
}

private class BallAnimationState {
    var balls by mutableStateOf(listOf<Ball>())
    private var w = 0f
    private var h = 0f

    fun initBalls(
        width: Float,
        height: Float,
        layer: Int = 3,
        inLayer: Int = 20,
        speed: Float = 3f
    ) {
        this.w = width
        this.h = height

        val newBalls = mutableListOf<Ball>()
        for (i in 0 until layer) {
            val radius = (minOf(width, height) / 2) / layer * i
            for (j in 0 until inLayer) {
                val deg = j * 2 * PI / inLayer
                newBalls.add(
                    Ball(
                        x = (radius * cos(deg) + width / 2).toFloat(),
                        y = (radius * sin(deg) + height / 2).toFloat(),
                        vx = (speed * cos(deg)).toFloat(),
                        vy = (speed * sin(deg)).toFloat()
                    )
                )
            }
        }
        balls = newBalls
    }

    fun update() {
        if (w > 0) balls = balls.map { it.move(w, h) }
    }
}

private data class Ball(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val radius: Float = 10f
) {
    fun move(w: Float, h: Float): Ball {
        val nx = x + vx
        val ny = y + vy
        var nvx = vx
        var nvy = vy

        if (nx < radius || nx > w - radius) nvx = -nvx
        if (ny < radius || ny > h - radius) nvy = -nvy

        return copy(
            x = nx.coerceIn(radius, w - radius),
            y = ny.coerceIn(radius, h - radius),
            vx = nvx,
            vy = nvy
        )
    }
}