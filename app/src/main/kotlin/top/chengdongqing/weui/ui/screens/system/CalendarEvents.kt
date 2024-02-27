package top.chengdongqing.weui.ui.screens.system

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.input.WeInput
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.picker.WeDatePicker
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

@Composable
fun CalendarEventsScreen() {
    WeScreen(title = "CalendarEvents", description = "日历事件") {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddCalendarEvent()
            Spacer(modifier = Modifier.height(20.dp))
            CalendarEvents()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddCalendarEvent() {
    val context = LocalContext.current
    val calendarPermissionState = rememberPermissionState(Manifest.permission.WRITE_CALENDAR)
    var title by remember { mutableStateOf("") }
    val toast = rememberWeToast()

    WeInput(
        value = title,
        label = "事件名称",
        placeholder = "请输入"
    ) {
        title = it
    }
    var date by remember { mutableStateOf<LocalDate?>(null) }
    var visible by remember { mutableStateOf(false) }
    WeInput(
        value = date?.toString(),
        label = "事件日期",
        placeholder = "请选择",
        disabled = true,
        onClick = { visible = true }
    )
    WeDatePicker(
        visible,
        value = date,
        start = LocalDate.now(),
        end = LocalDate.now().plusYears(3),
        onCancel = { visible = false },
        onChange = { date = it }
    )
    Spacer(modifier = Modifier.height(20.dp))

    WeButton(text = "添加日历事件", type = ButtonType.PLAIN) {
        if (calendarPermissionState.status.isGranted) {
            if (title.isNotEmpty() && date != null) {
                val values = ContentValues().apply {
                    put(CalendarContract.Events.TITLE, title)
                    val mills = date!!.atTime(10, 0)
                        .atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli()
                    put(CalendarContract.Events.DTSTART, mills)
                    put(CalendarContract.Events.DTEND, mills)
                    put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                    put(CalendarContract.Events.CALENDAR_ID, 1)
                }
                context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
                toast.show(ToastOptions("已添加", icon = ToastIcon.SUCCESS))
            } else {
                toast.show(ToastOptions("请正确输入", ToastIcon.FAIL))
            }
        } else {
            calendarPermissionState.launchPermissionRequest()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CalendarEvents() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val calendarPermissionState = rememberPermissionState(Manifest.permission.READ_CALENDAR)
    var loading by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    WeButton(text = "读取日历事件", loading = loading) {
        if (calendarPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                events = loadCalendarEvents(context)
                loading = false
            }
        } else {
            calendarPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WePairGroup(Modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp)) {
        items(events) {
            WePairItem(label = it.first, value = it.second)
        }
    }
}

private suspend fun loadCalendarEvents(context: Context): List<Pair<String, String>> =
    withContext(Dispatchers.IO) {
        val events = mutableListOf<Pair<String, String>>()

        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART
            ),
            null,
            null,
            CalendarContract.Events.DTSTART + " DESC"
        )?.use { cursor ->
            val titleCol = cursor.getColumnIndex(CalendarContract.Events.TITLE)
            val startCol = cursor.getColumnIndex(CalendarContract.Events.DTSTART)

            while (cursor.moveToNext()) {
                val title = cursor.getString(titleCol)
                val start =
                    DateFormat.format("yyyy年MM月dd日", Date(cursor.getLong(startCol))).toString()
                events.add(Pair(title, start))
            }
        }

        events
    }