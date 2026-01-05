package top.chengdongqing.weui.feature.qrcode.scanner

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun BoxScope.ScannerDecoration() {
    ScanningAnimation()

    Text(
        text = "扫二维码 / 条码",
        color = Color.White,
        fontSize = 16.sp,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = (-150).dp)
    )
}

@Composable
private fun BoxScope.ScanningAnimation() {
    val screenHeight = LocalWindowInfo.current.containerDpSize.height.value
    val transition = rememberInfiniteTransition(label = "")
    val offsetY by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.7f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "QrCodeScanningAnimation"
    )

    Box(
        modifier = Modifier
            .offset(y = (offsetY * screenHeight).dp)
            .align(Alignment.TopCenter)
            .fillMaxWidth(0.8f)
            .height(5.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.primary.copy(0.6f),
                        Color.Transparent
                    )
                )
            )
    )
}