package top.chengdongqing.weui.feature.location.data.repository

import com.amap.api.maps.model.LatLng
import com.amap.api.services.geocoder.RegeocodeAddress
import kotlinx.coroutines.flow.Flow
import top.chengdongqing.weui.feature.location.data.model.LocationItem

interface LocationRepository {
    suspend fun locationToAddress(latLng: LatLng): RegeocodeAddress?

    suspend fun search(
        location: LatLng?,
        keyword: String,
        pageNum: Int,
        pageSize: Int,
        current: LatLng?
    ): List<LocationItem>

    val currentLocation: Flow<LatLng?>

    suspend fun saveCurrentLocation(latLng: LatLng)
}