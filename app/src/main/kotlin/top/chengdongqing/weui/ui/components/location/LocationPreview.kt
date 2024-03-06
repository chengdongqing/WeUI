package top.chengdongqing.weui.ui.components.location

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
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
import top.chengdongqing.weui.ui.components.actionsheet.ActionSheetOptions
import top.chengdongqing.weui.ui.components.actionsheet.rememberWeActionSheet
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeLocationPreview(
    location: LatLng,
    zoom: Float = 10f,
    name: String = "位置",
    address: String? = null
) {
    val context = LocalContext.current
    var map by remember { mutableStateOf<AMap?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            WeAMap {
                map = it
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))

                val marker = MarkerOptions().apply {
                    position(location)
                    icon(
                        bitmapDescriptorFromResource(
                            context,
                            R.drawable.ic_location_marker,
                            120,
                            120
                        )
                    )
                }
                it.addMarker(marker)
            }
            LocationControl(map)
        }
        BottomBar(location, name, address)
    }
}

@Composable
private fun BottomBar(location: LatLng, name: String, address: String?) {
    val actionSheet = rememberWeActionSheet()

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
                        actionSheet.show(ActionSheetOptions(options = listOf(ActionSheetItem("高德地图"))) {
                            navigationToLocation(context, location, name)
                        })
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

private fun navigationToLocation(context: Context, location: LatLng, name: String) {
    val uri =
        Uri.parse("amapuri://route/plan?dlat=${location.latitude}&dlon=${location.longitude}&dname=$name")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast
            .makeText(context, "未安装高德地图", Toast.LENGTH_SHORT)
            .show()
    }
}

@Composable
private fun BoxScope.LocationControl(map: AMap?) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .offset(x = 12.dp, y = (-36).dp)
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable {
                map?.let {
                    it.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                it.myLocation.latitude,
                                it.myLocation.longitude
                            ),
                            16f
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.MyLocation,
            contentDescription = "当前位置",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(26.dp)
        )
    }
}