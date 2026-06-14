package top.chengdongqing.weui.samples.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.components.WeButton
import top.chengdongqing.weui.components.WeDigitalRoller
import top.chengdongqing.weui.components.WeRadioGroup
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.theme.WeTheme
import top.chengdongqing.weui.util.randomFloat

@Composable
fun DigitalRollerScreen(onBack: () -> Unit) {
    WeScreen(
        title = "DigitalRoller",
        description = "数字滚轮，数值变化时产生滚动效果",
        onBack = onBack
    ) {
        var value by remember { mutableFloatStateOf(0f) }
        val durationOptions = remember {
            listOf(
                Pair("400ms", 400),
                Pair("800ms", 800),
                Pair("1600ms", 1600)
            )
        }
        var duration by remember { mutableIntStateOf(durationOptions[1].second) }
        val decimalOptions = remember {
            listOf(
                Pair("不保留", 0),
                Pair("保留1位", 1),
                Pair("保留2位", 2)
            )
        }
        var decimals by remember { mutableIntStateOf(decimalOptions[2].second) }

        WeDigitalRoller(value, decimals, duration)
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "更新数值") {
            value = randomFloat(1f, 10000f)
        }
        Spacer(modifier = Modifier.height(40.dp))
        RadioCard(title = "动画时长", options = durationOptions, value = duration) {
            duration = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        RadioCard(title = "保留小数", options = decimalOptions, value = decimals) {
            decimals = it
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun <T> RadioCard(
    title: String,
    options: List<Pair<String, T>>,
    value: T,
    onChange: (T) -> Unit
) {
    Text(
        text = title,
        color = WeTheme.colorScheme.textSecondary,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    WeRadioGroup(
        options = options,
        value = value,
        onChange = onChange
    )
}