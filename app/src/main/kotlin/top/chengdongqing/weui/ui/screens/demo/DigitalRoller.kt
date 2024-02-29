package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.digitalroller.WeDigitalRoller
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme
import kotlin.random.Random

@Composable
fun DigitalRollerScreen() {
    WeScreen(title = "DigitalRoller", description = "数字滚轮，数值变化时产生滚动效果") {
        var value by remember { mutableFloatStateOf(0f) }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeDigitalRoller(value)
            Spacer(modifier = Modifier.height(40.dp))
            WeButton(text = "更新数值") {
                val integer = Random.nextInt(1, 10000)
                val decimal = Random.nextInt(10, 99)
                value = "$integer.$decimal".toFloat()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDigitalRoller() {
    WeUITheme {
        DigitalRollerScreen()
    }
}