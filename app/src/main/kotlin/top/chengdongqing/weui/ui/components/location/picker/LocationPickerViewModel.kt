package top.chengdongqing.weui.ui.components.location.picker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import top.chengdongqing.weui.utils.toLatLng
import top.chengdongqing.weui.utils.toLatLonPoint

class LocationPickerViewModel : ViewModel() {
    var map by mutableStateOf<AMap?>(null)
    var location by mutableStateOf<LatLng?>(null)
    var locationList by mutableStateOf<List<LocationItem>>(emptyList())
    var current by mutableIntStateOf(0)
    var loading by mutableStateOf(false)

    fun searchNearbyPlaces(
        context: Context,
        location: LatLng,
        keyword: String = "",
        onLoad: (List<LocationItem>) -> Unit
    ) {
        loading = true
        val query = PoiSearch.Query(keyword, "", "") // 第一个参数是关键字，第二个是类别，第三个是搜索区域（城市编码或名称）
        query.pageSize = 10
        query.pageNum = 0

        val poiSearch = PoiSearch(context, query)
        poiSearch.bound =
            PoiSearch.SearchBound(location.toLatLonPoint(), 1000 * 1000)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(result: PoiResult?, rCode: Int) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.query != null) {
                        val items = result.pois.map {
                            LocationItem(
                                name = it.title,
                                address = it.snippet,
                                distance = it.distance,
                                latLng = it.latLonPoint.toLatLng()
                            )
                        }
                        onLoad(items)
                    }
                }
                loading = false
            }

            override fun onPoiItemSearched(poiItem: PoiItem?, rCode: Int) {}
        })
        poiSearch.searchPOIAsyn()
    }

    fun setCenterChangeListener(map: AMap, onChange: (LatLng) -> Unit) {
        // 一开始获取当前位置
        var locationChangeListener: AMap.OnMyLocationChangeListener? =
            AMap.OnMyLocationChangeListener { location ->
                if (location != null && location.isComplete) {
                    map.moveCamera(CameraUpdateFactory.newLatLng(location.toLatLng()))
                }
            }
        map.addOnMyLocationChangeListener(locationChangeListener)

        // 地图中心点改变事件
        map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition?) {}

            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                cameraPosition?.let {
                    onChange(it.target)
                }

                if (locationChangeListener != null) {
                    map.removeOnMyLocationChangeListener(locationChangeListener)
                    locationChangeListener = null
                }
            }
        })

        // 地图点击事件
        map.setOnMapClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it))
        }
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