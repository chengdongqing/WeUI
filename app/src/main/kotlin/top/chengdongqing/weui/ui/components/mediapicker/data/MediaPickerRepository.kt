package top.chengdongqing.weui.ui.components.mediapicker.data

import kotlinx.coroutines.flow.Flow
import top.chengdongqing.weui.data.model.MediaItem

interface MediaPickerRepository {
    fun loadAllMedias(): Flow<List<MediaItem>>
}