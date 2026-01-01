package top.chengdongqing.weui.feature.hardware.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.cardList
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.formatDegree
import top.chengdongqing.weui.core.utils.showToast

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GNSSScreen() {
    WeScreen(title = "GNSS", description = "全球导航卫星系统", scrollEnabled = false) {
        val context = LocalContext.current
        val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val (observing, setObserving) = remember { mutableStateOf(false) }

        val (satelliteList, location) = rememberSatelliteList(locationManager, observing)
        val groupedList by remember {
            derivedStateOf {
                // 根据类型分组
                satelliteList.groupBy { it.type }.mapValues { (_, value) ->
                    // 根据编号排序
                    value.sortedBy { it.svid }
                }
            }
        }

        if (!observing) {
            WeButton(text = "开始扫描") {
                if (permissionState.status.isGranted) {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        context.showToast("位置服务未开启")
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    } else {
                        setObserving(true)
                    }
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
        } else {
            WeButton(text = "停止扫描", type = ButtonType.PLAIN) {
                setObserving(false)
            }
        }

        location?.let {
            LocationInfo(it, satelliteList.size)
            Spacer(modifier = Modifier.height(20.dp))
            SatelliteTable(groupedList)
        }
    }
}

@Composable
private fun LocationInfo(location: Location, satelliteCount: Int) {
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = buildString {
            appendLine("卫星数量：${satelliteCount}颗")
            val latitude = location.latitude.format(6)
            val longitude = location.longitude.format(6)
            appendLine("坐标：$latitude, $longitude")
            appendLine("海拔：${location.altitude.format()}m, 精度：${location.accuracy.format()}m")
        },
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp
    )
}

@Composable
private fun SatelliteTable(groups: Map<String, List<SatelliteInfo>>) {
    LazyColumn(modifier = Modifier.cardList(PaddingValues(top = 20.dp))) {
        groups.forEach { (type, list) ->
            stickyHeader(key = type) {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = type,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    SatelliteTableRow(
                        remember {
                            listOf(
                                "编号",
                                "方位角",
                                "高度角",
                                "频点",
                                "信号强度"
                            )
                        })
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
            items(list, key = { it.svid }) {
                SatelliteTableRow(
                    listOf(
                        it.svid.toString(),
                        it.azimuthDegrees,
                        it.elevationDegrees,
                        it.frequency,
                        "${it.signalStrength}dBHz"
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun SatelliteTableRow(columns: List<String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        columns.forEach {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun rememberSatelliteList(
    locationManager: LocationManager,
    observing: Boolean
): Pair<List<SatelliteInfo>, Location?> {
    val satelliteList = remember { mutableStateListOf<SatelliteInfo>() }
    var location by remember { mutableStateOf<Location?>(null) }

    val satelliteStatusCallback = remember { SatelliteStatusCallback(satelliteList) }
    val locationListener = remember { LocationListener { location = it } }

    fun stopScan() {
        locationManager.removeUpdates(locationListener)
        locationManager.unregisterGnssStatusCallback(satelliteStatusCallback)
    }

    LaunchedEffect(observing) {
        if (observing) {
            locationManager.registerGnssStatusCallback(
                satelliteStatusCallback,
                Handler(Looper.getMainLooper())
            )
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, // 多久更新一次
                0f, // 移动超过多少米才更新
                locationListener
            )
        } else {
            stopScan()
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            stopScan()
        }
    }

    return Pair(satelliteList, location)
}

private class SatelliteStatusCallback(
    val satelliteList: MutableList<SatelliteInfo>
) : GnssStatus.Callback() {
    override fun onSatelliteStatusChanged(status: GnssStatus) {
        val svidArray = Array(status.satelliteCount) { status.getSvid(it) }
        satelliteList.removeIf { !svidArray.contains(it.svid) }

        for (i in 0 until status.satelliteCount) {
            val info = SatelliteInfo(
                type = determineSatelliteType(status.getConstellationType(i)),
                svid = status.getSvid(i),
                azimuthDegrees = formatDegree(status.getAzimuthDegrees(i)),
                elevationDegrees = formatDegree(status.getElevationDegrees(i)),
                frequency = formatFrequency(status.getCarrierFrequencyHz(i)),
                signalStrength = status.getCn0DbHz(i)
            )
            val index = satelliteList.indexOfFirst { it.svid == status.getSvid(i) }
            if (index != -1) {
                satelliteList[index] = info
            } else {
                satelliteList.add(info)
            }
        }

        satelliteList.removeIf { it.signalStrength == 0f }
    }
}

private fun formatFrequency(value: Float): String {
    // 截取前4位整数并留1位小数
    var scaledValue = value
    while (scaledValue >= 10000) {
        scaledValue /= 10
    }
    return "%.1f".format(scaledValue)
}

private fun determineSatelliteType(constellationType: Int): String {
    return when (constellationType) {
        GnssStatus.CONSTELLATION_BEIDOU -> "北斗（中国）"
        GnssStatus.CONSTELLATION_GPS -> "GPS（美国）"
        GnssStatus.CONSTELLATION_GLONASS -> "GLONASS（俄罗斯）"
        GnssStatus.CONSTELLATION_GALILEO -> "GALILEO（欧盟）"
        GnssStatus.CONSTELLATION_QZSS -> "QZSS（日本）"
        GnssStatus.CONSTELLATION_IRNSS -> "IRNSS（印度）"
        GnssStatus.CONSTELLATION_SBAS -> "SBAS（地面导航增强系统）"
        else -> "未知"
    }
}

private data class SatelliteInfo(
    /**
     * 类型
     */
    val type: String,
    /**
     * 编号
     */
    val svid: Int,
    /**
     * 方位角
     */
    val azimuthDegrees: String,
    /**
     * 高度角
     */
    val elevationDegrees: String,
    /**
     * 频点
     */
    val frequency: String,
    /**
     * 信号强度
     */
    val signalStrength: Float
)