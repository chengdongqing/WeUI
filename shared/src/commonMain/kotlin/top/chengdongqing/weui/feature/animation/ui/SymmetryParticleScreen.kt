package top.chengdongqing.weui.feature.animation.ui

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
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.theme.GreenPrimary

@Composable
fun SymmetryParticleScreen(onBack: () -> Unit) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var ballPos by remember { mutableStateOf(Position2D(0f, 0f, 0f, 0f)) }

    LaunchedEffect(canvasSize) {
        if (canvasSize.width > 0) {
            ballPos = Position2D(canvasSize.width / 2f, canvasSize.height / 2f, 4f, 4f)
        }
    }
    LaunchedEffect(canvasSize) {
        if (canvasSize.width <= 0) return@LaunchedEffect

        while (isActive) {
            withFrameNanos { _ ->
                ballPos = ballPos.move(canvasSize.width.toFloat(), canvasSize.height.toFloat())
            }
        }
    }

    WeScreen(
        title = "SymmetryParticle",
        description = "对称粒子",
        onBack = onBack
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .onSizeChanged { canvasSize = it }
                .background(Color.White)
        ) {
            val p = ballPos
            val w = canvasSize.width.toFloat()
            val h = canvasSize.height.toFloat()
            val centerX = w / 2f
            val centerY = h / 2f

            val balls = listOf(
                Offset(p.x, centerY), Offset(centerX, p.y),
                Offset(w - p.x, centerY), Offset(centerX, h - p.y),
                Offset(p.x, p.y), Offset(w - p.x, h - p.y),
                Offset(p.x, h - p.y), Offset(w - p.x, p.y)
            )

            balls.forEach { offset ->
                drawCircle(
                    color = GreenPrimary,
                    radius = 10f,
                    center = offset
                )
            }
        }
    }
}

data class Position2D(val x: Float, val y: Float, val vx: Float, val vy: Float) {
    fun move(w: Float, h: Float): Position2D {
        val radius = 8f
        val nx = x + vx
        val ny = y + vy
        var nvx = vx
        var nvy = vy

        // 碰撞检测
        if (nx >= w - radius || nx <= radius) nvx = -nvx
        if (ny >= h - radius || ny <= radius) nvy = -nvy

        return Position2D(
            nx.coerceIn(radius, w - radius),
            ny.coerceIn(radius, h - radius),
            nvx,
            nvy
        )
    }
}