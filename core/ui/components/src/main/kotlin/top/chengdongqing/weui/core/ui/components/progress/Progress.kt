package top.chengdongqing.weui.core.ui.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.utils.format

@Composable
fun WeProgress(
    percent: Float,
    formatter: ((percent: Float) -> String)? = {
        "${it.format()}%"
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
                .background(MaterialTheme.colorScheme.outline)
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
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        formatter?.also {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = it(localPercent),
                modifier = Modifier.widthIn(40.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )
        }
    }
}