package top.chengdongqing.weui.ui.screens.hardware

import android.content.Context
import android.hardware.ConsumerIrManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast

@Composable
fun InfraredScreen() {
    WeScreen(title = "Infrared", description = "红外信号") {
        val context = LocalContext.current
        val irManager = context.getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager
        val toast = rememberWeToast()

        WeButton("发射红外信号") {
            if (irManager?.hasIrEmitter() == true) {
                // 红外信号模式是由一系列以微秒为单位的整数构成的数组，这些整数代表红外信号的“开”和“关”持续时间
                // 1.所有的值都要是正数
                // 2.至少应有两个值（一个“开”和一个“关”），且值的数量应为偶数
                val pattern = intArrayOf(9000, 4500, 560, 560)
                // 频率
                val frequency = 38000

                irManager.transmit(frequency, pattern)
                toast.show(ToastOptions("已发射", ToastIcon.SUCCESS))
            } else {
                toast.show(ToastOptions("此设备不支持红外", ToastIcon.FAIL))
            }
        }
    }
}