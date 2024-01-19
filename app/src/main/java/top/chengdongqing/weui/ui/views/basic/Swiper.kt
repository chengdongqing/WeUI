package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.Swiper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwiperPage() {
    Page(title = "Swiper", description = "滑动视图") {
        val banners = remember {
            listOf(
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/0b2bb13c396cc6205dd91da3a91a275a.jpg?f=webp&w=1080&h=540&bg=A8D4D5",
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/1ab1ffaaeb5ca4c02674e9f35b1fd17c.jpg?f=webp&w=1080&h=540&bg=59A5FD",
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/37bd342303515c7a1a54681599e319a1.jpg?f=webp&w=1080&h=540&bg=56B6A8",
                "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/a2b3ab270e5ae4c9e85d6607cdb97008.jpg?f=webp&w=1080&h=540&bg=DC85AF"
            )
        }

        val pagerState = rememberPagerState {
            banners.size
        }
        Swiper(
            pagerState,
            count = pagerState.pageCount,
            modifier = Modifier.clip(RoundedCornerShape(6.dp))
        ) {
            AsyncImage(
                model = banners[it],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 1f)
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
}