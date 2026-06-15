package top.chengdongqing.weui.feature.basic.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.WeSwitch
import top.chengdongqing.weui.core.ui.components.skeleton.WeSkeleton
import top.chengdongqing.weui.core.ui.theme.WeTheme

@Composable
fun SkeletonScreen(onBack: () -> Unit) {
    var isActive by remember { mutableStateOf(false) }

    WeScreen(
        title = "Skeleton",
        description = "骨架屏",
        containerColor = WeTheme.colorScheme.surface,
        onBack = onBack
    ) {
        ActiveControl(isActive) { isActive = it }
        Spacer(modifier = Modifier.height(60.dp))
        Content(isActive)
    }
}

@Composable
private fun ColumnScope.ActiveControl(
    value: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "加载中",
            color = WeTheme.colorScheme.textPrimary,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(30.dp))
        WeSwitch(checked = value, onChange = onChange)
    }
}

@Composable
private fun Content(isActive: Boolean) {
    Column {
        WeSkeleton.Rectangle(isActive)
        Spacer(modifier = Modifier.padding(8.dp))
        WeSkeleton.RectangleLineLong(isActive)
        Spacer(modifier = Modifier.padding(4.dp))
        WeSkeleton.RectangleLineShort(isActive)
    }
    Spacer(modifier = Modifier.height(48.dp))
    Row {
        WeSkeleton.Circle(isActive)
        Spacer(modifier = Modifier.padding(4.dp))
        Column {
            Spacer(modifier = Modifier.padding(8.dp))
            WeSkeleton.RectangleLineLong(isActive)
            Spacer(modifier = Modifier.padding(4.dp))
            WeSkeleton.RectangleLineShort(isActive)
        }
    }
    Spacer(modifier = Modifier.height(48.dp))
    Row {
        WeSkeleton.Square(isActive)
        Spacer(modifier = Modifier.padding(4.dp))
        Column {
            Spacer(modifier = Modifier.padding(8.dp))
            WeSkeleton.RectangleLineLong(isActive)
            Spacer(modifier = Modifier.padding(4.dp))
            WeSkeleton.RectangleLineShort(isActive)
        }
    }
    Spacer(modifier = Modifier.height(100.dp))
}