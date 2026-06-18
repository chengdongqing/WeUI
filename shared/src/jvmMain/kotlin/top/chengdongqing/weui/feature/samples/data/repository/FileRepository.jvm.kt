package top.chengdongqing.weui.feature.samples.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import top.chengdongqing.weui.feature.samples.data.model.JvmFileNode
import top.chengdongqing.weui.util.formatFileSize
import top.chengdongqing.weui.util.formatTime
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_document
import weui_kmp.shared.generated.resources.ic_file
import weui_kmp.shared.generated.resources.ic_music
import weui_kmp.shared.generated.resources.ic_picture
import weui_kmp.shared.generated.resources.ic_video
import java.awt.Desktop
import java.io.File
import java.nio.file.Files

class JvmFileRepository : FileRepository {
    override suspend fun getChildren(fileNode: FileNode): List<FileItem> {
        val node = fileNode as JvmFileNode

        return withContext(Dispatchers.IO) {
            node.file.listFiles()
                ?.sortedWith(
                    compareBy<File> { !it.isDirectory }
                        .thenBy { it.name }
                )
                ?.map { file ->
                    FileItem(
                        name = file.name,
                        node = JvmFileNode(
                            file = file,
                            id = file.path,
                            name = file.name,
                            isDirectory = file.isDirectory
                        ),
                        iconRes = file.getFileIcon(),
                        size = formatFileSize(file),
                        mimeType = file.getMimeType(),
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
        val mimeType = getMimeType()
        return mimeType.startsWith("image") || mimeType.startsWith("video")
    }

    private fun File.getMimeType(): String {
        return runCatching {
            Files.probeContentType(toPath()) ?: "*/*"
        }.getOrDefault("*/*")
    }


    private fun File.getFileIcon(): DrawableResource {
        val mimeType = getMimeType()
        return mimeType.let {
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
        val node = fileItem.node as JvmFileNode
        val file = node.file

        if (file.exists() && Desktop.isDesktopSupported()) {
            runCatching {
                Desktop.getDesktop().open(file)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    override suspend fun delete(fileNode: FileNode): Boolean = withContext(Dispatchers.IO) {
        val file = (fileNode as JvmFileNode).file
        if (file.isDirectory) {
            file.deleteRecursively()
        } else {
            file.delete()
        }
    }
}

actual fun getFileRepository(): FileRepository = JvmFileRepository()