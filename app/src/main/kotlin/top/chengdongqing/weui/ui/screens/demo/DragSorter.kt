package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.NoDragCancelledAnimation
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme

@Composable
fun DragSorterScreen() {
    WeScreen(
        title = "DragSorter",
        description = "拖拽排序",
        scrollEnabled = false,
        padding = PaddingValues(0.dp)
    ) {
        var isList by remember { mutableStateOf(true) }

        WeButton(text = "切换类型", type = ButtonType.PLAIN) {
            isList = !isList
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (isList) {
            VerticalReorderList()
        } else {
            VerticalReorderGrid()
        }
    }
}

@Composable
private fun VerticalReorderList() {
    val data = remember { mutableStateOf(List(100) { "Item ${it + 1}" }) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    LazyColumn(
        state = state.listState,
        verticalArrangement = Arrangement.spacedBy(0.5.dp),
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation.value)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(horizontal = 16.dp, vertical = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item, color = MaterialTheme.colorScheme.onPrimary)
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun VerticalReorderGrid() {
    val data = remember { mutableStateOf(List(100) { "Item ${it + 1}" }) }
    val state = rememberReorderableLazyGridState(
        dragCancelledAnimation = NoDragCancelledAnimation(),
        onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        state = state.gridState,
        horizontalArrangement = Arrangement.spacedBy(0.5.dp),
        verticalArrangement = Arrangement.spacedBy(0.5.dp),
        modifier = Modifier.reorderable(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .shadow(elevation.value)
                        .background(MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.detectReorderAfterLongPress(state)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDragSorter() {
    WeUITheme {
        DragSorterScreen()
    }
}