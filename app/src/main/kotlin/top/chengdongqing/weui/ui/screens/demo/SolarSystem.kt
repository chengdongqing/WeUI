package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.isActive
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.SetupStatusBarStyle
import top.chengdongqing.weui.utils.toIntOffset
import top.chengdongqing.weui.utils.toIntSize
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SolarSystemScreen() {
    SetupStatusBarStyle(isDark = false)
    WeUITheme(darkTheme = true) {
        WeScreen(
            title = "SolarSystem",
            description = "太阳系动画",
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            // 图片资源
            val sunImage = ImageBitmap.imageResource(id = R.drawable.solar_system_sun)
            val moonImage = ImageBitmap.imageResource(id = R.drawable.solar_system_moon)
            val earthImage = ImageBitmap.imageResource(id = R.drawable.solar_system_earth)
            // 动画时间状态
            val time by rememberAnimatedTime()

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                val center = Offset(size.width / 2, size.height / 2)

                // 绘制太阳
                drawImage(
                    image = sunImage,
                    dstSize = Size(size.width, size.height).toIntSize()
                )

                // 绘制地球及其轨道
                val earthPosition = calculateOrbitPosition(center, size.width / 3, time, 60000)
                drawOrbit(center, size.width / 3)
                drawCelestialBody(image = earthImage, position = earthPosition, scale = 3f)

                // 绘制月球
                val moonPosition = calculateOrbitPosition(earthPosition, 90f, time, 6000)
                drawCelestialBody(image = moonImage, position = moonPosition, scale = 3f)
            }
        }
    }
}

@Composable
private fun rememberAnimatedTime(): MutableLongState {
    val time = remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameMillis {
                time.longValue = System.currentTimeMillis()
            }
        }
    }

    return time
}

// 绘制轨道
private fun DrawScope.drawOrbit(center: Offset, radius: Float) {
    drawCircle(
        color = Color(0f, 153f / 255f, 255f / 255f, 0.4f),
        radius = radius,
        center = center,
        style = Stroke(width = 1f)
    )
}

// 计算轨道上的位置
private fun calculateOrbitPosition(
    center: Offset,
    radius: Float,
    time: Long,
    period: Long
): Offset {
    val rotation = (time % period) / period.toFloat() * 360
    return Offset(
        x = center.x + radius * cos(Math.toRadians(rotation.toDouble())).toFloat(),
        y = center.y + radius * sin(Math.toRadians(rotation.toDouble())).toFloat()
    )
}

// 绘制天体
private fun DrawScope.drawCelestialBody(
    image: ImageBitmap,
    position: Offset,
    scale: Float
) {
    val dstSize = Size((image.width * scale), (image.height * scale)).toIntSize()
    drawImage(
        image = image,
        dstOffset = Offset(
            (position.x - dstSize.width / 2f),
            (position.y - dstSize.height / 2f)
        ).toIntOffset(),
        dstSize = dstSize
    )
}

@Preview
@Composable
private fun PreviewSolarSystem() {
    WeUITheme {
        SolarSystemScreen()
    }
}