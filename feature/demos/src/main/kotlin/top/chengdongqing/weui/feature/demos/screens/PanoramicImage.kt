package top.chengdongqing.weui.feature.demos.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.rememberToggleState
import top.chengdongqing.weui.feature.demos.R
import top.chengdongqing.weui.feature.demos.components.WePanoramicImage

@Composable
fun PanoramicImageScreen() {
    WeScreen(
        title = "PanoramicImage",
        description = "全景图片，无缝衔接滚动",
        padding = PaddingValues(0.dp)
    ) {
        val image = ImageBitmap.imageResource(id = R.drawable.panoramic_yosemite_park)
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