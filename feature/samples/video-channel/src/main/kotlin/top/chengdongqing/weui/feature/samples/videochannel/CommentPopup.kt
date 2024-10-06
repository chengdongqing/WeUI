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
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun CommentPopup(
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(visible) {
        onClose()
    }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val maxHeight = density.run { configuration.screenHeightDp.dp.toPx() * 0.7f }
    val currentHeight = remember(visible) { mutableFloatStateOf(maxHeight) }
    val animatedHeightDp by animateDpAsState(
        targetValue = if (visible) density.run { currentHeight.floatValue.toDp() } else 0.dp,
        label = "CommentPopupAnimation"
    )

    if (animatedHeightDp > 50.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeightDp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(MaterialTheme.colorScheme.onBackground)
                .draggable(
                    state = rememberDraggableState {
                        handleDrag(currentHeight, it, maxHeight)
                    },
                    orientation = Orientation.Vertical,
                    onDragStopped = {
                        handleDragStopped(currentHeight, maxHeight, onClose)
                    })
                .nestedScroll(
                    remember(maxHeight) {
                        CommentNestedScrollConnection(
                            currentHeight,
                            maxHeight,
                            onClose
                        )
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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

private class CommentNestedScrollConnection(
    private val currentHeight: MutableFloatState,
    private val maxHeight: Float,
    private val onClose: () -> Unit
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (source == NestedScrollSource.UserInput && currentHeight.floatValue < maxHeight) {
            handleDrag(currentHeight, available.y, maxHeight)
            return available
        } else {
            return Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (source == NestedScrollSource.UserInput) {
            handleDrag(currentHeight, available.y, maxHeight)
            return available
        } else {
            return Offset.Zero
        }
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
        handleDragStopped(currentHeight, maxHeight, onClose)
        return available
    }
}

private fun handleDrag(currentHeight: MutableFloatState, delta: Float, maxHeight: Float) {
    currentHeight.floatValue = (currentHeight.floatValue - delta).coerceAtMost(maxHeight)
}

private fun handleDragStopped(
    currentHeight: MutableFloatState,
    maxHeight: Float,
    onClose: () -> Unit
) {
    if (currentHeight.floatValue > maxHeight * 0.75) {
        currentHeight.floatValue = maxHeight
    } else {
        onClose()
    }
}