package top.chengdongqing.weui.core.ui.components.refreshview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Stable
interface LoadMoreState {
    /**
     * 是否在加载更多
     */
    val isLoadingMore: Boolean

    /**
     * 滚动协调器
     */
    val nestedScrollConnection: NestedScrollConnection
}

@Composable
fun rememberLoadMoreState(onReachBottom: suspend () -> Unit): LoadMoreState {
    val coroutineScope = rememberCoroutineScope()

    return remember {
        LoadMoreStateImpl(onReachBottom, coroutineScope)
    }
}

private class LoadMoreStateImpl(
    onReachBottom: suspend () -> Unit,
    coroutineScope: CoroutineScope
) : LoadMoreState {
    override var isLoadingMore by mutableStateOf(false)
    override val nestedScrollConnection = object : NestedScrollConnection {
        override suspend fun onPostFling(
            consumed: Velocity,
            available: Velocity
        ): Velocity {
            if (available.y < 0) {
                coroutineScope.launch {
                    isLoadingMore = true
                    delay(200)
                    onReachBottom()
                    isLoadingMore = false
                }
                return available
            } else {
                return Velocity.Zero
            }
        }
    }
}