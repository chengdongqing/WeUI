package top.chengdongqing.weui.feature.demos.components.dropcard

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.geometry.Offset

data class DropCardAnimationState(val offsetY: Float, val scale: Float)

internal val cardAnimationStateConverter =
    TwoWayConverter<DropCardAnimationState, AnimationVector2D>(
        convertToVector = { state ->
            AnimationVector2D(state.offsetY, state.scale)
        },
        convertFromVector = { vector ->
            DropCardAnimationState(vector.v1, vector.v2)
        }
    )

internal val offsetConverter = TwoWayConverter<Offset, AnimationVector2D>(
    convertToVector = { offset ->
        AnimationVector2D(offset.x, offset.y)
    },
    convertFromVector = { vector ->
        Offset(vector.v1, vector.v2)
    }
)