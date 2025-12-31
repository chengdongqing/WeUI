package top.chengdongqing.weui.feature.form.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.picker.DateType
import top.chengdongqing.weui.core.ui.components.picker.TimeType
import top.chengdongqing.weui.core.ui.components.picker.WePicker
import top.chengdongqing.weui.core.ui.components.picker.rememberDatePickerState
import top.chengdongqing.weui.core.ui.components.picker.rememberSingleColumnPickerState
import top.chengdongqing.weui.core.ui.components.picker.rememberTimePickerState
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.ChineseDateFormatter
import top.chengdongqing.weui.core.utils.DefaultTimeFormatter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PickerScreen() {
    WeScreen(
        title = "Picker",
        description = "滚动选择器",
        padding = PaddingValues(bottom = 100.dp)
    ) {
        DatePickDemo()
        Spacer(modifier = Modifier.height(40.dp))
        TimePickDemo()
        Spacer(modifier = Modifier.height(40.dp))
        CountryPickDemo()
        Spacer(modifier = Modifier.height(40.dp))
        CarPickDemo()
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
    WeButton(text = "选择年月日") {
        state.show(value) {
            value = it
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择年月", type = ButtonType.PLAIN) {
        state.show(value, type = DateType.MONTH) {
            value = it
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择年", type = ButtonType.PLAIN) {
        state.show(value, type = DateType.YEAR) {
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
    WeButton(text = "选择时分秒") {
        picker.show(value) {
            value = it
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择时分", type = ButtonType.PLAIN) {
        picker.show(value, type = TimeType.MINUTE) {
            value = it
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择时", type = ButtonType.PLAIN) {
        picker.show(value, type = TimeType.HOUR) {
            value = it
        }
    }
}

@Composable
private fun CountryPickDemo() {
    val picker = rememberSingleColumnPickerState()
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

@Composable
private fun CarPickDemo() {
    var visible by remember { mutableStateOf(false) }
    var values by remember { mutableStateOf(arrayOf(0, 0)) }

    val sourceMap = remember {
        arrayOf(
            "小米" to listOf("YU7", "SU7", "SU7 Ultra"),
            "小鹏" to listOf("X9", "G9", "G7", "G6", "M03", "P7+", "P7"),
            "理想" to listOf("MEGA", "i6", "i8", "L9", "L8", "L7", "L6")
        )
    }
    var tmpValues by remember { mutableStateOf(values) }
    val ranges by remember {
        derivedStateOf {
            arrayOf(
                sourceMap.map { it.first },
                sourceMap[tmpValues.first()].second
            )
        }
    }

    WePicker(
        visible,
        ranges,
        values,
        title = "选择汽车",
        onCancel = { visible = false },
        onColumnValueChange = { _, _, newValues ->
            tmpValues = newValues
        }
    ) {
        values = it
    }

    WeInput(
        value = remember(values) {
            arrayOf(
                ranges[0].getOrNull(values[0]) ?: "",
                ranges[1].getOrNull(values[1]) ?: ""
            ).joinToString(" ")
        },
        textAlign = TextAlign.Center,
        disabled = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "选择汽车") {
        visible = true
    }
}