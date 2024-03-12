package top.chengdongqing.weui.ui.components.mediapicker

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ColumnScope.MediaGrid() {
    LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.weight(1f)) {

    }
}