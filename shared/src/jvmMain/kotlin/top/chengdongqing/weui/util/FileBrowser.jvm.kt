package top.chengdongqing.weui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.ButtonType
import top.chengdongqing.weui.core.ui.components.WeButton
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Files.walkFileTree
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.atomic.AtomicLong
import javax.swing.JFileChooser

@Composable
actual fun RequestStoragePermission(content: @Composable (String) -> Unit) {
    var directory by remember { mutableStateOf<String?>(null) }

    directory?.let {
        content(it)
    } ?: WeButton(
        text = "选择文件夹",
        width = 200.dp,
        type = ButtonType.Plain
    ) {
        val chooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialogTitle = "请选择一个文件夹"
        }

        val result = chooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            directory = chooser.selectedFile.absolutePath
        }
    }
}

actual suspend fun calculateFileSize(filePath: String): Long = withContext(Dispatchers.IO) {
    val size = AtomicLong(0)
    val path = Paths.get(filePath)

    if (Files.exists(path)) {
        walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                size.addAndGet(attrs.size())
                return FileVisitResult.CONTINUE
            }

            override fun visitFileFailed(file: Path, exc: IOException): FileVisitResult {
                // 忽略无法访问的文件/目录
                return FileVisitResult.CONTINUE
            }
        })
    }
    size.get()
}