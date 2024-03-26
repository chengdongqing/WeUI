package top.chengdongqing.weui.feature.samples.videochannel.data.model

import kotlin.time.Duration

data class VideoItem(
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: Duration,
    val username: String,
    val userAvatarUrl: String,
    val likes: Int = 2683,
    val forwards: Int = 4468,
    val favorites: Int = 399,
    val comments: Int = 1838,
    val content: String,
    val region: String,
    val time: String,
    val commentList: List<VideoComment> = emptyList()
)