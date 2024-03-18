package top.chengdongqing.weui.data.repository

import top.chengdongqing.weui.data.model.MediaItem

interface LocalMediaRepository {
    suspend fun loadAllMedias(): List<MediaItem>
}