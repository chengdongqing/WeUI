package top.chengdongqing.weui.ui.screens.location

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.ui.components.location.picker.LocationItem
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun LocationPickerScreen(
    navController: NavHostController,
    locationState: State<LocationItem?>?
) {
    WeScreen(title = "LocationPicker", description = "选择位置", scrollEnabled = false) {
        locationState?.value?.let { location ->
            WeCardList {
                item {
                    WeCardListItem(label = "纬度", value = location.latLng.latitude.toString())
                    WeCardListItem(label = "经度", value = location.latLng.longitude.toString())
                    WeCardListItem(label = "位置名称", value = location.name)
                    WeCardListItem(label = "详细位置", value = location.address ?: "")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "清空位置", type = ButtonType.PLAIN) {
                navController.currentBackStackEntry?.savedStateHandle?.set("location", null)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        WeButton(text = "选择位置") {
            navController.navigate("location/pick")
        }
    }
}