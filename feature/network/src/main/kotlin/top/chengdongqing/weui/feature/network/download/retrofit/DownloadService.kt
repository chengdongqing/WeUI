package top.chengdongqing.weui.feature.network.download.retrofit

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface DownloadService {
    @Streaming
    @GET("{filename}")
    suspend fun downloadFile(@Path("filename") filename: String): ResponseBody
}