package top.chengdongqing.weui.feature.system.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.system.address.AddressList

@Composable
fun DatabaseScreen(onNavigateToAddressForm: (id: Int?) -> Unit) {
    WeScreen(
        title = "Database",
        description = "数据库（SQLite+Room）",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        AddressList(onNavigateToAddressForm)
    }
}