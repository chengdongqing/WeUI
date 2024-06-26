package top.chengdongqing.weui.feature.network.download

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import top.chengdongqing.weui.feature.network.download.repository.DownloadRepositoryImpl

class DownloadViewModel : ViewModel() {
    private val downloadRepository by lazy {
        DownloadRepositoryImpl()
    }

    suspend fun downloadFile(filename: String): ImageBitmap? {
        return downloadRepository.downloadFile(filename)?.byteStream()?.use {
            BitmapFactory.decodeStream(it).asImageBitmap()
        }
    }
}