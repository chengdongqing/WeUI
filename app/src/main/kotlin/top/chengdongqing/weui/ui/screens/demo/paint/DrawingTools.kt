package top.chengdongqing.weui.ui.screens.demo.paint

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardReturn
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import top.chengdongqing.weui.ui.theme.BackgroundColorLight

@Composable
internal fun DrawingTools(
    color: Color,
    onColorChange: (Color) -> Unit,
    onWidthChange: (Float) -> Unit,
    onRollback: () -> Unit,
    onClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .offset(y = (-40).dp)
            .zIndex(1f)
            .padding(12.dp)
    ) {
        ColorPicker(onColorChange)
        MoreColors(onColorChange)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PaintBrushes(color, onWidthChange)
            MoreTools(onRollback, onClear)
        }
    }
}

@Composable
private fun MoreColors(onColorChange: (Color) -> Unit) {
    val colors = remember {
        listOf(Color.Black, Color.White, Color.Gray)
    }

    Row(
        modifier = Modifier
            .background(BackgroundColorLight)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .animateContentSize()
    ) {
        colors.forEach {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = it,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onColorChange(it)
                    }
            )
        }
    }
}

@Composable
private fun MoreTools(onRollback: () -> Unit, onClear: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(with(LocalDensity.current) { 196.toDp() }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onRollback,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardReturn,
                contentDescription = "回退"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = onClear,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "清空"
            )
        }
    }
}

@Composable
private fun PaintBrushes(color: Color, onChange: (Float) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val strokes = remember { (1..50 step 5).toList() }

    Column {
        FloatingActionButton(
            onClick = { visible = !visible },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Brush,
                contentDescription = "切换画笔",
                tint = color
            )
        }
        AnimatedVisibility(visible) {
            LazyColumn {
                items(strokes) {
                    IconButton(
                        onClick = {
                            onChange(it.toFloat())
                            visible = false
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .border(
                                width = with(LocalDensity.current) { it.toDp() },
                                color = Color.Gray,
                                shape = CircleShape
                            )
                    ) {}
                }
            }
        }
    }
}