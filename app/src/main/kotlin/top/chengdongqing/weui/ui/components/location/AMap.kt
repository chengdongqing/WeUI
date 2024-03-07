package top.chengdongqing.weui.ui.components.location

import android.Manifest
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.MyLocationStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.R
import top.chengdongqing.weui.utils.buildBitmapDescriptor
import top.chengdongqing.weui.utils.toLatLng

@Composable
fun WeAMap(modifier: Modifier = Modifier, onLoad: (AMap) -> Unit) {
    val context = LocalContext.current
    // 初始化地图
    val mapView = rememberMapView(onLoad)
    // 处理生命周期
    MapLifecycleHandler(mapView)
    // 处理定位权限
    LocationPermissionHandler {
        setLocationArrow(mapView.map, context)
    }

    Box(modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
        LocationControl(map = mapView.map)
    }
}

@Composable
private fun BoxScope.LocationControl(map: AMap) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .offset(x = 12.dp, y = (-36).dp)
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable {
                map.apply {
                    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            myLocation.isComplete
                        } else {
                            myLocation.latitude != 0.0 && myLocation.longitude != 0.0
                        }
                    ) {
                        animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation.toLatLng(), 16f))
                    } else {
                        Toast
                            .makeText(context, "定位中...", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.MyLocation,
            contentDescription = "当前位置",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(26.dp)
        )
    }
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
                buildBitmapDescriptor(
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

        val options = AMapOptions().apply {
            // 设置logo位置
            logoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT)
            // 不显示缩放控件
            zoomControlsEnabled(false)
            // 设置地图类型
            mapType(if (isDarkTheme) AMap.MAP_TYPE_NIGHT else AMap.MAP_TYPE_NORMAL)
        }
        MapView(context, options).apply {
            onLoad(map)
        }
    }
}

@Composable
private fun MapLifecycleHandler(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val lifecycleObserver = remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
    }
    val callbacks = remember(mapView) {
        object : ComponentCallbacks {
            override fun onConfigurationChanged(config: Configuration) {}
            override fun onLowMemory() {
                mapView.onLowMemory()
            }
        }
    }

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}