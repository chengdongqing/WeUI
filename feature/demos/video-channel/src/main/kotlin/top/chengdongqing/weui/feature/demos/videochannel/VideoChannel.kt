package top.chengdongqing.weui.feature.demos.videochannel

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.videoplayer.WeVideoPlayer
import top.chengdongqing.weui.core.ui.components.videoplayer.rememberVideoPlayerState
import top.chengdongqing.weui.core.ui.theme.BackgroundColorDark
import top.chengdongqing.weui.core.utils.SetupStatusBarStyle
import top.chengdongqing.weui.feature.demos.videochannel.data.VideoDataProvider
import top.chengdongqing.weui.feature.demos.videochannel.data.model.VideoItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoChannelScreen() {
    val videoList = remember {
        mutableStateListOf<VideoItem>().apply {
            addAll(VideoDataProvider.videoList)
        }
    }
    val pagerState = rememberPagerState { videoList.size }

    SetupStatusBarStyle(isDark = false)
    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .background(Color.Black)
            .statusBarsPadding()
            .fillMaxSize()
            .padding(top = 10.dp)
            .background(BackgroundColorDark)
    ) { index ->
        VideoItem(videoList[index])
    }
}

@Composable
private fun VideoItem(video: VideoItem) {
    var commentsVisible by remember { mutableStateOf(false) }

    Box {
        Column {
            val state = rememberVideoPlayerState(videoSource = Uri.parse(video.videoUrl))
            WeVideoPlayer(state, modifier = Modifier.weight(1f))
            InformationBar(video, onCommentsClick = {
                commentsVisible = true
            })
        }

        CommentsCard(video, commentsVisible) {
            commentsVisible = false
        }
    }
}