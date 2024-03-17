package top.chengdongqing.weui.ui.components.location.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.ui.components.actionsheet.rememberActionSheetState
import top.chengdongqing.weui.ui.components.location.AMap
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.MapType
import top.chengdongqing.weui.utils.createBitmapDescriptor
import top.chengdongqing.weui.utils.navigateToLocation

@Composable
fun WeLocationPreview(
    latitude: Double,
    longitude: Double,
    zoom: Float = 16f,
    name: String = "位置",
    address: String? = null
) {
    val context = LocalContext.current
    var map by remember { mutableStateOf<AMap?>(null) }
    val latLng = remember(latitude, longitude) { LatLng(latitude, longitude) }

    Column(modifier = Modifier.fillMaxSize()) {
        AMap(Modifier.weight(1f)) {
            map = it
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

            val marker = MarkerOptions().apply {
                position(latLng)
                icon(
                    createBitmapDescriptor(
                        context,
                        R.drawable.ic_location_marker,
                        120,
                        120
                    )
                )
            }
            it.addMarker(marker)
        }
        BottomBar(latLng, name, address)
    }
}

@Composable
private fun BottomBar(location: LatLng, name: String, address: String?) {
    val actionSheet = rememberActionSheetState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            address?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        actionSheet.show(
                            options = listOf(
                                ActionSheetItem("高德地图"),
                                ActionSheetItem("百度地图"),
                                ActionSheetItem("腾讯地图"),
                                ActionSheetItem("谷歌地图"),
                            )
                        ) { index ->
                            context.navigateToLocation(
                                MapType.ofIndex(index)!!,
                                location,
                                name
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Navigation,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "导航", color = MaterialTheme.colorScheme.onSecondary, fontSize = 14.sp)
        }
    }
}