package top.chengdongqing.weui.ui.screens.network.upload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast

@Composable
fun FileUploadScreen(uploadViewModel: UploadViewModel = viewModel()) {
    WeScreen(title = "FileUpload", description = "文件上传") {
        var uploading by remember { mutableStateOf(false) }
        var imageInfo by remember { mutableStateOf<UploadResponse.Files.FileItem?>(null) }

        val toast = rememberWeToast()
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val pickMediaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                coroutineScope.launch {
                    uploading = true
                    uploadViewModel.uploadFile(context, uri)?.let {
                        imageInfo = it
                        toast.show(WeToastOptions("上传成功", ToastIcon.SUCCESS))
                    } ?: toast.show(WeToastOptions("上传失败", ToastIcon.FAIL))
                    uploading = false
                }
            }
        }

        Column {
            WeButton(
                text = if (uploading) "上传中..." else "上传图片",
                loading = uploading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            imageInfo?.let { image ->
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildString {
                            appendLine("name: ${image.name}")
                            appendLine("size: ${image.size}")
                            appendLine("type: ${image.type}")
                            append("url: ${image.url}")
                        },
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}