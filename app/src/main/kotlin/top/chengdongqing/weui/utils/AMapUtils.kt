package top.chengdongqing.weui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint

fun bitmapDescriptorFromResource(
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