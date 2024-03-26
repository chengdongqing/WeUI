package top.chengdongqing.weui.feature.network.download.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class DownloadRepositoryImpl : DownloadRepository {
    private val downloadService by lazy {
        RetrofitManger.retrofit.create(DownloadService::class.java)
    }

    override suspend fun downloadFile(filename: String): Response<ResponseBody> {
        return withContext(Dispatchers.IO) { downloadService.downloadFile(filename) }
    }
}