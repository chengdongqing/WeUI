package top.chengdongqing.weui.ui.components.mediapicker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import top.chengdongqing.weui.ui.components.mediapicker.data.MediaPickerDIGraph
import top.chengdongqing.weui.ui.screens.demo.gallery.MediaItem
import top.chengdongqing.weui.utils.MediaType

@Suppress("UNCHECKED_CAST")
class MediaPickerViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaPickerViewModel(context) as T
    }
}

class MediaPickerViewModel(context: Context) : ViewModel() {
    private val pickerRepository by lazy { MediaPickerDIGraph.createMediaPickerRepository(context) }

    val mediaList by lazy { pickerRepository.loadAllMedias() }
    var mediaType by mutableStateOf<MediaType?>(null)
    val selectedList = mutableStateListOf<MediaItem>()
}