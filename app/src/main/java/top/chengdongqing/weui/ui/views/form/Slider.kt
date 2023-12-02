package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeSlider

@Composable
fun SliderPage() {
    Page(title = "Slider", description = "滑块") {
        val value = remember {
            mutableFloatStateOf(0f)
        }

        WeSlider(value.floatValue, onValueChange = {
            value.floatValue = it
        })
    }
}