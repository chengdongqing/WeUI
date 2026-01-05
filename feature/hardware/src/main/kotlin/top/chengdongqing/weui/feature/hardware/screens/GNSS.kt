package top.chengdongqing.weui.feature.hardware.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import top.chengdongqing.weui.core.ui.components.tabview.WeTabView
import top.chengdongqing.weui.core.utils.dbHzToPercentage
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.formatDegree
import top.chengdongqing.weui.core.utils.showToast
import top.chengdongqing.weui.feature.hardware.components.DiscoveryLoading

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GNSSScreen() {
    WeScreen(title = "GNSS", description = "全球卫星导航系统", scrollEnabled = false) {
        val context = LocalContext.current
        val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val (observing, setObserving) = remember { mutableStateOf(false) }

        val (satelliteList, location) = rememberSatelliteList(locationManager, observing)
        val groups by remember {
            derivedStateOf {
                // 根据类型分组
                satelliteList.groupBy { it.constellationTypeName }.mapValues { (_, value) ->
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

            if (groups.isEmpty()) {
                DiscoveryLoading("正在搜寻卫星信号...")
            } else {
                val tabs = remember {
                    listOf("卫星列表", "星盘图", "更多")
                }

                Spacer(modifier = Modifier.height(20.dp))
                WeTabView(
                    options = tabs,
                    modifier = Modifier.padding(PaddingValues(top = 10.dp)),
                    containerColor = Color.Transparent
                ) { index ->
                    Spacer(modifier = Modifier.height(20.dp))
                    when (index) {
                        0 -> SatelliteTable(groups)
                        1 -> GnssSkyView(satelliteList)
                        2 -> LocationInfo(location, satelliteList.size)
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationInfo(location: Location?, satelliteCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .cardList(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildString {
                appendLine("卫星数量：${satelliteCount}颗")
                location?.let {
                    val latitude = location.latitude.format(6)
                    val longitude = location.longitude.format(6)
                    appendLine("坐标：$latitude, $longitude")
                    appendLine("海拔：${location.altitude.format()}m, 精度：${location.accuracy.format()}m")
                }
            },
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
private fun SatelliteTable(groups: Map<String, List<SatelliteInfo>>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .cardList(PaddingValues(top = 20.dp))
    ) {
        groups.forEach { (type, list) ->
            stickyHeader(type) {
                Column(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.95f
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\uD83D\uDEF0\uFE0F $type",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${list.size} 颗",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    SatelliteTableRow(
                        listOf(
                            "编号",
                            "方位角",
                            "高度角",
                            "频段",
                            "信号强度"
                        ),
                        true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            items(list, key = { it.key }) { satellite ->
                SatelliteTableRow(
                    listOf(
                        satellite.svid.toString(),
                        satellite.azimuthDegrees,
                        satellite.elevationDegrees,
                        satellite.band,
                        "${satellite.percentage}%"
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
private fun SatelliteTableRow(columns: List<String>, isHeader: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columns.forEach {
            Text(
                text = it,
                color = if (isHeader) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.onPrimary,
                fontSize = if (isHeader) 10.sp else 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                maxLines = 1
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
    val backgroundHandler = rememberGnssHandler()

    fun stopScan() {
        locationManager.removeUpdates(locationListener)
        locationManager.unregisterGnssStatusCallback(satelliteStatusCallback)
        satelliteList.clear()
    }

    LaunchedEffect(observing) {
        if (observing) {
            locationManager.registerGnssStatusCallback(
                satelliteStatusCallback,
                backgroundHandler
            )
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, // 多久更新一次
                0f, // 移动超过多少米才更新
                locationListener,
                backgroundHandler.looper
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

@Composable
private fun rememberGnssHandler(): Handler {
    val handlerThread = remember {
        HandlerThread("GnssThread").apply { start() }
    }
    val handler = remember { Handler(handlerThread.looper) }

    DisposableEffect(Unit) {
        onDispose {
            handlerThread.quitSafely()
        }
    }

    return handler
}

private class SatelliteStatusCallback(
    val satelliteList: MutableList<SatelliteInfo>
) : GnssStatus.Callback() {
    override fun onSatelliteStatusChanged(status: GnssStatus) {
        val count = status.satelliteCount
        val satelliteMap = mutableMapOf<String, SatelliteInfo>()

        for (index in 0 until count) {
            val cn0 = status.getCn0DbHz(index)
            if (cn0 <= 0f) continue // 过滤无信号
            val svid = status.getSvid(index)
            val type = status.getConstellationType(index)
            val key = "${type}_$svid" // 构建唯一标识
            satelliteMap[key] = status.buildSatelliteInfo(key, svid, type, cn0, index)
        }

        Handler(Looper.getMainLooper()).post {
            // 移除已经消失的
            satelliteList.removeIf { it.key !in satelliteMap.keys }

            // 更新或添加
            satelliteMap.forEach { (svid, info) ->
                val index = satelliteList.indexOfFirst { it.key == svid }
                if (index != -1) {
                    if (satelliteList[index] != info) {
                        satelliteList[index] = info
                    }
                } else {
                    satelliteList.add(info)
                }
            }
        }
    }
}

private fun GnssStatus.buildSatelliteInfo(
    key: String,
    svid: Int,
    type: Int,
    cn0: Float,
    index: Int
): SatelliteInfo {
    // 载波频率
    val frequency = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getCarrierFrequencyHz(index) / 1_000_000f
    } else {
        0f
    }

    return SatelliteInfo(
        key,
        constellationType = type,
        constellationTypeName = SatelliteType.getById(type).fullName,
        svid = svid,
        azimuthDegrees = formatDegree(getAzimuthDegrees(index)),
        elevationDegrees = formatDegree(getElevationDegrees(index)),
        frequency = frequency,
        band = getSatelliteBand(type, frequency),
        cn0 = cn0,
        percentage = dbHzToPercentage(cn0)
    )
}

/**
 * 获取卫星频段
 */
private fun getSatelliteBand(constellationType: Int, frequency: Float): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return "--"
    }

    return when (constellationType) {
        GnssStatus.CONSTELLATION_GPS -> when (frequency) {
            in 1575.0..1576.0 -> "L1"
            in 1176.0..1177.0 -> "L5"
            else -> "%.1f".format(frequency)
        }

        GnssStatus.CONSTELLATION_BEIDOU -> when (frequency) {
            in 1561.0..1562.0 -> "B1I"
            in 1575.0..1576.0 -> "B1C"
            in 1176.0..1177.0 -> "B2a"
            in 1207.0..1208.0 -> "B2b"
            else -> "%.1f".format(frequency)
        }

        GnssStatus.CONSTELLATION_GALILEO -> when (frequency) {
            in 1575.0..1576.0 -> "E1"
            in 1176.0..1177.0 -> "E5a"
            else -> "%.1f".format(frequency)
        }

        GnssStatus.CONSTELLATION_GLONASS -> {
            if (frequency in 1598.0..1610.0) "G1"
            else "%.1f".format(frequency)
        }

        GnssStatus.CONSTELLATION_QZSS -> when (frequency) {
            in 1575.0..1576.0 -> "L1"
            in 1176.0..1177.0 -> "L5"
            else -> "%.1f".format(frequency)
        }

        GnssStatus.CONSTELLATION_IRNSS -> {
            if (frequency in 1176.0..1177.0) "L5" else "%.1f".format(frequency)
        }

        else -> "%.1f".format(frequency)
    }
}

/**
 * 星座配置
 */
enum class SatelliteType(
    val typeId: Int,
    val fullName: String,
    val shortName: String,
    val color: Color
) {
    BEIDOU(GnssStatus.CONSTELLATION_BEIDOU, "北斗（中国）", "北斗", Color(0xFFFF4D4D)),
    GPS(GnssStatus.CONSTELLATION_GPS, "GPS（美国）", "GPS", Color(0xFF4DFF4D)),
    GLONASS(GnssStatus.CONSTELLATION_GLONASS, "GLONASS（俄罗斯）", "GLO", Color(0xFFFFD700)),
    GALILEO(GnssStatus.CONSTELLATION_GALILEO, "GALILEO（欧盟）", "GAL", Color(0xFF4D94FF)),
    QZSS(GnssStatus.CONSTELLATION_QZSS, "QZSS（日本）", "QZSS", Color(0xFFFF69B4)),
    IRNSS(GnssStatus.CONSTELLATION_IRNSS, "IRNSS（印度）", "IRNSS", Color(0xFFA020F0)),
    SBAS(GnssStatus.CONSTELLATION_SBAS, "SBAS（导航增强系统）", "SBAS", Color(0xFF00FFFF)),
    UNKNOWN(-1, "未知", "未知", Color.White);

    companion object {
        fun getById(id: Int) = entries.find { it.typeId == id } ?: UNKNOWN
    }
}

data class SatelliteInfo(
    val key: String,                // 唯一标识
    val constellationType: Int,  // 星座类型
    val constellationTypeName: String,
    val svid: Int,                  // 卫星编号（Space Vehicle Identification：航天器识别号）
    val azimuthDegrees: String,     // 方位角
    val elevationDegrees: String,   // 高度角
    val frequency: Float,           // 频率
    val band: String,               // 频段
    val cn0: Float,                 // 载噪比
    val percentage: Int             // 信号强度（百分比）
)
