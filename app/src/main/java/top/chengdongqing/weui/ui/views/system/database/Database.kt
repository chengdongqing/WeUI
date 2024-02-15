package top.chengdongqing.weui.ui.views.system.database

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.views.system.database.address.AddressList

@Composable
fun DatabasePage(navController: NavController) {
    WePage(
        title = "Database",
        description = "数据库（SQLite+Room）",
        backgroundColor = Color.White,
        padding = PaddingValues(0.dp)
    ) {
        AddressList(navController)
    }
}