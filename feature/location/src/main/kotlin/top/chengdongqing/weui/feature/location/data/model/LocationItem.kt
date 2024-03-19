package top.chengdongqing.weui.feature.location.data.model

import android.os.Parcelable
import com.amap.api.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationItem(
    val name: String,
    val address: String? = null,
    val distance: Int? = null,
    val latLng: LatLng
) : Parcelable