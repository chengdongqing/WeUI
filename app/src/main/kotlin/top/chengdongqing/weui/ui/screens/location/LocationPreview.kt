package top.chengdongqing.weui.ui.screens.location

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amap.api.maps.model.LatLng
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.ui.components.location.picker.LocationItem
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun LocationPreviewScreen(navController: NavController) {
    WeScreen(title = "LocationPreview", description = "查看位置", scrollEnabled = false) {
        val location = remember {
            LocationItem(
                name = "庐山国家级旅游风景名胜区",
                address = "江西省九江市庐山市牯岭镇",
                latLng = LatLng(29.5628, 115.9928)
            )
        }

        WeCardList {
            item {
                WeCardListItem(label = "纬度", value = location.latLng.latitude.toString())
                WeCardListItem(label = "经度", value = location.latLng.longitude.toString())
                WeCardListItem(label = "位置名称", value = location.name)
                WeCardListItem(label = "详细位置", value = location.address!!)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "查看位置") {
            val route = buildString {
                append("location/preview")
                append("/${location.latLng.latitude}/${location.latLng.longitude}")
                append("?zoom=12&name=${location.name}&address=${location.address}")
            }
            navController.navigate(route)
        }
    }
}