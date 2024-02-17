package top.chengdongqing.weui.utils

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