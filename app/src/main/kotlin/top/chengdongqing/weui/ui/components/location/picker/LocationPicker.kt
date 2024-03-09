package top.chengdongqing.weui.ui.components.location.picker

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.model.MarkerOptions
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.location.WeAMap
import top.chengdongqing.weui.utils.buildBitmapDescriptor
import top.chengdongqing.weui.utils.clickableWithoutRipple

@Composable
fun WeLocationPicker(
    pickerViewModel: LocationPickerViewModel = viewModel(),
    onCancel: () -> Unit,
    onConfirm: (LocationItem) -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            WeAMap { map ->
                pickerViewModel.apply {
                    this.map = map
                    initializeMyLocation(map)
                    setCenterChangeListener(map, context)
                    setMapClickListener(map)
                }
            }
            TopBar(isEmpty = pickerViewModel.selectedLocation == null, onCancel) {
                onConfirm(pickerViewModel.selectedLocation!!)
            }
            LocationMarker(pickerViewModel)
        }
        LocationPickerBottom(pickerViewModel)
    }
}

@Composable
private fun BoxScope.LocationMarker(pickerViewModel: LocationPickerViewModel) {
    if (!pickerViewModel.isSearchMode || pickerViewModel.selectedLocation == null) {
        val offsetY = remember { Animatable(0f) }
        val animationSpec = remember { TweenSpec<Float>(durationMillis = 300) }
        LaunchedEffect(pickerViewModel.center) {
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
        DisposableEffect(pickerViewModel.selectedLocation) {
            val markerOptions = MarkerOptions().apply {
                position(pickerViewModel.selectedLocation?.latLng)
                icon(
                    buildBitmapDescriptor(
                        context,
                        R.drawable.ic_location_marker,
                        160,
                        160
                    )
                )
            }
            val marker = pickerViewModel.map?.addMarker(markerOptions)
            onDispose {
                marker?.remove()
            }
        }
    }
}

@Composable
private fun TopBar(isEmpty: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit) {
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
            disabled = isEmpty
        ) {
            onConfirm()
        }
    }
}