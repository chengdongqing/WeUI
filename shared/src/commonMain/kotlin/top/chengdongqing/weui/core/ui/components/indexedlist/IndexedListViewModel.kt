package top.chengdongqing.weui.core.ui.components.indexedlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.util.getInitial

class IndexedListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IndexedListUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun loadData(options: List<String>) {
        val groups = withContext(Dispatchers.Default) {
            options.groupBy { it.getInitial() }
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
        }

        val indexMap = calculateIndexMap(groups)

        _uiState.update {
            it.copy(
                isLoading = false,
                groups = groups,
                indexMap = indexMap
            )
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
}

data class IndexedListUiState(
    val isLoading: Boolean = true,
    val groups: Map<Char, List<String>> = emptyMap(),
    val indexMap: Map<Char, Int> = emptyMap()
)