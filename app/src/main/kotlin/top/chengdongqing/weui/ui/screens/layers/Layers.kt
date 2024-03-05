package top.chengdongqing.weui.ui.screens.layers

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.clickableWithoutRipple

@Composable
fun LayersScreen() {
    WeScreen(
        title = "WeUI页面层级",
        description = "用于规范WeUI页面元素所属层级、层级顺序及组合规范。"
    ) {
        val width = LocalConfiguration.current.screenWidthDp.dp / 2
        var expanded by remember { mutableStateOf(false) }
        val transition = updateTransition(targetState = expanded, label = "expanded")

        LaunchedEffect(Unit) {
            delay(500)
            expanded = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableWithoutRipple {
                    expanded = !expanded
                }
                .padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .zIndex(4f)
                    .width(width)
                    .aspectRatio(3 / 5f)
                    .offset(
                        x = transition.animateDp(label = "") {
                            if (it) (-60).dp else 0.dp
                        }.value,
                        y = transition.animateDp(label = "") {
                            if (it) 60.dp else 0.dp
                        }.value
                    )
                    .border(1.dp, Color.hsl(0f, 0f, 0.8f, 0.5f))
            )
            Box(
                modifier = Modifier
                    .zIndex(3f)
                    .width(width)
                    .aspectRatio(3 / 5f)
                    .offset(
                        x = transition.animateDp(label = "") {
                            if (it) (-20).dp else 0.dp
                        }.value,
                        y = transition.animateDp(label = "") {
                            if (it) 20.dp else 0.dp
                        }.value
                    )
                    .background(Color(0f, 0f, 0f, 0.5f))
            )
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .width(width)
                    .aspectRatio(3 / 5f)
                    .offset(
                        x = transition.animateDp(label = "") {
                            if (it) 20.dp else 0.dp
                        }.value,
                        y = transition.animateDp(label = "") {
                            if (it) (-20).dp else 0.dp
                        }.value
                    )
                    .background(Color(40 / 255f, 187 / 255f, 102 / 255f, 0.5f))
            )
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .width(width)
                    .aspectRatio(3 / 5f)
                    .offset(
                        x = transition.animateDp(label = "") {
                            if (it) 60.dp else 0.dp
                        }.value,
                        y = transition.animateDp(label = "") {
                            if (it) (-60).dp else 0.dp
                        }.value
                    )
                    .background(Color.White)
            )
        }
    }
}