package top.chengdongqing.weui.feature.location.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.core.ui.components.location.picker.LocationPickerActivity
import top.chengdongqing.weui.core.ui.components.location.preview.LocationPreviewActivity
import top.chengdongqing.weui.feature.location.data.model.LocationItem
import top.chengdongqing.weui.feature.location.data.model.LocationPreviewItem

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