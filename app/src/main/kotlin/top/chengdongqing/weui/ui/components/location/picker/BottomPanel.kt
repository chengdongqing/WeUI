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
import androidx.compose.ui.unit.dp
import com.amap.api.maps.CameraUpdateFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import top.chengdongqing.weui.data.model.LocationItem
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

@Composable
internal fun BottomPanel(state: LocationPickerState) {
    val isSearchMode = state.isSearchMode
    val animatedHeightFraction by animateFloatAsState(
        targetValue = if (isSearchMode) 0.6f else 0.4f,
        label = ""
    )
    var locationList by remember { mutableStateOf<List<LocationItem>>(emptyList()) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    // 地图中心点变化后（不包括点击列表）自动搜索附近POI
    LaunchedEffect(state.mapCenter, isSearchMode) {
        if (!state.isWaiting && !isSearchMode) {
            state.mapCenter?.let { center ->
                locationList = state.search(center.latLng)
                selectedIndex = 0
            }
        }
    }
    LaunchedEffect(state.mapCenter) {
        state.mapCenter?.let {
            state.selectedLocation = it
        }
    }

    // 处理列表地址点击事件
    val onLocationClick: (LocationItem) -> Unit = { location ->
        state.isWaiting = true
        state.selectedLocation = location
        state.map.animateCamera(CameraUpdateFactory.newLatLngZoom(location.latLng, 16f))

        coroutineScope.launch {
            delay(1000)
            state.isWaiting = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeightFraction)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SearchBar(
            state,
            isFocused = isSearchMode,
            onFocusChange = { focused ->
                state.isSearchMode = focused
                if (!focused && locationList.isNotEmpty()) {
                    state.selectedLocation = locationList[selectedIndex].apply {
                        state.map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                latLng,
                                16f
                            )
                        )
                    }
                }
            },
            onLocationClick
        )
        if (!isSearchMode) {
            LocationList(
                locationList,
                selectedIndex,
                state,
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
    state: LocationPickerState,
    onSelectChange: (Int) -> Unit,
    onLocationClick: (LocationItem) -> Unit
) {
    val options = rememberLocationOptions(
        locationList,
        state.mapCenter
    )

    LocationRadioGroup(
        options,
        value = selectedIndex,
        isLoading = state.isLoading
    ) { index ->
        onSelectChange(index)
        val location = if (state.mapCenter != null) {
            if (index == 0) {
                state.mapCenter!!
            } else {
                locationList[index - 1]
            }
        } else {
            locationList[index]
        }
        onLocationClick(location)
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun SearchBar(
    state: LocationPickerState,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    onLocationClick: (LocationItem) -> Unit
) {
    var locationList by remember { mutableStateOf<List<LocationItem>>(emptyList()) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val options = rememberLocationOptions(locationList)
    val keywordFlow = remember { MutableStateFlow("") }
    val keyword by keywordFlow.collectAsState()
    var type by remember { mutableIntStateOf(0) }
    var isEmpty by remember { mutableStateOf(false) }

    suspend fun search() {
        locationList = if (keyword.isNotEmpty()) {
            state.search(
                location = if (type == 0) state.current else null,
                keyword
            )
        } else {
            emptyList()
        }
        isEmpty = locationList.isEmpty()
    }

    // 输入后执行搜索
    LaunchedEffect(keywordFlow) {
        keywordFlow
            // 防抖处理
            .debounce(300)
            .filter { it.isNotEmpty() }
            .collect {
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
    ) {
        keywordFlow.value = it
    }
    if (isFocused) {
        LaunchedEffect(Unit) {
            state.selectedLocation = null
        }
        TypeTabRow(value = type) { type = it }
        LocationRadioGroup(
            options,
            value = selectedIndex,
            isLoading = state.isLoading,
            isEmpty
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
    isEmpty: Boolean = false,
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