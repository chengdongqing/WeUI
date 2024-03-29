package top.chengdongqing.weui.feature.location.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amap.api.maps.model.LatLng
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.cardlist.cartList
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.location.data.model.LocationPreviewItem
import top.chengdongqing.weui.feature.location.preview.previewLocation

@Composable
fun LocationPreviewScreen() {
    WeScreen(title = "LocationPreview", description = "查看位置", scrollEnabled = false) {
        val context = LocalContext.current
        val location = remember {
            LocationPreviewItem(
                name = "庐山国家级旅游风景名胜区",
                address = "江西省九江市庐山市牯岭镇",
                latLng = LatLng(29.5628, 115.9928),
                zoom = 12f
            )
        }

        LazyColumn(modifier = Modifier.cartList()) {
            item {
                location.apply {
                    WeCardListItem(label = "纬度", value = latLng.latitude.toString())
                    WeCardListItem(label = "经度", value = latLng.longitude.toString())
                    WeCardListItem(label = "位置名称", value = name)
                    WeCardListItem(label = "详细位置", value = address)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "查看位置") {
            context.previewLocation(location)
        }
    }
}