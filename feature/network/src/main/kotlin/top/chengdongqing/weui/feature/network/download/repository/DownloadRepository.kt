package top.chengdongqing.weui.feature.network.download.repository

import okhttp3.ResponseBody

interface DownloadRepository {
    suspend fun downloadFile(filename: String): ResponseBody?
}