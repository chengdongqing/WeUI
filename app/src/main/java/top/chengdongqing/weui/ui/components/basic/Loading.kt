package top.chengdongqing.weui.ui.components.basic

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.R

@Composable
fun WeLoading(size: Dp = 16.dp, color: Color = Color.Unspecified) {
    val infiniteTransition = rememberInfiniteTransition(label = "LoadingInfiniteTransition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "LoadingFloatAnimation"
    )

    Icon(
        painter = painterResource(id = R.drawable.ic_loading),
        contentDescription = "loading",
        modifier = Modifier
            .size(size)
            .graphicsLayer(rotationZ = angle),
        tint = color
    )
}