package top.chengdongqing.weui.ui.views.system

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.input.WeInput
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import java.util.Date

@Composable
fun ContactsPage() {
    WePage(title = "Contacts", description = "拨号与通讯录") {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhoneCall()
            Spacer(modifier = Modifier.height(20.dp))
            PhoneContacts()
            Spacer(modifier = Modifier.height(20.dp))
            PhoneCallLogs()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhoneCall() {
    val context = LocalContext.current
    val callPermissionState = rememberPermissionState(Manifest.permission.CALL_PHONE)
    var number by remember { mutableStateOf("") }
    val toast = rememberWeToast()

    WeInput(
        value = number,
        placeholder = "请输入号码",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    ) {
        number = it
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeButton(
            text = "打电话(直接拨打)",
            type = ButtonType.PLAIN,
            size = ButtonSize.MEDIUM
        ) {
            if (callPermissionState.status.isGranted) {
                if (number.isEmpty()) {
                    toast.show(WeToastOptions("请输入号码"))
                } else {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${number}"))
                    context.startActivity(intent)
                }
            } else {
                callPermissionState.launchPermissionRequest()
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        WeButton(
            text = "打电话(系统拨号盘)",
            type = ButtonType.PLAIN,
            size = ButtonSize.MEDIUM
        ) {
            if (number.isEmpty()) {
                toast.show(WeToastOptions("请输入号码"))
            } else {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${number}"))
                context.startActivity(intent)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhoneContacts() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val contactsPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    var loading by remember { mutableStateOf(false) }
    var contacts by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    WeButton(text = "读取通讯录", loading = loading) {
        if (contactsPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                contacts = loadContacts(context).map {
                    it.first to it.second.joinToString("\n")
                }
                loading = false
            }
        } else {
            contactsPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WePairGroup(Modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2)) {
        items(contacts) {
            WePairItem(label = it.first, value = it.second)
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
fun PhoneCallLogs() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val callLogPermissionState = rememberPermissionState(Manifest.permission.READ_CALL_LOG)
    var loading by remember { mutableStateOf(false) }
    var logs by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    WeButton(text = "读取通话记录", type = ButtonType.PLAIN, loading = loading) {
        if (callLogPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                logs = loadCallLogs(context).map {
                    it.first to it.second.joinToString("\n")
                }
                loading = false
            }
        } else {
            callLogPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WePairGroup(Modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2)) {
        items(logs) {
            WePairItem(label = it.first, value = it.second)
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
            CallLog.Calls.DATE + " DESC"
        )?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)

            while (cursor.moveToNext()) {
                val number = cursor.getString(numberIndex)
                val date = DateFormat.format("yyyy-MM-dd HH:mm:ss", Date(cursor.getLong(dateIndex)))
                    .toString()
                val type = when (cursor.getInt(typeIndex)) {
                    CallLog.Calls.OUTGOING_TYPE -> "呼出"
                    CallLog.Calls.INCOMING_TYPE -> "呼入"
                    CallLog.Calls.MISSED_TYPE -> "未接通"
                    else -> "未知"
                }
                val duration = cursor.getLong(durationIndex)
                logs.add(
                    Pair(
                        number,
                        listOf(date, type + if (duration > 0) duration.toString() + "秒" else "")
                    )
                )
            }
        }

        logs
    }