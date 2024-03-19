package top.chengdongqing.weui.core.ui.components.picker

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDate

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
                range.first().indexOf(initialValue.year),
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
                range[1] = when (fullValue.first()) {
                    0 -> IntRange(start.monthValue, 12)
                    range.first().lastIndex -> IntRange(1, end.monthValue)
                    else -> IntRange(1, 12)
                }.toList()
            }
            if (type == DateType.DAY) {
                range[1].getOrNull(fullValue[1])?.let {
                    range[2] = (if (fullValue.first() == 0 && it == start.monthValue) {
                        val days = LocalDate.now().withMonth(start.monthValue).lengthOfMonth()
                        IntRange(start.dayOfMonth, days)
                    } else if (fullValue.first() == range.first().lastIndex && it == end.monthValue) {
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
                range.first()[it.first()],
                range.getOrNull(1)?.get(it[1]) ?: 1,
                range.getOrNull(2)?.get(it[2]) ?: 1
            )
            onChange(date)
        },
        onCancel = onCancel
    )
}