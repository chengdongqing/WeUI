package top.chengdongqing.weui.ui.screens.demo.orgtree

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import top.chengdongqing.weui.ui.components.orgtree.WeOrgTree
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme

@Composable
fun OrgTreeScreen() {
    WeScreen(title = "OrgTree", description = "组织架构树") {
        Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            WeOrgTree(GovernmentDataProvider.governmentMap, true)
        }
    }
}

@Preview
@Composable
private fun PreviewOrgTree() {
    WeUITheme {
        OrgTreeScreen()
    }
}