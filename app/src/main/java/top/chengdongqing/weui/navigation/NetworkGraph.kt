package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import top.chengdongqing.weui.ui.views.network.FileDownloadPage
import top.chengdongqing.weui.ui.views.network.FileUploadPage
import top.chengdongqing.weui.ui.views.network.HTTPRequestPage
import top.chengdongqing.weui.ui.views.network.SocketPage

fun NavGraphBuilder.addNetworkGraph() {
    navigation("http", "network") {
        composable("http") {
            HTTPRequestPage()
        }
        composable("upload") {
            FileUploadPage()
        }
        composable("download") {
            FileDownloadPage()
        }
        composable("socket") {
            SocketPage()
        }
    }
}