package top.chengdongqing.weui.ui.components.location.picker

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amap.api.maps.CameraUpdateFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.components.radio.RadioOption
import top.chengdongqing.weui.ui.components.radio.WeRadioGroup
import top.chengdongqing.weui.ui.components.searchbar.WeSearchBar
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.UpdatedEffect
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.formatDistance
import java.util.Timer
import kotlin.concurrent.timerTask

@Composable
internal fun LocationPickerBottom(pickerViewModel: LocationPickerViewModel) {
    val context = LocalContext.current
    var isSearchFocused by remember { mutableStateOf(false) }
    val isListItemClicking = remember { mutableStateOf(false) }
    val animatedHeightFraction by animateFloatAsState(
        targetValue = if (isSearchFocused) 0.6f else 0.4f,
        label = ""
    )
    var locationList by remember { mutableStateOf<List<LocationItem>>(emptyList()) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    // 地图中心点变化后（不包括点击列表）自动搜索附近POI
    LaunchedEffect(pickerViewModel.center) {
        if (!isListItemClicking.value) {
            pickerViewModel.center?.let { center ->
                locationList = pickerViewModel.searchPOI(context, center)
                selectedIndex = 0
            }
        }
    }

    var timer by remember { mutableStateOf<Timer?>(null) }
    val onLocationClick: (LocationItem) -> Unit = { location ->
        timer?.cancel()

        isListItemClicking.value = true
        pickerViewModel.selectedLocation = location
        pickerViewModel.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(location.latLng, 16f))

        timer = Timer().apply {
            schedule(timerTask {
                isListItemClicking.value = false
            }, 1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeightFraction)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SearchBar(
            pickerViewModel,
            isFocused = isSearchFocused,
            onFocusChange = { focused ->
                isSearchFocused = focused
                if (!focused && locationList.isNotEmpty()) {
                    pickerViewModel.selectedLocation = locationList[selectedIndex]
                }
            },
            onLocationClick
        )
        if (!isSearchFocused) {
            LocationList(
                locationList,
                selectedIndex,
                pickerViewModel,
                onSelectChange = { selectedIndex = it },
                onLocationClick
            )
        }
    }
}

@Composable
private fun LocationList(
    locationList: List<LocationItem>,
    selectedIndex: Int,
    pickerViewModel: LocationPickerViewModel,
    onSelectChange: (Int) -> Unit,
    onLocationClick: (LocationItem) -> Unit
) {
    val options = rememberLocationOptions(
        locationList,
        pickerViewModel.centerLocation
    )

    LocationRadioGroup(
        options,
        value = selectedIndex,
        isLoading = pickerViewModel.isLoading,
        isEmpty = pickerViewModel.isEmpty,
    ) { index ->
        onSelectChange(index)
        onLocationClick(locationList[index])
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun SearchBar(
    pickerViewModel: LocationPickerViewModel,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    onLocationClick: (LocationItem) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var locationList by remember { mutableStateOf<List<LocationItem>>(emptyList()) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val options = rememberLocationOptions(locationList)
    val keywordFlow = remember { MutableStateFlow("") }
    val keyword by keywordFlow.collectAsState()
    var type by remember { mutableIntStateOf(0) }

    fun search() {
        coroutineScope.launch {
            locationList = if (keyword.isNotEmpty()) {
                pickerViewModel.searchPOI(
                    context,
                    location = if (type == 0) pickerViewModel.current else null,
                    keyword
                )
            } else {
                emptyList()
            }
        }
    }

    // 输入后执行搜索
    LaunchedEffect(keywordFlow) {
        // 防抖处理
        keywordFlow.debounce(300).collect {
            search()
        }
    }
    // 类型变化后执行搜索
    UpdatedEffect(type) {
        search()
        selectedIndex = null
    }
    // 取消搜索后重置相关临时数据
    UpdatedEffect(isFocused) {
        if (!isFocused) {
            locationList = emptyList()
            keywordFlow.value = ""
            type = 0
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
        LaunchedEffect(Unit) {
            pickerViewModel.selectedLocation = null
        }
        TypeTabRow(value = type) { type = it }
        LocationRadioGroup(
            options,
            value = selectedIndex,
            isLoading = pickerViewModel.isLoading,
            isEmpty = pickerViewModel.isEmpty
        ) { index ->
            selectedIndex = index
            onLocationClick(locationList[index])
        }
    }
}

@Composable
private fun TypeTabRow(value: Int, onChange: (Int) -> Unit) {
    val options = remember { listOf("附近", "不限") }
    val itemWidth = 40.dp

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row {
            options.forEachIndexed { index, item ->
                val active = index == value
                Text(
                    text = item,
                    color = if (active) PrimaryColor else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .width(itemWidth)
                        .clickableWithoutRipple {
                            onChange(index)
                        }
                        .padding(vertical = 3.dp)
                )
            }
        }

        val animatedOffsetX by animateDpAsState((value * itemWidth.value).dp, label = "")
        HorizontalDivider(
            modifier = Modifier
                .width(28.dp)
                .offset(x = animatedOffsetX),
            thickness = 2.dp,
            color = PrimaryColor
        )
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
private fun rememberLocationOptions(
    poiList: List<LocationItem>,
    currentLocation: LocationItem? = null
): List<RadioOption<Int>> {
    return remember(poiList) {
        poiList.toMutableList().apply {
            if (currentLocation != null) {
                add(0, currentLocation)
            }
        }.mapIndexed { index, item ->
            RadioOption(
                label = item.name,
                description = buildList {
                    item.distance?.let { add(formatDistance(it)) }
                    item.address?.let { add(it) }
                }.joinToString(" | "),
                value = index
            )
        }
    }
}