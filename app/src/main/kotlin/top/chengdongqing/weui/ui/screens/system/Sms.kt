package top.chengdongqing.weui.ui.screens.system

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
import top.chengdongqing.weui.ui.components.input.WeTextarea
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast

@Composable
fun SmsScreen() {
    WeScreen(title = "SMS", description = "短信") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WriteSms()
            Spacer(modifier = Modifier.height(20.dp))
            ReadSms()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun WriteSms() {
    val context = LocalContext.current
    val smsPermissionState = rememberPermissionState(Manifest.permission.SEND_SMS)
    var number by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val toast = rememberWeToast()

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
    WeButton(text = "发送短信", type = ButtonType.PLAIN) {
        if (smsPermissionState.status.isGranted) {
            if (number.isEmpty() || content.isEmpty()) {
                toast.show(ToastOptions("请正确输入"))
            } else {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$number")).apply {
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
private fun ReadSms() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val readSmsPermissionState = rememberPermissionState(Manifest.permission.READ_SMS)
    var loading by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    WeButton(text = "读取短信", loading = loading) {
        if (readSmsPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                messages = loadSmsMessages(context)
                loading = false
            }
        } else {
            readSmsPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    WePairGroup {
        items(messages) {
            WePairItem(label = it.first, value = it.second)
        }
    }
}

private suspend fun loadSmsMessages(context: Context): List<Pair<String, String>> =
    withContext(Dispatchers.IO) {
        val messages = mutableListOf<Pair<String, String>>()

        context.contentResolver
            .query(
                Uri.parse("content://sms/inbox"), null, null, null, null
            )?.use { cursor ->
                val addressIndex = cursor.getColumnIndex("address")
                val bodyIndex = cursor.getColumnIndex("body")

                while (cursor.moveToNext()) {
                    val item = Pair(cursor.getString(addressIndex), cursor.getString(bodyIndex))
                    messages.add(item)
                }
            }

        messages
    }