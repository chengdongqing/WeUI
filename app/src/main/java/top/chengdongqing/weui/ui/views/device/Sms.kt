package top.chengdongqing.weui.ui.views.device

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun SmsPage() {
    Page(title = "SMS", description = "短信") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SmsSend()
            Spacer(modifier = Modifier.height(20.dp))
            SmsMessages()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SmsSend() {
    val context = LocalContext.current
    val smsPermissionState =
        rememberPermissionState(android.Manifest.permission.SEND_SMS)
    var number by remember {
        mutableStateOf("")
    }
    var content by remember {
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
    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = content,
        onValueChange = {
            content = it
        },
        placeholder = {
            Text(text = "请输入内容")
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(20.dp))
    WeButton(
        text = "发送短信",
        type = ButtonType.PLAIN
    ) {
        if (smsPermissionState.status.isGranted) {
            if (number.isEmpty() || content.isEmpty()) {
                Toast.makeText(context, "请检查是否输入正确", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$number")
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
private fun SmsMessages() {
    val context = LocalContext.current
    val readSmsPermissionState =
        rememberPermissionState(android.Manifest.permission.READ_SMS)

    val coroutineScope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }
    val messages = remember {
        mutableStateListOf<Pair<String, String>>()
    }

    WeButton(text = "读取短信", loading = loading) {
        if (readSmsPermissionState.status.isGranted) {
            loading = true
            coroutineScope.launch {
                readSmsMessages(context) { res ->
                    if (messages.isNotEmpty()) {
                        messages.clear()
                    }
                    messages.addAll(res)
                    loading = false
                }
            }
        } else {
            readSmsPermissionState.launchPermissionRequest()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    KeyValueCard(Modifier.verticalScroll(rememberScrollState())) {
        messages.forEach {
            KeyValueRow(label = it.first, value = it.second)
        }
    }
}

private suspend fun readSmsMessages(
    context: Context,
    onResult: (List<Pair<String, String>>) -> Unit
) {
    val messages = mutableListOf<Pair<String, String>>()
    val cursor = context.contentResolver
        .query(Uri.parse("content://sms/inbox"), null, null, null, null)

    withContext(Dispatchers.IO) {
        cursor?.use {
            val addressIndex = it.getColumnIndex("address")
            val bodyIndex = it.getColumnIndex("body")

            while (it.moveToNext()) {
                val item = Pair(it.getString(addressIndex), it.getString(bodyIndex))
                messages.add(item)
            }
        }
    }

    onResult(messages)
}