package top.chengdongqing.weui.ui.components.mediapicker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.chengdongqing.weui.data.model.MediaItem
import top.chengdongqing.weui.data.repository.LocalMediaRepositoryImpl
import top.chengdongqing.weui.enums.MediaType

@Stable
interface MediaPickerState {
    /**
     * 可选择的媒体类型
     */
    val type: MediaType?

    /**
     * 可选择的最大数量
     */
    val count: Int

    /**
     * 类型是否可以切换
     */
    val isTypeEnabled: Boolean

    /**
     * 是否加载中
     */
    val isLoading: Boolean

    /**
     * 可选择的媒体文件列表
     */
    val mediaList: List<MediaItem>

    /**
     * 已选择的媒体文件列表
     */
    val selectedMediaList: List<MediaItem>

    /**
     * 添加选择项
     */
    fun add(media: MediaItem)

    /**
     * 删除选择项
     */
    fun removeAt(index: Int)

    /**
     * 刷新可选项
     */
    fun refresh(type: MediaType?)
}

@Composable
fun rememberMediaPickerState(type: MediaType?, count: Int): MediaPickerState {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        MediaPickerStateImpl(context, coroutineScope, type, count).apply {
            refresh(type)
        }
    }
}

private class MediaPickerStateImpl(
    context: Context,
    private val coroutineScope: CoroutineScope,
    val initialType: MediaType?,
    count: Int
) : MediaPickerState {
    override val type: MediaType?
        get() = _type
    override val isTypeEnabled: Boolean
        get() = initialType == null
    override val count: Int
        get() = _count
    override val isLoading: Boolean
        get() = _isLoading
    override val mediaList: List<MediaItem>
        get() = _mediaList
    override val selectedMediaList: List<MediaItem>
        get() = _selectedMediaList

    override fun add(media: MediaItem) {
        _selectedMediaList.add(media)
    }

    override fun removeAt(index: Int) {
        _selectedMediaList.removeAt(index)
    }

    override fun refresh(type: MediaType?) {
        val types = buildList {
            if (type == null || type == MediaType.IMAGE) {
                add(MediaType.IMAGE)
            }
            if (type == null || type == MediaType.VIDEO) {
                add(MediaType.VIDEO)
            }
        }.toTypedArray()

        _type = type
        _isLoading = true
        coroutineScope.launch(Dispatchers.IO) {
            _mediaList = mediaRepository.loadMediaList(types)
            _isLoading = false
        }
    }

    private val mediaRepository = LocalMediaRepositoryImpl(context)
    private var _type by mutableStateOf(initialType)
    private var _count by mutableIntStateOf(count)
    private var _isLoading by mutableStateOf(true)
    private var _mediaList by mutableStateOf<List<MediaItem>>(emptyList())
    private var _selectedMediaList = mutableStateListOf<MediaItem>()
}