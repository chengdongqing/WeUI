package top.chengdongqing.weui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.chengdongqing.weui.navigation.MenuItem
import top.chengdongqing.weui.navigation.MenuTreeData

class HomeViewModel : ViewModel() {

    private val _menuTree = MutableStateFlow(emptyList<MenuItem>())
    val menuTree = _menuTree.asStateFlow()

    private val _expandedIndex = mutableStateOf<Int?>(null)
    val expandedIndex = _expandedIndex

    init {
        viewModelScope.launch {
            _menuTree.value = MenuTreeData.map { group ->
                group.children?.sortedBy { it.label }
                group
            }
        }
    }

    fun setExpandedIndex(index: Int?) {
        _expandedIndex.value = index
    }
}