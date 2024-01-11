package top.chengdongqing.weui.ui.views.device

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import top.chengdongqing.weui.ui.components.KeyValueCard
import top.chengdongqing.weui.ui.components.KeyValueRow
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import java.util.Date

@Composable
fun CallAndContactsPage() {
    Page(title = "CallAndContacts", description = "拨号和通讯录") {
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
    val callPermissionState =
        rememberPermissionState(android.Manifest.permission.CALL_PHONE)
    var number by remember {
        mutableStateOf("")
    }

    TextField(
        value = number,
        onValueChange = {
            number = it
        },
        placeholder = {
            Text(text = "请输入号码")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth()
    )
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
                    Toast.makeText(context, "请输入号码", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${number}")
                    }
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
                Toast.makeText(context, "请输入号码", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${number}")
                }
                context.startActivity(intent)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhoneContacts() {
    val context = LocalContext.current
    val contactsPermissionState =
        rememberPermissionState(android.Manifest.permission.READ_CONTACTS)

    val coroutineScope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }
    val contacts = remember {
        mutableStateListOf<Pair<String, String>>()
    }

    WeButton(text = "读取通讯录", loading = loading) {
        if (contactsPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                readContacts(context) { res ->
                    if (contacts.isNotEmpty()) {
                        contacts.clear()
                    }
                    contacts.addAll(res.map {
                        Pair(
                            it.first,
                            it.second.joinToString("\n")
                        )
                    })
                    loading = false
                }
            }
        } else {
            contactsPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    KeyValueCard(
        Modifier
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2)
            .verticalScroll(rememberScrollState())
    ) {
        contacts.forEach {
            KeyValueRow(label = it.first, value = it.second)
        }
    }
}

private suspend fun readContacts(
    context: Context,
    onResult: (List<Pair<String, List<String>>>) -> Unit
) {
    val contacts = mutableListOf<Pair<String, String>>()
    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    withContext(Dispatchers.IO) {
        cursor?.use {
            val nameIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Pair(name, number))
            }
        }
    }


    // 聚合相同联系人的多个号码
    onResult(contacts.groupBy({
        it.first
    }, {
        it.second
    }).map {
        Pair(it.key, it.value)
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhoneCallLogs() {
    val context = LocalContext.current
    val callLogPermissionState =
        rememberPermissionState(android.Manifest.permission.READ_CALL_LOG)

    val coroutineScope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }
    val logs = remember {
        mutableStateListOf<Pair<String, String>>()
    }

    WeButton(text = "读取通话记录", loading = loading) {
        if (callLogPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                readCallLogs(context) { res ->
                    if (logs.isNotEmpty()) {
                        logs.clear()
                    }
                    logs.addAll(res.map {
                        Pair(
                            it.first,
                            it.second.joinToString("\n")
                        )
                    })
                    loading = false
                }
            }
        } else {
            callLogPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    KeyValueCard(
        Modifier
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2)
            .verticalScroll(rememberScrollState())
    ) {
        logs.forEach {
            KeyValueRow(label = it.first, value = it.second)
        }
    }
}

private suspend fun readCallLogs(
    context: Context,
    onResult: (List<Pair<String, List<String>>>) -> Unit
) {
    val logs = mutableListOf<Pair<String, List<String>>>()
    val cursor = context.contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        null,
        null,
        null,
        CallLog.Calls.DATE + " DESC"
    )

    withContext(Dispatchers.IO) {
        cursor?.use {
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)

            while (it.moveToNext()) {
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
    }

    onResult(logs)
}