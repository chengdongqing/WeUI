package top.chengdongqing.weui.feature.network.download.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.IOException

class DownloadRepositoryImpl : DownloadRepository {
    private val downloadService by lazy {
        RetrofitManger.retrofit.create(DownloadService::class.java)
    }

    override suspend fun downloadFile(filename: String): ResponseBody? {
        return withContext(Dispatchers.IO) {
            try {
                downloadService.downloadFile(filename)
            } catch (e: IOException) {
                null
            }
        }
    }
}