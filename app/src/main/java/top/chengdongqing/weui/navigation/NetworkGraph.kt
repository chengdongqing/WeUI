package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import top.chengdongqing.weui.ui.views.network.FileDownloadPage
import top.chengdongqing.weui.ui.views.network.FileUploadPage
import top.chengdongqing.weui.ui.views.network.SocketPage
import top.chengdongqing.weui.ui.views.network.request.HttpRequestPage

fun NavGraphBuilder.addNetworkGraph() {
    navigation("http-request", "network") {
        composable("http-request") {
            HttpRequestPage()
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
}