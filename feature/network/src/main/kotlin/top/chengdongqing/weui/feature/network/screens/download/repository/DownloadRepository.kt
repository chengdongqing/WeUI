package top.chengdongqing.weui.feature.network.screens.download.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface DownloadRepository {
    suspend fun downloadFile(filename: String): Response<ResponseBody>
}