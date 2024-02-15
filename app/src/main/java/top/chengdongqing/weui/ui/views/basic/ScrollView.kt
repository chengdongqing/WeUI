package top.chengdongqing.weui.ui.views.basic

import androidx.compose.runtime.Composable
import kotlinx.coroutines.delay
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.scrollview.WeScrollView

@Composable
fun ScrollViewPage() {
    WePage(title = "ScrollView", description = "可滚动视图") {
        WeScrollView(onRefresh = {
            println("刷新中----------")
            delay(3000)
        }, onReachBottom = {
            println("加载更多-----------")
        }) {
            WePairGroup {
                items(30) {
                    WePairItem(label = "名字", value = "值${it + 1}")
                }
            }
        }
    }
}
