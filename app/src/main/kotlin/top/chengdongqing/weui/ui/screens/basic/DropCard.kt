package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.components.dropcard.WeDropCard
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun DropCardScreen() {
    WeScreen(
        title = "DropCard",
        description = "划走式卡片",
        scrollEnabled = false
    ) {
        val items = remember {
            mutableStateListOf(
                "https://s.xiaopeng.com/xp-fe/mainsite/2023/p7i/m/p17.jpg",
                "https://s.xiaopeng.com/xp-fe/mainsite/2023/g6/v1_5/p5-4-1.jpg",
                "https://s1.xiaomiev.com/activity-outer-assets/web/su7/1-3_m.jpg",
                "https://s.xiaopeng.com/xp-fe/mainsite/2023/g92024/v2/Gu72d34fs245/m/p3-1.jpg",
                "https://p.ampmake.com/lilibrary/787536779512770/551b13df-1fe3-43a0-9049-f4cff1de370f.jpg",
                "https://p.ampmake.com/lilibrary/688863524210496/fe2ee1c6-5c94-420a-bee0-7bb040f84f03.jpg"
            )
        }

        WeDropCard(
            items,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            onSwiped = { _, item ->
                items.remove(item)
                items.add(item)
            }
        ) { item ->
            AsyncImage(
                model = item,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black)
            )
        }
    }
}