package top.chengdongqing.weui.feature.network.screens.upload.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.upload.data.model.UploadResponse
import top.chengdongqing.weui.feature.network.screens.upload.retrofit.RetrofitManger
import top.chengdongqing.weui.feature.network.screens.upload.retrofit.UploadService

class UploadRepositoryImpl : UploadRepository {
    private val uploadService by lazy {
        RetrofitManger.retrofit.create(UploadService::class.java)
    }

    override suspend fun uploadFile(file: MultipartBody.Part): Response<UploadResponse> {
        return withContext(Dispatchers.IO) { uploadService.uploadFile(file) }
    }
}