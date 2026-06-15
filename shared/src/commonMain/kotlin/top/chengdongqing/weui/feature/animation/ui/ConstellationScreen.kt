package top.chengdongqing.weui.feature.animation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.isActive
import top.chengdongqing.weui.core.ui.components.WeScreen
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun ConstellationScreen(onBack: () -> Unit) {
    val particles = remember {
        mutableStateListOf<ParticleWithVelocity>().apply {
            repeat(20) { add(ParticleWithVelocity()) }
        }
    }
    var frameTrigger by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameNanos { frameTime ->
                particles.forEach { it.move(1000f, 1000f) }
                frameTrigger = frameTime
            }
        }
    }

    WeScreen(
        title = "Constellation",
        description = "动态三角网格",
        onBack = onBack
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.White)
        ) {
            frameTrigger

            for (i in particles.indices) {
                for (j in (i + 1) until particles.size) {
                    val dist = distance(particles[i], particles[j])
                    if (dist < 200f) {
                        drawLine(
                            color = Color.Gray.copy(alpha = 1f - (dist / 200f)),
                            start = Offset(particles[i].x, particles[i].y),
                            end = Offset(particles[j].x, particles[j].y),
                            strokeWidth = 2f
                        )
                    }
                }
            }

            particles.forEach {
                drawCircle(Color.Black, 6f, Offset(it.x, it.y))
            }
        }
    }
}

private class ParticleWithVelocity(
    var x: Float = (0..800).random().toFloat(),
    var y: Float = (0..800).random().toFloat(),
    var vx: Float = (-2..2).random().toFloat(),
    var vy: Float = (-2..2).random().toFloat()
) {
    fun move(w: Float, h: Float) {
        x += vx; y += vy
        if (x !in 0.0..w.toDouble()) vx = -vx
        if (y !in 0.0..h.toDouble()) vy = -vy
    }
}

private fun distance(p1: ParticleWithVelocity, p2: ParticleWithVelocity) =
    sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))