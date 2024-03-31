package top.chengdongqing.weui.feature.location.picker

import android.view.MotionEvent
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import top.chengdongqing.weui.feature.location.data.model.LocationItem
import top.chengdongqing.weui.feature.location.data.repository.LocationRepository
import top.chengdongqing.weui.feature.location.data.repository.LocationRepositoryImpl
import top.chengdongqing.weui.feature.location.picker.locationlist.PagingState
import top.chengdongqing.weui.feature.location.picker.locationlist.PagingStateImpl
import top.chengdongqing.weui.feature.location.utils.isLoaded
import top.chengdongqing.weui.feature.location.utils.toLatLng

@Stable
interface LocationPickerState {
    /**
     * 地图实例
     */
    val map: AMap

    /**
     * 当前设备坐标
     */
    val currentLatLng: LatLng?

    /**
     * 地图中心点坐标
     */
    var mapCenterLatLng: LatLng?

    /**
     * 是否是搜索模式
     */
    var isSearchMode: Boolean

    /**
     * 是否展开列表
     */
    var isListExpanded: Boolean

    /**
     * 分页信息
     */
    val paging: PagingState<LocationItem>

    /**
     * 选中的位置索引
     */
    var selectedIndex: Int

    /**
     * 搜索模式下的分页信息
     */
    val pagingOfSearch: PagingState<LocationItem>

    /**
     * 搜索模式下选中的位置索引
     */
    var selectedIndexOfSearch: Int?

    /**
     * 当前选中的位置信息
     */
    val selectedLocation: LocationItem?

    /**
     * 搜索位置
     */
    suspend fun search(
        location: LatLng?,
        keyword: String = "",
        pageNum: Int = 0,
        pageSize: Int = 10
    ): List<LocationItem>
}

@Composable
fun rememberLocationPickerState(map: AMap, listState: LazyListState): LocationPickerState {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val locationRepository = remember { LocationRepositoryImpl(context) }
    val currentLocation = locationRepository.currentLocation.collectAsState(initial = null)

    val state = remember {
        LocationPickerStateImpl(
            map,
            locationRepository,
            coroutineScope,
            currentLocation,
            listState
        )
    }

    LaunchedEffect(currentLocation.value) {
        currentLocation.value?.let {
            if (state.mapCenterLatLng == null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
                state.mapCenterLatLng = it
            }
        }
    }

    return state
}

private class LocationPickerStateImpl(
    override val map: AMap,
    private val locationRepository: LocationRepository,
    private val coroutineScope: CoroutineScope,
    private val _currentLatLng: State<LatLng?>,
    private val listState: LazyListState
) : LocationPickerState {
    override var currentLatLng: LatLng?
        get() = _currentLatLng.value
        set(value) {
            value?.let {
                coroutineScope.launch {
                    locationRepository.saveCurrentLocation(it)
                }
            }
        }
    override var mapCenterLatLng: LatLng?
        get() = _mapCenterLatLng
        set(value) {
            _mapCenterLatLng = value

            if (value != null && !isSearchMode) {
                selectedIndex = 0
                paging.startRefresh()
                coroutineScope.launch {
                    // 获取地图中心点的位置信息，转为列表是方便和之后附近的位置列表合并
                    val centerLocationList = locationToAddress(value)?.let { listOf(it) }
                        ?: emptyList()
                    // 搜索附近位置信息
                    search(value).apply {
                        paging.endRefresh(centerLocationList + this)
                    }
                    // 滚动到第一项
                    listState.scrollToItem(0)
                }
            }
        }
    override var isSearchMode: Boolean
        get() = _isSearchMode
        set(value) {
            _isSearchMode = value
            isListExpanded = value

            if (!value) {
                pagingOfSearch.dataList = emptyList()
                mapCenterLatLng?.let {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
                }
            }
        }
    override var isListExpanded by mutableStateOf(false)
    override val paging = PagingStateImpl<LocationItem>(initialLoading = true)
    override var selectedIndex by mutableIntStateOf(0)
    override val pagingOfSearch = PagingStateImpl<LocationItem>()
    override var selectedIndexOfSearch by mutableStateOf<Int?>(null)
    override val selectedLocation: LocationItem?
        get() {
            return if (!isSearchMode) {
                paging.dataList.getOrNull(selectedIndex)
            } else {
                selectedIndexOfSearch?.let { pagingOfSearch.dataList.getOrNull(it) }
            }
        }

    private var _mapCenterLatLng by mutableStateOf<LatLng?>(null)
    private var _isSearchMode by mutableStateOf(false)

    override suspend fun search(
        location: LatLng?,
        keyword: String,
        pageNum: Int,
        pageSize: Int
    ): List<LocationItem> {
        if (location == null && keyword.isBlank()) {
            return emptyList()
        }

        return locationRepository.search(
            location,
            keyword,
            pageNum,
            pageSize,
            currentLatLng
        )
    }

    init {
        setMapClickAndDragListener()
        setLocationChangeListener()
    }

    private suspend fun locationToAddress(latLng: LatLng): LocationItem? {
        return locationRepository.locationToAddress(latLng)?.let {
            val address = it.formatAddress
            val startIndex = address.lastIndexOf(it.district)
            val name = address.slice(startIndex..address.lastIndex)
            LocationItem(
                name,
                address,
                latLng = latLng
            )
        }
    }

    // 监听地图点击或拖拽事件
    private fun setMapClickAndDragListener() {
        map.setOnMapClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it))
            mapCenterLatLng = it
        }
        map.setOnPOIClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLng(it.coordinate))
            mapCenterLatLng = it.coordinate
        }
        map.setOnMapTouchListener {
            if (it.action == MotionEvent.ACTION_UP) {
                mapCenterLatLng = map.cameraPosition.target
            }
        }
    }

    // 监听当前位置变化
    private fun setLocationChangeListener() {
        map.setOnMyLocationChangeListener { location ->
            if (location.isLoaded()) {
                location.toLatLng().let {
                    // 只有在当前位置为空且非搜索模式时才将地图视野移至当前位置
                    if (currentLatLng == null && !isSearchMode) {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
                        mapCenterLatLng = it
                    }
                    currentLatLng = it
                }
            }
        }
    }
}