package top.chengdongqing.weui.feature.demos.components

import android.icu.text.DecimalFormat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun WeDigitalRoller(value: Float, decimals: Int = 2, animationDuration: Int = 800) {
    val formatter = DecimalFormat("#,##0" + if (decimals > 0) "." + "0".repeat(decimals) else "")
    val formattedValue = formatter.format(value)
    val parts = formattedValue.split('.')
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else ""

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        // 整数部分
        integerPart.forEach { char ->
            if (char.isDigit()) {
                RollerItem(
                    end = char.toString().toInt(),
                    animationDuration = animationDuration
                )
            } else {
                DigitalItem(char.toString())
            }
        }

        // 小数部分
        if (decimals > 0) {
            DigitalItem(".")
            decimalPart.forEach { digit ->
                RollerItem(
                    end = digit.toString().toInt(),
                    animationDuration = animationDuration
                )
            }
        }
    }
}

@Composable
private fun RollerItem(
    end: Int,
    start: Int = if (end < 9) end + 1 else end - 1,
    animationDuration: Int
) {
    val density = LocalDensity.current
    val heightPerItem = remember { density.run { 35.dp.toPx().roundToInt() } }
    val scrollState = rememberScrollState(start * heightPerItem)

    // 滚动到目标位置
    LaunchedEffect(end) {
        scrollState.animateScrollTo(
            value = end * heightPerItem,
            animationSpec = tween(animationDuration, easing = LinearEasing)
        )
    }

    Column(
        modifier = Modifier
            .height(35.dp)
            .verticalScroll(state = scrollState, enabled = false),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(10) { index ->
            Box(
                modifier = Modifier.height(35.dp),
                contentAlignment = Alignment.Center
            ) {
                DigitalItem(index.toString())
            }
        }
    }
}

@Composable
private fun DigitalItem(value: String) {
    Text(
        text = value,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
    )
}