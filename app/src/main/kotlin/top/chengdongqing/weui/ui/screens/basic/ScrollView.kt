package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.screen.WeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollViewScreen() {
    WeScreen(title = "ScrollView", description = "可滚动视图", scrollEnabled = false) {
        val refreshState = rememberPullToRefreshState()

        LazyColumn(
            modifier = Modifier
                .nestedScroll(refreshState.nestedScrollConnection)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp)
        ) {
            items(30) {
                WePairItem(label = "名字", value = "值${it + 1}")
            }
        }
        PullToRefreshContainer(state = refreshState)
    }
}
