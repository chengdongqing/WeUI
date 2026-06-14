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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import top.chengdongqing.weui.components.WeDivider
import top.chengdongqing.weui.components.loading.WeLoading
import top.chengdongqing.weui.theme.WeTheme

@Composable
fun WeIndexedList(
    labels: List<String>,
    viewModel: IndexedListViewModel = viewModel(factory = viewModelFactory {
        initializer {
            IndexedListViewModel()
        }
    })
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData(labels)
    }

    if (uiState.isLoading) {
        WeLoading()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                indexGroups(uiState.groups)
            }
            AlphabetIndexer(uiState.groups) { initial ->
                uiState.indexMap[initial]?.let { targetIndex ->
                    scope.launch {
                        listState.scrollToItem(targetIndex)
                    }
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