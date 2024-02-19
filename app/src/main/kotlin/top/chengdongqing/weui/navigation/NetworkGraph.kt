package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.network.download.FileDownloadPage
import top.chengdongqing.weui.ui.views.network.request.HttpRequestPage
import top.chengdongqing.weui.ui.views.network.upload.FileUploadPage
import top.chengdongqing.weui.ui.views.network.websocket.WebSocketPage

fun NavGraphBuilder.addNetworkGraph() {
    composable("http-request") {
        HttpRequestPage()
    }
    composable("file-upload") {
        FileUploadPage()
    }
    composable("file-download") {
        FileDownloadPage()
    }
    composable("web-socket") {
        WebSocketPage()
    }
}