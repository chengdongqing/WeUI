package top.chengdongqing.weui.ui.screens.location

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import top.chengdongqing.weui.ui.components.dialog.DialogOptions
import top.chengdongqing.weui.ui.components.dialog.rememberWeDialog
import top.chengdongqing.weui.ui.components.location.picker.WeLocationPicker
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.formatDistance

@Composable
fun LocationPickerScreen() {
    val dialog = rememberWeDialog()

    WeLocationPicker(onCancel = {}) {
        dialog.show(
            DialogOptions(
                title = "您选择的位置",
                content = buildString {
                    appendLine(it.name)
                    appendLine(it.address)
                    appendLine("距离：" + formatDistance(it.distance))
                    append(it.latLng.toString())
                },
                onCancel = null
            )
        )
    }
}

@Preview
@Composable
private fun PreviewLocationPicker() {
    WeUITheme {
        LocationPickerScreen()
    }
}