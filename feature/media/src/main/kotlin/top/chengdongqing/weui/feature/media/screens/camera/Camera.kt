package top.chengdongqing.weui.feature.media.screens.camera

import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.core.ui.components.actionsheet.rememberActionSheetState
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.camera.rememberCameraLauncher
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.popup.WePopup
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.videoplayer.WeVideoPlayer
import top.chengdongqing.weui.core.ui.components.videoplayer.rememberVideoPlayerState
import top.chengdongqing.weui.core.ui.theme.WeUITheme

@Composable
fun CameraScreen() {
    WeScreen(title = "Camera", description = "相机") {
        val options = remember {
            listOf(
                ActionSheetItem(label = "照片", value = VisualMediaType.IMAGE),
                ActionSheetItem(label = "视频", value = VisualMediaType.VIDEO),
                ActionSheetItem(label = "照片或视频", value = VisualMediaType.IMAGE_AND_VIDEO)
            )
        }
        var current by remember { mutableIntStateOf(2) }
        val actionSheet = rememberActionSheetState()

        var uri by remember { mutableStateOf<Uri?>(null) }
        var type by remember { mutableStateOf<VisualMediaType?>(null) }
        var visible by remember { mutableStateOf(false) }

        val launchCamera = rememberCameraLauncher { uri1, type1 ->
            uri = uri1
            type = type1
            visible = true
        }

        WeInput(
            label = "媒体类型",
            value = options[current].label,
            onClick = {
                actionSheet.show(options) {
                    current = it
                }
            })

        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "拍摄") {
            launchCamera(options[current].value as VisualMediaType)
        }

        WeUITheme(darkTheme = true, darkStatusBar = true) {
            WePopup(visible, onClose = { visible = false }) {
                uri?.let {
                    if (type == VisualMediaType.IMAGE) {
                        AsyncImage(
                            model = it,
                            contentDescription = null
                        )
                    } else {
                        WeVideoPlayer(
                            state = rememberVideoPlayerState(videoSource = it)
                        )
                    }
                }
            }
        }
    }
}