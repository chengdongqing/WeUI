package top.chengdongqing.weui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.data.model.LocationItem
import top.chengdongqing.weui.data.model.LocationPreviewItem
import top.chengdongqing.weui.data.model.MediaItem
import top.chengdongqing.weui.enums.MediaType
import top.chengdongqing.weui.ui.components.location.picker.LocationPickerActivity
import top.chengdongqing.weui.ui.components.location.preview.LocationPreviewActivity
import top.chengdongqing.weui.ui.components.mediapicker.MediaPickerActivity
import top.chengdongqing.weui.ui.components.mediapreview.MediaPreviewActivity
import top.chengdongqing.weui.ui.components.qrcode.scanner.QrCodeScannerActivity

fun Context.previewMedias(medias: List<MediaItem>, current: Int = 0) {
    val intent = MediaPreviewActivity.newIntent(this).apply {
        putExtra("medias", medias.toTypedArray())
        putExtra("current", current)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }
    startActivity(intent)
}

@Composable
fun rememberPickMediasLauncher(onChange: (Array<MediaItem>) -> Unit): (type: MediaType?, count: Int) -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getParcelableArrayExtra("medias", MediaItem::class.java)?.let(onChange)
                } else {
                    @Suppress("DEPRECATION", "UNCHECKED_CAST")
                    (getParcelableArrayExtra("medias") as? Array<MediaItem>)?.let(onChange)
                }
            }
        }
    }

    return { type, count ->
        val intent = MediaPickerActivity.newIntent(context).apply {
            if (type != null) {
                putExtra("type", type.toString())
            }
            putExtra("count", count)
        }
        launcher.launch(intent)
    }
}

fun Context.previewLocation(location: LocationPreviewItem) {
    val intent = LocationPreviewActivity.newIntent(this).apply {
        putExtra("location", location)
    }
    startActivity(intent)
}

@Composable
fun rememberPickLocationLauncher(onChange: (LocationItem) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getParcelableExtra("location", LocationItem::class.java)?.let(onChange)
                } else {
                    @Suppress("DEPRECATION")
                    (getParcelableExtra("location") as? LocationItem)?.let(onChange)
                }
            }
        }
    }

    return {
        launcher.launch(LocationPickerActivity.newIntent(context))
    }
}

@Composable
fun rememberScanCodeLauncher(onChange: (Array<String>) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringArrayExtra("codes")?.let(onChange)
        }
    }

    return {
        launcher.launch(QrCodeScannerActivity.newIntent(context))
    }
}