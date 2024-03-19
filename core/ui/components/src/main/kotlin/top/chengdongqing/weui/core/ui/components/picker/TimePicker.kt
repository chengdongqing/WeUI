package top.chengdongqing.weui.core.ui.components.picker

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                range.first().indexOf(initialValue.hour),
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
                range[1] = when (fullValue.first()) {
                    0 -> IntRange(start.minute, 59)
                    range.first().lastIndex -> IntRange(0, end.minute)
                    else -> IntRange(0, 59)
                }.toList()
            }
            if (type == TimeType.SECOND) {
                range[1].getOrNull(fullValue[1])?.let {
                    range[2] = (if (fullValue.first() == 0 && it == start.minute) {
                        IntRange(start.second, 59)
                    } else if (fullValue.first() == range.first().lastIndex && it == end.minute) {
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
                range.first()[it.first()],
                range.getOrNull(1)?.get(it[1]) ?: 0,
                range.getOrNull(2)?.get(it[2]) ?: 0
            )
            onChange(date)
        },
        onCancel = onCancel
    )
}