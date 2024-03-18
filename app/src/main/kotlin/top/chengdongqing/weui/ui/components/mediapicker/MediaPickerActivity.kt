package top.chengdongqing.weui.ui.components.mediapicker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.enums.MediaType
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.SetupStatusBarStyle

class MediaPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.getStringExtra("type")?.run { MediaType.valueOf(this) }
        val count = intent.getIntExtra("count", 99)

        setContent {
            SetupStatusBarStyle(isDark = false)
            WeUITheme(darkTheme = true) {
                WeMediaPicker(type, count, onCancel = { finish() }) { medias ->
                    val intent = Intent().apply {
                        putExtra("medias", medias)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MediaPickerActivity::class.java)
    }
}