package top.chengdongqing.weui.ui.screens.demo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.calculateBezierPath
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.toIntOffset

@Composable
fun CubicBezierScreen() {
    var visible by remember { mutableStateOf(false) }
    var startOffset by remember { mutableStateOf(Offset.Zero) }
    var endOffset by remember { mutableStateOf(Offset.Zero) }
    var endOffsetLeft by remember { mutableStateOf(Offset.Zero) }
    var endOffsetRight by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier.fillMaxSize()) {
        WeScreen(
            title = "CubicBezier",
            description = "贝塞尔曲线",
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            repeat(3) {
                ProductList(size = 2) {
                    startOffset = it
                    endOffset = endOffsetLeft
                    visible = true
                }
                ProductList(size = 2, startFromRight = true) {
                    startOffset = it
                    endOffset = endOffsetRight
                    visible = true
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }

        if (visible) {
            AnimateToCart(
                startOffset,
                endOffset,
                onFinish = { visible = false }
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }

        ShoppingBag(Alignment.BottomStart) {
            endOffsetLeft = it
        }
        ShoppingBag(Alignment.BottomEnd) {
            endOffsetRight = it
        }
    }
}

@Composable
private fun ProductList(size: Int, startFromRight: Boolean = false, onAddToCart: (Offset) -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides if (startFromRight) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onBackground,
                    RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            repeat(size) { index ->
                ProductItem(onAddToCart)
                if (index < size - 1) {
                    WeDivider(modifier = Modifier.padding(vertical = 12.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductItem(onAddToCart: (Offset) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = "https://img2.baidu.com/it/u=2453063091,239533720&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=631",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.height(60.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "珍珠奶茶",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 17.sp
            )
            Text(
                text = "¥29.00",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        var position by remember { mutableStateOf(Offset.Zero) }
        Icon(
            imageVector = Icons.Outlined.AddCircleOutline,
            contentDescription = "加入购物车",
            tint = PrimaryColor,
            modifier = Modifier
                .size(28.dp)
                .onGloballyPositioned {
                    position = it.positionInRoot()
                }
                .clickableWithoutRipple {
                    onAddToCart(position)
                }
        )
    }
}

@Composable
private fun BoxScope.ShoppingBag(align: Alignment, onPositioned: (Offset) -> Unit) {
    Box(
        modifier = Modifier
            .offset(y = (-LocalConfiguration.current.screenHeightDp * 0.3).dp)
            .padding(horizontal = 16.dp)
            .size(46.dp)
            .align(align)
            .onGloballyPositioned {
                onPositioned(it.positionInRoot())
            }
            .shadow(16.dp, CircleShape, spotColor = MaterialTheme.colorScheme.outline)
            .background(MaterialTheme.colorScheme.onBackground, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingBag,
            contentDescription = "购物车",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun AnimateToCart(
    startOffset: Offset,
    endOffset: Offset,
    onFinish: () -> Unit,
    content: @Composable () -> Unit
) {
    val animationProgress = remember { Animatable(0f) }
    val currentOffset by remember {
        derivedStateOf {
            val path = calculateBezierPath(startOffset, endOffset, animationProgress.value)
            path.toIntOffset()
        }
    }

    LaunchedEffect(startOffset, endOffset) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        onFinish()
    }

    Box(
        modifier = Modifier
            .offset { currentOffset }
            .graphicsLayer {
                alpha = 1f - animationProgress.value
            }
    ) {
        content()
    }
}

@Preview
@Composable
fun PreviewCubicBezier() {
    WeUITheme {
        CubicBezierScreen()
    }
}