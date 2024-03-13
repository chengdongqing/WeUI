package top.chengdongqing.weui.ui.components.mediapicker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import top.chengdongqing.weui.ui.components.mediapicker.data.MediaPickerDIGraph

@Suppress("UNCHECKED_CAST")
class MediaPickerViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaPickerViewModel(context) as T
    }
}

class MediaPickerViewModel(context: Context) : ViewModel() {
    private val pickerRepository by lazy { MediaPickerDIGraph.createMediaPickerRepository(context) }
    val allMedias by lazy { pickerRepository.loadAllMedias() }
}