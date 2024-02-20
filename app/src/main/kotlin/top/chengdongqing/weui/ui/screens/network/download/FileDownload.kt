package top.chengdongqing.weui.ui.screens.network.download

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
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun FileDownloadScreen(downloadViewModel: DownloadViewModel = viewModel()) {
    WeScreen(title = "FileDownload", description = "文件下载") {
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        var downloading by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        bitmap?.let {
            Image(it, contentDescription = null)
        } ?: WeButton(
            text = if (downloading) "下载中..." else "下载图片",
            loading = downloading
        ) {
            coroutineScope.launch {
                downloading = true
                bitmap = downloadViewModel.downloadFile("section1.jpg")
                downloading = false
            }
        }
    }
}