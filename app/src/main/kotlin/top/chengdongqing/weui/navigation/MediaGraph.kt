package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.media.camera.CameraScreen
import top.chengdongqing.weui.ui.screens.media.cropper.ImageCropperScreen
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
    composable("image-cropper") {
        ImageCropperScreen()
    }
    composable("live-pusher") {
        LivePusherScreen()
    }
    composable("live-player") {
        LivePlayerScreen()
    }
    composable("media-picker") {
        MediaPickerScreen()
    }
    composable("audio-player") {
        AudioPlayerScreen()
    }
    composable("video-player") {
        VideoPlayerScreen()
    }
    composable("audio-recorder") {
        AudioRecorderScreen()
    }
    composable("video-recorder") {
        VideoRecorderScreen(navController)
    }
}