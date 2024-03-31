package top.chengdongqing.weui.feature.location.picker.locationlist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.amap.api.maps.CameraUpdateFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import top.chengdongqing.weui.core.ui.components.searchbar.WeSearchBar
import top.chengdongqing.weui.core.utils.UpdatedEffect
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.core.utils.rememberKeyboardHeight
import top.chengdongqing.weui.feature.location.data.model.LocationItem
import top.chengdongqing.weui.feature.location.picker.LocationPickerState

@Composable
fun SearchPanel(state: LocationPickerState) {
    val listState = rememberLazyListState()
    val paging = state.pagingOfSearch

    val keywordFlow = remember { MutableStateFlow("") }
    val keyword by keywordFlow.collectAsState()
    var type by remember { mutableIntStateOf(0) }

    SearchingEffect(keywordFlow, state, paging, listState, keyword, type)
    KeyboardEffect(state)

    Column {
        WeSearchBar(
            value = keyword,
            modifier = Modifier.padding(16.dp),
            focused = true,
            onFocusChange = {
                if (!it) {
                    state.isSearchMode = false
                }
            }
        ) {
            keywordFlow.value = it
        }
        TypeTabRow(type) { type = it }

        LocationList(
            listState,
            paging,
            state.selectedIndexOfSearch,
            onSelect = {
                // 保存选择的位置索引
                state.selectedIndexOfSearch = it
                // 移动地图中心点到选中的位置
                val latLng = paging.dataList[it].latLng
                state.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                // 选择位置后展开地图
                state.isListExpanded = false
            }
        ) {
            if (!paging.isAllLoaded) {
                val pageNum = paging.startLoadMore()
                state.search(
                    location = if (type == 0) state.currentLatLng else null,
                    keyword = keyword,
                    pageNum = pageNum
                ).apply {
                    paging.endLoadMore(this)
                }
            }
        }
    }
}

@Composable
private fun TypeTabRow(type: Int, onChange: (Int) -> Unit) {
    val options = remember { listOf("附近", "不限") }
    var itemWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            options.forEachIndexed { index, item ->
                val active = index == type
                Text(
                    text = item,
                    color = if (active) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    modifier = Modifier
                        .onSizeChanged {
                            itemWidth = with(density) { it.width.toDp() }
                        }
                        .clickableWithoutRipple {
                            onChange(index)
                        }
                        .padding(vertical = 3.dp)
                )
            }
        }

        val animatedOffsetX by animateDpAsState(
            (type * itemWidth.value + type * 16).dp,
            label = ""
        )
        HorizontalDivider(
            modifier = Modifier
                .width(itemWidth)
                .offset(x = animatedOffsetX),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun SearchingEffect(
    keywordFlow: Flow<String>,
    state: LocationPickerState,
    paging: PagingState<LocationItem>,
    listState: LazyListState,
    keyword: String,
    type: Int
) {
    val currentKeyword by rememberUpdatedState(newValue = keyword)
    val currentType by rememberUpdatedState(newValue = type)

    LaunchedEffect(keywordFlow) {
        keywordFlow
            .debounce(300)
            .filter { it.isNotBlank() }
            .collect {
                refresh(state, paging, listState, it, currentType)
            }
    }
    UpdatedEffect(currentType) {
        refresh(state, paging, listState, currentKeyword, currentType)
    }
}

private suspend fun refresh(
    state: LocationPickerState,
    paging: PagingState<LocationItem>,
    listState: LazyListState,
    keyword: String,
    type: Int
) {
    state.selectedIndexOfSearch = null

    if (keyword.isNotBlank()) {
        paging.startRefresh()
        state.search(
            location = if (type == 0) state.currentLatLng else null,
            keyword = keyword
        ).apply {
            paging.endRefresh(this)
        }
        listState.scrollToItem(0)
    } else {
        paging.dataList = emptyList()
    }
}

@Composable
private fun KeyboardEffect(state: LocationPickerState) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardHeight = rememberKeyboardHeight()

    UpdatedEffect(keyboardHeight) {
        if (keyboardHeight > 0.dp) {
            state.isListExpanded = true
        }
    }
    UpdatedEffect(state.isListExpanded) {
        if (!state.isListExpanded) {
            keyboardController?.hide()
        }
    }
}