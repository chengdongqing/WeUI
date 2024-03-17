package top.chengdongqing.weui.ui.screens.location

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.data.model.LocationItem
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.rememberLocationPicker

@Composable
fun LocationPickerScreen() {
    WeScreen(title = "LocationPicker", description = "选择位置", scrollEnabled = false) {
        var location by remember { mutableStateOf<LocationItem?>(null) }

        location?.apply {
            WeCardList {
                item {
                    WeCardListItem(label = "纬度", value = latLng.latitude.toString())
                    WeCardListItem(label = "经度", value = latLng.longitude.toString())
                    WeCardListItem(label = "位置名称", value = name)
                    WeCardListItem(label = "详细位置", value = address ?: "")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        val pickLocation = rememberLocationPicker {
            location = it
        }
        WeButton(text = "选择位置") {
            pickLocation()
        }
    }
}