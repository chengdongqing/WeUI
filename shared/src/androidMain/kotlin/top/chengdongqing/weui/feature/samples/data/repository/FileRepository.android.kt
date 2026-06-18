package top.chengdongqing.weui.feature.samples.data.repository

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import top.chengdongqing.weui.androidAppInstance
import top.chengdongqing.weui.feature.samples.data.model.AndroidFileNode
import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import top.chengdongqing.weui.util.formatFileSize
import top.chengdongqing.weui.util.formatTime
import top.chengdongqing.weui.util.getFileProviderUri
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_apk
import weui_kmp.shared.generated.resources.ic_document
import weui_kmp.shared.generated.resources.ic_file
import weui_kmp.shared.generated.resources.ic_music
import weui_kmp.shared.generated.resources.ic_picture
import weui_kmp.shared.generated.resources.ic_video
import java.io.File

class AndroidFileRepository(val context: Context) : FileRepository {
    override suspend fun getChildren(fileNode: FileNode): List<FileItem> {
        val node = fileNode as AndroidFileNode

        return withContext(Dispatchers.IO) {
            node.file.listFiles()
                ?.sortedWith(
                    compareBy<File> { !it.isDirectory }
                        .thenBy { it.name }
                )
                ?.map { file ->
                    FileItem(
                        name = file.name,
                        node = AndroidFileNode(
                            file = file,
                            id = file.path,
                            name = file.name,
                            isDirectory = file.isDirectory
                        ),
                        iconRes = file.getFileIcon(),
                        size = formatFileSize(file),
                        mimeType = file.getFileMimeType(),
                        isDirectory = file.isDirectory,
                        isVisualMedia = file.isVisualMedia(),
                        isReadable = file.canRead(),
                        isWriteable = file.canWrite(),
                        isHidden = file.isHidden,
                        lastModified = formatTime(file.lastModified()),
                        childrenCount = file.listFiles()?.filter { !it.isHidden }?.size ?: 0
                    )
                } ?: emptyList()
        }
    }

    private fun File.isVisualMedia(): Boolean {
        val mimeType = getFileMimeType()
        return mimeType.startsWith("image") || mimeType.startsWith("video")
    }

    private fun File.getFileMimeType(): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: when (this.extension) {
                "mp4", "mkv", "flv" -> "video/*"
                "mp3", "flac", "aac", "wav" -> "audio/*"
                else -> "*/*"
            }
    }

    private fun File.getFileIcon(): DrawableResource {
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)?.let {
            when {
                it.startsWith("image") -> Res.drawable.ic_picture
                it.startsWith("video") -> Res.drawable.ic_video
                it.startsWith("audio") -> Res.drawable.ic_music
                it.startsWith("text")
                        || it.endsWith("pdf")
                        || it.endsWith("msword")
                        || it.endsWith("vnd.ms-excel")
                        || it.endsWith("vnd.ms-powerpoint")
                    -> Res.drawable.ic_document

                else -> Res.drawable.ic_file
            }
        } ?: when (this.extension) {
            "apk" -> Res.drawable.ic_apk
            "mp3", "flac", "aac", "wav" -> Res.drawable.ic_music
            else -> {
                Res.drawable.ic_file
            }
        }
    }

    /**
     * 格式化文件大小
     */
    fun formatFileSize(file: File): String {
        val size = if (file.exists()) file.length() else 0
        return formatFileSize(size)
    }

    override suspend fun open(fileItem: FileItem) {
        val node = fileItem.node as AndroidFileNode

        val uri = context.getFileProviderUri(node.file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, fileItem.mimeType)
            // 授予临时访问权限
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val finalIntent = Intent.createChooser(intent, "打开文件").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(finalIntent)
    }

    override suspend fun delete(fileNode: FileNode): Boolean = withContext(Dispatchers.IO) {
        val file = (fileNode as AndroidFileNode).file
        if (file.isDirectory) {
            file.deleteRecursively()
        } else {
            file.delete()
        }
    }
}

actual fun getFileRepository(): FileRepository =
    AndroidFileRepository(androidAppInstance.applicationContext)