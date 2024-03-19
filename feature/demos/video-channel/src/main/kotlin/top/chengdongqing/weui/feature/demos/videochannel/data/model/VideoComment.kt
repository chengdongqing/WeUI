package top.chengdongqing.weui.feature.demos.videochannel.data.model

data class VideoComment(
    val username: String,
    val userAvatarUrl: String,
    val content: String,
    val likes: Int,
    val region: String,
    val time: String,
    val replayList: List<VideoComment> = emptyList()
)