package top.chengdongqing.weui.ui.components.basic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.BackgroundColor
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun WeProgress(
    percent: Float,
    formatter: ((percent: Float) -> String)? = {
        "${formatFloat(it)}%"
    }
) {
    val localPercent = percent.coerceIn(0f, 100f)

    Row(
        modifier = Modifier.height(66.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(BackgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        animateFloatAsState(
                            targetValue = localPercent / 100,
                            label = "ProgressAnimation"
                        ).value
                    )
                    .fillMaxHeight()
                    .background(PrimaryColor)
            )
        }
        formatter?.also {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = it(localPercent),
                modifier = Modifier.widthIn(40.dp),
                color = FontColo1,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun WeCircleProgress(
    percent: Float,
    size: Dp = 100.dp,
    strokeWidth: Dp = 6.dp,
    fontSize: TextUnit = 16.sp,
    formatter: ((percent: Float) -> String)? = {
        "${formatFloat(it)}%"
    }
) {
    val localPercent = percent.coerceIn(0f, 100f)
    val animatedAngle by animateFloatAsState(
        targetValue = 360 * (localPercent / 100),
        label = "CircleProgressAnimation"
    )

    Box(modifier = Modifier.padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = Color(0f, 0f, 0f, 0.06f),
                radius = this.size.width / 2,
                style = Stroke(width = strokeWidth.toPx())
            )
            drawArc(
                color = PrimaryColor,
                startAngle = -90f,
                sweepAngle = animatedAngle,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        formatter?.also {
            Text(
                text = it(localPercent),
                color = FontColor,
                fontSize = fontSize,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun WeDashboardProgress(
    percent: Float,
    size: Dp = 100.dp,
    strokeWidth: Dp = 6.dp,
    fontSize: TextUnit = 16.sp,
    formatter: ((percent: Float) -> String)? = {
        "${formatFloat(it)}%"
    }
) {
    val localPercent = percent.coerceIn(0f, 100f)
    val animatedAngle by animateFloatAsState(
        targetValue = 270 * (localPercent / 100),
        label = "DashboardProgressAnimation"
    )

    Box(modifier = Modifier.padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            drawArc(
                color = Color(0f, 0f, 0f, 0.06f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
            drawArc(
                color = PrimaryColor,
                startAngle = 135f,
                sweepAngle = animatedAngle,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        formatter?.also {
            Text(
                text = it(localPercent),
                color = FontColor,
                fontSize = fontSize,
                textAlign = TextAlign.End
            )
        }
    }
}