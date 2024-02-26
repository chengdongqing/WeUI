package top.chengdongqing.weui.ui.screens.network.download

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface DownloadService {
    @Streaming
    @GET("{filename}")
    suspend fun downloadFile(@Path("filename") filename: String): Response<ResponseBody>
}