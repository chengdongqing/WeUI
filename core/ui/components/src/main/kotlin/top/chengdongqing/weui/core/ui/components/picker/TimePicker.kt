package top.chengdongqing.weui.core.ui.components.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import java.time.LocalTime

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
    require(end.isAfter(start)) {
        "Invalid time range: end ($end) must be after start ($start)"
    }

    var rangesSource by rememberRangesSource(start, end, type)
    var values by rememberValues(rangesSource, value, start, end)
    val ranges by rememberFinalRanges(rangesSource)

    WePicker(
        visible,
        ranges,
        values,
        title = "选择时间",
        onCancel = onCancel,
        onColumnValueChange = { column, _, newValues ->
            handleColumnChange(rangesSource, newValues, column, start, end, type) {
                rangesSource = it
            }
        }
    ) {
        values = it

        val date = LocalTime.of(
            rangesSource.first()[it.first()],
            rangesSource.getOrNull(1)?.get(it[1]) ?: 0,
            rangesSource.getOrNull(2)?.get(it[2]) ?: 0
        )
        onChange(date)
    }
}

private fun handleColumnChange(
    ranges: Array<List<Int>>,
    values: Array<Int>,
    column: Int = 0,
    start: LocalTime,
    end: LocalTime,
    type: TimeType,
    onRangesChange: (Array<List<Int>>) -> Unit
) {
    if (type == TimeType.HOUR || column == 2) return

    // 计算可选分钟
    val minuteRange = when (values.first()) {
        0 -> IntRange(start.minute, 59)
        ranges.first().lastIndex -> IntRange(0, end.minute)
        else -> IntRange(0, 59)
    }
    ranges[1] = minuteRange.toList()

    // 如果精确到秒，就计算可选的秒
    if (type == TimeType.SECOND) {
        ranges[1].getOrNull(values[1])?.let {
            ranges[2] = (if (values.first() == 0 && it == start.minute) {
                IntRange(start.second, 59)
            } else if (values.first() == ranges.first().lastIndex && it == end.minute) {
                IntRange(0, end.second)
            } else {
                IntRange(0, 59)
            }).toList()
        }
    }

    onRangesChange(ranges.copyOf())
}

@Composable
private fun rememberRangesSource(
    start: LocalTime,
    end: LocalTime,
    type: TimeType
): MutableState<Array<List<Int>>> {
    return remember(start, end, type) {
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
}

@Composable
private fun rememberFinalRanges(ranges: Array<List<Int>>): State<Array<List<String>>> {
    val units = remember { arrayOf("时", "分", "秒") }
    val currentRanges by rememberUpdatedState(newValue = ranges)

    return remember {
        derivedStateOf {
            currentRanges.mapIndexed { index, options ->
                val unit = units[index]
                options.map { it.toString() + unit }
            }.toTypedArray()
        }
    }
}

@Composable
private fun rememberValues(
    range: Array<List<Int>>,
    value: LocalTime?,
    start: LocalTime,
    end: LocalTime
): MutableState<Array<Int>> {
    return remember(range, value) {
        val finalValue = value ?: LocalTime.now()
        val initialValue = if (finalValue.isBefore(start)) {
            start
        } else if (finalValue.isAfter(end)) {
            end
        } else {
            finalValue
        }

        mutableStateOf(
            arrayOf(
                range.first().indexOf(initialValue.hour),
                range.getOrNull(1)?.indexOf(initialValue.minute) ?: 0,
                range.getOrNull(2)?.indexOf(initialValue.second) ?: 0
            )
        )
    }
}

@Stable
interface TimePickerState {
    val visible: Boolean

    fun show(
        value: LocalTime? = null,
        type: TimeType = TimeType.SECOND,
        start: LocalTime = LocalTime.MIN,
        end: LocalTime = LocalTime.MAX,
        onChange: (LocalTime) -> Unit
    )

    fun hide()
}

@Composable
fun rememberTimePickerState(): TimePickerState {
    val state = remember { TimePickerStateImpl() }

    state.props?.let { props ->
        WeTimePicker(
            visible = state.visible,
            value = props.value,
            type = props.type,
            start = props.start,
            end = props.end,
            onCancel = { state.hide() },
            onChange = props.onChange
        )
    }

    return state
}

private class TimePickerStateImpl : TimePickerState {
    override var visible by mutableStateOf(false)
    var props by mutableStateOf<TimePickerProps?>(null)
        private set

    override fun show(
        value: LocalTime?,
        type: TimeType,
        start: LocalTime,
        end: LocalTime,
        onChange: (LocalTime) -> Unit
    ) {
        props = TimePickerProps(value, type, start, end, onChange)
        visible = true
    }

    override fun hide() {
        visible = false
    }
}

private data class TimePickerProps(
    val value: LocalTime?,
    val type: TimeType,
    val start: LocalTime,
    val end: LocalTime,
    val onChange: (LocalTime) -> Unit
)