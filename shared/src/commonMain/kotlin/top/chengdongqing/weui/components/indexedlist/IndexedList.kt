package top.chengdongqing.weui.components.indexedlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.components.WeDivider
import top.chengdongqing.weui.components.loading.WeLoading
import top.chengdongqing.weui.theme.WeTheme
import top.chengdongqing.weui.util.getInitial

@Composable
fun WeIndexedList(labels: List<String>) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    val groups by produceState(initialValue = emptyMap(), key1 = labels) {
        loading = true
        value = labels.groupBy { it.getInitial() }
            .entries.sortedWith { e1, e2 ->
                val a = e1.key
                val b = e2.key
                when {
                    a == '#' -> 1
                    b == '#' -> -1
                    else -> a.compareTo(b)
                }
            }
            .associate { it.key to it.value }
        loading = false
    }
    val indexMap by remember(groups) {
        derivedStateOf {
            calculateIndexMap(groups)
        }
    }

    if (loading) {
        WeLoading()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            indexGroups(groups)
        }
        AlphabetIndexer(groups) { initial ->
            indexMap[initial]?.let { targetIndex ->
                scope.launch {
                    listState.scrollToItem(targetIndex)
                }
            }
        }
    }
}

private fun LazyListScope.indexGroups(groups: Map<Char, List<String>>) {
    groups.forEach { (initial, list) ->
        stickyHeader {
            Text(
                text = initial.toString(),
                color = WeTheme.colorScheme.textSecondary,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WeTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        itemsIndexed(list) { index, item ->
            Column(modifier = Modifier.background(WeTheme.colorScheme.surface)) {
                Text(
                    text = item,
                    color = WeTheme.colorScheme.textPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WeTheme.colorScheme.surface)
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
                if (index < list.lastIndex) {
                    WeDivider(modifier = Modifier.padding(start = 16.dp, end = 30.dp))
                }
            }
        }
    }
}

private fun calculateIndexMap(groups: Map<Char, List<String>>): Map<Char, Int> {
    var currentIndex = 0
    return buildMap {
        groups.forEach { (initial, apks) ->
            put(initial, currentIndex)
            currentIndex += apks.size + 1
        }
    }
}