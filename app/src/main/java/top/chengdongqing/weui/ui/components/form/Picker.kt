package top.chengdongqing.weui.ui.components.form

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.feedback.WePopup
import top.chengdongqing.weui.ui.theme.FontColor
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

@Composable
fun WePicker(
    visible: Boolean,
    title: String? = null,
    range: Array<List<String>>,
    value: Array<Int>,
    onColumnChange: ((column: Int, value: Int, fullValue: Array<Int>) -> Unit)? = null,
    onChange: (Array<Int>) -> Unit,
    onCancel: () -> Unit
) {
    val localValue = remember(visible) {
        value.copyOf()
    }

    WePopup(visible, title = title, onClose = onCancel) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .height(280.dp)
                .drawBehind {
                    // 指示栏
                    drawRoundRect(
                        Color(0xFFF7F7F7),
                        topLeft = Offset(0f, size.height / 2 - 56.dp.toPx() / 2),
                        size = Size(size.width, 56.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                }
            ) {
                // 数据列表
                Row(verticalAlignment = Alignment.CenterVertically) {
                    range.forEachIndexed { index, options ->
                        PickerColumn(
                            modifier = Modifier.weight(1f),
                            options = options,
                            index = localValue[index]
                        ) {
                            localValue[index] = it
                            onColumnChange?.invoke(index, it, localValue)
                        }
                    }
                }
                // 遮罩
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(255, 255, 255, (255 * 0.95).roundToInt()),
                                        Color(255, 255, 255, (255 * 0.6).roundToInt())
                                    )
                                )
                            )
                    )
                    Box(modifier = Modifier.height(56.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(255, 255, 255, (255 * 0.6).roundToInt()),
                                        Color(255, 255, 255, (255 * 0.95).roundToInt())
                                    )
                                )
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(56.dp))
            Row {
                WeButton(text = "取消", type = ButtonType.PLAIN, width = 144.dp) {
                    onCancel()
                }
                Spacer(modifier = Modifier.width(20.dp))
                WeButton(text = "确定", width = 144.dp) {
                    onChange(localValue)
                    onCancel()
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PickerColumn(
    modifier: Modifier,
    options: List<String>,
    index: Int,
    onChange: (Int) -> Unit
) {
    val itemHeight = 56.dp
    val listState = rememberLazyListState(index)
    val snapFlingBehavior = rememberSnapFlingBehavior(listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                onChange(it)
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = (280.dp - itemHeight) / 2),
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = snapFlingBehavior
    ) {
        items(options.size) {
            val item = options[it]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item, color = FontColor, fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun WeSingleColumnPicker(
    visible: Boolean,
    title: String? = null,
    range: List<String>,
    value: Int,
    onChange: (Int) -> Unit,
    onCancel: () -> Unit
) {
    WePicker(
        visible,
        title,
        range = arrayOf(range),
        value = arrayOf(value),
        onChange = {
            onChange(it[0])
        },
        onCancel = onCancel
    )
}

enum class DateType {
    YEAR,
    MONTH,
    DAY
}

@Composable
fun WeDatePicker(
    visible: Boolean,
    value: LocalDate? = null,
    type: DateType = DateType.DAY,
    start: LocalDate = LocalDate.now().minusYears(50),
    end: LocalDate = LocalDate.now().plusYears(10),
    onCancel: () -> Unit,
    onChange: (LocalDate) -> Unit
) {
    if (start.isAfter(end)) {
        Log.e("WeDatePicker", "Invalid date range: start ($start) must be before end ($end)")
        return
    }

    var range by remember(start, end, type) {
        val options = arrayOf(
            IntRange(start.year, end.year).toList(),
            IntRange(1, 12).toList(),
            IntRange(1, 31).toList()
        )
        mutableStateOf(
            when (type) {
                DateType.YEAR -> options.copyOfRange(0, 1)
                DateType.MONTH -> options.copyOfRange(0, 2)
                DateType.DAY -> options
            }
        )
    }
    var localValue by remember(range, value, start, end) {
        val value1 = value ?: LocalDate.now()
        val initialValue = if (value1.isBefore(start)) start
        else if (value1.isAfter(end)) end
        else value1

        mutableStateOf(
            arrayOf(
                range[0].indexOf(initialValue.year),
                range.getOrNull(1)?.indexOf(initialValue.monthValue) ?: 0,
                range.getOrNull(2)?.indexOf(initialValue.dayOfMonth) ?: 0
            )
        )
    }

    WePicker(
        visible,
        title = "选择日期",
        range = remember {
            derivedStateOf {
                val units = arrayOf("年", "月", "日")
                range.mapIndexed { listIndex, list ->
                    val unit = units[listIndex]
                    list.map { it.toString() + unit }
                }.toTypedArray()
            }
        }.value,
        value = localValue,
        onColumnChange = { column, _, fullValue ->
            if (type != DateType.YEAR) {
                range[1] = when (fullValue[0]) {
                    0 -> IntRange(start.monthValue, 12)
                    range[0].size - 1 -> IntRange(1, end.monthValue)
                    else -> IntRange(1, 12)
                }.toList()
            }
            if (type == DateType.DAY) {
                range[1].getOrNull(fullValue[1])?.let {
                    range[2] = (if (fullValue[0] == 0 && it == start.monthValue) {
                        val days = LocalDate.now().withMonth(start.monthValue).lengthOfMonth()
                        IntRange(start.dayOfMonth, days)
                    } else if (fullValue[0] == range[0].size - 1 && it == end.monthValue) {
                        IntRange(1, end.dayOfMonth)
                    } else {
                        val days = LocalDate.now().withMonth(range[1][fullValue[1]]).lengthOfMonth()
                        IntRange(1, days)
                    }).toList()
                }
            }
            if (type != DateType.YEAR && column != 2) {
                range = range.copyOf()
            }
        },
        onChange = {
            localValue = it

            val date = LocalDate.of(
                range[0][it[0]],
                range.getOrNull(1)?.get(it[1]) ?: 1,
                range.getOrNull(2)?.get(it[2]) ?: 1
            )
            onChange(date)
        },
        onCancel = onCancel
    )
}

enum class TimeType {
    HOUR,
    MINUTE,
    SECOND
}

@Composable
fun WeTimePicker(
    visible: Boolean,
    value: LocalTime? = null,
    type: TimeType = TimeType.SECOND,
    start: LocalTime = LocalTime.MIN,
    end: LocalTime = LocalTime.MAX,
    onCancel: () -> Unit,
    onChange: (LocalTime) -> Unit
) {
    if (start.isAfter(end)) {
        Log.e("WeTimePicker", "Invalid time range: start ($start) must be before end ($end)")
        return
    }

    var range by remember(start, end, type) {
        val options = arrayOf(
            IntRange(start.hour, end.hour).toList(),
            IntRange(0, 59).toList(),
            IntRange(0, 59).toList()
        )
        mutableStateOf(
            when (type) {
                TimeType.HOUR -> options.copyOfRange(0, 1)
                TimeType.MINUTE -> options.copyOfRange(0, 2)
                TimeType.SECOND -> options
            }
        )
    }
    var localValue by remember(range, value) {
        val value1 = value ?: LocalTime.now()
        val initialValue = if (value1.isBefore(start)) start
        else if (value1.isAfter(end)) end
        else value1

        mutableStateOf(
            arrayOf(
                range[0].indexOf(initialValue.hour),
                range.getOrNull(1)?.indexOf(initialValue.minute) ?: 0,
                range.getOrNull(2)?.indexOf(initialValue.second) ?: 0
            )
        )
    }

    WePicker(
        visible,
        title = "选择时间",
        range = remember {
            derivedStateOf {
                val units = arrayOf("时", "分", "秒")
                range.mapIndexed { listIndex, list ->
                    val unit = units[listIndex]
                    list.map { it.toString() + unit }
                }.toTypedArray()
            }
        }.value,
        value = localValue,
        onColumnChange = { column, _, fullValue ->
            if (type != TimeType.HOUR) {
                range[1] = when (fullValue[0]) {
                    0 -> IntRange(start.minute, 59)
                    range[0].size - 1 -> IntRange(0, end.minute)
                    else -> IntRange(0, 59)
                }.toList()
            }
            if (type == TimeType.SECOND) {
                range[1].getOrNull(fullValue[1])?.let {
                    range[2] = (if (fullValue[0] == 0 && it == start.minute) {
                        IntRange(start.second, 59)
                    } else if (fullValue[0] == range[0].size - 1 && it == end.minute) {
                        IntRange(0, end.second)
                    } else {
                        IntRange(0, 59)
                    }).toList()
                }
            }
            if (type != TimeType.HOUR && column != 2) {
                range = range.copyOf()
            }
        },
        onChange = {
            localValue = it

            val date = LocalTime.of(
                range[0][it[0]],
                range.getOrNull(1)?.get(it[1]) ?: 0,
                range.getOrNull(2)?.get(it[2]) ?: 0
            )
            onChange(date)
        },
        onCancel = onCancel
    )
}