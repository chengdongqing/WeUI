package top.chengdongqing.weui.ui.screens.qrcode.scanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import top.chengdongqing.weui.ui.components.dialog.WeDialogOptions
import top.chengdongqing.weui.ui.components.dialog.rememberWeDialog
import top.chengdongqing.weui.ui.components.qrcode.scanner.WeQrCodeScanner
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import top.chengdongqing.weui.ui.screens.system.setClipboardData
import top.chengdongqing.weui.utils.RequestCameraPermission

@Composable
fun QrCodeScanScreen(navController: NavController) {
    val context = LocalContext.current
    val dialog = rememberWeDialog()
    val toast = rememberWeToast()

    RequestCameraPermission(navController) {
        WeQrCodeScanner { res ->
            val value = res.first().rawValue ?: ""
            dialog.show(
                WeDialogOptions(
                    title = "扫描结果",
                    content = value,
                    cancelText = "复制",
                    onCancel = {
                        setClipboardData(context, value)
                        toast.show(WeToastOptions("已复制", ToastIcon.SUCCESS))
                    }
                )
            )
        }
    }
}