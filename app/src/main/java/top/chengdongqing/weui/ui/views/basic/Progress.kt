package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.components.progress.WeCircleProgress
import top.chengdongqing.weui.ui.components.progress.WeDashboardProgress
import top.chengdongqing.weui.ui.components.progress.WeProgress
import java.util.Timer
import kotlin.concurrent.timerTask

@Composable
fun ProgressPage() {
    WePage(title = "Progress", description = "进度条", backgroundColor = Color.White) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeProgress(20f, null)
            WeProgress(44.57898f)

            var value by remember {
                mutableFloatStateOf(0f)
            }
            var loading by remember {
                mutableStateOf(false)
            }
            val coroutineScope = rememberCoroutineScope()

            WeProgress(value)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeCircleProgress(value)
                WeDashboardProgress(value)
            }
            Spacer(modifier = Modifier.height(60.dp))
            WeButton(
                text = if (!loading) "上传" else "上传中...",
                loading = loading
            ) {
                value = 0f
                loading = true

                coroutineScope.launch {
                    val timer = Timer()
                    timer.schedule(timerTask {
                        if (value < 100) {
                            value += 2
                        } else {
                            timer.cancel()
                            loading = false
                        }
                    }, 0L, 50L)
                }
            }
        }
    }
}