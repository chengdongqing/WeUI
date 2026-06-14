package top.chengdongqing.weui.util

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun saveBitmap(bitmap: ImageBitmap, filename: String): Boolean