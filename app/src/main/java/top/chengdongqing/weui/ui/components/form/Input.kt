package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.basic.WeDivider
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

@Composable
fun WeInput(
    value: String?,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    disabled: Boolean = false,
    labelWidth: Dp = 68.dp,
    textAlign: TextAlign = TextAlign.Unspecified,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClick: (() -> Unit)? = null,
    onChange: ((String) -> Unit)? = null
) {
    val localValue = value ?: ""

    Column {
        Row(modifier = modifier.height(56.dp), verticalAlignment = Alignment.CenterVertically) {
            if (label?.isNotEmpty() == true) {
                Text(text = label, fontSize = 16.sp, modifier = Modifier.width(labelWidth))
                Spacer(modifier = Modifier.width(16.dp))
            }
            BasicTextField(
                value = localValue,
                onValueChange = {
                    onChange?.invoke(it)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                readOnly = disabled,
                singleLine = true,
                textStyle = TextStyle(color = FontColor, fontSize = 16.sp, textAlign = textAlign),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password)
                    PasswordVisualTransformation() else VisualTransformation.None,
                cursorBrush = SolidColor(PrimaryColor)
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(onClick?.let {
                            Modifier.clickableWithoutRipple { it() }
                        } ?: Modifier),
                    contentAlignment = when (textAlign) {
                        TextAlign.Center -> Alignment.Center
                        TextAlign.Right -> Alignment.CenterEnd
                        else -> Alignment.CenterStart
                    }
                ) {
                    innerTextField()
                    if (localValue.isEmpty() && placeholder?.isNotEmpty() == true) {
                        Text(
                            text = placeholder,
                            color = LightColor,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        WeDivider()
    }
}