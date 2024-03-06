package top.chengdongqing.weui.ui.screens.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.amap.api.maps.model.LatLng
import top.chengdongqing.weui.ui.components.location.WeLocationPreview
import top.chengdongqing.weui.ui.theme.WeUITheme

@Composable
fun LocationPreviewScreen() {
    val location = remember { LatLng(29.5628, 115.9928) }

    WeLocationPreview(
        location,
        zoom = 12f,
        name = "庐山国家级旅游风景名胜区",
        address = "江西省九江市庐山市牯岭镇"
    )
}

@Preview
@Composable
private fun PreviewLocationPreview() {
    WeUITheme {
        LocationPreviewScreen()
    }
}