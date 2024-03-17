package top.chengdongqing.weui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.map
import top.chengdongqing.weui.ui.components.mediapicker.WeMediaPicker
import top.chengdongqing.weui.ui.screens.media.camera.CameraScreen
import top.chengdongqing.weui.ui.screens.media.live.LivePlayerScreen
import top.chengdongqing.weui.ui.screens.media.live.LivePusherScreen
import top.chengdongqing.weui.ui.screens.media.picker.MediaPickerScreen
import top.chengdongqing.weui.ui.screens.media.player.AudioPlayerScreen
import top.chengdongqing.weui.ui.screens.media.player.VideoPlayerScreen
import top.chengdongqing.weui.ui.screens.media.recorder.AudioRecorderScreen
import top.chengdongqing.weui.ui.screens.media.recorder.VideoRecorderScreen
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.chooseMedias

fun NavGraphBuilder.addMediaGraph(navController: NavController) {
    composable("camera") {
        CameraScreen(navController)
    }
    composable("live-pusher") {
        LivePusherScreen()
    }
    composable("live-player") {
        LivePlayerScreen()
    }
    composable("media-picker/entrance") { backStackEntry ->
        val mediaListFlow = backStackEntry.savedStateHandle
            .getStateFlow<Array<String>>("mediaList", emptyArray())
            .map { it.toList() }

        MediaPickerScreen(mediaListFlow) { type, count ->
            backStackEntry.savedStateHandle.remove<Array<String>>("mediaList")
            navController.chooseMedias(type, count)
        }
    }
    composable("media-picker?type={type}&count={count}") {
        val mediaType = it.arguments?.getString("type")
        val count = it.arguments?.getString("count")?.toInt()

        WeMediaPicker(
            mediaType = mediaType?.run { MediaType.valueOf(mediaType) },
            countLimits = count,
            onCancel = { navController.popBackStack() }
        ) { list ->
            navController.previousBackStackEntry?.savedStateHandle?.set("mediaList", list)
            navController.popBackStack()
        }
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