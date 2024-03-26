package top.chengdongqing.weui.feature.samples.videochannel

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun CommentsPopup(
    visible: Boolean,
    onClose: () -> Unit,
    title: String,
    content: @Composable () -> Unit
) {
    BackHandler(visible) {
        onClose()
    }

    val density = LocalDensity.current
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    var currentHeight by remember(visible) {
        mutableStateOf((screenHeightDp * 0.7).dp)
    }
    val animatedHeight by animateDpAsState(
        targetValue = if (visible) currentHeight else 0.dp,
        label = "CommentsPopupAnimation"
    )

    if (animatedHeight > 50.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .draggable(
                        state = rememberDraggableState { dragAmount ->
                            currentHeight -= density.run { dragAmount.toDp() }
                        },
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            if (currentHeight > screenHeightDp.dp * 0.7f / 2) {
                                currentHeight = screenHeightDp.dp * 0.7f
                            } else {
                                onClose()
                            }
                        })
                    .padding(horizontal = 12.dp, vertical = 18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outline)
                        .clickable { onClose() }
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            content()
        }
    }
}