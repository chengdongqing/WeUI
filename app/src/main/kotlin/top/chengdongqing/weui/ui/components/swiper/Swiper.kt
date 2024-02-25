package top.chengdongqing.weui.ui.components.swiper

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WeSwiper(
    options: List<T>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState {
        options.size
    },
    autoplay: Boolean = true,
    interval: Long = 3000,
    showDots: Boolean = true,
    content: @Composable (PagerScope.(T) -> Unit)
) {
    LaunchedEffect(autoplay) {
        while (autoplay) {
            yield() // 在长时间运行的协程中，使用 yield() 可以确保协程不会长时间占用线程，从而提高程序的性能和响应性
            delay(interval)
            val targetPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(targetPage)
        }
    }

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = modifier,
            pageSpacing = 20.dp
        ) { index ->
            content(options[index])
        }

        if (showDots) {
            Indicator(options.size, pagerState.currentPage)
        }
    }
}

@Composable
private fun BoxScope.Indicator(count: Int, index: Int) {
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
            targetValue = (index * 16).dp,
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