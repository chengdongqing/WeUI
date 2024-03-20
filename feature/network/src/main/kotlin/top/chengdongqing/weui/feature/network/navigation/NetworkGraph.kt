package top.chengdongqing.weui.feature.network.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.network.screens.download.FileDownloadScreen
import top.chengdongqing.weui.feature.network.screens.request.HttpRequestScreen
import top.chengdongqing.weui.feature.network.screens.upload.FileUploadScreen
import top.chengdongqing.weui.feature.network.screens.websocket.WebSocketScreen

fun NavGraphBuilder.addNetworkGraph() {
    composable("http_request") {
        HttpRequestScreen()
    }
    composable("file_upload") {
        FileUploadScreen()
    }
    composable("file_download") {
        FileDownloadScreen()
    }
    composable("web_socket") {
        WebSocketScreen()
    }
}