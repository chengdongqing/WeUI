package top.chengdongqing.weui.core.ui.components.camera

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
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.ui.theme.WeUITheme

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.getStringExtra("type")?.run { VisualMediaType.valueOf(this) }
            ?: VisualMediaType.IMAGE_AND_VIDEO

        setContent {
            WeUITheme {
                WeCamera(type, onRevoked = { finish() }) { uri, type ->
                    val intent = Intent().apply {
                        putExtra("uri", uri)
                        putExtra("type", type.name)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, CameraActivity::class.java)
    }
}

@Composable
fun rememberCameraLauncher(onChange: (Uri, VisualMediaType) -> Unit): (VisualMediaType) -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("uri", Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                (result.data?.getParcelableExtra("uri"))
            }
            val type = result.data?.getStringExtra("type")?.let { typeName ->
                VisualMediaType.valueOf(typeName)
            }

            uri?.let { type }?.let { type ->
                onChange(uri, type)
            }
        }
    }

    return {
        val intent = CameraActivity.newIntent(context).apply {
            putExtra("type", it.toString())
        }
        launcher.launch(intent)
    }
}