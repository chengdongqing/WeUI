package top.chengdongqing.weui.ui.components.location.picker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amap.api.maps.CameraUpdateFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import top.chengdongqing.weui.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.components.radio.RadioOption
import top.chengdongqing.weui.ui.components.radio.WeRadioGroup
import top.chengdongqing.weui.ui.components.searchbar.WeSearchBar
import top.chengdongqing.weui.utils.formatDistance
import java.util.Timer
import kotlin.concurrent.timerTask

@Composable
internal fun LocationList(pickerViewModel: LocationPickerViewModel) {
    val context = LocalContext.current
    var isSearchFocused by remember { mutableStateOf(false) }
    val isListItemClicking = remember { mutableStateOf(false) }
    val animatedHeightFraction by animateFloatAsState(
        targetValue = if (isSearchFocused) 0.6f else 0.4f,
        label = ""
    )

    // 地图中心点变化后（不包括点击列表）自动搜索附近POI
    LaunchedEffect(pickerViewModel.center) {
        if (!isListItemClicking.value) {
            pickerViewModel.center?.let { center ->
                pickerViewModel.locationList = pickerViewModel.searchPOI(context, center)
                pickerViewModel.selectedIndex = 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeightFraction)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SearchGroup(pickerViewModel, isSearchFocused) { focused ->
            isSearchFocused = focused
        }
        if (!isSearchFocused) {
            NearbyLocation(pickerViewModel, isListItemClicking)
        }
    }
}

@Composable
private fun NearbyLocation(
    pickerViewModel: LocationPickerViewModel,
    isListItemClicking: MutableState<Boolean>
) {
    val options = rememberLocationOptions(pickerViewModel.locationList)
    var timer by remember { mutableStateOf<Timer?>(null) }

    LocationRadioGroup(
        options,
        value = pickerViewModel.selectedIndex,
        isLoading = pickerViewModel.isLoading,
        isEmpty = pickerViewModel.isEmpty
    ) { index ->
        timer?.cancel()

        isListItemClicking.value = true
        pickerViewModel.selectedIndex = index
        val latLng = pickerViewModel.locationList[index].latLng
        pickerViewModel.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

        timer = Timer().apply {
            schedule(timerTask {
                isListItemClicking.value = false
            }, 1000)
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun SearchGroup(
    pickerViewModel: LocationPickerViewModel,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var items by remember { mutableStateOf<List<LocationItem>>(emptyList()) }
    val options = rememberLocationOptions(items)
    val keywordFlow = remember { MutableStateFlow("") }
    val keyword by keywordFlow.collectAsState()

    LaunchedEffect(keywordFlow) {
        keywordFlow.debounce(300).collect {
            items = if (it.isNotEmpty()) {
                pickerViewModel.searchPOI(context, keyword = keyword)
            } else {
                emptyList()
            }
        }
    }
    LaunchedEffect(isFocused) {
        if (isFocused) {
            items = emptyList()
            keywordFlow.value = ""
        }
    }

    WeSearchBar(
        value = keyword,
        label = "搜索地点",
        focused = isFocused,
        onFocusChange = onFocusChange,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) { keywordFlow.value = it }
    if (isFocused) {
        LocationRadioGroup(
            options,
            isLoading = pickerViewModel.isLoading,
            isEmpty = pickerViewModel.isEmpty
        ) {
            pickerViewModel.locationList = items
            pickerViewModel.selectedIndex = it
            onFocusChange(false)
        }
    }
}

@Composable
private fun <T> LocationRadioGroup(
    options: List<RadioOption<T>>,
    value: T? = null,
    isLoading: Boolean,
    isEmpty: Boolean,
    onChange: (T) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            WeLoading(size = 80.dp)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if (options.isNotEmpty()) {
                WeRadioGroup(
                    options,
                    value = value,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    onChange = onChange
                )
            } else if (!isLoading && isEmpty) {
                WeLoadMore(type = LoadMoreType.EMPTY_DATA)
            }
        }
    }
}

@Composable
private fun rememberLocationOptions(poiList: List<LocationItem>): List<RadioOption<Int>> {
    return remember(poiList) {
        poiList.mapIndexed { index, item ->
            RadioOption(
                label = item.name,
                description = "${formatDistance(item.distance)} | ${item.address}",
                value = index
            )
        }
    }
}