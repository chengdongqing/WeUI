package top.chengdongqing.weui.feature.samples.ui.paint

/// Inspired from https://github.com/msasikanth/compose_colorpicker to understand hue creations .

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.toIntOffset
import kotlin.random.Random

@Composable
fun ColorPicker(onColorChange: (Color) -> Unit) {
    val screenWidth = LocalWindowInfo.current.containerDpSize.width
    val screenWidthInPx = with(LocalDensity.current) { screenWidth.toPx() }
    var activeColor by remember { mutableStateOf(Red) }

    val max = screenWidth - 16.dp
    val min = 0.dp
    val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
    val dragOffset = remember { mutableFloatStateOf(0f) }

    Box(modifier = Modifier.padding(8.dp)) {
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(brush = remember { colorMapGradient(screenWidthInPx) })
                .align(Alignment.Center)
                .pointerInput("painter") {
                    detectTapGestures { offset ->
                        dragOffset.floatValue = offset.x
                        activeColor = getActiveColor(dragOffset.floatValue, screenWidthInPx)
                        onColorChange.invoke(activeColor)
                    }
                }
        )
        Icon(
            imageVector = Icons.Filled.FiberManualRecord,
            tint = activeColor,
            contentDescription = null,
            modifier = Modifier
                .offset { Offset(dragOffset.floatValue, 0f).toIntOffset() }
                .border(
                    border = BorderStroke(4.dp, WeTheme.colorScheme.surfaceVariant),
                    shape = CircleShape
                )
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newValue = dragOffset.floatValue + delta
                        dragOffset.floatValue = newValue.coerceIn(minPx, maxPx)
                        activeColor = getActiveColor(dragOffset.floatValue, screenWidthInPx)
                        onColorChange.invoke(activeColor)
                    }
                )
        )
    }
}

private fun createColorMap(): List<Color> {
    val colorList = mutableListOf<Color>()
    for (i in 0..360 step (10)) {
        val randomSaturation = (90 + Random.nextFloat() * 10) / 100f
        val randomLightness = (50 + Random.nextFloat() * 10) / 100f

        val hsv = Color.hsl(
            i.toFloat().coerceIn(0f, 360f),
            randomSaturation,
            randomLightness
        )
        colorList.add(hsv)
    }
    return colorList
}

private fun colorMapGradient(screenWidthInPx: Float) = Brush.horizontalGradient(
    colors = createColorMap(),
    startX = 0f,
    endX = screenWidthInPx
)

private fun getActiveColor(dragPosition: Float, screenWidth: Float): Color {
    val hue = (dragPosition / screenWidth * 360f).coerceIn(0f, 360f)
    val randomSaturation = (90 + Random.nextFloat() * 10) / 100f
    val randomLightness = (50 + Random.nextFloat() * 10) / 100f

    return Color.hsl(
        hue,
        randomSaturation,
        randomLightness
    )
}