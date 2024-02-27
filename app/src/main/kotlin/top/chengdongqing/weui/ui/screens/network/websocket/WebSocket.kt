package top.chengdongqing.weui.ui.screens.network.websocket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.input.WeTextarea
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast

@Composable
fun WebSocketScreen(socketViewModel: WebSocketViewModel = viewModel()) {
    WeScreen(title = "WebSocket", description = "双向通信") {
        var connected by remember { mutableStateOf(false) }

        DisposableEffect(Unit) {
            onDispose {
                socketViewModel.close()
            }
        }

        if (!connected) {
            UnconnectedScreen(socketViewModel) { connected = it }
        } else {
            ConnectedScreen(socketViewModel) { connected = it }
        }
    }
}

@Composable
private fun UnconnectedScreen(
    socketViewModel: WebSocketViewModel,
    setConnectState: (Boolean) -> Unit
) {
    var connecting by remember { mutableStateOf(false) }
    val toast = rememberWeToast()

    WeButton(
        text = if (connecting) "连接中..." else "连接WebSocket",
        loading = connecting
    ) {
        connecting = true
        socketViewModel.open("wss://echo.websocket.org", onFailure = {
            toast.show(ToastOptions("连接失败", ToastIcon.FAIL))
            connecting = false
            setConnectState(false)
        }) {
            toast.show(ToastOptions("连接成功", ToastIcon.SUCCESS))
            setConnectState(true)
        }
    }
}

@Composable
private fun ConnectedScreen(
    socketViewModel: WebSocketViewModel,
    setConnectState: (Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    val toast = rememberWeToast()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        WeTextarea(value = text, placeholder = "请输入内容") { text = it }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "发送") {
            if (text.isNotBlank()) {
                socketViewModel.send(text)
                keyboardController?.hide()
                text = ""
            } else {
                toast.show(ToastOptions("请输入内容", ToastIcon.FAIL))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        WeButton(text = "断开连接", type = ButtonType.DANGER) {
            socketViewModel.close()
            setConnectState(false)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "收到的消息", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
        WePairGroup {
            itemsIndexed(socketViewModel.messages) { index, item ->
                WePairItem(label = "${index + 1}", value = item)
            }
        }
    }
}