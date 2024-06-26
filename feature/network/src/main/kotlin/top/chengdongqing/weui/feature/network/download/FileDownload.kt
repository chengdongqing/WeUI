package top.chengdongqing.weui.feature.network.download

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState

@Composable
fun FileDownloadScreen(downloadViewModel: DownloadViewModel = viewModel()) {
    WeScreen(title = "FileDownload", description = "文件下载") {
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        var downloading by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val toast = rememberToastState()

        bitmap?.let {
            Image(bitmap = it, contentDescription = null)
        } ?: WeButton(
            text = if (downloading) "下载中..." else "下载图片",
            loading = downloading
        ) {
            downloading = true
            coroutineScope.launch {
                bitmap = downloadViewModel.downloadFile("section1.jpg").also {
                    if (it == null) {
                        toast.show("下载失败", ToastIcon.FAIL)
                    }
                }
                downloading = false
            }
        }
    }
}