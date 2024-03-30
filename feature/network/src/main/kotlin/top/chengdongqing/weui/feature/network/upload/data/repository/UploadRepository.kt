package top.chengdongqing.weui.feature.network.upload.data.repository

import okhttp3.MultipartBody
import top.chengdongqing.weui.feature.network.upload.data.model.UploadResult

interface UploadRepository {
    suspend fun uploadFile(file: MultipartBody.Part): UploadResult?
}