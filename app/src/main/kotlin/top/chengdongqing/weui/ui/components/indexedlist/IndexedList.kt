package top.chengdongqing.weui.ui.components.indexedlist

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum
import com.github.houbb.pinyin.util.PinyinHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.components.loading.WeLoading
import top.chengdongqing.weui.ui.theme.BackgroundColorLight

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
            indexGroups(groups)
        }
        IndexBar(listState, groups)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.indexGroups(groups: Map<Char, List<String>>) {
    groups.forEach { (letter, list) ->
        stickyHeader {
            Text(
                text = letter.toString(),
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColorLight)
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

private suspend fun groupByFirstLetter(labels: List<String>): Map<Char, List<String>> =
    withContext(Dispatchers.IO) {
        val groupedCities = mutableMapOf<Char, MutableList<String>>()

        labels.forEach { label ->
            val firstChar = label.first()
            val firstLetter = if (firstChar.isChinese()) {
                PinyinHelper.toPinyin(label, PinyinStyleEnum.FIRST_LETTER).first()
            } else {
                firstChar
            }.uppercaseChar()
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