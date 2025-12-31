package top.chengdongqing.weui.feature.location

import android.Manifest
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.core.ui.components.R
import top.chengdongqing.weui.core.utils.isTrue
import top.chengdongqing.weui.core.utils.showToast
import top.chengdongqing.weui.feature.location.utils.createBitmapDescriptor
import top.chengdongqing.weui.feature.location.utils.isLoaded
import top.chengdongqing.weui.feature.location.utils.toLatLng

@Composable
fun AMap(
    modifier: Modifier = Modifier,
    state: AMapState = rememberAMapState(),
    controls: @Composable BoxScope.(AMap) -> Unit = {
        LocationControl(it)
    }
) {
    val context = LocalContext.current
    val mapSaveState = rememberSaveable { Bundle() }

    // 处理生命周期
    LifecycleEffect(state.mapView, mapSaveState)
    // 处理定位权限
    PermissionHandler {
        if (mapSaveState.isEmpty) {
            setLocationArrow(state.map, context)
        }
    }

    Box(modifier) {
        AndroidView(
            factory = { state.mapView },
            modifier = Modifier.fillMaxSize()
        )
        controls(state.map)
    }
}


@Composable
fun BoxScope.LocationControl(map: AMap, onClick: ((LatLng) -> Unit)? = null) {
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
                    if (myLocation
                            ?.isLoaded()
                            .isTrue()
                    ) {
                        val latLng = myLocation.toLatLng()
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                        onClick?.invoke(latLng)
                    } else {
                        context.showToast("定位中...")
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
            val icon = createBitmapDescriptor(
                context,
                R.drawable.ic_location_rotatable,
                90,
                90,
                -60f
            )
            myLocationIcon(icon)
            // 去除精度圆圈
            radiusFillColor(Color.TRANSPARENT)
            strokeWidth(0f)
        }
        isMyLocationEnabled = true
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionHandler(onGranted: (() -> Unit)? = null) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION) {
        if (it) {
            onGranted?.invoke()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        } else {
            onGranted?.invoke()
        }
    }
}

@Composable
private fun LifecycleEffect(mapView: MapView, mapSaveState: Bundle) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, mapView) {
        val lifecycle = lifecycleOwner.lifecycle
        val callbacks = mapView.componentCallbacks()

        // 创建生命周期观察者
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(mapSaveState)
                // 恢复活跃
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    // 强制重绘，解决返回时偶尔黑屏
                    mapView.postInvalidate()
                }
                // 挂起
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onSaveInstanceState(mapSaveState)
                    mapView.onPause()
                }

                else -> {}
            }
        }

        // 注册
        lifecycle.addObserver(observer)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            // 销毁前保存一次，这里的销毁可能是屏幕旋转或主题切换导致
            mapView.onSaveInstanceState(mapSaveState)

            // 取消注册与彻底销毁
            lifecycle.removeObserver(observer)
            context.unregisterComponentCallbacks(callbacks)

            // 彻底清理地图
            mapView.onPause()
            mapView.onDestroy()
            mapView.removeAllViews()
        }
    }
}

private fun MapView.componentCallbacks(): ComponentCallbacks2 =
    object : ComponentCallbacks2 {
        override fun onConfigurationChanged(config: Configuration) {}

        // 系统极度缺内存，无条件清理
        @Suppress("OVERRIDE_DEPRECATION")
        override fun onLowMemory() {
            // 这里能获取到2个this：1.componentCallbacks本身；2.MapView实例
            // 直接用this是获取当前作用域；而this@componentCallbacks可以获取到componentCallbacks的作用域
            this@componentCallbacks.onLowMemory()
        }

        // 可精细控制，分等级
        override fun onTrimMemory(level: Int) {
            // 只要 UI 隐藏或者内存中度紧张，就让地图清理一下
            if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                this@componentCallbacks.onLowMemory()
            }
        }
    }

@Stable
interface AMapState {
    /**
     * 地图视图
     */
    val mapView: MapView

    /**
     * 地图实例
     */
    val map: AMap
}

@Composable
fun rememberAMapState(): AMapState {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()

    return remember {
        AMapStateImpl(context, isDarkTheme)
    }
}

private class AMapStateImpl(context: Context, isDarkTheme: Boolean) : AMapState {
    override val mapView: MapView
    override val map: AMap

    init {
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
        mapView = MapView(context, options)
        map = mapView.map
    }
}