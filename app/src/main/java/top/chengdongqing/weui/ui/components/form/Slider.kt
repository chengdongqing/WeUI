package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeSlider(
    value: Float,
    min: Float = 0f,
    max: Float = 1f,
    onValueChange: (Float) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    offsetX += pan.x
                    val newValue = (value + offsetX / 300).coerceIn(0f, 1f)
                    onValueChange(newValue)
                    offsetX = 0f
                }
            }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(BorderColor),
            contentAlignment = Alignment.CenterStart
        ) {
            val thumbX = (value - min) / (max - min)
            Box(
                Modifier
                    .fillMaxWidth(fraction = thumbX)
                    .height(2.dp)
                    .background(PrimaryColor)
            )
        }
        val thumbX = (value - min) / (max - min)
        Box(
            Modifier
                .offset(x = (thumbX * 100).dp, y = (-14).dp)
                .size(28.dp)
                .shadow(14.dp, CircleShape, spotColor = BorderColor)
                .background(Color.White, CircleShape)
        )
    }
}
