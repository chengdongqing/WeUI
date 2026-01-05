package top.chengdongqing.weui.feature.system.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Telephony.Sms
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.cardlist.cardList
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.input.WeTextarea
import top.chengdongqing.weui.core.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.popup.WePopup
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState

@Composable
fun SmsScreen() {
    WeScreen(title = "SMS", description = "短信") {
        WritingSms()
        Spacer(modifier = Modifier.height(20.dp))
        ReadingSms()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun WritingSms() {
    val context = LocalContext.current
    val smsPermissionState = rememberPermissionState(Manifest.permission.SEND_SMS)
    var number by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val toast = rememberToastState()

    WeInput(
        value = number,
        placeholder = "请输入号码",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    ) {
        number = it
    }
    WeTextarea(content, placeholder = "请输入内容", max = 200) {
        content = it
    }
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(text = "发送短信") {
        if (smsPermissionState.status.isGranted) {
            if (number.isEmpty() || content.isEmpty()) {
                toast.show("请正确输入")
            } else {
                val intent = Intent(Intent.ACTION_SENDTO, "smsto:$number".toUri()).apply {
                    putExtra("sms_body", content)
                }
                context.startActivity(intent)
            }
        } else {
            smsPermissionState.launchPermissionRequest()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ReadingSms() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val readSmsPermissionState = rememberPermissionState(Manifest.permission.READ_SMS)
    var loading by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var visible by remember { mutableStateOf(false) }

    WeButton(text = "读取短信", type = ButtonType.PLAIN, loading = loading) {
        if (readSmsPermissionState.status.isGranted) {
            coroutineScope.launch {
                loading = true
                messages = loadSmsMessages(context)
                loading = false
                visible = true
            }
        } else {
            readSmsPermissionState.launchPermissionRequest()
        }
    }
    WePopup(visible, title = "短信", onClose = { visible = false }) {
        LazyColumn(
            modifier = Modifier
                .cardList()
                .fillMaxHeight(0.5f)
        ) {
            items(messages) {
                WeCardListItem(label = it.first, value = it.second)
            }
            if (messages.isEmpty()) {
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

private suspend fun loadSmsMessages(context: Context): List<Pair<String, String>> =
    withContext(Dispatchers.IO) {
        val messages = mutableListOf<Pair<String, String>>()

        context.contentResolver.query(
            Sms.Inbox.CONTENT_URI,
            arrayOf(
                Sms.Inbox._ID,
                Sms.Inbox.ADDRESS,
                Sms.Inbox.BODY,
                Sms.Inbox.DATE
            ),
            null,
            null,
            null
        )?.use { cursor ->
            val addressIndex = cursor.getColumnIndex(Sms.Inbox.ADDRESS)
            val bodyIndex = cursor.getColumnIndex(Sms.Inbox.BODY)

            while (cursor.moveToNext()) {
                val item = Pair(cursor.getString(addressIndex), cursor.getString(bodyIndex))
                messages.add(item)
            }
        }

        messages
    }