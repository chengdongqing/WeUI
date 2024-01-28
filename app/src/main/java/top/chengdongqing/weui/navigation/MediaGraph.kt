package top.chengdongqing.weui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.media.camera.CameraPage
import top.chengdongqing.weui.ui.views.media.cropper.ImageCropperPage
import top.chengdongqing.weui.ui.views.media.filebrowser.FileBrowserPage
import top.chengdongqing.weui.ui.views.media.gallery.GalleryPage
import top.chengdongqing.weui.ui.views.media.gallery.GalleryViewModel
import top.chengdongqing.weui.ui.views.media.gallery.MediaPreviewPage
import top.chengdongqing.weui.ui.views.media.live.LivePlayerPage
import top.chengdongqing.weui.ui.views.media.live.LivePusherPage
import top.chengdongqing.weui.ui.views.media.picker.MediaPickerPage
import top.chengdongqing.weui.ui.views.media.player.AudioPlayerPage
import top.chengdongqing.weui.ui.views.media.player.VideoPlayerPage
import top.chengdongqing.weui.ui.views.media.recorder.AudioRecorderPage
import top.chengdongqing.weui.ui.views.media.recorder.VideoRecorderPage

fun NavGraphBuilder.mediaGraph(navController: NavController, galleryViewModel: GalleryViewModel) {
    composable("gallery") {
        GalleryPage(galleryViewModel, navController)
    }
    composable("media-preview?index={index}",
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        val index = it.arguments?.getInt("index") ?: 0
        MediaPreviewPage(galleryViewModel, index)
    }
    composable("file-browser") {
        FileBrowserPage()
    }
    composable("camera") {
        CameraPage()
    }
    composable("image-cropper") {
        ImageCropperPage()
    }
    composable("live-pusher") {
        LivePusherPage()
    }
    composable("live-player") {
        LivePlayerPage()
    }
    composable("media-picker") {
        MediaPickerPage()
    }
    composable("audio-player") {
        AudioPlayerPage()
    }
    composable("video-player") {
        VideoPlayerPage()
    }
    composable("audio-recorder") {
        AudioRecorderPage()
    }
    composable("video-recorder") {
        VideoRecorderPage()
    }
}