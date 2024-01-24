package top.chengdongqing.weui.ui.views.device

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.utils.formatFloat
import java.io.File
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun InstalledAppsPage() {
    Page(title = "InstalledApps", description = "已安装的应用") {
        val context = LocalContext.current
        val packageManager = context.packageManager
        var apps by remember {
            mutableStateOf<List<AppItem>>(emptyList())
        }
        var loading by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(Unit) {
            delay(300)
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            apps = packageManager.queryIntentActivities(intent, 0).map { resolveInfo ->
                val name = resolveInfo.loadLabel(packageManager).toString()
                val icon = resolveInfo.loadIcon(packageManager).toBitmap().asImageBitmap()
                val packageName = resolveInfo.activityInfo.packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionName = packageInfo.versionName
                val lastUpdateTime = packageInfo.lastUpdateTime
                val apkPath = packageInfo.applicationInfo.sourceDir
                val apkSize = getFileSize(apkPath)
                AppItem(
                    name,
                    icon,
                    packageName,
                    versionName,
                    lastUpdateTime,
                    apkPath,
                    apkSize
                )
            }.sortedByDescending { it.lastUpdateTime }
            loading = false
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            item {
                if (!loading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        WeButton(
                            text = "在地图上查看指定位置",
                            type = ButtonType.PLAIN,
                            size = ButtonSize.MEDIUM,
                            width = 200.dp
                        ) {
                            val latitude = 37.7749
                            val longitude = -122.4194
                            val locationUri = Uri.parse("geo:$latitude,$longitude")
                            val mapIntent = Intent(Intent.ACTION_VIEW, locationUri)
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                Toast.makeText(context, "未安装地图应用", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                } else {
                    WeLoadMore()
                }
            }
            items(apps) {
                AppItem(it)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalPermissionsApi::class)
@Composable
private fun AppItem(appItem: AppItem) {
    val context = LocalContext.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = appItem.icon,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop
            )
            Text(appItem.name, textAlign = TextAlign.Center)
            Text(
                "v${appItem.versionName}",
                color = FontColo1,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(2f)) {
            val content = buildAnnotatedString {
                appendLine("包名: ${appItem.packageName}")
                appendLine("最后更新: ${formatTime(appItem.lastUpdateTime)}")
                append("APK大小: ${formatFloat(appItem.apkSize / 1024 / 1024f)} MB")
            }
            Text(content, color = FontColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WeButton(text = "打开APP", size = ButtonSize.SMALL) {
                    val intent =
                        context.packageManager.getLaunchIntentForPackage(appItem.packageName)
                    context.startActivity(intent)
                }
                WeButton(
                    text = "复制到下载目录",
                    type = ButtonType.PLAIN,
                    size = ButtonSize.SMALL
                ) {
                    copyFileToPublicDirectory(
                        context,
                        appItem.apkPath,
                        "${appItem.name}-v${appItem.versionName}.apk"
                    )
                }
                WeButton(
                    text = "安装APK",
                    type = ButtonType.PLAIN,
                    size = ButtonSize.SMALL
                ) {
                    installApk(context, appItem.apkPath)
                }
            }
        }
    }
}

private fun installApk(context: Context, apkPath: String) {

}

private fun copyFileToPublicDirectory(
    context: Context,
    sourceFilePath: String,
    destinationFileName: String,
    targetDirectory: String = Environment.DIRECTORY_DOWNLOADS
) {
    val sourceFile = File(sourceFilePath)
    val resolver = context.contentResolver

    Toast.makeText(context, "开始复制", Toast.LENGTH_SHORT).show()
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, destinationFileName)
                put(
                    MediaStore.MediaColumns.MIME_TYPE,
                    "application/vnd.android.package-archive"
                )
                put(MediaStore.MediaColumns.RELATIVE_PATH, targetDirectory)
            }

            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    sourceFile.inputStream().use { input ->
                        if (outputStream != null) {
                            input.copyTo(outputStream)
                            Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } ?: throw IOException("无法创建媒体存储URI")
        } else {
            val destinationFile = File(
                Environment.getExternalStoragePublicDirectory(targetDirectory),
                destinationFileName
            )
            sourceFile.inputStream().use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
    } catch (e: Exception) {
        Toast.makeText(context, "复制失败: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun getFileSize(filePath: String): Long {
    val file = File(filePath)
    return if (file.exists()) file.length() else 0
}

private fun formatTime(milliseconds: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern(pattern))
}

private data class AppItem(
    val name: String,
    val icon: ImageBitmap,
    val packageName: String,
    val versionName: String,
    val lastUpdateTime: Long,
    val apkPath: String,
    val apkSize: Long
)