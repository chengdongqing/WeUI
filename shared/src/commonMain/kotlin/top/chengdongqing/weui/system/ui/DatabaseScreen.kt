package top.chengdongqing.weui.system.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.system.address.AddressList

@Composable
fun DatabaseScreen(
    onNavigateToAddressForm: (id: Int?) -> Unit,
    onBack: () -> Unit
) {
    WeScreen(
        title = "Database",
        description = "数据库（SQLite+Room）",
        padding = PaddingValues(0.dp),
        scrollEnabled = false,
        onBack = onBack
    ) {
        AddressList(onNavigateToAddressForm)
    }
}