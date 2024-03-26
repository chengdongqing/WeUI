package top.chengdongqing.weui.feature.network.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.network.download.FileDownloadScreen
import top.chengdongqing.weui.feature.network.request.HttpRequestScreen
import top.chengdongqing.weui.feature.network.upload.FileUploadScreen
import top.chengdongqing.weui.feature.network.websocket.WebSocketScreen

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