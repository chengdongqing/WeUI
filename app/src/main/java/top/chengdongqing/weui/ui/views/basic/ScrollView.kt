package top.chengdongqing.weui.ui.views.basic

import androidx.compose.runtime.Composable
import kotlinx.coroutines.delay
import top.chengdongqing.weui.ui.components.basic.KeyValueCard
import top.chengdongqing.weui.ui.components.basic.KeyValueRow
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.basic.WeScrollView

@Composable
fun ScrollViewPage() {
    WePage(title = "ScrollView", description = "可滚动视图") {
        WeScrollView(onRefresh = {
            println("刷新中----------")
            delay(3000)
        }, onReachBottom = {
            println("加载更多-----------")
        }) {
            KeyValueCard {
                items(30) {
                    KeyValueRow(label = "名字", value = "值${it + 1}")
                }
            }
        }
    }
}
