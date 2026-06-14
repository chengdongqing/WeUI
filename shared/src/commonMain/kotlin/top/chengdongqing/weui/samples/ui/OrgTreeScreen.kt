package top.chengdongqing.weui.samples.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.chengdongqing.weui.components.WeOrgTree
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.samples.data.GovernmentDataProvider

@Composable
fun OrgTreeScreen(onBack: () -> Unit) {
    WeScreen(
        title = "OrgTree",
        description = "组织架构树",
        onBack = onBack
    ) {
        Box(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            WeOrgTree(
                dataSource = GovernmentDataProvider.governmentMap,
                isTopLevel = true
            )
        }
    }
}