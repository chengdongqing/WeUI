package top.chengdongqing.core.data.repository

import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.MediaType

interface LocalMediaRepository {
    suspend fun loadMediaList(types: Array<MediaType>): List<MediaItem>
}