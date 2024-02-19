package top.chengdongqing.weui.ui.views.network.download

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DownloadViewModel : ViewModel() {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://s1.xiaomiev.com/activity-outer-assets/web/home/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val downloadService by lazy { retrofit.create(DownloadService::class.java) }

    suspend fun downloadFile(filename: String): ImageBitmap? {
        return downloadService.downloadFile(filename).body()?.byteStream()?.use {
            BitmapFactory.decodeStream(it).asImageBitmap()
        }
    }
}