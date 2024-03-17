package top.chengdongqing.weui.ui.components.location.picker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.data.model.LocationItem
import top.chengdongqing.weui.utils.isLoaded
import top.chengdongqing.weui.utils.locationToAddress
import top.chengdongqing.weui.utils.toLatLng
import top.chengdongqing.weui.utils.toLatLonPoint
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

class LocationPickerViewModel : ViewModel() {
    var map by mutableStateOf<AMap?>(null) // 地图实例
    var center by mutableStateOf<LatLng?>(null) // 当前地图中心点
        private set
    var centerLocation by mutableStateOf<LocationItem?>(null) // 地图中心点位置信息
        private set
    var current by mutableStateOf<LatLng?>(null) // 当前设备位置
        private set
    var selectedLocation by mutableStateOf<LocationItem?>(null) // 当前选中的位置信息
    val isListItemClicking = mutableStateOf(false) // 是否刚点击了列表
    var isSearchMode by mutableStateOf(false) // 是否搜索模式
    var isLoading by mutableStateOf(true) // 是否查询中
    var isEmpty by mutableStateOf(false) // 是否没有查询到结果

    suspend fun searchPOI(
        context: Context,
        location: LatLng? = current,
        keyword: String = "",
        pageNumber: Int = 0
    ): List<LocationItem> = withContext(Dispatchers.IO) {
        isLoading = true
        suspendCoroutine { continuation ->
            // 构建搜索参数：关键字，类别，区域
            val query = PoiSearchV2.Query(keyword, "", "").apply {
                // 设置分页参数
                pageNum = pageNumber
                pageSize = 10
            }
            val poiSearch = PoiSearchV2(context, query)
            // 限定搜索范围：坐标及半径
            if (location != null) {
                poiSearch.bound = PoiSearchV2.SearchBound(location.toLatLonPoint(), 100_000)
            }

            // 设置搜索结果监听器
            poiSearch.setOnPoiSearchListener(object : PoiSearchV2.OnPoiSearchListener {
                override fun onPoiSearched(result: PoiResultV2?, resultCode: Int) {
                    isLoading = false
                    if (resultCode == AMapException.CODE_AMAP_SUCCESS && result?.query != null && result.pois.isNotEmpty()) {
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
                        isEmpty = false
                        continuation.resume(items)
                    } else {
                        isEmpty = true
                        continuation.resume(emptyList())
                    }
                }

                override fun onPoiItemSearched(poiItem: PoiItemV2?, rCode: Int) {}
            })
            // 执行异步搜索
            poiSearch.searchPOIAsyn()
        }
    }

    // 监听地图中心点变化事件
    fun setCenterChangeListener(map: AMap, context: Context) {
        map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition?) {}

            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                cameraPosition?.let { position ->
                    val target = position.target
                    center = target
                    if (!isSearchMode && !isListItemClicking.value) {
                        viewModelScope.launch {
                            locationToAddress(context, target)?.let {
                                val address = it.formatAddress
                                val startIndex = address.lastIndexOf(it.district)
                                val name = address.slice(startIndex..address.lastIndex)
                                centerLocation = LocationItem(
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

    // 监听地图点击事件
    fun setMapClickListener(map: AMap) {
        map.setOnMapClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it))
        }
        map.setOnPOIClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it.coordinate))
        }
    }

    // 初始化地图视野
    fun initializeMyLocation(map: AMap) {
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