package top.chengdongqing.weui.feature.location.picker.locationlist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.amap.api.maps.CameraUpdateFactory
import top.chengdongqing.weui.core.ui.components.searchbar.WeSearchBar
import top.chengdongqing.weui.feature.location.picker.LocationPickerState

@Composable
fun SearchableLocationList(state: LocationPickerState, listState: LazyListState) {
    val animatedHeightFraction by animateFloatAsState(
        targetValue = if (state.isListExpanded) 0.7f else 0.4f,
        label = "LocationListHeightAnimation"
    )
    val nestedScrollConnection = rememberNestedScrollConnection(
        state.isListExpanded,
        animatedHeightFraction
    ) {
        state.isListExpanded = it
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeightFraction)
            .expandedStyle(state.isListExpanded)
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection)
    ) {
        if (state.isListExpanded) {
            TopArrow(state)
        }

        if (state.isSearchMode) {
            SearchPanel(state)
        } else {
            SearchInput(state)
            LocationList(
                listState,
                state.paging,
                state.selectedIndex,
                onSelect = {
                    // 保存选择的位置索引
                    state.selectedIndex = it
                    // 移动地图中心点到选中的位置
                    val latLng = state.paging.dataList[it].latLng
                    state.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                }
            ) {
                if (!state.paging.isAllLoaded && !state.paging.isLoading) {
                    val pageNum = state.paging.startLoadMore()
                    state.search(state.mapCenterLatLng, pageNum = pageNum)
                        .apply {
                            val filteredList = this.filter { item ->
                                state.paging.dataList.none { it.name == item.name }
                            }
                            state.paging.endLoadMore(filteredList)
                        }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.TopArrow(state: LocationPickerState) {
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { state.isListExpanded = false }
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(22.dp)
        )
    }
}

private fun Modifier.expandedStyle(expanded: Boolean) = if (expanded) {
    this
        .offset(y = (-12).dp)
        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
} else {
    this
}

@Composable
private fun SearchInput(state: LocationPickerState) {
    WeSearchBar(
        value = "",
        modifier = Modifier.padding(16.dp),
        placeholder = "搜索地点",
        disabled = true,
        onClick = {
            state.isSearchMode = true
        }
    ) {}
}

@Composable
private fun rememberNestedScrollConnection(
    expanded: Boolean,
    heightFraction: Float,
    onExpandChange: (Boolean) -> Unit
): NestedScrollConnection {
    val currentExpanded by rememberUpdatedState(newValue = expanded)
    val currentHeightFraction by rememberUpdatedState(newValue = heightFraction)

    return remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y < 0 && !currentExpanded) {
                    onExpandChange(true)
                }
                return if (currentHeightFraction == 0.7f) Offset.Zero else available
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y > 0 && currentExpanded) {
                    onExpandChange(false)
                }
                return Offset.Zero
            }
        }
    }
}