package top.chengdongqing.weui.ui.screens.network.upload

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Response<UploadResponse>
}

data class UploadResponse(
    val error: String,
    val strings: List<String>,
    val files: Files
) {
    data class Files(val file: FileItem) {
        data class FileItem(
            val name: String,
            val size: Long,
            val type: String,
            val url: String
        )
    }
}