package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.swiper.WeSwiper

@Composable
fun SwiperScreen() {
    WeScreen(title = "Swiper", description = "滑动视图") {
        val banners = remember {
            listOf(
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/0b2bb13c396cc6205dd91da3a91a275a.jpg?f=webp&w=1080&h=540&bg=A8D4D5",
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/1ab1ffaaeb5ca4c02674e9f35b1fd17c.jpg?f=webp&w=1080&h=540&bg=59A5FD",
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/37bd342303515c7a1a54681599e319a1.jpg?f=webp&w=1080&h=540&bg=56B6A8",
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/a2b3ab270e5ae4c9e85d6607cdb97008.jpg?f=webp&w=1080&h=540&bg=DC85AF"
            )
        }
        val state = rememberPagerState { banners.size }
        var isCardMode by remember { mutableStateOf(false) }

        WeSwiper(
            state = state,
            options = banners,
            modifier = Modifier.clip(RoundedCornerShape(6.dp)),
            contentPadding = if (isCardMode) PaddingValues(horizontal = 50.dp) else PaddingValues(0.dp),
            pageSpacing = if (isCardMode) 0.dp else 20.dp
        ) { index, item ->
            val animatedScale by animateFloatAsState(
                targetValue = if (index == state.currentPage || !isCardMode) 1f else 0.85f,
                label = ""
            )

            AsyncImage(
                model = item,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 1f)
                    .scale(animatedScale)
                    .clip(RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        WeButton(text = "切换样式", type = ButtonType.PLAIN) {
            isCardMode = !isCardMode
        }
    }
}