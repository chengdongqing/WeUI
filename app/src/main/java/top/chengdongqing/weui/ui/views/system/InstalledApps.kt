package top.chengdongqing.weui.ui.views.system

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.utils.formatFileSize
import top.chengdongqing.weui.utils.formatTime
import java.io.File

@Composable
fun InstalledAppsPage() {
    WePage(title = "InstalledApps", description = "已安装的应用") {
        val context = LocalContext.current
        val apps = rememberInstalledApps()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            item {
                if (apps.isNotEmpty()) {
                    ActionBar(context)
                } else {
                    WeLoadMore()
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(apps) { app ->
                AppListItem(app, context)
            }
        }
    }
}

@Composable
private fun ActionBar(context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeButton(
            text = "打开地图",
            type = ButtonType.PLAIN,
            size = ButtonSize.MEDIUM,
            width = 140.dp
        ) {
            val latitude = "37.7749"
            val longitude = "-122.4194"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude"))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "未安装地图应用", Toast.LENGTH_SHORT).show()
            }
        }
        WeButton(
            text = "打开浏览器",
            type = ButtonType.PLAIN,
            size = ButtonSize.MEDIUM,
            width = 140.dp
        ) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://weui.io"))
            context.startActivity(intent)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppListItem(app: App, context: Context) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(56.dp)) {
                produceState<ImageBitmap?>(initialValue = null) {
                    value = withContext(Dispatchers.IO) {
                        app.icon.toBitmap().asImageBitmap()
                    }
                }.value?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(app.name, textAlign = TextAlign.Center)
            Text(
                "v${app.versionName}",
                color = FontColor1,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(2f)) {
            Text(buildString {
                appendLine("包名: ${app.packageName}")
                appendLine("最后更新: ${app.lastModified}")
                append("APK大小: ${app.apkSize}")
            }, color = FontColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WeButton(text = "打开APP", size = ButtonSize.SMALL) {
                    val intent = context.packageManager
                        .getLaunchIntentForPackage(app.packageName)
                    context.startActivity(intent)
                }
                WeButton(
                    text = "复制到下载目录",
                    type = ButtonType.PLAIN,
                    size = ButtonSize.SMALL
                ) {
                    copyFileToPublicDirectory(
                        context,
                        app.apkPath,
                        "${app.name}-v${app.versionName}.apk"
                    )
                }
                WeButton(
                    text = "安装APK",
                    type = ButtonType.PLAIN,
                    size = ButtonSize.SMALL
                ) {
                    installApk(context, app.apkPath)
                }
            }
        }
    }
}

fun installApk(context: Context, apkPath: String) {
    // 复制到可访问的临时文件
    val tempFile = File.createTempFile("app_", ".apk", context.externalCacheDir)
    File(apkPath).copyTo(tempFile, true)
    // 创建文件共享访问路径
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tempFile
    )
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(intent)
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
                resolver.openOutputStream(it)?.use { outputStream ->
                    sourceFile.inputStream().use { input ->
                        input.copyTo(outputStream)
                        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

@Composable
private fun rememberInstalledApps(): List<App> {
    val context = LocalContext.current
    val packageManager = context.packageManager
    var apps by remember { mutableStateOf<List<App>>(emptyList()) }

    LaunchedEffect(Unit) {
        delay(300)
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        withContext(Dispatchers.IO) {
            apps = packageManager.queryIntentActivities(intent, 0).map { resolveInfo ->
                val name = resolveInfo.loadLabel(packageManager).toString()
                val icon = resolveInfo.loadIcon(packageManager)
                val packageName = resolveInfo.activityInfo.packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionName = packageInfo.versionName
                val lastModified = formatTime(packageInfo.lastUpdateTime)
                val apkPath = packageInfo.applicationInfo.sourceDir
                val apkSize = formatFileSize(File(apkPath))
                App(
                    name,
                    icon,
                    packageName,
                    versionName,
                    lastModified,
                    apkPath,
                    apkSize
                )
            }.sortedByDescending {
                it.lastModified
            }
        }
    }

    return apps
}

private data class App(
    val name: String,
    val icon: Drawable,
    val packageName: String,
    val versionName: String,
    val lastModified: String,
    val apkPath: String,
    val apkSize: String
)