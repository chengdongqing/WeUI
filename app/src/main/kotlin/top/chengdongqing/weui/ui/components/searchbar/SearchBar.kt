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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.theme.LinkColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

@Composable
fun WeSearchBar(value: String, onChange: (value: String) -> Unit) {
    var focused by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember {
        FocusRequester()
    }
    // 输入框自动聚焦
    LaunchedEffect(focused) {
        if (focused) {
            focusRequester.requestFocus()
        }
    }

    // 返回时先取消聚焦
    BackHandler(focused) {
        focused = false
    }

    Row(
        modifier = Modifier
            .height(38.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(4.dp))
        ) {
            if (focused) {
                BasicTextField(
                    value,
                    onValueChange = onChange,
                    modifier = Modifier.focusRequester(focusRequester),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color(0f, 0f, 0f, 0.9f)),
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
                                tint = FontColor1
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (value.isEmpty()) {
                                    Text(
                                        text = "搜索",
                                        color = LightColor,
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
                            focused = true
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
                        tint = FontColor1
                    )
                    Text(text = "搜索", color = FontColor1, fontSize = 16.sp)
                }
            }
        }
        if (focused) {
            Text(
                text = "取消",
                color = LinkColor,
                modifier = Modifier
                    .clickableWithoutRipple {
                        focused = false
                    }
                    .padding(start = 8.dp)
            )
        }
    }
}