package top.chengdongqing.weui.feature.system.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.formatFileSize
import top.chengdongqing.weui.core.utils.formatTime
import top.chengdongqing.weui.core.utils.openFile
import top.chengdongqing.weui.core.utils.showToast
import java.io.File

@Composable
fun InstalledAppsScreen() {
    WeScreen(
        title = "InstalledApps",
        description = "已安装的应用",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        val context = LocalContext.current
        val appList = rememberInstalledApps()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                if (appList.isNotEmpty()) {
                    ActionBar(context)
                } else {
                    WeLoadMore()
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(appList) { app ->
                AppItem(app, context)
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
                context.showToast("未安装地图应用")
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
private fun AppItem(app: AppItem, context: Context) {
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
            Text(
                app.name,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                "v${app.versionName}",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(2f)) {
            Text(
                buildString {
                    appendLine("包名: ${app.packageName}")
                    appendLine("最后更新: ${app.lastModified}")
                    append("APK大小: ${app.apkSize}")
                },
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )
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
                    fileToPublicDirectory(
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
    val tempFile = File.createTempFile("app_", ".apk").apply {
        deleteOnExit()
    }
    File(apkPath).copyTo(tempFile, true)
    context.openFile(tempFile, "application/vnd.android.package-archive")
}

private fun fileToPublicDirectory(
    context: Context,
    sourceFilePath: String,
    destinationFileName: String,
    targetDirectory: String = Environment.DIRECTORY_DOWNLOADS
) {
    val sourceFile = File(sourceFilePath)
    val resolver = context.contentResolver

    context.showToast("开始复制")
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
                        context.showToast("已复制")
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
                    context.showToast("复制成功")
                }
            }
        }
    } catch (e: Exception) {
        context.showToast("复制失败: ${e.message}")
    }
}

@Composable
private fun rememberInstalledApps(): List<AppItem> {
    val context = LocalContext.current
    val packageManager = context.packageManager

    val appList by produceState(initialValue = emptyList()) {
        value = withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            packageManager.queryIntentActivities(intent, 0).map { resolveInfo ->
                val name = resolveInfo.loadLabel(packageManager).toString()
                val icon = resolveInfo.loadIcon(packageManager)
                val packageName = resolveInfo.activityInfo.packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionName = packageInfo.versionName
                val lastModified = formatTime(packageInfo.lastUpdateTime)
                val apkPath = packageInfo.applicationInfo.sourceDir
                val apkSize = formatFileSize(File(apkPath))
                AppItem(
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

    return appList
}

private data class AppItem(
    val name: String,
    val icon: Drawable,
    val packageName: String,
    val versionName: String,
    val lastModified: String,
    val apkPath: String,
    val apkSize: String
)