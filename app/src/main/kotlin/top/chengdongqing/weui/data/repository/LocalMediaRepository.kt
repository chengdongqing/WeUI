package top.chengdongqing.weui.data.repository

import top.chengdongqing.weui.data.model.MediaItem
import top.chengdongqing.weui.enums.MediaType

interface LocalMediaRepository {
    suspend fun loadMediaList(types: Array<MediaType>): List<MediaItem>
}