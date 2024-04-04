package top.chengdongqing.weui.feature.location.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeAddress
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.feature.location.data.model.LocationItem
import top.chengdongqing.weui.feature.location.utils.toLatLng
import top.chengdongqing.weui.feature.location.utils.toLatLonPoint
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

class LocationRepositoryImpl(private val context: Context) : LocationRepository {
    override suspend fun locationToAddress(latLng: LatLng): RegeocodeAddress? =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val geocoderSearch = GeocodeSearch(context)
                geocoderSearch.setOnGeocodeSearchListener(object :
                    GeocodeSearch.OnGeocodeSearchListener {
                    override fun onRegeocodeSearched(result: RegeocodeResult?, resultCode: Int) {
                        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
                            continuation.resume(result?.regeocodeAddress)
                        } else {
                            continuation.resume(null)
                        }
                    }

                    override fun onGeocodeSearched(geocodeResult: GeocodeResult?, rCode: Int) {}
                })
                val query = RegeocodeQuery(
                    latLng.toLatLonPoint(),
                    100_000f,
                    GeocodeSearch.AMAP
                )
                geocoderSearch.getFromLocationAsyn(query)
            }
        }

    override suspend fun search(
        location: LatLng?,
        keyword: String,
        pageNum: Int,
        pageSize: Int,
        current: LatLng?
    ): List<LocationItem> = withContext(Dispatchers.IO) {
        // 构建搜索参数：关键字，类别，区域
        val query = PoiSearchV2.Query(keyword, "", "").apply {
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
                    if (resultCode == AMapException.CODE_AMAP_SUCCESS && result?.query != null) {
                        val items = result.pois.map { poiItem ->
                            LocationItem(
                                id = poiItem.poiId,
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

    override val currentLocation: Flow<LatLng?>
        get() = context.locationDataStore.data
            .map { preferences ->
                val latitude = preferences[PreferencesKeys.LATITUDE]
                val longitude = preferences[PreferencesKeys.LONGITUDE]
                if (latitude != null && longitude != null) {
                    LatLng(latitude, longitude)
                } else {
                    null
                }
            }

    override suspend fun saveCurrentLocation(latLng: LatLng) {
        context.locationDataStore.edit { preferences ->
            preferences[PreferencesKeys.LATITUDE] = latLng.latitude
            preferences[PreferencesKeys.LONGITUDE] = latLng.longitude
        }
    }
}