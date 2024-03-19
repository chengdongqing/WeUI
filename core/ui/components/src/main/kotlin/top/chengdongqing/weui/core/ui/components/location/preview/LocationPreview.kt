package top.chengdongqing.weui.core.ui.components.location.preview

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.MarkerOptions
import top.chengdongqing.weui.core.ui.components.R
import top.chengdongqing.weui.core.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.core.ui.components.actionsheet.rememberActionSheetState
import top.chengdongqing.weui.core.ui.components.location.AMap
import top.chengdongqing.weui.core.ui.components.location.rememberAMapState
import top.chengdongqing.weui.feature.location.utils.MapType
import top.chengdongqing.weui.feature.location.utils.createBitmapDescriptor
import top.chengdongqing.weui.feature.location.utils.navigateToLocation

@Composable
fun WeLocationPreview(location: LocationPreviewItem) {
    val context = LocalContext.current
    val state = rememberAMapState()
    val map = state.map

    LaunchedEffect(state) {
        // 设置地图视野
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location.latLng, location.zoom))

        // 添加定位标记
        val marker = MarkerOptions().apply {
            position(location.latLng)
            icon(
                top.chengdongqing.weui.feature.location.utils.createBitmapDescriptor(
                    context,
                    R.drawable.ic_location_marker,
                    120,
                    120
                )
            )
        }
        map.addMarker(marker)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AMap(modifier = Modifier.weight(1f), state)
        BottomBar(location)
    }
}

@Composable
private fun BottomBar(location: LocationPreviewItem) {
    val actionSheet = rememberActionSheetState()
    val mapOptions = remember {
        listOf(
            ActionSheetItem("高德地图"),
            ActionSheetItem("百度地图"),
            ActionSheetItem("腾讯地图"),
            ActionSheetItem("谷歌地图"),
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = location.name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            location.address?.let {
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
                        actionSheet.show(mapOptions) { index ->
                            context.navigateToLocation(
                                top.chengdongqing.weui.feature.location.utils.MapType.ofIndex(index)!!,
                                location.latLng,
                                location.name
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Navigation,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "导航", color = MaterialTheme.colorScheme.onSecondary, fontSize = 14.sp)
        }
    }
}