package top.chengdongqing.weui.ui.components.location.picker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.utils.toLatLng
import top.chengdongqing.weui.utils.toLatLonPoint
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

class LocationPickerViewModel : ViewModel() {
    var map by mutableStateOf<AMap?>(null) // 地图实例
    var current by mutableStateOf<LatLng?>(null) // 当前设备定位
    var center by mutableStateOf<LatLng?>(null) // 当前地图中心
    var locationList by mutableStateOf<List<LocationItem>>(emptyList()) // 位置列表
    var selectedIndex by mutableIntStateOf(0) // 已选位置索引
    var isLoading by mutableStateOf(true) // 是否加载中
    var isEmpty by mutableStateOf(false) // 是否没有结果

    suspend fun searchPOI(
        context: Context,
        location: LatLng? = current,
        keyword: String = "",
        pageNumber: Int = 0
    ): List<LocationItem> {
        return if (current == null) {
            emptyList()
        } else {
            withContext(Dispatchers.IO) {
                suspendCoroutine { continuation ->
                    isLoading = true
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
                                        address = poiItem.snippet,
                                        distance = AMapUtils.calculateLineDistance(
                                            current,
                                            poiItem.latLonPoint.toLatLng()
                                        ).roundToInt(),
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
        }
    }

    fun setCenterChangeListener(map: AMap) {
        // 初始化地图视野
        map.setOnMyLocationChangeListener { location ->
            if (location != null) {
                val p = location.toLatLng()
                current = p
                // 移动地图视野到当前位置
                map.moveCamera(CameraUpdateFactory.newLatLng(p))
                // 不再监听当前位置变化
                map.setOnMyLocationChangeListener(null)
            }
        }

        // 监听地图中心点变化事件
        map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition?) {}

            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                cameraPosition?.let {
                    center = it.target
                }
            }
        })

        // 监听地图点击事件，不包含各种覆盖物的点击事件
        map.setOnMapClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it))
        }
        // 监听地名标记的点击事件
        map.setOnPOIClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it.coordinate))
        }
    }
}

data class LocationItem(
    val name: String,
    val address: String? = null,
    val distance: Int = 0,
    val latLng: LatLng
)