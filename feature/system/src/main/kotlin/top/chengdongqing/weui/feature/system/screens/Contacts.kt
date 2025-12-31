package top.chengdongqing.weui.feature.system.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.cardlist.cardList
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.popup.WePopup
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.DefaultDateTimeFormatter
import top.chengdongqing.weui.core.utils.formatChinese
import java.util.Date
import kotlin.time.Duration.Companion.seconds

@Composable
fun ContactsScreen() {
    WeScreen(
        title = "Contacts",
        description = "拨号与通讯录",
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PhoneCall()
        Spacer(modifier = Modifier.height(20.dp))
        PhoneContactList()
        PhoneCallLogList()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhoneCall() {
    val context = LocalContext.current
    val callPermissionState = rememberPermissionState(Manifest.permission.CALL_PHONE)
    var number by remember { mutableStateOf("") }

    WeInput(
        value = number,
        placeholder = "请输入号码",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    ) { newValue ->
        // 过滤为纯数字
        number = newValue.filter { it.isDigit() }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeButton(
            text = "拨号(系统拨号盘)",
            type = ButtonType.PLAIN,
            size = ButtonSize.MEDIUM,
            width = Dp.Unspecified,
            disabled = number.isEmpty()
        ) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${number}"))
            context.startActivity(intent)
        }
        Spacer(modifier = Modifier.width(16.dp))
        WeButton(
            text = "拨号(直接拨打)",
            type = ButtonType.PLAIN,
            size = ButtonSize.MEDIUM,
            width = Dp.Unspecified,
            disabled = number.isEmpty()
        ) {
            if (callPermissionState.status.isGranted) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${number}"))
                context.startActivity(intent)
            } else {
                callPermissionState.launchPermissionRequest()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhoneContactList() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val contactsPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    var loading by remember { mutableStateOf(false) }
    var contacts by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var visible by remember { mutableStateOf(false) }

    WeButton(text = "读取通讯录", loading = loading) {
        if (contactsPermissionState.status.isGranted) {
            coroutineScope.launch {
                loading = true
                contacts = loadContacts(context).map {
                    it.first to it.second.joinToString("\n")
                }
                loading = false
                visible = true
            }
        } else {
            contactsPermissionState.launchPermissionRequest()
        }
    }
    WePopup(visible, title = "通讯录", onClose = { visible = false }) {
        LazyColumn(
            modifier = Modifier
                .cardList()
                .fillMaxHeight(0.5f)
        ) {
            items(contacts) {
                WeCardListItem(label = it.first, value = it.second)
            }
            if (contacts.isEmpty()) {
                item {
                    WeLoadMore(
                        type = LoadMoreType.EMPTY_DATA,
                        modifier = Modifier.height(300.dp)
                    )
                }
            }
        }
    }
}

private suspend fun loadContacts(context: Context): (List<Pair<String, List<String>>>) =
    withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Pair<String, String>>()
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                contacts.add(Pair(name, number))
            }
        }

        contacts.groupBy({
            it.first
        }, {
            it.second
        }).map {
            Pair(it.key, it.value)
        }
    }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhoneCallLogList() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val callLogPermissionState = rememberPermissionState(Manifest.permission.READ_CALL_LOG)
    var loading by remember { mutableStateOf(false) }
    var logs by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var visible by remember { mutableStateOf(false) }

    WeButton(text = "读取通话记录", type = ButtonType.PLAIN, loading = loading) {
        if (callLogPermissionState.status.isGranted) {
            coroutineScope.launch {
                loading = true
                logs = loadCallLogs(context).map {
                    it.first to it.second.joinToString("\n")
                }
                loading = false
                visible = true
            }
        } else {
            callLogPermissionState.launchPermissionRequest()
        }
    }
    WePopup(visible, title = "通话记录", onClose = { visible = false }) {
        LazyColumn(
            modifier = Modifier
                .cardList()
                .fillMaxHeight(0.5f)
        ) {
            items(logs) {
                WeCardListItem(label = it.first, value = it.second)
            }
            if (logs.isEmpty()) {
                item {
                    WeLoadMore(
                        type = LoadMoreType.EMPTY_DATA,
                        modifier = Modifier.height(300.dp)
                    )
                }
            }
        }
    }
}

private suspend fun loadCallLogs(context: Context): (List<Pair<String, List<String>>>) =
    withContext(Dispatchers.IO) {
        val logs = mutableListOf<Pair<String, List<String>>>()
        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)

            while (cursor.moveToNext()) {
                val number = cursor.getString(numberIndex)
                val date = DateFormat.format(
                    DefaultDateTimeFormatter,
                    Date(cursor.getLong(dateIndex))
                ).toString()
                val type = when (cursor.getInt(typeIndex)) {
                    CallLog.Calls.OUTGOING_TYPE -> "呼出"
                    CallLog.Calls.INCOMING_TYPE -> "呼入"
                    CallLog.Calls.MISSED_TYPE -> "未接通"
                    else -> "未知"
                }
                val duration = cursor.getInt(durationIndex)
                logs.add(
                    Pair(
                        number,
                        listOf(date, type + duration.seconds.formatChinese())
                    )
                )
            }
        }

        logs
    }