package top.chengdongqing.weui.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.onTap

@Composable
fun WeTabView(
    options: List<String>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState {
        options.size
    },
    containerColor: Color = WeTheme.colorScheme.surface,
    content: @Composable PagerScope.(Int) -> Unit
) {
    Column {
        TabBar(pagerState, options, containerColor)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) { index ->
            content(index)
        }
    }
}

@Composable
private fun TabBar(pagerState: PagerState, options: List<String>, containerColor: Color) {
    val coroutineScope = rememberCoroutineScope()

    SecondaryScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = containerColor,
        edgePadding = 0.dp,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    selectedTabIndex = pagerState.currentPage
                ),
                height = 2.5.dp,
                color = WeTheme.colorScheme.primary
            )
        },
        divider = {}
    ) {
        options.forEachIndexed { index, item ->
            val selected = index == pagerState.currentPage

            Text(
                text = item,
                color = if (selected) {
                    WeTheme.colorScheme.primary
                } else {
                    WeTheme.colorScheme.textSecondary
                },
                fontSize = 15.sp,
                modifier = Modifier
                    .onTap {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                    .padding(horizontal = 26.dp, vertical = 14.dp)
            )
        }
    }
}