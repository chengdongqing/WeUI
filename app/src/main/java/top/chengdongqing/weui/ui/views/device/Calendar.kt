package top.chengdongqing.weui.ui.views.device

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
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
import top.chengdongqing.weui.ui.components.KeyValueCard
import top.chengdongqing.weui.ui.components.KeyValueRow
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeInput
import java.util.Date
import java.util.TimeZone

@Composable
fun CalendarPage() {
    Page(title = "Calendar", description = "日历事件") {
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

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddCalendarEvent() {
    val context = LocalContext.current
    val calendarPermissionState = rememberPermissionState(Manifest.permission.WRITE_CALENDAR)

    var title by remember {
        mutableStateOf("")
    }
    WeInput(
        value = title,
        placeholder = "事件名称",
        modifier = Modifier.fillMaxWidth()
    ) {
        title = it
    }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    DatePicker(state = datePickerState)

    WeButton(text = "添加日历事件", type = ButtonType.PLAIN) {
        if (calendarPermissionState.status.isGranted) {
            if (title.isNotEmpty() && datePickerState.selectedDateMillis != null) {
                val values = ContentValues().apply {
                    put(CalendarContract.Events.TITLE, title)
                    put(CalendarContract.Events.DTSTART, datePickerState.selectedDateMillis)
                    put(CalendarContract.Events.DTEND, datePickerState.selectedDateMillis)
                    put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                    put(CalendarContract.Events.CALENDAR_ID, 1)
                }
                context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
                Toast.makeText(context, "已添加", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "请输入正确的事件信息", Toast.LENGTH_SHORT).show()
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
    val calendarPermissionState = rememberPermissionState(Manifest.permission.READ_CALENDAR)

    val coroutineScope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }
    val events = remember {
        mutableListOf<Pair<String, String>>()
    }

    WeButton(text = "读取日历事件", loading = loading) {
        if (calendarPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                val events1 = readCalendarEvents(context)
                if (events.isNotEmpty()) {
                    events.clear()
                }
                events.addAll(events1)
                loading = false
            }
        } else {
            calendarPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    KeyValueCard(
        Modifier
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp)
            .verticalScroll(rememberScrollState())
    ) {

        events.forEach {
            KeyValueRow(label = it.first, value = it.second)
        }
    }
}

suspend fun readCalendarEvents(context: Context): List<Pair<String, String>> =
    withContext(Dispatchers.IO) {
        val events = mutableListOf<Pair<String, String>>()
        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART
            ),
            null,
            null,
            CalendarContract.Events.DTSTART + " DESC"
        )

        cursor?.use {
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