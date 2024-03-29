package top.chengdongqing.weui.feature.location.picker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.maps.model.MarkerOptions
import top.chengdongqing.weui.core.ui.components.R
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.feature.location.AMap
import top.chengdongqing.weui.feature.location.data.model.LocationItem
import top.chengdongqing.weui.feature.location.rememberAMapState
import top.chengdongqing.weui.feature.location.utils.createBitmapDescriptor

@Composable
fun WeLocationPicker(
    onCancel: () -> Unit,
    onConfirm: (LocationItem) -> Unit
) {
    val mapState = rememberAMapState()
    val state = rememberLocationPickerState(mapState.map)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            AMap(state = mapState)
            TopBar(
                hasSelected = state.selectedLocation != null,
                onCancel
            ) {
                onConfirm(state.selectedLocation!!)
            }
            LocationMarker(state)
        }
        BottomPanel(state)
    }
}

@Composable
private fun BoxScope.LocationMarker(state: LocationPickerState) {
    if (!state.isSearchMode || state.selectedLocation == null) {
        val offsetY = remember { Animatable(0f) }
        val animationSpec = remember { TweenSpec<Float>(durationMillis = 300) }
        LaunchedEffect(state.mapCenter) {
            offsetY.animateTo(-10f, animationSpec)
            offsetY.animateTo(0f, animationSpec)
        }
        Image(
            painter = painterResource(id = R.drawable.ic_location_marker),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp)
                .offset(y = (-25).dp + offsetY.value.dp)
        )
    } else {
        val context = LocalContext.current
        DisposableEffect(state.selectedLocation) {
            val markerOptions = MarkerOptions().apply {
                position(state.selectedLocation?.latLng)
                icon(
                    createBitmapDescriptor(
                        context,
                        R.drawable.ic_location_marker,
                        160,
                        160
                    )
                )
            }
            val marker = state.map.addMarker(markerOptions)
            onDispose {
                marker?.remove()
            }
        }
    }
}

@Composable
private fun TopBar(hasSelected: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 100.dp)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "取消",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.clickableWithoutRipple {
                onCancel()
            }
        )
        WeButton(
            text = "确定",
            size = ButtonSize.SMALL,
            disabled = !hasSelected
        ) {
            onConfirm()
        }
    }
}