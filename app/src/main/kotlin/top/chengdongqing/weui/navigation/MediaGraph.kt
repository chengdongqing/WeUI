package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.media.camera.CameraScreen
import top.chengdongqing.weui.ui.screens.media.live.LivePlayerScreen
import top.chengdongqing.weui.ui.screens.media.live.LivePusherScreen
import top.chengdongqing.weui.ui.screens.media.picker.MediaPickerScreen
import top.chengdongqing.weui.ui.screens.media.player.AudioPlayerScreen
import top.chengdongqing.weui.ui.screens.media.player.VideoPlayerScreen
import top.chengdongqing.weui.ui.screens.media.recorder.AudioRecorderScreen
import top.chengdongqing.weui.ui.screens.media.recorder.VideoRecorderScreen

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