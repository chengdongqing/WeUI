package top.chengdongqing.weui.utils

import android.content.Context
import android.content.Intent
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode
import top.chengdongqing.weui.ui.screens.demo.gallery.preview.MediaPreviewActivity

fun Context.previewMedias(uris: List<String>, current: Int = 0) {
    val intent = MediaPreviewActivity.newIntent(this).apply {
        putExtra("uris", uris.toTypedArray())
        putExtra("current", current)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }
    startActivity(intent)
}

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