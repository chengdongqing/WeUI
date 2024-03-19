package top.chengdongqing.weui.feature.demos.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.isActive
import top.chengdongqing.weui.core.utils.toIntOffset
import top.chengdongqing.weui.core.utils.toIntSize

@Composable
fun WePanoramicImage(image: ImageBitmap, scrollStep: Float = 0.75f) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var x by remember { mutableFloatStateOf(0f) }
    val scale = remember(size, image) {
        size.height / image.height.toFloat()
    }
    val imgW = image.width * scale
    val imgH = image.height * scale

    // 设置图片初始位置
    LaunchedEffect(size) {
        x = size.width.toFloat()
    }
    // 图片循环滚动
    LaunchedEffect(imgW, scrollStep) {
        while (isActive) {
            withFrameNanos {
                x += scrollStep
                // 完全滚动完后重置位置
                if (x >= imgW) x = 0f
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 1f)
            .clipToBounds()
            .onSizeChanged {
                size = it
            }
    ) {
        // 默认图片
        drawImage(
            image = image,
            dstOffset = Offset(x, 0f).toIntOffset(),
            dstSize = Size(imgW, imgH).toIntSize()
        )

        // 辅助图片，当图片宽度不足以填满容器宽度时用于补充
        if (x + imgW > size.width) {
            drawImage(
                image = image,
                dstOffset = Offset(x - imgW, 0f).toIntOffset(),
                dstSize = Size(imgW, imgH).toIntSize()
            )
        }
    }
}