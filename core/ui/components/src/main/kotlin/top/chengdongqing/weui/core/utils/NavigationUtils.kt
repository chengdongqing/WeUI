package top.chengdongqing.weui.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.ui.components.mediapicker.MediaPickerActivity
import top.chengdongqing.weui.core.ui.components.mediapreview.MediaPreviewActivity

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