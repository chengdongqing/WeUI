package top.chengdongqing.weui.ui.components.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.R
import top.chengdongqing.weui.extensions.clickableWithoutRipple
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeCheckbox(
    label: String,
    checked: Boolean = false,
    disabled: Boolean = false,
    onChange: ((checked: Boolean) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickableWithoutRipple(!disabled) {
                onChange?.invoke(!checked)
            }
            .padding(16.dp)
            .alpha(if (disabled) 0.1f else 1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(50))
                .border(
                    if (checked) Dp.Unspecified else 1.dp,
                    Color(0f, 0f, 0f, 0.3f),
                    RoundedCornerShape(50)
                )
                .background(if (checked) PrimaryColor else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = if (checked) Color.White else Color.Transparent
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = label, color = FontColor, fontSize = 17.sp)
            WeDivider(Modifier.offset(y = 16.dp))
        }
    }
}
