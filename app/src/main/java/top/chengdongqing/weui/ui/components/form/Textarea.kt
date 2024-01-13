package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeTextarea(
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    disabled: Boolean = false,
    labelWidth: Dp = 68.dp,
    max: Int? = null,
    onChange: ((String) -> Unit)? = null
) {
    Column {
        Row(
            modifier = modifier.padding(vertical = 16.dp)
        ) {
            if (label?.isNotEmpty() == true) {
                Text(text = label, fontSize = 16.sp, modifier = Modifier.width(labelWidth))
                Spacer(modifier = Modifier.width(16.dp))
            }
            BasicTextField(
                value = value,
                onValueChange = { str ->
                    onChange?.apply {
                        if (max != null && str.length > max) {
                            invoke(str.slice(0..<max))
                        } else {
                            invoke(str)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f),
                readOnly = disabled,
                textStyle = TextStyle(color = FontColor, fontSize = 16.sp),
                minLines = 3,
                cursorBrush = SolidColor(PrimaryColor)
            ) { innerTextField ->
                Box {
                    innerTextField()

                    if (value.isEmpty() && placeholder?.isNotEmpty() == true) {
                        Text(
                            text = placeholder,
                            color = LightColor,
                            fontSize = 16.sp
                        )
                    }

                    max?.let {
                        Text(
                            text = "${value.length}/$it",
                            color = LightColor,
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
        Divider(thickness = 0.5.dp, color = BorderColor)
    }
}