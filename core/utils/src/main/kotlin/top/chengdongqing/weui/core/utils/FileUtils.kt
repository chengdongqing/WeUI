package top.chengdongqing.weui.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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

fun formatFileSize(file: File): String {
    val size = if (file.exists()) file.length() else 0
    return formatFileSize(size)
}

fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${(size / 1024f).format()} KB"
        size < 1024 * 1024 * 1024 -> "${(size / (1024 * 1024f)).format()} MB"
        else -> "${(size / (1024 * 1024 * 1024f)).format()} GB"
    }
}

fun Context.getFileProviderUri(file: File): Uri {
    return FileProvider.getUriForFile(this, "$packageName.provider", file)
}

fun Context.shareFile(file: File, mimeType: String = "image/*") {
    val sharingUri = getFileProviderUri(file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        this.type = mimeType
        putExtra(Intent.EXTRA_STREAM, sharingUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(intent)
}

fun Context.openFile(file: File, mimeType: String) {
    val uri = getFileProviderUri(file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(intent)
}