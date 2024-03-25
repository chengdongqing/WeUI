package top.chengdongqing.weui.feature.form.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.picker.rememberDatePickerState
import top.chengdongqing.weui.core.ui.components.picker.rememberPickerState
import top.chengdongqing.weui.core.ui.components.picker.rememberTimePickerState
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.ChineseDateFormatter
import top.chengdongqing.weui.core.utils.DefaultTimeFormatter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PickerScreen() {
    WeScreen(title = "Picker", description = "滚动选择器") {
        DatePickDemo()
        Spacer(modifier = Modifier.height(40.dp))
        TimePickDemo()
        Spacer(modifier = Modifier.height(40.dp))
        CountryPickDemo()
    }
}

@Composable
private fun DatePickDemo() {
    val state = rememberDatePickerState()
    var value by remember { mutableStateOf(LocalDate.now()) }

    WeInput(
        value = value.format(DateTimeFormatter.ofPattern(ChineseDateFormatter)),
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择日期") {
        state.show(value) {
            value = it
        }
    }
}

@Composable
private fun TimePickDemo() {
    val picker = rememberTimePickerState()
    var value by remember { mutableStateOf(LocalTime.now()) }

    WeInput(
        value = value.format(DateTimeFormatter.ofPattern(DefaultTimeFormatter)),
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择时间") {
        picker.show(value) {
            value = it
        }
    }
}

@Composable
private fun CountryPickDemo() {
    val picker = rememberPickerState()
    var value by remember { mutableIntStateOf(0) }
    val range = remember {
        listOf(
            "中国",
            "美国",
            "德国",
            "法国",
            "英国",
            "瑞士",
            "希腊",
            "西班牙",
            "荷兰"
        )
    }

    WeInput(
        value = range[value],
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择国家") {
        picker.show(
            title = "选择国家",
            range,
            value
        ) {
            value = it
        }
    }
}