package top.chengdongqing.weui.feature.samples.components

import androidx.compose.foundation.DefaultMarqueeVelocity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NotificationBarEffect {
    ELLIPSIS,
    SCROLL,
    WRAP
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeNotificationBar(
    content: String,
    effect: NotificationBarEffect = NotificationBarEffect.SCROLL,
    scrollSpacingFraction: Float = 1f,
    scrollVelocity: Dp = DefaultMarqueeVelocity,
    colors: NotificationBarColors = MaterialTheme.notificationBarColorScheme,
    padding: PaddingValues = if (effect == NotificationBarEffect.SCROLL) {
        PaddingValues(vertical = 12.dp)
    } else {
        PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.containerColor)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = content,
            color = colors.contentColor,
            fontSize = 13.sp,
            maxLines = if (effect == NotificationBarEffect.WRAP) Int.MAX_VALUE else 1,
            softWrap = effect == NotificationBarEffect.WRAP,
            overflow = if (effect == NotificationBarEffect.ELLIPSIS) TextOverflow.Ellipsis else TextOverflow.Visible,
            modifier = if (effect == NotificationBarEffect.SCROLL) {
                Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    delayMillis = 0,
                    initialDelayMillis = 0,
                    spacing = MarqueeSpacing.fractionOfContainer(scrollSpacingFraction),
                    velocity = scrollVelocity
                )
            } else {
                Modifier
            }
        )
    }
}

data class NotificationBarColors(
    val containerColor: Color,
    val contentColor: Color
)

val MaterialTheme.notificationBarColorScheme: NotificationBarColors
    @Composable
    get() = NotificationBarColors(
        containerColor = colorScheme.errorContainer,
        contentColor = colorScheme.error
    )