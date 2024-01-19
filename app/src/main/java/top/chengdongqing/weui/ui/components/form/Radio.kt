package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

data class RadioOption(
    val label: String,
    val value: Any,
    val disabled: Boolean = false
)

/**
 * 单选框
 *
 * @param options 可选项列表
 * @param value 已选中的value
 * @param disabled 是否禁用
 * @param onChange 选中项改变事件
 */
@Composable
fun WeRadioGroup(
    options: List<RadioOption>,
    value: Any? = null,
    disabled: Boolean = false,
    onChange: ((value: Any) -> Unit)? = null
) {
    Column {
        for (option in options) {
            WeRadio(
                label = option.label,
                checked = option.value == value,
                disabled = disabled || option.disabled
            ) {
                onChange?.invoke(option.value)
            }
        }
    }
}

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
                color = FontColor,
                fontSize = 17.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (checked) PrimaryColor else Color.Transparent
            )
        }
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 0.5.dp,
            color = BorderColor
        )
    }
}