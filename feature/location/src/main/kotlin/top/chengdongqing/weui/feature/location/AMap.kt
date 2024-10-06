package top.chengdongqing.weui.feature.location

import android.Manifest
import android.content.ComponentCallbacks
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    // 处理生命周期
    LifecycleEffect(state.mapView)
    // 处理定位权限
    PermissionHandler {
        setLocationArrow(state.map, context)
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
private fun LifecycleEffect(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }

    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(previousState)
        val callbacks = mapView.componentCallbacks()

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
    DisposableEffect(mapView) {
        onDispose {
            mapView.onDestroy()
            mapView.removeAllViews()
        }
    }
}

private fun MapView.lifecycleObserver(previousState: MutableState<Lifecycle.Event>): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                if (previousState.value != Lifecycle.Event.ON_STOP) {
                    this.onCreate(Bundle())
                }
            }

            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()

            else -> {}
        }
        previousState.value = event
    }

private fun MapView.componentCallbacks(): ComponentCallbacks =
    object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
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