package top.chengdongqing.weui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import top.chengdongqing.weui.navigation.MenuItem
import top.chengdongqing.weui.navigation.MenuTreeData

class HomeViewModel : ViewModel() {

    private val _menuTree = MutableStateFlow(emptyList<MenuItem>())
    val menuTree = _menuTree.asStateFlow()

    private val _expandedIndex = MutableStateFlow<Int?>(null)
    val expandedIndex = _expandedIndex.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _menuTree.update {
            MenuTreeData.map { group ->
                group.copy(children = group.children?.sortedBy { it.label })
            }
        }
    }

    fun setExpandedIndex(index: Int) {
        _expandedIndex.update {
            if (it == index) null else index
        }
    }
}