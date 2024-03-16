package top.chengdongqing.weui.utils

import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode

fun NavController.chooseMedias(
    type: MediaType? = null,
    count: Int = 99
) {
    navigate("media-picker?type=${type}&count=${count}")
}

fun NavController.openLocation(
    latitude: Double,
    longitude: Double,
    name: String,
    address: String? = null,
    zoom: Int = 16
) {
    val route = buildString {
        append("location-preview/${latitude}/${longitude}")
        append("?zoom=${zoom}&name=${name}&address=${address}")
    }
    navigate(route)
}

fun NavController.scanCode(onChange: (List<Barcode>) -> Unit) {
    navigate("qrcode-scanner")
}