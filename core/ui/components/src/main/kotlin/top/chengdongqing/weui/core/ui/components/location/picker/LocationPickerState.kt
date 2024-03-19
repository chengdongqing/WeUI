package top.chengdongqing.weui.core.ui.components.location.picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.feature.location.utils.isLoaded
import top.chengdongqing.weui.feature.location.utils.locationToAddress
import top.chengdongqing.weui.feature.location.utils.toLatLng
import top.chengdongqing.weui.feature.location.utils.toLatLonPoint
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

@Stable
interface LocationPickerState {
    /**
     * 地图实例
     */
    val map: AMap

    /**
     * 当前设备坐标
     */
    val current: LatLng?

    /**
     * 地图中心点位置信息
     */
    val mapCenter: LocationItem?

    /**
     * 当前选中的位置信息
     */
    var selectedLocation: LocationItem?

    /**
     * 是否是搜索模式
     */
    var isSearchMode: Boolean

    /**
     * 是否加载中
     */
    val isLoading: Boolean

    /**
     * 是否暂不处理相关事件
     * （如点击列表上的位置后会触发地图中心点变化，然后会触发连锁反应，不符合预期）
     */
    var isWaiting: Boolean

    /**
     * 搜索位置
     */
    suspend fun search(
        location: LatLng? = current,
        keyword: String = "",
        category: String = "",
        region: String = "",
        pageNum: Int = 0,
        pageSize: Int = 10
    ): List<LocationItem>
}

@Composable
fun rememberLocationPickerState(map: AMap): LocationPickerState {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        LocationPickerStateImpl(map, context, coroutineScope)
    }
}

private class LocationPickerStateImpl(
    override val map: AMap,
    val context: Context,
    val coroutineScope: CoroutineScope
) : LocationPickerState {
    override var current by mutableStateOf<LatLng?>(null)
    override var mapCenter by mutableStateOf<LocationItem?>(null)
    override var selectedLocation by mutableStateOf<LocationItem?>(null)
    override var isSearchMode by mutableStateOf(false)
    override var isLoading by mutableStateOf(true)
    override var isWaiting by mutableStateOf(false)

    override suspend fun search(
        location: LatLng?,
        keyword: String,
        category: String,
        region: String,
        pageNum: Int,
        pageSize: Int
    ): List<LocationItem> = withContext(Dispatchers.IO) {
        isLoading = true
        // 构建搜索参数：关键字，类别，区域
        val query = PoiSearchV2.Query(keyword, category, region).apply {
            this.pageNum = pageNum
            this.pageSize = pageSize
        }
        val poiSearch = PoiSearchV2(context, query)
        // 限定搜索范围：坐标，半径
        if (location != null) {
            poiSearch.bound = PoiSearchV2.SearchBound(location.toLatLonPoint(), 100_000)
        }

        suspendCoroutine { continuation ->
            poiSearch.setOnPoiSearchListener(object : PoiSearchV2.OnPoiSearchListener {
                override fun onPoiSearched(result: PoiResultV2?, resultCode: Int) {
                    isLoading = false
                    if (resultCode == AMapException.CODE_AMAP_SUCCESS && result?.query != null) {
                        val items = result.pois.map { poiItem ->
                            LocationItem(
                                name = poiItem.title,
                                address = poiItem.adName,
                                distance = if (current != null) {
                                    AMapUtils.calculateLineDistance(
                                        current,
                                        poiItem.latLonPoint.toLatLng()
                                    ).roundToInt()
                                } else null,
                                latLng = poiItem.latLonPoint.toLatLng()
                            )
                        }
                        continuation.resume(items)
                    } else {
                        continuation.resume(emptyList())
                    }
                }

                override fun onPoiItemSearched(poiItem: PoiItemV2?, rCode: Int) {}
            })
            poiSearch.searchPOIAsyn()
        }
    }

    init {
        setMapClickListener()
        setCenterChangeListener()
        setLocationChangeListener()
    }

    // 监听地图点击事件
    private fun setMapClickListener() {
        map.setOnMapClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it))
        }
        map.setOnPOIClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it.coordinate))
        }
    }

    // 监听地图中心点变化事件
    private fun setCenterChangeListener() {
        map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition?) {}

            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                cameraPosition?.let { position ->
                    val target = position.target
                    if (!isSearchMode && !isWaiting) {
                        coroutineScope.launch {
                            top.chengdongqing.weui.feature.location.utils.locationToAddress(
                                context,
                                target
                            )?.let {
                                val address = it.formatAddress
                                val startIndex = address.lastIndexOf(it.district)
                                val name = address.slice(startIndex..address.lastIndex)
                                mapCenter = LocationItem(
                                    name,
                                    address,
                                    latLng = target
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    // 初始化地图视野
    private fun setLocationChangeListener() {
        map.setOnMyLocationChangeListener { location ->
            if (location.isLoaded()) {
                location.toLatLng().let {
                    current = it
                    // 移动地图视野到当前位置
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
                    // 不再监听当前位置变化
                    map.setOnMyLocationChangeListener(null)
                }
            }
        }
    }
}