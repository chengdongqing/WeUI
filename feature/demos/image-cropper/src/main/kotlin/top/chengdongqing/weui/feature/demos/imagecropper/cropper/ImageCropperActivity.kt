package top.chengdongqing.weui.feature.demos.imagecropper.cropper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.core.utils.SetupStatusBarStyle

class ImageCropperActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("uri", Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("uri")
            }!!

            SetupStatusBarStyle(isDark = false)
            WeUITheme(darkTheme = true) {
                WeImageCropper(uri, onCancel = { finish() }) {
                    val intent = Intent().apply {
                        putExtra("uri", it)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ImageCropperActivity::class.java)
    }
}


@Composable
fun rememberImageCropperLauncher(onChange: (Uri) -> Unit): (Uri) -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getParcelableExtra("uri", Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    (getParcelableExtra("uri") as? Uri)
                }?.let(onChange)
            }
        }
    }

    return {
        val intent = ImageCropperActivity.newIntent(context).apply {
            putExtra("uri", it)
        }
        launcher.launch(intent)
    }
}