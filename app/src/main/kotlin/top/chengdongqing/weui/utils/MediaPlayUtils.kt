package top.chengdongqing.weui.utils

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun rememberPlayProgress(player: MediaPlayer?, isPlaying: Boolean): Int {
    var progress by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(isPlaying, player) {
        if (isPlaying && player != null) {
            val job = coroutineScope.launch {
                while (isActive) {
                    progress = player.currentPosition
                    delay(500)
                }
            }
            onDispose { job.cancel() }
        } else {
            onDispose { }
        }
    }

    return progress
}

@Composable
fun rememberPlayPercent(passed: Int, duration: Int): MutableIntState {
    val progress = remember { mutableIntStateOf(0) }

    LaunchedEffect(passed) {
        progress.intValue = if (duration > 0) {
            (passed / duration.toFloat() * 100).roundToInt()
        } else {
            0
        }
    }

    return progress
}