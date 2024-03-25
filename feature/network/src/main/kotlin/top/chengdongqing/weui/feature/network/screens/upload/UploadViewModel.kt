package top.chengdongqing.weui.feature.network.screens.upload

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
import top.chengdongqing.weui.feature.network.screens.upload.repository.UploadRepositoryImpl
import java.io.File

class UploadViewModel : ViewModel() {
    private val uploadRepository by lazy {
        UploadRepositoryImpl()
    }

    suspend fun uploadFile(context: Context, uri: Uri): UploadResponse.Files.FileItem? {
        val ctx = context.applicationContext
        return withContext(Dispatchers.IO) {
            // 查询文件元数据
            val deferredMetadata = async<Pair<String, String>?> {
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
            }
            // 构建临时文件
            val deferredFile = async {
                ctx.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val file = File.createTempFile("uploadFile", ".temp").apply {
                        deleteOnExit()
                    }
                    inputStream.copyTo(file.outputStream())
                    return@async file
                }
                null
            }
            val metadata = deferredMetadata.await()
            val file = deferredFile.await()

            if (metadata != null && file != null) {
                val requestFile = file.asRequestBody(metadata.second.toMediaType())
                val body = MultipartBody.Part.createFormData("file", metadata.first, requestFile)

                uploadRepository.uploadFile(body).body()?.files?.file
            } else {
                null
            }
        }
    }
}