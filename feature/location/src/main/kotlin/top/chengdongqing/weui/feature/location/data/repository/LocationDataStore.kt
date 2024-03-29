package top.chengdongqing.weui.feature.location.data.repository

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.amap.api.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.locationDataStore by preferencesDataStore(name = "location")

private object PreferencesKeys {
    val LATITUDE = doublePreferencesKey("latitude")
    val LONGITUDE = doublePreferencesKey("longitude")
}

suspend fun Context.saveLocation(latLng: LatLng) {
    this.locationDataStore.edit { preferences ->
        preferences[PreferencesKeys.LATITUDE] = latLng.latitude
        preferences[PreferencesKeys.LONGITUDE] = latLng.longitude
    }
}

val Context.locationFlow: Flow<LatLng?>
    get() = this.locationDataStore.data
        .map { preferences ->
            val latitude = preferences[PreferencesKeys.LATITUDE]
            val longitude = preferences[PreferencesKeys.LONGITUDE]
            if (latitude != null && longitude != null) {
                LatLng(latitude, longitude)
            } else {
                null
            }
        }
