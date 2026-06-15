package top.chengdongqing.weui.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.ChineseDateFormatter
import top.chengdongqing.weui.util.localDate
import kotlin.time.Clock

@Composable
fun WeCalendar(
    state: CalendarState = rememberCalendarState()
) {
    Column {
        Header(state.currentMonth) {
            state.setMonth(it)
        }
        WeekDaysBar()
        WeDivider()
        DaysGrid(state.pagerState)
    }
}

@Composable
private fun Header(
    currentMonth: LocalDate,
    onMonthChange: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onMonthChange(currentMonth.minus(1, DateTimeUnit.YEAR))
        }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowLeft,
                contentDescription = "上一年",
                tint = WeTheme.colorScheme.textSecondary
            )
        }
        IconButton(onClick = {
            onMonthChange(currentMonth.minus(1, DateTimeUnit.MONTH))
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = "上个月",
                tint = WeTheme.colorScheme.textSecondary
            )
        }
        Text(
            text = currentMonth.format(ChineseDateFormatter),
            color = WeTheme.colorScheme.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = {
            onMonthChange(currentMonth.plus(1, DateTimeUnit.MONTH))
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "下个月",
                tint = WeTheme.colorScheme.textSecondary
            )
        }
        IconButton(onClick = {
            onMonthChange(currentMonth.plus(1, DateTimeUnit.YEAR))
        }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowRight,
                contentDescription = "下一年",
                tint = WeTheme.colorScheme.textSecondary
            )
        }
    }
}

@Composable
private fun WeekDaysBar() {
    Row {
        WeekDays.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    color = WeTheme.colorScheme.textSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun DaysGrid(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        val offset = page - InitialPage
        // 当前月份
        val date = Today.plus(offset, DateTimeUnit.MONTH)
        // 当月总天数
        val daysOfMonth = date.yearMonth.numberOfDays
        // 当月第一天是星期几
        val firstDayOfWeek = date.dayOfWeek.ordinal

        Box(contentAlignment = Alignment.Center) {
            // 月份背景
            BackgroundMonth(date.month.number)

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
                                val lastMonth = date.minus(1, DateTimeUnit.MONTH)
                                val day =
                                    lastMonth.yearMonth.numberOfDays - (firstDayOfWeek - index)
                                DayItem(
                                    date = LocalDate(lastMonth.year, lastMonth.month, day),
                                    outInMonth = true
                                )
                            }
                            // 下月的日期
                            index - firstDayOfWeek > daysOfMonth -> {
                                val nextMonth = date.plus(1, DateTimeUnit.MONTH)
                                val day = index - (daysOfMonth + firstDayOfWeek)
                                DayItem(
                                    date = LocalDate(nextMonth.year, nextMonth.month, day),
                                    outInMonth = true
                                )
                            }
                            // 本月的日期
                            else -> {
                                val currentDate =
                                    LocalDate(date.year, date.month, index - firstDayOfWeek)
                                val isToday = Today == currentDate
                                DayItem(
                                    date = currentDate,
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
private fun BackgroundMonth(numberOfMonth: Int) {
    Text(
        text = numberOfMonth.toString(),
        color = WeTheme.colorScheme.primary.copy(0.2f),
        fontSize = 160.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Cursive
    )
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
            text = date.day.toString(),
            color = if (isToday) {
                WeTheme.colorScheme.primary
            } else {
                WeTheme.colorScheme.textPrimary
            },
            fontSize = 18.sp,
            fontWeight = if (!outInMonth) FontWeight.Bold else FontWeight.Normal
        )
        // 农历日期
//        val lunarDate = Lunar(Solar(date.year, date.month.number, date.day))
//        val lunarDay = if (lunarDate.festivals.isNotEmpty()) {
//            lunarDate.festivals.first()
//        } else if (lunarDate.day == 1) {
//            lunarDate.monthInChinese + "月"
//        } else {
//            lunarDate.dayInChinese
//        }
        val lunarDay = "七月"
        Text(
            text = lunarDay,
            color = if (isToday) {
                WeTheme.colorScheme.primary
            } else {
                WeTheme.colorScheme.textSecondary
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

@Composable
fun rememberCalendarState(initialDate: LocalDate = Today): CalendarState {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = InitialPage) { TotalPage }
    val state = remember { CalendarStateImpl(initialDate, pagerState, coroutineScope) }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                val diff = page - InitialPage
                state.setMonth(
                    month = Today.plus(diff, DateTimeUnit.MONTH),
                    scrollToPage = false
                )
            }
    }

    return state
}

private class CalendarStateImpl(
    initialDate: LocalDate,
    override val pagerState: PagerState,
    val coroutineScope: CoroutineScope
) : CalendarState {
    override val currentMonth: LocalDate
        get() = _currentMonth

    override fun setMonth(month: LocalDate, scrollToPage: Boolean) {
        _currentMonth = month

        if (scrollToPage) {
            coroutineScope.launch {
                val diff = initialMonth.periodUntil(month)
                val page = diff.years * 12 + diff.months
                pagerState.scrollToPage(page)
            }
        }
    }

    private val initialMonth = Today.minus(InitialPage, DateTimeUnit.MONTH)
    private var _currentMonth by mutableStateOf(initialDate)
}

private const val TotalPage = 1000
private const val InitialPage = 500
private val Today = Clock.localDate
private val WeekDays = arrayOf("日", "一", "二", "三", "四", "五", "六")