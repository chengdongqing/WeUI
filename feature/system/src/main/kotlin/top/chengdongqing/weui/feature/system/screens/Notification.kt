package top.chengdongqing.weui.feature.system.screens

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
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.theme.R
import top.chengdongqing.weui.core.utils.isTrue

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {
    WeScreen(title = "Notification", description = "系统通知") {
        val context = LocalContext.current
        val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }
        val channelId = "test_channel_id"
        val channelName = "Test Channel Name"

        WeButton(text = "发送通知") {
            if (permissionState?.status?.isGranted.isTrue() || permissionState == null) {
                context.createNotificationChannel(channelId, channelName)
                context.sendNotification(channelId, "测试标题", "测试内容")
            } else {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

/**
 * 发送通知
 */
@SuppressLint("MissingPermission")
private fun Context.sendNotification(channelId: String, title: String, content: String) {
    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_logo) // 设置通知小图标
        .setContentTitle(title) // 设置通知标题
        .setContentText(content) // 设置通知内容
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    NotificationManagerCompat.from(this).apply {
        notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

/**
 * 创建通知通道
 */
private fun Context.createNotificationChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, channelName, importance).apply {
        description = "测试通道"
    }
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
