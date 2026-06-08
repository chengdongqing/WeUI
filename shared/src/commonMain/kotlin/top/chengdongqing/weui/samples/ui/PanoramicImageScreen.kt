package top.chengdongqing.weui.samples.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.imageResource
import top.chengdongqing.weui.components.ButtonType
import top.chengdongqing.weui.components.WeButton
import top.chengdongqing.weui.components.WePanoramicImage
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.utils.rememberToggleState
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.panoramic_yosemite_park

@Composable
fun PanoramicImageScreen(onBack: () -> Unit) {
    WeScreen(
        title = "PanoramicImage",
        description = "全景图片",
        padding = PaddingValues(0.dp),
        onBack = onBack
    ) {
        val image = imageResource(Res.drawable.panoramic_yosemite_park)
        val (scrollStep, toggleScrollStep) = rememberToggleState(
            defaultValue = 0.75f,
            reverseValue = 5f
        )

        WePanoramicImage(image, scrollStep.value)
        Spacer(modifier = Modifier.height(40.dp))
        WeButton(text = "切换滚动速度", type = ButtonType.PLAIN) {
            toggleScrollStep()
        }
    }
}