package top.chengdongqing.weui.layers

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.clickableWithoutRipple

@Composable
fun LayersScreen() {
    WeScreen(
        title = "WeUI页面层级",
        description = "用于规范WeUI页面元素所属层级、层级顺序及组合规范。"
    ) {
        val width = LocalWindowInfo.current.containerDpSize.width / 2
        var expanded by remember { mutableStateOf(false) }

        val progress by animateFloatAsState(
            targetValue = if (expanded) 1f else 0f,
            animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
            label = "layers"
        )

        LaunchedEffect(Unit) {
            delay(500)
            expanded = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableWithoutRipple { expanded = !expanded }
                .padding(top = 150.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            layerItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .zIndex(item.zIndex)
                        .width(width)
                        .aspectRatio(3 / 5f)
                        .drawWithContent {
                            drawIntoCanvas { canvas ->
                                // 设置原点为中心
                                canvas.translate(size.width / 2f, size.height / 2f)
                                // 矩阵变换
                                val matrix = Matrix()
                                matrix.rotateX(45f * progress)
                                matrix.rotateZ(30f * progress)
                                matrix.translate(z = item.translateZ.toPx() * progress)
                                canvas.concat(matrix)
                                // 恢复原点，以便绘制内容
                                canvas.translate(-size.width / 2f, -size.height / 2f)

                                drawContent()
                            }
                        }
                        .then(
                            if (item.hasBorder) {
                                Modifier.border(1.dp, Color.hsl(0f, 0f, 0.8f, 0.5f))
                            } else {
                                Modifier.background(item.color)
                            }
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // 标题（展开后显示）
                    Text(
                        text = item.title,
                        color = if (item.title == "Content") Color.Black else Color.White,
                        modifier = Modifier
                            .offset(y = (-10).dp)
                            .alpha(progress)
                    )
                }
            }
        }
    }
}

private val layerItems = listOf(
    LayerItem(4f, 100.dp, Color.White, "Popup", hasBorder = true),
    LayerItem(3f, 40.dp, Color(0, 0, 0, 128), "Mask"),
    LayerItem(2f, (-20).dp, Color(40, 187, 102, 128), "Navigation"),
    LayerItem(1f, (-80).dp, Color.White, "Content")
)

private data class LayerItem(
    val zIndex: Float,
    val translateZ: Dp,
    val color: Color,
    val title: String,
    val hasBorder: Boolean = false
)
