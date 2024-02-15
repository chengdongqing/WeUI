package top.chengdongqing.weui.ui.components.indexedlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.houbb.pinyin.util.PinyinHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.theme.BackgroundColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeIndexedList(labels: List<String>) {
    val listState = rememberLazyListState()
    var loading by remember { mutableStateOf(false) }
    val groups by produceState<Map<Char, List<String>>>(initialValue = emptyMap(), key1 = labels) {
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
            groups.forEach { (letter, list) ->
                stickyHeader {
                    Text(
                        text = letter.toString(),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundColor)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                itemsIndexed(list) { index, item ->
                    Column(modifier = Modifier.background(Color.White)) {
                        Text(
                            text = item,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                        if (index < list.lastIndex) {
                            WeDivider(modifier = Modifier.padding(start = 16.dp, end = 30.dp))
                        }
                    }
                }
            }
        }
        IndexBar(listState, groups)
    }
}

@Composable
private fun BoxScope.IndexBar(listState: LazyListState, groups: Map<Char, List<String>>) {
    val coroutineScope = rememberCoroutineScope()
    val indexes = remember { ('A'..'Z').toList() + '#' }

    Column(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        indexes.forEach { title ->
            Text(
                text = title.toString(),
                fontSize = 11.sp,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .clickable {
                        coroutineScope.launch {
                            val index = calculateIndexForTitle(
                                title,
                                groups,
                                listState.layoutInfo.totalItemsCount
                            )
                            if (index >= 0) {
                                listState.scrollToItem(index)
                            }
                        }
                    }
            )
        }
    }
}

private fun calculateIndexForTitle(
    title: Char,
    groups: Map<Char, List<String>>,
    totalCount: Int
): Int {
    return if (title == '#') {
        totalCount - 1
    } else {
        var cumulativeIndex = 0
        for ((groupTitle, items) in groups) {
            if (groupTitle == title) {
                return cumulativeIndex
            }
            cumulativeIndex += items.size + 1
        }
        return -1
    }
}

private suspend fun groupByFirstLetter(labels: List<String>): Map<Char, List<String>> =
    withContext(Dispatchers.IO) {
        val groupedCities = mutableMapOf<Char, MutableList<String>>()

        labels.forEach { label ->
            val firstChar = label.first()
            val firstLetter = if (firstChar.isChinese()) {
                PinyinHelper.toPinyin(label).first().uppercaseChar()
            } else {
                firstChar
            }
            if (firstLetter in 'A'..'Z') {
                groupedCities.getOrPut(firstLetter) { mutableListOf() }.add(label)
            } else {
                groupedCities.getOrPut('#') { mutableListOf() }.add(label)
            }
        }
        groupedCities.values.forEach { it.sort() }

        groupedCities
    }

private fun Char.isChinese(): Boolean {
    return this.code in 0x4E00..0x9FFF
}