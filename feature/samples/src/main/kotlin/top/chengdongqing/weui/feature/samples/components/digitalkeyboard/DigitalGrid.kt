package top.chengdongqing.weui.feature.samples.components.digitalkeyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RowScope.DigitalGrid(
    widthPerItem: Dp,
    allowDecimal: Boolean,
    onClick: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3
    ) {
        repeat(9) { index ->
            val value = (index + 1).toString()
            KeyItem(
                key = value,
                modifier = Modifier.weight(1f)
            ) {
                onClick(value)
            }
        }
        KeyItem(
            key = "0",
            modifier = Modifier.width(widthPerItem * 2 + 8.dp)
        ) {
            onClick("0")
        }
        KeyItem(
            key = if (allowDecimal) "." else "",
            modifier = Modifier.weight(1f),
            clickable = allowDecimal
        ) {
            onClick(".")
        }
    }
}

@Composable
private fun KeyItem(
    key: String,
    modifier: Modifier,
    clickable: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable(enabled = clickable) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = key,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}