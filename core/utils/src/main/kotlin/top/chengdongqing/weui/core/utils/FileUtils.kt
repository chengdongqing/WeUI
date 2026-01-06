package top.chengdongqing.weui.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * 计算文件大小
 */
suspend fun calculateFileSize(file: File): Long = withContext(Dispatchers.IO) {
    if (file.isFile) {
        // 如果是文件，直接返回其大小
        file.length()
    } else if (file.isDirectory) {
        // 如果是目录，递归计算所有子文件和子目录的大小
        val children = file.listFiles()
        var totalSize: Long = 0
        if (children != null) {
            for (child in children) {
                totalSize += calculateFileSize(child)
            }
        }
        totalSize
    } else {
        0
    }
}

/**
 * 格式化文件大小
 */
fun formatFileSize(file: File): String {
    val size = if (file.exists()) file.length() else 0
    return formatFileSize(size)
}

/**
 * 格式化文件大小
 */
fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${(size / 1024f).format()} KB"
        size < 1024 * 1024 * 1024 -> "${(size / (1024 * 1024f)).format()} MB"
        else -> "${(size / (1024 * 1024 * 1024f)).format()} GB"
    }
}

/**
 * 获取隔离的文件uri
 */
fun Context.getFileProviderUri(file: File): Uri {
    return FileProvider.getUriForFile(this, "$packageName.provider", file)
}

/**
 * 分享文件
 */
fun Context.shareContent(content: Any, mimeType: String, title: String = "分享文件") {
    val shareUri: Uri = when (content) {
        is File -> getFileProviderUri(content)
        is Uri -> content
        else -> throw IllegalArgumentException("不支持的内容类型: ${content::class.java}")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_STREAM, shareUri)
        // 授予临时访问权限
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, title))
}

/**
 * 打开文件
 */
fun Context.openFile(file: File, mimeType: String) {
    val uri = getFileProviderUri(file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        // 授予临时访问权限
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(intent, "打开文件"))
}


/**
 * 删除文件
 */
fun deleteFile(file: File): Boolean {
    if (file.isDirectory) {
        // 如果是目录，递归删除其下的所有文件和子目录
        file.listFiles()?.forEach { child ->
            deleteFile(child)
        }
    }
    // 删除文件或目录本身
    return file.delete()
}

/**
 * 复制文件
 */
fun InputStream.copyToFile(destFile: File): Boolean {
    return try {
        this.use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun InputStream.copyToStream(outputStream: OutputStream): Boolean {
    return try {
        this.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}