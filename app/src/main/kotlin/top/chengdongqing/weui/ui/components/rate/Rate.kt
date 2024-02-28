package top.chengdongqing.weui.ui.components.rate

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun WeRate(
    value: Float,
    count: Int = 5,
    allowHalf: Boolean = false,
    onChange: ((Float) -> Unit)? = null
) {
    var starWidth by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .pointerInput(starWidth) {
                onChange?.let {
                    detectHorizontalDragGestures { change, _ ->
                        val newValue = change.position
                            .calculateRateValue(starWidth, count, allowHalf)
                        onChange(newValue)
                    }
                }
            }
    ) {
        repeat(count) { index ->
            val isHalfStar = allowHalf && index == value.toInt() && value % 1f > 0f
            StarItem(
                isHalfStar,
                isActive = index < value,
                modifier = Modifier
                    .size(26.dp)
                    .onSizeChanged {
                        starWidth = it.width
                    }
                    .pointerInput(Unit) {
                        onChange?.let {
                            detectTapGestures {
                                onChange(index + 1f)
                            }
                        }
                    }
            )
        }
    }
}

@Composable
private fun StarItem(
    isHalfStar: Boolean,
    isActive: Boolean,
    modifier: Modifier
) {
    Icon(
        imageVector = if (isHalfStar) {
            Icons.AutoMirrored.Filled.StarHalf
        } else if (isActive) {
            Icons.Filled.StarRate
        } else {
            Icons.Filled.StarOutline
        },
        contentDescription = null,
        tint = if (isActive) {
            Color(0xffFF6700)
        } else {
            MaterialTheme.colorScheme.outline
        },
        modifier = modifier
    )
}

private fun Offset.calculateRateValue(starWidth: Int, count: Int, allowHalf: Boolean): Float {
    val newRating = (this.x / starWidth)
        .coerceIn(0f, count.toFloat())

    return if (allowHalf) {
        (newRating * 2).roundToInt() / 2f
    } else {
        newRating
            .roundToInt()
            .toFloat()
    }
}