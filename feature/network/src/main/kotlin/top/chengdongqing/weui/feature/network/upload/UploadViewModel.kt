package top.chengdongqing.weui.feature.network.upload

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import top.chengdongqing.weui.core.utils.copyToFile
import top.chengdongqing.weui.feature.network.upload.data.model.UploadResult
import top.chengdongqing.weui.feature.network.upload.data.repository.UploadRepositoryImpl
import java.io.File

class UploadViewModel(private val application: Application) : AndroidViewModel(application) {
    private val uploadRepository by lazy {
        UploadRepositoryImpl()
    }

    suspend fun uploadFile(uri: Uri): UploadResult.Files.FileItem? {
        return withContext(Dispatchers.IO) {
            // 查询文件元数据
            val deferredMetadata = async<Pair<String, String>?> {
                val projection = arrayOf(
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.MIME_TYPE
                )
                application.contentResolver.query(uri, projection, null, null, null)
                    ?.use { cursor ->
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
                application.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val tempFile = File.createTempFile("uploadFile", null)
                    inputStream.copyToFile(tempFile)
                    return@async tempFile
                }
                null
            }
            val metadata = deferredMetadata.await()
            val file = deferredFile.await()

            if (metadata != null && file != null) {
                val requestFile = file.asRequestBody(metadata.second.toMediaType())
                val body = MultipartBody.Part.createFormData("file", metadata.first, requestFile)

                uploadRepository.uploadFile(body)?.files?.file
            } else {
                null
            }
        }
    }
}