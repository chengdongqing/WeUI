package top.chengdongqing.weui.components

import androidx.compose.foundation.MarqueeDefaults
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.theme.WeTheme

enum class NotificationBarEffect {
    Ellipsis,
    Scroll,
    Wrap
}

@Composable
fun WeNotificationBar(
    content: String,
    effect: NotificationBarEffect = NotificationBarEffect.Scroll,
    scrollSpacingFraction: Float = 1f,
    scrollVelocity: Dp = MarqueeDefaults.Velocity,
    colors: NotificationBarColors = WeTheme.notificationBarColorScheme,
    padding: PaddingValues = if (effect == NotificationBarEffect.Scroll) {
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
            maxLines = if (effect == NotificationBarEffect.Wrap) Int.MAX_VALUE else 1,
            softWrap = effect == NotificationBarEffect.Wrap,
            overflow = if (effect == NotificationBarEffect.Ellipsis) TextOverflow.Ellipsis else TextOverflow.Visible,
            modifier = if (effect == NotificationBarEffect.Scroll) {
                Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    repeatDelayMillis = 0,
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

val WeTheme.notificationBarColorScheme: NotificationBarColors
    @Composable
    get() = NotificationBarColors(
        containerColor = Color(red = 249, green = 222, blue = 220),
        contentColor = colorScheme.danger
    )