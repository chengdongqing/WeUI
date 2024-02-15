package top.chengdongqing.weui.ui.components.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val startIndex = 100
private val today = LocalDate.now()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeCalendar(state: CalendarState = rememberCalendarState()) {
    Column(modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))) {
        Header(state.currentMonth) {
            state.setMonth(it)
        }
        WeekDaysBar()
        WeDivider()
        DaysGrid(state.pagerState)
    }
}

@Composable
private fun Header(currentMonth: LocalDate, setCurrentMonth: (LocalDate) -> Unit) {
    val formatter = remember {
        DateTimeFormatter.ofPattern("yyyy 年 MM 月")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { setCurrentMonth(currentMonth.minusYears(1)) }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowLeft,
                contentDescription = "上一年",
                tint = LightColor
            )
        }
        IconButton(onClick = { setCurrentMonth(currentMonth.minusMonths(1)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = "上个月",
                tint = LightColor
            )
        }
        Text(
            text = currentMonth.format(formatter),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { setCurrentMonth(currentMonth.plusMonths(1)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "下个月",
                tint = LightColor
            )
        }
        IconButton(onClick = { setCurrentMonth(currentMonth.plusYears(1)) }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowRight,
                contentDescription = "下一年",
                tint = LightColor
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
                Text(text = it, color = FontColor1, fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DaysGrid(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        val offset = page - startIndex
        // 当前月份
        val date = today.plusMonths(offset.toLong())
        // 当月总天数
        val daysOfMonth = date.lengthOfMonth()
        // 当月第一天是星期几
        val firstDayOfWeek = date.withDayOfMonth(1).dayOfWeek.value - 1

        Box(contentAlignment = Alignment.Center) {
            // 月份背景
            Text(
                text = date.monthValue.toString(),
                color = PrimaryColor.copy(alpha = 0.2f),
                fontSize = 160.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )
            // 日期网格
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(7 * 6) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                val isToday = today == date.withDayOfMonth(index - firstDayOfWeek)
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
            color = if (isToday) PrimaryColor else FontColor,
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
        Text(text = lunarDay, color = if (isToday) PrimaryColor else LightColor, fontSize = 11.sp)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberCalendarState(initialDate: LocalDate = today): CalendarState {
    val (currentMonth, setCurrentMonth) = remember {
        mutableStateOf(initialDate)
    }
    val pagerState = rememberPagerState(initialPage = startIndex) { 200 }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val offset = pagerState.currentPage - startIndex
        setCurrentMonth(today.plusMonths(offset.toLong()))
    }

    val initialMonth = remember { YearMonth.now().minusMonths(startIndex.toLong()) }
    fun setMonth(value: LocalDate) {
        setCurrentMonth(value)
        val page = ChronoUnit.MONTHS.between(initialMonth, YearMonth.from(value)).toInt()
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
    }

    return CalendarState(currentMonth, ::setMonth, pagerState)
}

@OptIn(ExperimentalFoundationApi::class)
data class CalendarState(
    val currentMonth: LocalDate,
    val setMonth: (LocalDate) -> Unit,
    val pagerState: PagerState
) {
    fun toToday() {
        setMonth(today)
    }
}