package top.chengdongqing.weui.feature.samples.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.utils.ChineseDateFormatter
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeCalendar(state: CalendarState = rememberCalendarState()) {
    Column {
        Header(state.currentMonth) { state.setMonth(it) }
        WeekDaysBar()
        WeDivider()
        DaysGrid(state.pagerState)
    }
}

@Composable
private fun Header(currentMonth: LocalDate, onMonthChange: (LocalDate) -> Unit) {
    val formatter = remember {
        DateTimeFormatter.ofPattern(ChineseDateFormatter)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMonthChange(currentMonth.minusYears(1)) }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowLeft,
                contentDescription = "上一年",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = "上个月",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Text(
            text = currentMonth.format(formatter),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "下个月",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        IconButton(onClick = { onMonthChange(currentMonth.plusYears(1)) }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowRight,
                contentDescription = "下一年",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun WeekDaysBar() {
    val weekDays = remember {
        arrayOf("日", "一", "二", "三", "四", "五", "六")
    }

    Row {
        weekDays.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun DaysGrid(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        val offset = page - InitialPage
        // 当前月份
        val date = Today.plusMonths(offset.toLong())
        // 当月总天数
        val daysOfMonth = date.lengthOfMonth()
        // 当月第一天是星期几
        val firstDayOfWeek = date.withDayOfMonth(1).dayOfWeek.value - 1

        Box(contentAlignment = Alignment.Center) {
            // 月份背景
            Text(
                text = date.monthValue.toString(),
                color = MaterialTheme.colorScheme.primary.copy(0.2f),
                fontSize = 160.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )
            // 日期网格
            FlowRow(
                maxItemsInEachRow = 7,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                repeat(7 * 6) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            // 上月的日期
                            index <= firstDayOfWeek -> {
                                val lastMonth = date.minusMonths(1)
                                val day = lastMonth.lengthOfMonth() - (firstDayOfWeek - index)
                                DayItem(
                                    date = date.minusMonths(1).withDayOfMonth(day),
                                    outInMonth = true
                                )
                            }
                            // 下月的日期
                            index - firstDayOfWeek > daysOfMonth -> {
                                val day = index - (daysOfMonth + firstDayOfWeek)
                                DayItem(
                                    date = date.plusMonths(1).withDayOfMonth(day),
                                    outInMonth = true
                                )
                            }
                            // 本月的日期
                            else -> {
                                val isToday = Today == date.withDayOfMonth(index - firstDayOfWeek)
                                val day = index - firstDayOfWeek
                                DayItem(
                                    date = date.withDayOfMonth(day),
                                    isToday
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayItem(
    date: LocalDate,
    isToday: Boolean = false,
    outInMonth: Boolean = false
) {
    Column(
        modifier = if (outInMonth) Modifier.alpha(0.4f) else Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 公历日期
        Text(
            text = date.dayOfMonth.toString(),
            color = if (isToday) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            fontSize = 18.sp,
            fontWeight = if (!outInMonth) FontWeight.Bold else FontWeight.Normal
        )
        // 农历日期
        val lunarDate = Lunar(Solar(date.year, date.monthValue, date.dayOfMonth))
        val lunarDay = if (lunarDate.festivals.isNotEmpty()) {
            lunarDate.festivals.first()
        } else if (lunarDate.day == 1) {
            lunarDate.monthInChinese + "月"
        } else {
            lunarDate.dayInChinese
        }
        Text(
            text = lunarDay,
            color = if (isToday) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSecondary
            },
            fontSize = 11.sp
        )
    }
}

@Stable
interface CalendarState {
    /**
     * 当前月份
     */
    val currentMonth: LocalDate

    @OptIn(ExperimentalFoundationApi::class)
    val pagerState: PagerState

    /**
     * 设置月份
     */
    fun setMonth(month: LocalDate, scrollToPage: Boolean = true)

    /**
     * 回到今天
     */
    fun toToday() {
        setMonth(Today)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberCalendarState(initialDate: LocalDate = Today): CalendarState {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = InitialPage) { TotalPage }
    val state = remember { CalendarStateImpl(initialDate, pagerState, coroutineScope) }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val diff = page - InitialPage
            state.setMonth(month = Today.plusMonths(diff.toLong()), scrollToPage = false)
        }
    }

    return state
}

@OptIn(ExperimentalFoundationApi::class)
private class CalendarStateImpl(
    initialDate: LocalDate,
    override val pagerState: PagerState,
    val coroutineScope: CoroutineScope
) : CalendarState {
    override val currentMonth: LocalDate get() = _currentMonth

    override fun setMonth(month: LocalDate, scrollToPage: Boolean) {
        _currentMonth = month

        if (scrollToPage) {
            coroutineScope.launch {
                val page = ChronoUnit.MONTHS.between(initialMonth, YearMonth.from(month)).toInt()
                pagerState.scrollToPage(page)
            }
        }
    }

    private val initialMonth = YearMonth.now().minusMonths(InitialPage.toLong())
    private var _currentMonth by mutableStateOf(initialDate)
}

private val Today = LocalDate.now()
private const val TotalPage = 2000
private const val InitialPage = 1000