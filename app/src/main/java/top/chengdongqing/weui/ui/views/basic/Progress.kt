package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.WeCircleProgress
import top.chengdongqing.weui.ui.components.basic.WeDashboardProgress
import top.chengdongqing.weui.ui.components.basic.WeProgress
import top.chengdongqing.weui.ui.components.form.WeButton
import java.util.Timer
import kotlin.concurrent.timerTask

@Composable
fun ProgressPage() {
    Page(title = "Progress", description = "进度条", bgColor = Color.White) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeProgress(20f, null)
            WeProgress(44.57898f)

            val value = remember {
                mutableFloatStateOf(0f)
            }
            val loading = remember {
                mutableStateOf(false)
            }

            WeProgress(value.floatValue)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeCircleProgress(value.floatValue)
                WeDashboardProgress(value.floatValue)
            }
            Spacer(modifier = Modifier.height(60.dp))
            WeButton(
                text = if (!loading.value) "上传" else "上传中...",
                loading = loading.value
            ) {
                value.floatValue = 0f
                loading.value = true

                val timer = Timer()
                timer.schedule(timerTask {
                    if (value.floatValue < 100) {
                        value.floatValue += 2
                    } else {
                        timer.cancel()
                        loading.value = false
                    }
                }, 0L, 50L)
            }
        }
    }
}