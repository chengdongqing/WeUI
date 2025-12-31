package top.chengdongqing.weui.feature.samples.screens

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen

@Composable
fun ReorderableScreen() {
    WeScreen(
        title = "Reorderable",
        description = "拖拽排序",
        scrollEnabled = false,
        padding = PaddingValues(horizontal = 16.dp)
    ) {
        var isList by remember { mutableStateOf(true) }

        WeButton(text = "切换类型", type = ButtonType.PLAIN) {
            isList = !isList
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (isList) {
            ReorderableList()
        } else {
            ReorderableGrid()
        }
    }
}

@Composable
private fun ReorderableList() {
    val data = remember {
        mutableStateListOf<String>().apply {
            addAll(List(100) { "Item ${it + 1}" })
        }
    }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.apply {
            add(to.index, removeAt(from.index))
        }
    })

    LazyColumn(
        state = state.listState,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data, key = { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation)
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
private fun ReorderableGrid() {
    val data = remember {
        mutableStateListOf<String>().apply {
            addAll((List(100) { "Item ${it + 1}" }))
        }
    }
    val state = rememberReorderableLazyGridState(onMove = { from, to ->
        data.apply {
            add(to.index, removeAt(from.index))
        }
    })

    LazyVerticalGrid(
        state = state.gridState,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.reorderable(state)
    ) {
        items(data, key = { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .shadow(elevation)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .detectReorderAfterLongPress(state),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
