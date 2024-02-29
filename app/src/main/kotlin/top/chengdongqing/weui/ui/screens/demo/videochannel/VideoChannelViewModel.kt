package top.chengdongqing.weui.ui.screens.demo.videochannel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

data class Video(
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

data class VideoComment(
    val username: String,
    val userAvatarUrl: String,
    val content: String,
    val likes: Int,
    val region: String,
    val time: String,
    val replayList: List<VideoComment> = emptyList()
)

class VideoChannelViewModel : ViewModel() {
    val videoList = mutableStateListOf(
        Video(
            videoUrl = "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/e25d81c4922fca5ebe51877717ef9b76.mp4",
            thumbnailUrl = "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/2fd26bb99b723337a2f8eaba84f7d5bb.jpg",
            duration = 1.minutes + 33.seconds,
            username = "小米科技",
            userAvatarUrl = "https://cdn.cnbj1.fds.api.mi-img.com/mi.com-assets/shop/img/logo-mi2.png",
            content = "童年的一个午后，风吹树叶沙沙作响，村庄集齐了各种声音哄我睡觉，我躺在竹席上睡得很沉，然后做了一个好长好长的梦，梦见菜园里种满了瓜果蔬菜......",
            region = "北京",
            time = "6天前",
            commentList = MutableList(10) {
                VideoComment(
                    username = "张子枫",
                    userAvatarUrl = "https://img0.baidu.com/it/u=2901830406,3460421538&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
                    content = "那个时候整个村子都在午睡，只有自己一个人醒的感觉",
                    likes = 258,
                    region = "美国",
                    time = "4天前",
                    replayList = listOf(
                        VideoComment(
                            username = "吴磊",
                            userAvatarUrl = "https://img0.baidu.com/it/u=3979949991,2513156939&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
                            content = "真是一语中的，小时候我妈鼓捣睡午觉。翻来覆去睡不着，真的感觉全村就我醒着",
                            likes = 382,
                            region = "重庆",
                            time = "2天前"
                        )
                    )
                )
            }
        ),
        Video(
            videoUrl = "https://xps02.xiaopeng.com/cms/material/video/2023/06-29/video_20230629100416_00416.mp4",
            thumbnailUrl = "https://s.xiaopeng.com/xp-fe/mainsite/2023/g6/v1_5/p1-2.12.0.jpg",
            duration = 57.seconds,
            username = "小鹏汽车",
            userAvatarUrl = "https://xps01.xiaopeng.com/cms/material/pic/2023/06-05/pic_20230605100914_45631.png",
            content = "童年的一个午后，风吹树叶沙沙作响，村庄集齐了各种声音哄我睡觉，我躺在竹席上睡得很沉，然后做了一个好长好长的梦，梦见菜园里种满了瓜果蔬菜......",
            region = "广州",
            time = "10天前",
            commentList = MutableList(100) {
                VideoComment(
                    username = "张子枫",
                    userAvatarUrl = "https://img0.baidu.com/it/u=2901830406,3460421538&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
                    content = "那个时候整个村子都在午睡，只有自己一个人醒的感觉",
                    likes = 258,
                    region = "美国",
                    time = "4天前",
                    replayList = listOf(
                        VideoComment(
                            username = "吴磊",
                            userAvatarUrl = "https://img0.baidu.com/it/u=3979949991,2513156939&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
                            content = "真是一语中的，小时候我妈鼓捣睡午觉。翻来覆去睡不着，真的感觉全村就我醒着",
                            likes = 382,
                            region = "重庆",
                            time = "2天前"
                        )
                    )
                )
            }
        ),
    )
}