package top.chengdongqing.weui.ui.screens.hardware

import android.hardware.Sensor
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.compass.CompassViewModel
import top.chengdongqing.weui.ui.components.compass.WeCompass
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.FontSecondaryColorLight
import top.chengdongqing.weui.utils.determineAccuracy
import top.chengdongqing.weui.utils.format
import top.chengdongqing.weui.utils.rememberSensorValue

/**
 * 基于磁力计和加速度计实现的原因：
 * 1. 磁力计：这种传感器可以检测地球磁场的强度和方向。由于地球本身是一个巨大的磁体，磁力计可以用来确定手机相对于地球磁北极的方向。这是指南针功能的核心部分，因为它提供了指向地理北极（根据地球磁场）的基础方向信息。
 * 2. 加速度计：虽然磁力计能提供方向信息，但它本身无法确定手机的倾斜状态（即手机是水平放置还是倾斜）。加速度计可以检测手机相对于地心引力的加速度，从而帮助判断手机的倾斜角度或方位。这对于调整指南针读数以适应用户在不同姿势（如站立、坐着或躺着）下使用手机时是非常重要的。
 * 结合这两种传感器，手机能够提供更准确的方向信息。当你移动手机时，磁力计和加速度计的数据会被实时处理，以确保指南针指示的方向尽可能准确，无论设备的放置方式如何。这种组合使得手机上的指南针功能既实用又灵活，能够满足日常生活中对方向判断的需求。
 */
@Composable
fun CompassScreen(compassViewModel: CompassViewModel = viewModel()) {
    WeScreen(title = "Compass", description = "罗盘（指南针），基于磁力计与加速度计") {
        val pressure = rememberSensorValue(Sensor.TYPE_PRESSURE, compassViewModel.observing)

        WeCompass(compassViewModel)
        Spacer(modifier = Modifier.height(20.dp))
        compassViewModel.accuracy?.let {
            Text(
                text = "精度：${determineAccuracy(it)}",
                color = FontSecondaryColorLight,
                fontSize = 10.sp
            )
        }
        pressure?.let {
            Text(
                text = "气压：${pressure.format()}hPa（百帕斯卡）",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        if (!compassViewModel.observing) {
            WeButton(text = "开始监听") {
                compassViewModel.observing = true
            }
        } else {
            WeButton(text = "取消监听", type = ButtonType.PLAIN) {
                compassViewModel.observing = false
            }
        }
    }
}