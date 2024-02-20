package top.chengdongqing.weui.ui.screens.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.constants.ChineseDateFormatter
import top.chengdongqing.weui.constants.DefaultTimeFormatter
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.input.WeInput
import top.chengdongqing.weui.ui.components.picker.WeDatePicker
import top.chengdongqing.weui.ui.components.picker.WeSingleColumnPicker
import top.chengdongqing.weui.ui.components.picker.WeTimePicker
import top.chengdongqing.weui.ui.components.screen.WeScreen
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PickerScreen() {
    WeScreen(title = "Picker", description = "滚动选择器") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DatePickDemo()
            Spacer(modifier = Modifier.height(40.dp))
            TimePickDemo()
            Spacer(modifier = Modifier.height(40.dp))
            CountryPickDemo()
        }
    }
}

@Composable
private fun DatePickDemo() {
    var visible by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(LocalDate.now()) }

    WeDatePicker(visible, value, onCancel = { visible = false }) { value = it }
    WeInput(
        value = value.format(DateTimeFormatter.ofPattern(ChineseDateFormatter)),
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择日期") {
        visible = true
    }
}

@Composable
private fun TimePickDemo() {
    var visible by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(LocalTime.now()) }

    WeTimePicker(visible, value, onCancel = { visible = false }) { value = it }
    WeInput(
        value = value.format(DateTimeFormatter.ofPattern(DefaultTimeFormatter)),
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择时间") {
        visible = true
    }
}

@Composable
private fun CountryPickDemo() {
    var visible by remember { mutableStateOf(false) }
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

    WeSingleColumnPicker(
        visible,
        title = "选择国家",
        range = range,
        value = value,
        onChange = { value = it },
        onCancel = { visible = false }
    )
    WeInput(
        value = range[value],
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择国家") {
        visible = true
    }
}