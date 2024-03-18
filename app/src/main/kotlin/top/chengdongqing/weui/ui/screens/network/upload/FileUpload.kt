package top.chengdongqing.weui.ui.screens.network.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.chengdongqing.weui.enums.MediaType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState
import top.chengdongqing.weui.utils.rememberPickMediasLauncher

@Composable
fun FileUploadScreen(uploadViewModel: UploadViewModel = viewModel()) {
    WeScreen(title = "FileUpload", description = "文件上传") {
        var uploading by remember { mutableStateOf(false) }
        var imageInfo by remember { mutableStateOf<UploadResponse.Files.FileItem?>(null) }

        val toast = rememberToastState()
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val pickMedia = rememberPickMediasLauncher {
            coroutineScope.launch {
                uploading = true
                uploadViewModel.uploadFile(context, it.first().uri)?.let {
                    imageInfo = it
                    toast.show("上传成功", ToastIcon.SUCCESS)
                } ?: toast.show("上传失败", ToastIcon.FAIL)
                uploading = false
            }
        }

        WeButton(
            text = if (uploading) "上传中..." else "上传图片",
            loading = uploading,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            pickMedia(MediaType.IMAGE, 1)
        }

        imageInfo?.let { image ->
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(6.dp)
                    )
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
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
            }
        }
    }
}