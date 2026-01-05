package top.chengdongqing.weui.feature.media.imagecropper

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.max

/**
 * 计算最小缩放比例：确保图片缩放后能覆盖整个裁剪框
 */
internal fun calculateMinScale(img: IntSize, box: IntSize, rotation: Float): Float {
    val isRotated = (rotation / 90f).toInt() % 2 != 0
    val w = if (isRotated) img.height else img.width
    val h = if (isRotated) img.width else img.height
    return max(box.width / w.toFloat(), box.height / h.toFloat())
}

/**
 * 边界计算
 */
internal fun calculateCorrectBounds(
    scale: Float,
    rotation: Float,
    curX: Float,
    curY: Float,
    img: IntSize,
    box: IntSize,
    screen: IntSize
): Offset {
    val isRotated = (rotation / 90f).toInt() % 2 != 0
    val visualW = if (isRotated) img.height * scale else img.width * scale
    val visualH = if (isRotated) img.width * scale else img.height * scale

    // 计算视觉左上角
    val visualLeft = (curX + (img.width * scale) / 2f) - visualW / 2f
    val visualTop = (curY + (img.height * scale) / 2f) - visualH / 2f

    val boxLeft = (screen.width - box.width) / 2f
    val boxTop = (screen.height - box.height) / 2f

    var diffX = 0f
    var diffY = 0f

    // 如果左边露底了，往右挪；如果右边露底了，往左挪
    if (visualLeft > boxLeft) diffX = boxLeft - visualLeft
    else if (visualLeft + visualW < boxLeft + box.width) diffX =
        (boxLeft + box.width) - (visualLeft + visualW)

    // 纵向同理
    if (visualTop > boxTop) diffY = boxTop - visualTop
    else if (visualTop + visualH < boxTop + box.height) diffY =
        (boxTop + box.height) - (visualTop + visualH)

    return Offset(curX + diffX, curY + diffY)
}

/**
 * 计算当前状态下，Offset 的允许波动区间
 * 如果区间 start == end，说明图片比裁剪框小（或刚好），此时该轴向会被锁定在中心
 */
internal fun calculateOffsetRange(
    scale: Float,
    rotation: Float,
    img: IntSize,
    box: IntSize,
    screen: IntSize
): Pair<ClosedFloatingPointRange<Float>, ClosedFloatingPointRange<Float>> {
    val isRotated = (rotation / 90f).toInt() % 2 != 0
    val visualW = if (isRotated) img.height * scale else img.width * scale
    val visualH = if (isRotated) img.width * scale else img.height * scale

    val boxLeft = (screen.width - box.width) / 2f
    val boxTop = (screen.height - box.height) / 2f

    // 补偿 Canvas 变换时的原点偏移
    val renderOffsetX = (img.width * scale - visualW) / 2f
    val renderOffsetY = (img.height * scale - visualH) / 2f

    val rangeX = if (visualW > box.width) {
        (boxLeft + box.width - visualW - renderOffsetX)..(boxLeft - renderOffsetX)
    } else {
        val centerX = boxLeft + (box.width - visualW) / 2f - renderOffsetX
        centerX..centerX
    }

    val rangeY = if (visualH > box.height) {
        (boxTop + box.height - visualH - renderOffsetY)..(boxTop - renderOffsetY)
    } else {
        val centerY = boxTop + (box.height - visualH) / 2f - renderOffsetY
        centerY..centerY
    }

    return rangeX to rangeY
}

/**
 * 将当前屏幕上的视觉变换应用到原始 Bitmap 上，裁剪出目标区域
 */
internal fun Bitmap.crop(state: CropperState): Bitmap {
    val result = createBitmap(state.boxSize.width, state.boxSize.height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(result)

    val boxLeft = (state.screenSize.width - state.boxSize.width) / 2f
    val boxTop = (state.screenSize.height - state.boxSize.height) / 2f

    val matrix = android.graphics.Matrix().apply {
        // 缩放
        postScale(state.scale.value, state.scale.value)
        // 绕图片中心旋转
        postRotate(
            state.rotation.value,
            (width * state.scale.value) / 2f,
            (height * state.scale.value) / 2f
        )
        // 平移
        postTranslate(state.offsetX.value - boxLeft, state.offsetY.value - boxTop)
    }

    val paint =
        android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG or android.graphics.Paint.FILTER_BITMAP_FLAG)
    canvas.drawBitmap(this, matrix, paint)
    return result
}
