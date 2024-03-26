package top.chengdongqing.weui.feature.samples.filebrowser.data.repository

import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.utils.formatFileSize
import top.chengdongqing.weui.core.utils.formatTime
import top.chengdongqing.weui.feature.samples.filebrowser.R
import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem
import java.io.File

class FileRepositoryImpl : FileRepository {
    override suspend fun getListFiles(filepath: String): List<FileItem> =
        withContext(Dispatchers.IO) {
            File(filepath).listFiles()?.filter { !it.isHidden }
                ?.sortedWith(compareBy {
                    !it.isDirectory
                })?.map { file ->
                    FileItem(
                        name = file.name,
                        path = file.path,
                        size = formatFileSize(file),
                        mimeType = getFileMimeType(file),
                        isDirectory = file.isDirectory,
                        lastModified = formatTime(file.lastModified()),
                        childrenCount = file.listFiles()?.filter { !it.isHidden }?.size ?: 0,
                        iconId = getFileIcon(file)
                    )
                } ?: emptyList()
        }

    private fun getFileMimeType(file: File): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: when (file.extension) {
                "mp4", "mkv", "flv" -> "video/*"
                "mp3", "flac", "aac", "wav" -> "audio/*"
                else -> "*/*"
            }
    }

    private fun getFileIcon(file: File): Int {
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