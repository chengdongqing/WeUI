package top.chengdongqing.weui.ui.components.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

object WeSkeleton {
    @Composable
    fun Circle(isActive: Boolean = true) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .shimmerLoading(isActive)
        )
    }

    @Composable
    fun Square(isActive: Boolean = true) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .shimmerLoading(isActive)
        )
    }

    @Composable
    fun Rectangle(isActive: Boolean = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .shimmerLoading(isActive)
        )
    }

    @Composable
    fun RectangleLineLong(isActive: Boolean = true) {
        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .shimmerLoading(isActive)
        )
    }

    @Composable
    fun RectangleLineShort(isActive: Boolean = true) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .shimmerLoading(isActive)
        )
    }
}