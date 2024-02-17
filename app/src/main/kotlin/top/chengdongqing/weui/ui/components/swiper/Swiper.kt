package top.chengdongqing.weui.ui.components.swiper

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timerTask

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeSwiper(
    pagerState: PagerState,
    count: Int,
    modifier: Modifier = Modifier,
    autoplay: Boolean = true,
    interval: Long = 3000,
    showDots: Boolean = true,
    content: @Composable (PagerScope.(page: Int) -> Unit)
) {
    if (autoplay) {
        val coroutineScope = rememberCoroutineScope()
        DisposableEffect(pagerState.currentPage) {
            val timer = Timer()

            timer.schedule(timerTask {
                coroutineScope.launch {
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % count)
                }
            }, interval, interval)

            onDispose {
                timer.cancel()
            }
        }
    }

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = modifier,
            pageSpacing = 20.dp,
            pageContent = content
        )

        if (showDots) {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-10).dp)
                    .width((count * 16).dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                val offsetX by animateDpAsState(
                    targetValue = (pagerState.currentPage * 16).dp,
                    label = "OffsetAnimation"
                )
                Box(
                    Modifier
                        .width(16.dp)
                        .height(3.dp)
                        .offset(x = offsetX)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White)
                )
            }
        }
    }
}