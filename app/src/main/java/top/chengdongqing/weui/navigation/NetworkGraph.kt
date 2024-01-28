package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.network.FileDownloadPage
import top.chengdongqing.weui.ui.views.network.FileUploadPage
import top.chengdongqing.weui.ui.views.network.HTTPRequestPage
import top.chengdongqing.weui.ui.views.network.SocketPage

fun NavGraphBuilder.networkGraph() {
    composable("http-request") {
        HTTPRequestPage()
    }
    composable("file-upload") {
        FileUploadPage()
    }
    composable("file-download") {
        FileDownloadPage()
    }
    composable("socket") {
        SocketPage()
    }
}