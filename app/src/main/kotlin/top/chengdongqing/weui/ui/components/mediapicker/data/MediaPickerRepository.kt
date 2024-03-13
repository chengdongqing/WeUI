package top.chengdongqing.weui.ui.components.mediapicker.data

import kotlinx.coroutines.flow.Flow
import top.chengdongqing.weui.ui.screens.demo.gallery.MediaItem

interface MediaPickerRepository {
    fun loadAllMedias(): Flow<List<MediaItem>>
}