package top.chengdongqing.weui.ui.views.feedback

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.ActionSheetItem
import top.chengdongqing.weui.ui.components.feedback.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun ActionSheetPage() {
    Page(title = "ActionSheet", description = "弹出式菜单") {
        val actionSheet = rememberWeActionSheet()
        val toast = rememberWeToast()

        Column {
            WeButton(text = "立即支付", type = ButtonType.PLAIN) {
                actionSheet.open(
                    "请选择支付方式",
                    listOf(
                        ActionSheetItem("微信", color = PrimaryColor),
                        ActionSheetItem("支付宝", color = Color(0xFF00BBEE)),
                        ActionSheetItem("QQ钱包", color = Color.Red),
                        ActionSheetItem("小米钱包", "禁用", disabled = true)
                    )
                ) {
                    toast.open("点击了第${it + 1}个")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            WeButton(text = "开始通话", type = ButtonType.PLAIN) {
                val options = listOf(
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

                actionSheet.open(
                    options = options
                ) {
                    toast.open("开始${options[it].label}")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val takePictureLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
            ) {

            }
            val pickMultipleMediaLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia()
            ) {

            }
            WeButton(text = "发朋友圈") {
                val options = listOf(
                    ActionSheetItem("拍摄", "照片或视频"),
                    ActionSheetItem("从相册选择")
                )

                actionSheet.open(
                    options = options
                ) {
                    when (it) {
                        0 -> takePictureLauncher.launch()
                        1 -> pickMultipleMediaLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                    }
                }
            }
        }
    }
}