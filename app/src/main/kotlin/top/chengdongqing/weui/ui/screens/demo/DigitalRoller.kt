package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.digitalroller.WeDigitalRoller
import top.chengdongqing.weui.ui.components.radio.RadioOption
import top.chengdongqing.weui.ui.components.radio.WeRadioGroup
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.randomFloatInRange

@Composable
fun DigitalRollerScreen() {
    WeScreen(title = "DigitalRoller", description = "数字滚轮，数值变化时产生滚动效果") {
        var value by remember { mutableFloatStateOf(0f) }
        val durationOptions = remember {
            listOf(
                RadioOption(label = "400ms", value = 400),
                RadioOption(label = "800ms", value = 800),
                RadioOption(label = "1600ms", value = 1600)
            )
        }
        var duration by remember { mutableIntStateOf(durationOptions[1].value) }
        val decimalOptions = remember {
            listOf(
                RadioOption(label = "不保留", value = 0),
                RadioOption(label = "保留1位", value = 1),
                RadioOption(label = "保留2位", value = 2)
            )
        }
        var decimals by remember { mutableIntStateOf(decimalOptions[2].value) }

        WeDigitalRoller(value, decimals, duration)
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数值") {
            value = randomFloatInRange(1f, 10000f)
        }
        Spacer(modifier = Modifier.height(40.dp))
        RadioCard(title = "动画时长", options = durationOptions, value = duration) {
            duration = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        RadioCard(title = "保留小数", options = decimalOptions, value = decimals) {
            decimals = it
        }
    }
}

@Composable
private fun <T> RadioCard(
    title: String,
    options: List<RadioOption<T>>,
    value: T,
    onChange: (T) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onSecondary)
    }
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.onBackground,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp)
    ) {
        WeRadioGroup(options, value, onChange = onChange)
    }
}

@Preview
@Composable
private fun PreviewDigitalRoller() {
    WeUITheme {
        DigitalRollerScreen()
    }
}