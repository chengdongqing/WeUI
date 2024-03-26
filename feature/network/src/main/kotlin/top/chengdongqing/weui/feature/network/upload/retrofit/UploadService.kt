package top.chengdongqing.weui.feature.network.upload.retrofit

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import top.chengdongqing.weui.feature.network.upload.data.model.UploadResult

interface UploadService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): UploadResult
}