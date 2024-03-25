package top.chengdongqing.weui.feature.network.screens.upload.data.repository

import okhttp3.MultipartBody
import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.upload.data.model.UploadResponse

interface UploadRepository {
    suspend fun uploadFile(file: MultipartBody.Part): Response<UploadResponse>
}