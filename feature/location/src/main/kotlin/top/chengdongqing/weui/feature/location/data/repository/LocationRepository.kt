package top.chengdongqing.weui.feature.location.data.repository

import com.amap.api.maps.model.LatLng
import com.amap.api.services.geocoder.RegeocodeAddress
import top.chengdongqing.weui.feature.location.data.model.LocationItem

interface LocationRepository {
    suspend fun locationToAddress(latLng: LatLng): RegeocodeAddress?

    suspend fun search(
        location: LatLng?,
        keyword: String,
        category: String,
        region: String,
        pageNum: Int,
        pageSize: Int,
        current: LatLng?
    ): List<LocationItem>
}