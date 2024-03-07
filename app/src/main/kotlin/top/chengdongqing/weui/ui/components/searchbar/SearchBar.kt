package top.chengdongqing.weui.ui.components.searchbar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontLinkColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

@Composable
fun WeSearchBar(
    value: String,
    modifier: Modifier = Modifier,
    label: String = "搜索",
    focused: Boolean? = null,
    onFocusChange: ((Boolean) -> Unit)? = null,
    onChange: (value: String) -> Unit
) {
    var localFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val finalFocused = focused ?: localFocused
    fun setFocus(value: Boolean) {
        localFocused = value
        onFocusChange?.invoke(value)
    }

    // 输入框自动聚焦
    LaunchedEffect(finalFocused) {
        if (finalFocused) {
            focusRequester.requestFocus()
        }
    }
    // 返回时先取消聚焦
    BackHandler(finalFocused) {
        setFocus(false)
    }

    Row(
        modifier = modifier
            .height(38.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
        ) {
            if (finalFocused) {
                BasicTextField(
                    value,
                    onValueChange = onChange,
                    modifier = Modifier.focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    cursorBrush = SolidColor(PrimaryColor),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 4.dp)
                                    .size(20.dp),
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (value.isEmpty()) {
                                    Text(
                                        text = label,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    })
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickableWithoutRipple {
                            setFocus(true)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp
                    )
                }
            }
        }
        if (finalFocused) {
            Text(
                text = "取消",
                color = FontLinkColor,
                modifier = Modifier
                    .clickableWithoutRipple {
                        setFocus(false)
                        onChange("")
                    }
                    .padding(start = 8.dp)
            )
        }
    }
}