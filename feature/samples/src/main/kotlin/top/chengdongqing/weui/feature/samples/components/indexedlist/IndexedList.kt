package top.chengdongqing.weui.feature.samples.components.indexedlist

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.components.loading.WeLoading
import top.chengdongqing.weui.core.utils.PinyinUtils.groupByFirstLetter

@Composable
fun WeIndexedList(labels: List<String>) {
    val listState = rememberLazyListState()
    var loading by remember { mutableStateOf(false) }
    val groups by produceState(initialValue = emptyMap(), key1 = labels) {
        loading = true
        value = groupByFirstLetter(labels).toSortedMap { a, b -> if (a != '#') a - b else 1 }
        loading = false
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
        IndexBar(listState, groups)
    }
}

private fun LazyListScope.indexGroups(groups: Map<Char, List<String>>) {
    groups.forEach { (letter, list) ->
        stickyHeader {
            Text(
                text = letter.toString(),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        itemsIndexed(list) { index, item ->
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)) {
                Text(
                    text = item,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onBackground)
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