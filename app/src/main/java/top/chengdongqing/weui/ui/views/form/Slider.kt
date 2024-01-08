package top.chengdongqing.weui.ui.views.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeSlider

@Composable
fun SliderPage() {
    Page(title = "Slider", description = "滑块") {
        var value by remember {
            mutableIntStateOf(0)
        }
        var step by remember {
            mutableIntStateOf(1)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.padding(24.dp)) {
                WeSlider(value, step) {
                    value = it
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            Text(text = "value: $value")
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "设置值为50") {
                value = 50
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                WeButton(text = "设置步长为10", size = ButtonSize.SMALL) {
                    step = 10
                }
                Spacer(modifier = Modifier.width(10.dp))
                WeButton(text = "设置步长为1", size = ButtonSize.SMALL) {
                    step = 1
                }
            }
        }
    }
}