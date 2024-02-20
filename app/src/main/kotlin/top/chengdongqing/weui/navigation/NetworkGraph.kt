package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.network.download.FileDownloadScreen
import top.chengdongqing.weui.ui.screens.network.request.HttpRequestScreen
import top.chengdongqing.weui.ui.screens.network.upload.FileUploadScreen
import top.chengdongqing.weui.ui.screens.network.websocket.WebSocketScreen

fun NavGraphBuilder.addNetworkGraph() {
    composable("http-request") {
        HttpRequestScreen()
    }
    composable("file-upload") {
        FileUploadScreen()
    }
    composable("file-download") {
        FileDownloadScreen()
    }
    composable("web-socket") {
        WebSocketScreen()
    }
}