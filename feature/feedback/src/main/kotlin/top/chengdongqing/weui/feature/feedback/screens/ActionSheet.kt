package top.chengdongqing.weui.feature.feedback.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.core.ui.components.actionsheet.rememberActionSheetState
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState
import top.chengdongqing.weui.core.ui.theme.PrimaryColor

@Composable
fun ActionSheetScreen() {
    WeScreen(title = "ActionSheet", description = "弹出式菜单") {
        MakeCall()
        Spacer(modifier = Modifier.height(20.dp))
        RequestPay()
        Spacer(modifier = Modifier.height(20.dp))
        ShareToTimeline()
    }
}

@Composable
private fun RequestPay() {
    val actionSheet = rememberActionSheetState()
    val toast = rememberToastState()
    val options = remember {
        listOf(
            ActionSheetItem("微信", color = PrimaryColor),
            ActionSheetItem("支付宝", color = Color(0xFF00BBEE)),
            ActionSheetItem("QQ钱包", color = Color.Red),
            ActionSheetItem("小米钱包", "禁用", disabled = true)
        )
    }

    WeButton(text = "立即支付", type = ButtonType.DANGER) {
        actionSheet.show(options, "请选择支付方式") {
            toast.show("点击了第${it + 1}个")
        }
    }
}

@Composable
private fun MakeCall() {
    val actionSheet = rememberActionSheetState()
    val toast = rememberToastState()
    val options = remember {
        listOf(
            ActionSheetItem("视频通话", icon = {
                Icon(
                    imageVector = Icons.Filled.Videocam,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }),
            ActionSheetItem("语音通话", icon = {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            })
        )
    }

    WeButton(text = "开始通话", type = ButtonType.PLAIN) {
        actionSheet.show(options) {
            toast.show("开始${options[it].label}")
        }
    }
}

@Composable
private fun ShareToTimeline() {
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {}
    val pickMultipleMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) {}

    val actionSheet = rememberActionSheetState()
    val options = remember {
        listOf(
            ActionSheetItem("拍摄", "照片或视频"),
            ActionSheetItem("从相册选择")
        )
    }

    WeButton(text = "发朋友圈") {
        actionSheet.show(options) {
            when (it) {
                0 -> takePictureLauncher.launch()
                1 -> pickMultipleMediaLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }
        }
    }
}