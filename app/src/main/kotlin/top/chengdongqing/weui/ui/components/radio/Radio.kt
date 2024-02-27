package top.chengdongqing.weui.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.theme.FontColorLight
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

@Composable
fun WeRadio(label: String, checked: Boolean, disabled: Boolean, onClick: () -> Unit) {
    Column {
        Row(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickableWithoutRipple(!disabled) {
                    onClick()
                }
                .padding(16.dp)
                .alpha(if (disabled) 0.1f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = FontColorLight,
                fontSize = 17.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (checked) PrimaryColor else Color.Transparent
            )
        }
        WeDivider(Modifier.padding(horizontal = 16.dp))
    }
}