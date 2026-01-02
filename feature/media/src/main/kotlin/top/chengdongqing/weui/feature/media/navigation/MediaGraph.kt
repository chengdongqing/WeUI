package top.chengdongqing.weui.feature.media.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.media.screens.audio.AudioPlayerScreen
import top.chengdongqing.weui.feature.media.screens.audio.AudioRecorderScreen
import top.chengdongqing.weui.feature.media.screens.camera.CameraScreen
import top.chengdongqing.weui.feature.media.screens.gallery.GalleryScreen
import top.chengdongqing.weui.feature.media.screens.image.ImageCropperScreen
import top.chengdongqing.weui.feature.media.screens.image.PanoramicImageScreen
import top.chengdongqing.weui.feature.media.screens.picker.MediaPickerScreen

fun NavGraphBuilder.addMediaGraph(navController: NavController) {
    composable("camera") {
        CameraScreen()
    }
    composable("media_picker") {
        MediaPickerScreen()
    }
    composable("audio_player") {
        AudioPlayerScreen()
    }
    composable("audio_recorder") {
        AudioRecorderScreen()
    }
    composable("gallery") {
        GalleryScreen(navController)
    }
    composable("image_cropper") {
        ImageCropperScreen()
    }
    composable("panoramic_image") {
        PanoramicImageScreen()
    }
}