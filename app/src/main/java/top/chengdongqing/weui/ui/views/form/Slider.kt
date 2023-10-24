package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeSlider

@Composable
fun SliderPage() {
    Page(title = "Slider", description = "滑块") {
        WeSlider()
    }
}