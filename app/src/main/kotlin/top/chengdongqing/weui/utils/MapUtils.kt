package top.chengdongqing.weui.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import java.net.URLEncoder

/**
 * 调用地图软件导航到指定位置
 */
fun navigateToLocation(
    context: Context,
    mapType: MapType,
    location: LatLng,
    name: String,
) {
    val uri: Uri = when (mapType) {
        MapType.AMAP -> {
            Uri.parse("amapuri://route/plan?dlat=${location.latitude}&dlon=${location.longitude}&dname=$name")
        }

        MapType.BAIDU -> {
            val encodedName = URLEncoder.encode(name, "UTF-8")
            Uri.parse("baidumap://map/direction?destination=latlng:${location.latitude},${location.longitude}|name:$encodedName")
        }

        MapType.TENCENT -> {
            Uri.parse("qqmap://map/routeplan?to=$name&tocoord=${location.latitude},${location.longitude}")
        }

        MapType.GOOGLE -> {
            Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
        }
    }

    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        if (mapType == MapType.GOOGLE) {
            // 指定谷歌地图APP打开，因为谷歌地图的URI模式可能被多个应用支持
            `package` = "com.google.android.apps.maps"
        }
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "未安装${mapType.appName}地图", Toast.LENGTH_SHORT).show()
    }
}

enum class MapType(val appName: String) {
    AMAP("高德"),
    BAIDU("百度"),
    TENCENT("腾讯"),
    GOOGLE("谷歌");

    companion object {
        fun ofIndex(index: Int): MapType? {
            return entries.getOrNull(index)
        }
    }
}

/**
 * 将指定的图片资源转为地图支持的bitmap
 * 支持指定宽高、旋转角度
 */
fun buildBitmapDescriptor(
    context: Context,
    resId: Int,
    width: Int? = null,
    height: Int? = null,
    rotationAngle: Float? = null
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, resId) ?: return null
    val originalWidth = width ?: drawable.intrinsicWidth
    val originalHeight = height ?: drawable.intrinsicHeight
    val bitmap = Bitmap.createBitmap(
        originalWidth,
        originalHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    // 如果提供了旋转角度，就在绘制之前旋转画布
    rotationAngle?.let {
        val pivotX = originalWidth / 2f
        val pivotY = originalHeight / 2f
        canvas.save() // 保存画布当前的状态
        canvas.rotate(it, pivotX, pivotY) // 应用旋转
    }

    drawable.setBounds(0, 0, originalWidth, originalHeight)
    drawable.draw(canvas)

    // 如果旋转了画布，现在恢复到之前保存的状态
    rotationAngle?.let {
        canvas.restore()
    }

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun LatLonPoint.toLatLng() = LatLng(latitude, longitude)
fun LatLng.toLatLonPoint() = LatLonPoint(latitude, longitude)
fun Location.toLatLng() = LatLng(latitude, longitude)