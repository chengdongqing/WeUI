package top.chengdongqing.weui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.theme.RedDanger

@Composable
fun WeBadge(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    content: String? = null,
    alignment: Alignment = Alignment.TopEnd,
    size: Dp = 10.dp,
    gap: Dp = 8.dp,
    offset: DpOffset? = null,
    contentColor: Color = Color.White,
    containerColor: Color = RedDanger,
    holder: (@Composable () -> Unit)? = null
) {
    Box(modifier = modifier) {
        holder?.invoke()

        if (visible) {
            val density = LocalDensity.current
            var localWidth by remember {
                mutableStateOf(0.dp)
            }

            val finalOffset = offset ?: run {
                val offsetX = when (alignment) {
                    Alignment.TopEnd, Alignment.BottomEnd -> localWidth / 2
                    Alignment.TopCenter, Alignment.BottomCenter, Alignment.Center -> 0.dp
                    Alignment.CenterStart -> -(localWidth + gap)
                    Alignment.CenterEnd -> localWidth + gap
                    else -> -localWidth / 2
                }
                val offsetY = when (alignment) {
                    Alignment.BottomStart, Alignment.BottomCenter, Alignment.BottomEnd -> size / 2
                    Alignment.CenterEnd, Alignment.CenterStart, Alignment.Center -> 0.dp
                    else -> -size / 2
                }
                DpOffset(offsetX, offsetY)
            }

            Box(
                modifier = Modifier
                    .align(alignment)
                    .widthIn(size)
                    .height(size)
                    .onSizeChanged { size ->
                        with(density) {
                            localWidth = size.width.toDp()
                        }
                    }
                    .offset(x = finalOffset.x, y = finalOffset.y)
                    .clip(if (localWidth > size) RoundedCornerShape(20.dp) else CircleShape)
                    .background(containerColor)
                    .padding(horizontal = if (localWidth > size && content != null) 6.dp else 0.dp),
                contentAlignment = Alignment.Center
            ) {
                content?.let {
                    Text(text = content, color = contentColor, fontSize = 12.sp)
                }
            }
        }
    }
}

fun Int.toBadgeText(): String? = when {
    this <= 0 -> null
    this > 99 -> "99+"
    else -> this.toString()
}