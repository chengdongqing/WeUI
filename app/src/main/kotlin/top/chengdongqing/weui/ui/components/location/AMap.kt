package top.chengdongqing.weui.ui.components.location

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.MyLocationStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.R

@Composable
fun WeAMap(onLoad: (AMap) -> Unit) {
    val context = LocalContext.current
    // 初始化地图
    val mapView = rememberMapView(onLoad)
    // 处理生命周期
    MapLifecycleHandler(mapView)
    // 处理定位权限
    LocationPermissionHandler {
        setLocationArrow(mapView.map, context)
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}

private fun setLocationArrow(map: AMap, context: Context) {
    map.apply {
        myLocationStyle = MyLocationStyle().apply {
            // 设置定位频率
            interval(5000)
            // 设置定位类型
            myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
            // 设置定位图标
            myLocationIcon(
                bitmapDescriptorFromResource(
                    context,
                    R.drawable.ic_location_rotatable,
                    90,
                    90,
                    -60f
                )
            )
            // 去除精度圆圈
            radiusFillColor(Color.TRANSPARENT)
            strokeWidth(0f)
        }
        isMyLocationEnabled = true
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionHandler(onGranted: () -> Unit) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION) {
        if (it) {
            onGranted()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        } else {
            onGranted()
        }
    }
}

@Composable
private fun rememberMapView(onLoad: (AMap) -> Unit): MapView {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()

    return remember {
        MapsInitializer.updatePrivacyShow(context, true, true)
        MapsInitializer.updatePrivacyAgree(context, true)

        MapView(context, AMapOptions().apply {
            // 设置logo位置
            logoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT)
            // 不显示缩放控件
            zoomControlsEnabled(false)
            // 设置地图类型
            mapType(if (isDarkTheme) AMap.MAP_TYPE_NIGHT else AMap.MAP_TYPE_NORMAL)
        }).apply { onLoad(map) }
    }
}

@Composable
private fun MapLifecycleHandler(mapView: MapView) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleObserver = remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
    }

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

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
