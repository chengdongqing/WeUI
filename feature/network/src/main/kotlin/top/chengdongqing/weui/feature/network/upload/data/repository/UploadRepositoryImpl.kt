package top.chengdongqing.weui.feature.network.upload.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import top.chengdongqing.weui.feature.network.upload.data.model.UploadResult
import top.chengdongqing.weui.feature.network.upload.retrofit.RetrofitManger
import top.chengdongqing.weui.feature.network.upload.retrofit.UploadService

class UploadRepositoryImpl : UploadRepository {
    private val uploadService by lazy {
        RetrofitManger.retrofit.create(UploadService::class.java)
    }

    override suspend fun uploadFile(file: MultipartBody.Part): UploadResult {
        return withContext(Dispatchers.IO) {
            uploadService.uploadFile(file)
        }
    }
}