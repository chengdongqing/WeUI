package top.chengdongqing.weui.ui.screens.network.upload

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class UploadViewModel : ViewModel() {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://unidemo.dcloud.net.cn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val uploadService by lazy { retrofit.create(UploadService::class.java) }

    suspend fun uploadFile(context: Context, uri: Uri): UploadResponse.Files.FileItem? {
        val ctx = context.applicationContext
        return withContext(Dispatchers.IO) {
            // 查询文件元数据
            val metadata = async<Pair<String, String>?> {
                val projection = arrayOf(
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.MIME_TYPE
                )
                ctx.contentResolver.query(uri, projection, null, null)?.use { cursor ->
                    val nameColumn = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    val mimeTypeColumn =
                        cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)

                    if (cursor.moveToFirst()) {
                        val fileName = cursor.getString(nameColumn)
                        val mimeType = cursor.getString(mimeTypeColumn)
                        return@async fileName to mimeType
                    }
                }
                null
            }.await()
            // 构建临时文件
            val file = async {
                ctx.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val file = File(ctx.cacheDir, "uploadFile")
                    inputStream.copyTo(file.outputStream())
                    return@async file
                }
                null
            }.await()

            if (metadata != null && file != null) {
                val requestFile = file.asRequestBody(metadata.second.toMediaType())
                val body = MultipartBody.Part.createFormData("file", metadata.first, requestFile)

                uploadService.uploadFile(body).body()?.files?.file
            } else {
                null
            }
        }
    }
}