package top.chengdongqing.weui.feature.network.upload

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.mediapicker.rememberPickMediasLauncher
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState

@Composable
fun FileUploadScreen(uploadViewModel: UploadViewModel = viewModel()) {
    WeScreen(title = "FileUpload", description = "文件上传") {
        var uploading by remember { mutableStateOf(false) }
        var content by remember { mutableStateOf<String?>(null) }

        val toast = rememberToastState()
        val coroutineScope = rememberCoroutineScope()

        val pickMedia = rememberPickMediasLauncher {
            uploading = true
            coroutineScope.launch {
                uploadViewModel.uploadFile(it.first().uri)?.let { res ->
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    content = gson.toJson(res)
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
            pickMedia(VisualMediaType.IMAGE, 1)
        }

        content?.let {
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
                    text = it,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
            }
        }
    }
}