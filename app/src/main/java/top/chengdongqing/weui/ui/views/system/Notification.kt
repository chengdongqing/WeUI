package top.chengdongqing.weui.ui.views.system

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPage() {
    val context = LocalContext.current
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }
    val channelId = "test_channel_id"
    val channelName = "Test Channel Name"

    WePage(title = "Notification", description = "系统通知") {
        WeButton(text = "发送通知") {
            if (permissionState?.status?.isGranted == true || permissionState == null) {
                createNotificationChannel(context, channelId, channelName)
                sendNotification(context, channelId, "测试标题", "测试内容")
            } else {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun sendNotification(context: Context, channelId: String, title: String, content: String) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_logo) // 设置通知小图标
        .setContentTitle(title) // 设置通知标题
        .setContentText(content) // 设置通知内容
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    NotificationManagerCompat.from(context).apply {
        notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

private fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, channelName, importance).apply {
        description = "测试通道"
    }
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
