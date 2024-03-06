package top.chengdongqing.weui.ui.components.location.picker

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amap.api.maps.CameraUpdateFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.components.radio.RadioOption
import top.chengdongqing.weui.ui.components.radio.WeRadioGroup
import top.chengdongqing.weui.ui.components.searchbar.WeSearchBar
import top.chengdongqing.weui.utils.formatDistance
import top.chengdongqing.weui.utils.rememberToggleState

@Composable
internal fun LocationList(locationPickerViewModel: LocationPickerViewModel) {
    val location = locationPickerViewModel.location

    val (height, toggleHeight) = rememberToggleState(
        defaultValue = (LocalConfiguration.current.screenHeightDp * 0.4).dp,
        reverseValue = (LocalConfiguration.current.screenHeightDp * 0.7).dp
    )
    val locationOptions by remember {
        derivedStateOf {
            locationPickerViewModel.locationList.mapIndexed { index, item ->
                RadioOption(
                    label = item.name,
                    description = "${formatDistance(item.distance)} | ${item.address}",
                    value = index
                )
            }
        }
    }
    var keyword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val listItemClicking = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(location) {
        if (!listItemClicking.value) {
            location?.let { location ->
                locationPickerViewModel.searchNearbyPlaces(context, location) { items ->
                    locationPickerViewModel.locationList = items
                    locationPickerViewModel.current = 0
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(animateDpAsState(targetValue = height, label = "").value)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        WeSearchBar(
            value = keyword,
            label = "搜索地点",
            onFocusChange = {
                toggleHeight()
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            keyword = it
            location?.let { location ->
                locationPickerViewModel.searchNearbyPlaces(context, location, keyword) { items ->
                    locationPickerViewModel.locationList = items
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (locationPickerViewModel.loading) {
                WeLoading(size = 80.dp)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (locationPickerViewModel.locationList.isNotEmpty()) {
                    WeRadioGroup(
                        options = locationOptions,
                        value = locationPickerViewModel.current,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) { index ->
                        locationPickerViewModel.current = index
                        listItemClicking.value = true
                        val latLng = locationPickerViewModel.locationList[index].latLng
                        locationPickerViewModel.map?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                        )
                        coroutineScope.launch {
                            delay(1000)
                            listItemClicking.value = false
                        }
                    }
                } else {
                    WeLoadMore(type = LoadMoreType.EMPTY_DATA)
                }
            }
        }
    }
}