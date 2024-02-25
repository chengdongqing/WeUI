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
fun <T> WeSwiper(
    options: List<T>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState {
        options.size
    },
    autoplay: Boolean = true,
    interval: Long = 3000,
    indicator: (@Composable BoxScope.(count: Int, current: Int) -> Unit)? = { count, current ->
        WeSwiperDefaults.Indicator(
            count,
            current,
            Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-10).dp)
        )
    },
    content: @Composable (PagerScope.(T) -> Unit)
) {
    if (autoplay) {
        val coroutineScope = rememberCoroutineScope()
        DisposableEffect(pagerState.currentPage) {
            val timer = Timer()
            timer.schedule(timerTask {
                coroutineScope.launch {
                    val targetPage = (pagerState.currentPage + 1) % options.size
                    pagerState.animateScrollToPage(targetPage)
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
            pageSpacing = 20.dp
        ) { index ->
            content(options[index])
        }

        if (indicator != null) {
            indicator(options.size, pagerState.currentPage)
        }
    }
}

object WeSwiperDefaults {
    @Composable
    fun Indicator(count: Int, current: Int, modifier: Modifier = Modifier) {
        Row(
            modifier
                .width((count * 16).dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(0.2f))
        ) {
            val offsetX by animateDpAsState(
                targetValue = (current * 16).dp,
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