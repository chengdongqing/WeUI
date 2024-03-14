package top.chengdongqing.weui.ui.screens.qrcode.scanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import top.chengdongqing.weui.ui.components.dialog.rememberDialogState
import top.chengdongqing.weui.ui.components.qrcode.scanner.WeQrCodeScanner
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState
import top.chengdongqing.weui.utils.RequestCameraPermission
import top.chengdongqing.weui.utils.setClipboardData

@Composable
fun QrCodeScanScreen(navController: NavController) {
    val context = LocalContext.current
    val dialog = rememberDialogState()
    val toast = rememberToastState()

    RequestCameraPermission(navController) {
        WeQrCodeScanner { res ->
            val value = res.first().rawValue ?: ""
            dialog.show(
                title = "扫描结果",
                content = value,
                cancelText = "复制",
                onCancel = {
                    context.setClipboardData(value)
                    toast.show("已复制", ToastIcon.SUCCESS)
                }
            )
        }
    }
}