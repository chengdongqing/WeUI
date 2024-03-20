package top.chengdongqing.weui.feature.media.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.media.screens.camera.CameraScreen
import top.chengdongqing.weui.feature.media.screens.live.LivePlayerScreen
import top.chengdongqing.weui.feature.media.screens.live.LivePusherScreen
import top.chengdongqing.weui.feature.media.screens.picker.MediaPickerScreen
import top.chengdongqing.weui.feature.media.screens.player.AudioPlayerScreen
import top.chengdongqing.weui.feature.media.screens.recorder.AudioRecorderScreen
import top.chengdongqing.weui.feature.media.screens.recorder.VideoRecorderScreen
import top.chengdongqing.weui.ui.screens.media.player.VideoPlayerScreen

fun NavGraphBuilder.addMediaGraph(navController: NavController) {
    composable("camera") {
        CameraScreen(navController)
    }
    composable("live_pusher") {
        LivePusherScreen()
    }
    composable("live_player") {
        LivePlayerScreen()
    }
    composable("media_picker") {
        MediaPickerScreen()
    }
    composable("audio_player") {
        AudioPlayerScreen()
    }
    composable("video_player") {
        VideoPlayerScreen()
    }
    composable("audio_recorder") {
        AudioRecorderScreen()
    }
    composable("video_recorder") {
        VideoRecorderScreen(navController)
    }
}