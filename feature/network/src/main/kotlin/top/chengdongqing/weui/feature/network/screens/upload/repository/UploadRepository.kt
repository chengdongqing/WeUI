package top.chengdongqing.weui.feature.network.screens.upload.repository

import okhttp3.MultipartBody
import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.upload.UploadResponse

interface UploadRepository {
    suspend fun uploadFile(file: MultipartBody.Part): Response<UploadResponse>
}