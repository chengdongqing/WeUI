package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.radio.RadioOption
import top.chengdongqing.weui.ui.components.radio.WeRadioGroup
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.skeleton.WeSkeleton
import top.chengdongqing.weui.ui.components.switch.WeSwitch

@Composable
fun SkeletonScreen() {
    WeScreen(
        title = "Skeleton",
        description = "骨架屏",
        padding = PaddingValues(0.dp),
        containerColor = Color.White
    ) {
        var isActive by remember { mutableStateOf(false) }
        var isLightMode by remember { mutableStateOf(true) }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Controls(isLightMode, isActive, { isLightMode = it }, { isActive = !it })
            Spacer(modifier = Modifier.height(60.dp))
            Content(isLightMode, isActive)
        }
    }
}

@Composable
private fun ColumnScope.Controls(
    isLightMode: Boolean,
    isActive: Boolean,
    onLightModeChange: (Boolean) -> Unit,
    onActiveChange: (Boolean) -> Unit
) {
    val themes = remember {
        listOf(
            RadioOption("浅色", true),
            RadioOption("深色", false)
        )
    }

    WeRadioGroup(options = themes, value = isLightMode, onChange = onLightModeChange)
    Spacer(modifier = Modifier.height(30.dp))
    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "加载中", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(30.dp))
        WeSwitch(checked = !isActive, onChange = onActiveChange)
    }
}

@Composable
private fun Content(isLightMode: Boolean, isActive: Boolean) {
    Column(
        modifier = Modifier
            .background(if (isLightMode) Color.Transparent else Color.Black)
            .padding(16.dp)
    ) {
        Column {
            WeSkeleton.Rectangle(isActive, isLightMode)
            Spacer(modifier = Modifier.padding(8.dp))
            WeSkeleton.RectangleLineLong(isActive, isLightMode)
            Spacer(modifier = Modifier.padding(4.dp))
            WeSkeleton.RectangleLineShort(isActive, isLightMode)
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row {
            WeSkeleton.Circle(isActive, isLightMode)
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Spacer(modifier = Modifier.padding(8.dp))
                WeSkeleton.RectangleLineLong(isActive, isLightMode)
                Spacer(modifier = Modifier.padding(4.dp))
                WeSkeleton.RectangleLineShort(isActive, isLightMode)
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row {
            WeSkeleton.Square(isActive, isLightMode)
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Spacer(modifier = Modifier.padding(8.dp))
                WeSkeleton.RectangleLineLong(isActive, isLightMode)
                Spacer(modifier = Modifier.padding(4.dp))
                WeSkeleton.RectangleLineShort(isActive, isLightMode)
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview
@Composable
private fun PreviewSkeleton() {
    SkeletonScreen()
}