package top.chengdongqing.weui.feature.location.picker.locationlist

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface PagingState<T> {
    var dataList: List<T>
    val pageNumber: Int
    val pageSize: Int
    val isLoading: Boolean
    val isAllLoaded: Boolean

    fun startRefresh()
    fun endRefresh(dataList: List<T>)
    fun startLoadMore(): Int
    fun endLoadMore(dataList: List<T>)
}

class PagingStateImpl<T>(
    override val pageSize: Int = 10,
    initialLoading: Boolean = false
) : PagingState<T> {
    override var dataList by mutableStateOf<List<T>>(emptyList())
    override var pageNumber by mutableIntStateOf(1)
    override var isLoading by mutableStateOf(initialLoading)
    override var isAllLoaded by mutableStateOf(false)

    override fun startRefresh() {
        isLoading = true
        pageNumber = 1
    }

    override fun endRefresh(dataList: List<T>) {
        this.dataList = dataList
        isAllLoaded = dataList.size < pageSize
        if (!isAllLoaded) {
            pageNumber++
        }
        isLoading = false
    }

    override fun startLoadMore(): Int {
        isLoading = true
        return pageNumber
    }

    override fun endLoadMore(dataList: List<T>) {
        this.dataList += dataList.also {
            if (it.isNotEmpty()) {
                pageNumber++
            }
            isAllLoaded = it.size < pageSize
        }
        isLoading = false
    }
}