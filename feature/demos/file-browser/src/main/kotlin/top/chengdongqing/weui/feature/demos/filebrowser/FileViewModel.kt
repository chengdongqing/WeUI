package top.chengdongqing.weui.feature.demos.filebrowser

import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.utils.formatFileSize
import top.chengdongqing.weui.core.utils.formatTime
import java.io.File

class FileViewModel : ViewModel() {
    var fileList by mutableStateOf<List<FileItem>>(emptyList())
    var loading by mutableStateOf(true)

    fun refresh(filePath: String) {
        viewModelScope.launch {
            loading = true
            fileList = buildFileList(filePath)
            loading = false
        }
    }

    private suspend fun buildFileList(filePath: String): List<FileItem> =
        withContext(Dispatchers.IO) {
            File(filePath).listFiles()?.filter { !it.isHidden }
                ?.sortedWith(compareBy {
                    !it.isDirectory
                })?.map { file ->
                    FileItem(
                        name = file.name,
                        path = file.path,
                        isDirectory = file.isDirectory,
                        size = formatFileSize(file),
                        lastModified = formatTime(file.lastModified()),
                        childrenCount = file.listFiles()?.filter { !it.isHidden }?.size ?: 0,
                        iconId = getIconByFileCategory(file)
                    )
                } ?: emptyList()
        }

    private fun getIconByFileCategory(file: File): Int {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)?.let {
            when {
                it.startsWith("image") -> R.drawable.ic_picture
                it.startsWith("video") -> R.drawable.ic_video
                it.startsWith("audio") -> R.drawable.ic_music
                it.startsWith("text")
                        || it.endsWith("pdf")
                        || it.endsWith("msword")
                        || it.endsWith("vnd.ms-excel")
                        || it.endsWith("vnd.ms-powerpoint")
                -> R.drawable.ic_document

                else -> R.drawable.ic_file
            }
        } ?: when (file.extension) {
            "apk" -> R.drawable.ic_apk
            "mp3", "flac", "aac", "wav" -> R.drawable.ic_music
            else -> {
                R.drawable.ic_file
            }
        }
    }
}