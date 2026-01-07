package top.chengdongqing.weui.feature.system.screens

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.data.model.MimeTypes
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.MediaStoreUtils.createContentValues
import top.chengdongqing.weui.core.utils.MediaStoreUtils.finishPending
import top.chengdongqing.weui.core.utils.copyToStream
import top.chengdongqing.weui.core.utils.formatFileSize
import top.chengdongqing.weui.core.utils.formatTime
import top.chengdongqing.weui.core.utils.openFile
import top.chengdongqing.weui.core.utils.shareContent
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
        val appList by produceInstalledApps()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                if (appList.isNotEmpty()) {
                    ActionBar()
                } else {
                    WeLoadMore()
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(appList) { AppItem(it) }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ActionBar() {
    val context = LocalContext.current

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
            val intent = Intent(Intent.ACTION_VIEW, "geo:$latitude,$longitude".toUri())
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
            val intent = Intent(Intent.ACTION_VIEW, "https://weui.io".toUri())
            context.startActivity(intent)
        }
    }
}

@Composable
private fun AppItem(app: AppItem) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row(verticalAlignment = Alignment.CenterVertically) {
        AppIcon(app)
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(2f)) {
            Text(
                buildString {
                    appendLine("包名: ${app.packageName}")
                    appendLine("最后更新: ${app.lastModified}")
                    append("APK大小: ${app.apkSize}")
                },
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppActionButtons(
                onOpen = {
                    context.packageManager.getLaunchIntentForPackage(app.packageName)?.let {
                        context.startActivity(it)
                    }
                },
                onCopy = {
                    coroutineScope.launch {
                        context.copyFileToPublicDirectory(
                            app.apkPath,
                            "${app.name}-v${app.versionName}.apk"
                        )
                    }
                },
                onShare = {
                    context.shareContent(
                        content = File(app.apkPath),
                        mimeType = MimeTypes.APK
                    )
                },
                onInstall = {
                    context.openFile(
                        file = File(app.apkPath),
                        mimeType = MimeTypes.APK,
                        showChooser = false
                    )
                }
            )
        }
    }
}

@Composable
private fun AppActionButtons(
    onOpen: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onInstall: () -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeButton(text = "打开APP", size = ButtonSize.SMALL, onClick = onOpen)
        WeButton(
            text = "复制到下载目录",
            type = ButtonType.PLAIN,
            size = ButtonSize.SMALL,
            onClick = onCopy
        )
        WeButton(
            text = "分享APK",
            type = ButtonType.PLAIN,
            size = ButtonSize.SMALL,
            onClick = onShare
        )
        WeButton(
            text = "安装APK",
            type = ButtonType.PLAIN,
            size = ButtonSize.SMALL,
            onClick = onInstall
        )
    }
}

@Composable
private fun RowScope.AppIcon(app: AppItem) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(56.dp)) {
            val iconBitmap by produceState<ImageBitmap?>(initialValue = null, app.packageName) {
                value = withContext(Dispatchers.IO) {
                    app.icon.toBitmap().asImageBitmap()
                }
            }

            iconBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            app.name,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "v${app.versionName}",
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 拷贝文件到公共文件夹
 */
private suspend fun Context.copyFileToPublicDirectory(
    sourceFilePath: String,
    destinationFileName: String,
    targetDirectory: String = Environment.DIRECTORY_DOWNLOADS
) {
    val sourceFile = File(sourceFilePath)
    if (!sourceFile.exists()) {
        showToast("源文件不存在")
        return
    }

    withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = createContentValues(
                    filename = destinationFileName,
                    mimeType = MimeTypes.APK,
                    relativePath = targetDirectory
                )

                val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                val targetUri =
                    contentResolver.insert(contentUri, contentValues) ?: return@withContext
                val outputStream = contentResolver.openOutputStream(targetUri) ?: return@withContext
                val isSuccess = sourceFile.inputStream().copyToStream(outputStream)

                if (isSuccess) {
                    // 成功：解除挂起状态
                    finishPending(targetUri)
                    withContext(Dispatchers.Main) {
                        showToast("已保存至下载目录")
                    }
                } else {
                    // 失败：删除数据库中的占位记录
                    contentResolver.delete(targetUri, null, null)
                    withContext(Dispatchers.Main) {
                        showToast("操作失败")
                    }
                }
            } else {
                // Android 9 及以下逻辑
                val targetDir = Environment.getExternalStoragePublicDirectory(targetDirectory)
                if (!targetDir.exists()) targetDir.mkdirs()

                val destFile = File(targetDir, destinationFileName)
                sourceFile.inputStream().use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                withContext(Dispatchers.Main) { showToast("复制成功") }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                showToast("操作失败")
            }
        }
    }
}

@Composable
private fun produceInstalledApps(): State<List<AppItem>> {
    val context = LocalContext.current
    val packageManager = context.packageManager

    return produceState(initialValue = emptyList()) {
        value = withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            packageManager.queryIntentActivities(intent, 0).map { resolveInfo ->
                val name = resolveInfo.loadLabel(packageManager).toString()
                val icon = resolveInfo.loadIcon(packageManager)
                val packageName = resolveInfo.activityInfo.packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionName = packageInfo.versionName ?: "0.0"
                val lastModified = formatTime(packageInfo.lastUpdateTime)
                val apkPath = packageInfo.applicationInfo?.sourceDir ?: ""
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