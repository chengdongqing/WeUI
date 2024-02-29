package top.chengdongqing.weui.ui.screens.demo.paint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun PaintScreen() {
    WeScreen(
        title = "Paint",
        description = "画板",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        var color by remember { mutableStateOf(Color.Black) }
        var strokeWidth by remember { mutableFloatStateOf(5f) }
        val paths = remember { mutableStateListOf<StrokeItem>() }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            DrawingTools(
                color,
                onColorChange = { color = it },
                onWidthChange = { strokeWidth = it },
                onBack = {
                    if (paths.isNotEmpty()) {
                        paths.removeAt(paths.lastIndex)
                    }
                },
                onClear = { paths.clear() }
            )
            DrawingBoard(paths, color, strokeWidth)
        }
    }
}

internal data class StrokeItem(
    val path: Path,
    val color: Color,
    val width: Float
)