package top.chengdongqing.weui.feature.media.screens.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.components.picker.rememberDatePickerState
import top.chengdongqing.weui.core.utils.ChineseDateFormatter
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun FilterBar(gridState: LazyGridState, state: GalleryState) {
    val picker = rememberDatePickerState()
    var value by remember { mutableStateOf(LocalDate.now()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.firstOrNull()?.key
        }.filter {
            it is LocalDate
        }.map {
            it as LocalDate
        }.collect {
            value = it
        }
    }

    Column {
        WeDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithoutRipple {
                    picker.show(
                        value,
                        // start = state.mediaGroups.keys.last(),
                        end = LocalDate.now()
                    ) {
                        value = it
                        with(state) {
                            coroutineScope.launch {
                                gridState.scrollToDate(it)
                            }
                        }
                    }
                }
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.format(DateTimeFormatter.ofPattern(ChineseDateFormatter)),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}