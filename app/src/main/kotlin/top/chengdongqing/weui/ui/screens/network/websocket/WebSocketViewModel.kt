package top.chengdongqing.weui.ui.screens.network.websocket

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketViewModel : ViewModel() {
    private var client: OkHttpClient? = null
    private var webSocket: WebSocket? = null

    val messages = mutableStateListOf<String>()

    fun open(
        url: String,
        onFailure: ((Throwable) -> Unit)? = null,
        onOpen: ((Response) -> Unit)? = null
    ) {
        val request = Request.Builder().url(url).build()
        val webSocketListener = TestWebSocketListener(
            onOpen = {
                onOpen?.invoke(it)
            },
            onFailure = {
                onFailure?.invoke(it)
            }
        ) {
            messages.add(it)
        }
        client = OkHttpClient().apply {
            webSocket = newWebSocket(request, webSocketListener)
        }
    }

    fun send(message: String): Boolean {
        return webSocket?.send(message) == true
    }

    fun close() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, null)
        client?.dispatcher?.executorService?.shutdown()
        messages.clear()
    }
}

private const val NORMAL_CLOSURE_STATUS = 1000

private class TestWebSocketListener(
    val onOpen: (Response) -> Unit,
    val onFailure: (Throwable) -> Unit,
    val onMessage: (String) -> Unit
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        onOpen(response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessage(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
        onFailure(t)
    }
}