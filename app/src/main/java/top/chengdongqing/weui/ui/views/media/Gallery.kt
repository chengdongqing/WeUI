package top.chengdongqing.weui.ui.views.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.WeGallery

@Composable
fun GalleryPage() {
    Page(title = "Gallery", description = "相册", paddingValues = PaddingValues(0.dp)) {
        WeGallery()
    }
}