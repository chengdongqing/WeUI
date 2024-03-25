package top.chengdongqing.weui.feature.network.screens.upload.retrofit

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import top.chengdongqing.weui.feature.network.screens.upload.data.model.UploadResponse

interface UploadService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Response<UploadResponse>
}