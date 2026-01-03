package top.chengdongqing.weui.feature.media.imagecropper

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * 状态管理
 * 负责处理缩放、平移、旋转动画以及边界回弹逻辑等
 */
class CropperState() {
    // 基础变换状态
    val scale = Animatable(1f)
    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)
    val rotation = Animatable(0f)

    // 布局尺寸信息
    var screenSize by mutableStateOf(IntSize.Zero)
    var boxSize by mutableStateOf(IntSize.Zero)
    var imageSize by mutableStateOf(IntSize.Zero)

    /**
     * 重置图片位置和缩放，使其刚好填充裁剪框
     */
    suspend fun reset(animate: Boolean = true) = coroutineScope {
        val img = imageSize.takeIf { it != IntSize.Zero } ?: return@coroutineScope
        if (boxSize == IntSize.Zero) return@coroutineScope

        val minScale = calculateMinScale(img, boxSize, rotation.value)
        val targetX = (screenSize.width - img.width * minScale) / 2f
        val targetY = (screenSize.height - img.height * minScale) / 2f

        if (animate) {
            launch { rotation.animateTo(0f, spring()) }
            launch { scale.animateTo(minScale, spring()) }
            launch { offsetX.animateTo(targetX, spring()) }
            launch { offsetY.animateTo(targetY, spring()) }
        } else {
            rotation.snapTo(0f)
            scale.snapTo(minScale)
            offsetX.snapTo(targetX)
            offsetY.snapTo(targetY)
        }
    }

    /**
     * 处理手势结束后的回弹逻辑
     */
    suspend fun settle() = coroutineScope {
        val img = imageSize.takeIf { it != IntSize.Zero } ?: return@coroutineScope

        val minScale = calculateMinScale(img, boxSize, rotation.value)
        val targetScale = max(scale.value, minScale)

        // 计算修正后的边界坐标
        val bounds = calculateCorrectBounds(
            targetScale, rotation.value, offsetX.value, offsetY.value,
            img, boxSize, screenSize
        )

        val springSpec = spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )

        launch { scale.animateTo(targetScale, springSpec) }
        launch { offsetX.animateTo(bounds.x, springSpec) }
        launch { offsetY.animateTo(bounds.y, springSpec) }
    }

    /**
     * 核心变换逻辑：处理双指缩放和平移
     * 这里计算了“缩放中心补偿”，防止图片在缩放时由于 Offset 原点在左上角而乱跳
     */
    suspend fun applyTransform(pan: Offset, zoom: Float) = coroutineScope {
        val img = imageSize.takeIf { it != IntSize.Zero } ?: return@coroutineScope
        val oldScale = scale.value

        // 1. 计算缩放后的比例（带有一点弹性余量，允许往里缩一点点再回弹）
        val minScale = calculateMinScale(img, boxSize, rotation.value)
        val newScale = (oldScale * zoom).coerceAtLeast(minScale * 0.8f)

        // 2. 缩放中心补偿逻辑：
        // 默认 Canvas 的 scale 是以左上角 (0,0) 为准，
        // 计算出图片中心点在缩放前后的位移差，并补回给 Offset。
        val currentImgCenterX = offsetX.value + (img.width * oldScale) / 2f
        val currentImgCenterY = offsetY.value + (img.height * oldScale) / 2f
        val scaleChange = newScale / oldScale

        val compensatedX = currentImgCenterX - (currentImgCenterX - offsetX.value) * scaleChange
        val compensatedY = currentImgCenterY - (currentImgCenterY - offsetY.value) * scaleChange

        // 3. 计算合法的移动区间（用于阻尼判断）
        val (rangeX, rangeY) = calculateOffsetRange(
            newScale, rotation.value, img, boxSize, screenSize
        )

        // 4. 应用阻尼：如果已经拉到边缘了，手感会变重（只能拉动 20%）
        val dampedPanX = applyDamping(pan.x, offsetX.value, rangeX)
        val dampedPanY = applyDamping(pan.y, offsetY.value, rangeY)

        // 5. 更新状态（用 snapTo 保证手势跟随的实时性）
        launch { scale.snapTo(newScale) }
        launch { offsetX.snapTo(compensatedX + dampedPanX) }
        launch { offsetY.snapTo(compensatedY + dampedPanY) }
    }

    private fun applyDamping(
        delta: Float,
        current: Float,
        range: ClosedFloatingPointRange<Float>
    ): Float {
        if (range.start >= range.endInclusive) return 0f // 轴向锁定

        return when {
            current > range.endInclusive && delta > 0 -> delta * 0.2f
            current < range.start && delta < 0 -> delta * 0.2f
            else -> delta
        }
    }
}

@Composable
fun rememberCropperState() = remember { CropperState() }
