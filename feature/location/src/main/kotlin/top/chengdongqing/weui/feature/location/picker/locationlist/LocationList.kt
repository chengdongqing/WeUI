package top.chengdongqing.weui.feature.location.picker.locationlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.R
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.loading.WeLoading
import top.chengdongqing.weui.core.ui.components.refreshview.rememberLoadMoreState
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.core.utils.formatDistance
import top.chengdongqing.weui.feature.location.data.model.LocationItem

@Composable
fun LocationList(
    listState: LazyListState,
    pagingState: PagingState<LocationItem>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    onLoadMore: suspend () -> Unit
) {
    val loadMoreState = rememberLoadMoreState(onLoadMore)

    Box(modifier = Modifier.nestedScroll(loadMoreState.nestedScrollConnection)) {
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 16.dp)) {
            itemsIndexed(
                pagingState.dataList,
                key = { _, item -> item.name }
            ) { index, item ->
                LocationListItem(index == selectedIndex, item) {
                    onSelect(index)
                }
                if (index < pagingState.dataList.lastIndex) {
                    WeDivider()
                }
            }
            item {
                if (loadMoreState.isLoadingMore) {
                    WeLoadMore(listState = listState)
                } else if (pagingState.isAllLoaded) {
                    WeLoadMore(type = LoadMoreType.ALL_LOADED)
                }
            }
        }

        if (pagingState.isLoading && !loadMoreState.isLoadingMore) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                WeLoading(size = 80.dp)
            }
        }
    }
}

@Composable
private fun LocationListItem(checked: Boolean, location: LocationItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = location.name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildList {
                    location.distance?.let { add(formatDistance(it)) }
                    location.address?.let { add(it) }
                }.joinToString(" | "),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp
            )
        }
        if (checked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}