package top.chengdongqing.weui.ui.screens.media.picker

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.burnoutcrew.reorderable.NoDragCancelledAnimation
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun MediaPickerScreen(navController: NavController) {
    WeScreen(
        title = "MediaPicker",
        description = "媒体选择器",
        containerColor = MaterialTheme.colorScheme.surface,
        scrollEnabled = false
    ) {
        val data = remember { mutableStateOf<List<Uri>>(emptyList()) }
        val state = rememberReorderableLazyGridState(
            dragCancelledAnimation = NoDragCancelledAnimation(),
            onMove = { from, to ->
                data.value = data.value.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = state.gridState,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.reorderable(state)
        ) {
            items(data.value, { it }) { item ->
                ReorderableItem(state, key = item) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .shadow(elevation.value)
                            .background(MaterialTheme.colorScheme.onSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = item,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.aspectRatio(1f)
                        )
                    }
                }
            }
            item {
                PlusButton {
                    navController.navigate("media-picker")
                }
            }
        }
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.onSurface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "添加",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(60.dp)
        )
    }
}