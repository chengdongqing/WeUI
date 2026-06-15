package top.chengdongqing.weui.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.core.ui.theme.WeTheme
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_check

@Composable
fun WeCheckBox(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .border(
                width = if (checked) Dp.Unspecified else 1.dp,
                color = WeTheme.colorScheme.divider,
                shape = CircleShape
            )
            .background(if (checked) WeTheme.colorScheme.primary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = if (checked) Color.White else Color.Transparent
        )
    }
}