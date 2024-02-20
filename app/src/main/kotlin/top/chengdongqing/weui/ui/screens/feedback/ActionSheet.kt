package top.chengdongqing.weui.ui.screens.feedback

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.ui.components.actionsheet.WeActionSheetOptions
import top.chengdongqing.weui.ui.components.actionsheet.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun ActionSheetScreen() {
    WeScreen(title = "ActionSheet", description = "弹出式菜单") {
        Column {
            RequestPay()
            Spacer(modifier = Modifier.height(20.dp))
            MakeCall()
            Spacer(modifier = Modifier.height(20.dp))
            ShareToTimeline()
        }
    }
}

@Composable
private fun RequestPay() {
    val actionSheet = rememberWeActionSheet()
    val toast = rememberWeToast()
    val options = remember {
        listOf(
            ActionSheetItem("微信", color = PrimaryColor),
            ActionSheetItem("支付宝", color = Color(0xFF00BBEE)),
            ActionSheetItem("QQ钱包", color = Color.Red),
            ActionSheetItem("小米钱包", "禁用", disabled = true)
        )
    }

    WeButton(text = "立即支付", type = ButtonType.PLAIN) {
        actionSheet.show(WeActionSheetOptions(options, "请选择支付方式") {
            toast.show(WeToastOptions("点击了第${it + 1}个"))
        })
    }
}

@Composable
private fun MakeCall() {
    val actionSheet = rememberWeActionSheet()
    val toast = rememberWeToast()
    val options = remember {
        listOf(
            ActionSheetItem("视频通话", icon = {
                Icon(
                    imageVector = Icons.Filled.Videocam,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }),
            ActionSheetItem("语音通话", icon = {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            })
        )
    }

    WeButton(text = "开始通话", type = ButtonType.PLAIN) {
        actionSheet.show(WeActionSheetOptions(options) {
            toast.show(WeToastOptions("开始${options[it].label}"))
        })
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ShareToTimeline() {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {}
    val pickMultipleMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) {}

    val actionSheet = rememberWeActionSheet()
    val options = remember {
        listOf(
            ActionSheetItem("拍摄", "照片或视频"),
            ActionSheetItem("从相册选择")
        )
    }

    WeButton(text = "发朋友圈") {
        actionSheet.show(WeActionSheetOptions(options) {
            when (it) {
                0 -> {
                    if (permissionState.status.isGranted) {
                        takePictureLauncher.launch()
                    } else {
                        permissionState.launchPermissionRequest()
                    }
                }

                1 -> pickMultipleMediaLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }
        })
    }
}