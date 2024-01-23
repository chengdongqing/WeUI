package top.chengdongqing.weui.ui.views.device

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeInput
import top.chengdongqing.weui.ui.components.form.WeTextarea

@Composable
fun DownloaderPage() {
    Page(title = "Downloader", description = "系统下载") {
        val context = LocalContext.current
        var name by remember {
            mutableStateOf("su7.jpg")
        }
        var url by remember {
            mutableStateOf("https://s1.xiaomiev.com/activity-outer-assets/web/home/section1.jpg")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeInput(value = name, label = "文件名称", placeholder = "请输入") { name = it }
            WeTextarea(value = url, label = "下载地址", placeholder = "请输入") { url = it }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "下载") {
                download(context, name, url)
            }
        }
    }
}

private fun download(context: Context, name: String, url: String) {
    val request = DownloadManager.Request(Uri.parse(url)).apply {
        setTitle(name)
        setDescription(url)
        // 设置保存位置
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)
        // 设置网络类型为任何网络
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        // 设置通知栏是否可见
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    }
    // 加入下载队列
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)

    // 注册广播接收器
    ContextCompat.registerReceiver(
        context,
        DownloadBroadcastReceiver(downloadManager, downloadId),
        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
        ContextCompat.RECEIVER_NOT_EXPORTED
    )
}

private class DownloadBroadcastReceiver(
    val downloadManager: DownloadManager,
    val downloadId: Long
) : BroadcastReceiver() {
    @SuppressLint("Range")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (downloadId == id) {
            val query = DownloadManager.Query().setFilterById(id)
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // 下载成功
                    val uriString =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    val uri = Uri.parse(uriString)
                    // 如果下载的是APK文件，启动安装
                    if (uriString.endsWith(".apk")) {
                        val installIntent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/vnd.android.package-archive")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }
                        context.startActivity(installIntent)
                    }
                }
            }
            cursor.close()
        }
    }
}