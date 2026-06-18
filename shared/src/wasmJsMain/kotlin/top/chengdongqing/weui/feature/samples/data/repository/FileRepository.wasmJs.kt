package top.chengdongqing.weui.feature.samples.data.repository

import kotlinx.browser.document
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.compose.resources.DrawableResource
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import top.chengdongqing.weui.feature.samples.data.model.FileItem
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import top.chengdongqing.weui.feature.samples.data.model.FileSystemDirectoryHandle
import top.chengdongqing.weui.feature.samples.data.model.FileSystemFileHandle
import top.chengdongqing.weui.feature.samples.data.model.JsFile
import top.chengdongqing.weui.feature.samples.data.model.WasmFileNode
import top.chengdongqing.weui.feature.samples.data.model.createRemoveEntryOptions
import top.chengdongqing.weui.feature.samples.data.model.isDirectory
import top.chengdongqing.weui.util.children
import top.chengdongqing.weui.util.formatFileSize
import top.chengdongqing.weui.util.formatTime
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_document
import weui_kmp.shared.generated.resources.ic_file
import weui_kmp.shared.generated.resources.ic_music
import weui_kmp.shared.generated.resources.ic_picture
import weui_kmp.shared.generated.resources.ic_video

@OptIn(ExperimentalWasmJsInterop::class)
class WasmFileRepository : FileRepository {

    override suspend fun getChildren(fileNode: FileNode): List<FileItem> = coroutineScope {
        val node = fileNode as WasmFileNode

        node.handle.children()
            .sortedWith(
                compareBy<FileSystemDirectoryHandle> { !it.isDirectory }
                    .thenBy { it.name.lowercase() }
            )
            .map { handle ->
                async {
                    handle.toFileItem(node.id, node.handle)
                }
            }
            .awaitAll()
    }

    private fun JsFile.isVisualMedia(): Boolean {
        val type = this.type.toString()
        return type.startsWith("image") || type.startsWith("video")
    }

    private fun JsFile?.getFileIcon(): DrawableResource {
        val type = this?.type?.toString() ?: return Res.drawable.ic_file

        return when {
            type.startsWith("image") -> Res.drawable.ic_picture
            type.startsWith("video") -> Res.drawable.ic_video
            type.startsWith("audio") -> Res.drawable.ic_music
            type.startsWith("text")
                    || type.endsWith("pdf")
                    || type.endsWith("msword")
                    || type.endsWith("vnd.ms-excel")
                    || type.endsWith("vnd.ms-powerpoint")
                -> Res.drawable.ic_document

            else -> Res.drawable.ic_file
        }
    }

    private suspend fun FileSystemDirectoryHandle.toFileItem(
        parentId: String,
        parentHandle: FileSystemDirectoryHandle
    ): FileItem {
        val isDirectory = isDirectory

        val file = if (!isDirectory) {
            unsafeCast<FileSystemFileHandle>().getFile().await<JsFile>()
        } else {
            null
        }
        val isVisualMedia = file?.isVisualMedia() ?: false

        return FileItem(
            name = name,
            node = WasmFileNode(
                id = "$parentId/$name",
                handle = this,
                parentHandle = parentHandle,
                name = name,
                isDirectory = isDirectory
            ),
            iconRes = file.getFileIcon(),
            size = file.formatFileSize(),
            mimeType = file?.type?.toString().orEmpty(),
            isDirectory = isDirectory,
            isVisualMedia = isVisualMedia,
            isReadable = true,
            isWriteable = true,
            isHidden = false,
            lastModified = file?.let {
                formatTime(it.lastModified.toLong())
            },
            childrenCount = if (isDirectory) {
                this.children().size
            } else {
                0
            },
            thumbnailUrl = null
        )
    }

    /**
     * 格式化文件大小
     */
    fun JsFile?.formatFileSize(): String? {
        val size = this?.size?.toLong() ?: return null
        return formatFileSize(size)
    }

    override suspend fun open(fileItem: FileItem) {
        val node = fileItem.node as WasmFileNode
        val file = node.handle.unsafeCast<FileSystemFileHandle>().getFile().await<JsFile>()

        val url = URL.createObjectURL(file as Blob)
        val a = document.createElement("a") as HTMLAnchorElement
        a.href = url

        when {
            fileItem.mimeType.startsWith("image") ||
                    fileItem.mimeType.startsWith("video") ||
                    fileItem.mimeType.startsWith("audio") ||
                    fileItem.mimeType.startsWith("text") ||
                    fileItem.mimeType.startsWith("pdf") -> {
                a.target = "_blank"
            }

            else -> {
                a.download = fileItem.name
            }
        }

        document.body?.appendChild(a)
        a.click()

        document.body?.removeChild(a)
        URL.revokeObjectURL(url)
    }

    override suspend fun delete(fileNode: FileNode): Boolean {
        val node = (fileNode as WasmFileNode)

        return runCatching {
            node.parentHandle.removeEntry(
                node.handle.name,
                createRemoveEntryOptions().apply {
                    recursive = true
                }
            ).await<Unit>()
            true
        }.getOrDefault(false)
    }
}

actual fun getFileRepository(): FileRepository = WasmFileRepository()