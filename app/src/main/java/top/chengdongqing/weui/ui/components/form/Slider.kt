package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun WeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float = 0f,
    max: Float = 1f,
    trackColor: Color = Color.Gray,
    thumbColor: Color = Color.Blue
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    val newValue = (value + pan.x / 300).coerceIn(min, max)
                    onValueChange(newValue)
                }
            }
    ) {
        // Draw track
        Box(
            Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(trackColor),
            contentAlignment = Alignment.CenterStart
        ) {
            // Draw filled part of the track
            val thumbX = (value - min) / (max - min)
            Box(
                Modifier
                    .fillMaxWidth(fraction = thumbX)
                    .height(4.dp)
                    .background(thumbColor)
            )
        }
        // Draw thumb on top of track
        val thumbX = (value - min) / (max - min)
        Box(
            Modifier
                .offset(x = (thumbX * 100).dp)
                .size(16.dp)
                .background(thumbColor),
        )
    }
}
