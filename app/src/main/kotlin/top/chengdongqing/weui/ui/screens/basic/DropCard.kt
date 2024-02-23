package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.dropcard.WeDropCard
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun DropCardScreen() {
    WeScreen(
        title = "DropCard",
        description = "划走式卡片"
    ) {
        val items = remember {
            mutableStateListOf(
                "https://s.xiaopeng.com/xp-fe/mainsite/2023/p7i/m/p17.jpg",
                "https://s.xiaopeng.com/xp-fe/mainsite/2023/g92024/v2/Gu72d34fs245/m/p3-1.jpg",
                "https://s1.xiaomiev.com/activity-outer-assets/web/su7/1-3_m.jpg"
            )
        }
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val cardHeight = (screenHeight * 0.65).dp
        val coroutineScope = rememberCoroutineScope()

        WeDropCard(
            items,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight),
            onSwiped = { _, item ->
                coroutineScope.launch {
                    items.remove(item)
                    delay(200)
                    items.add(item)
                }
            }
        ) { item ->
            AsyncImage(
                model = item,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}