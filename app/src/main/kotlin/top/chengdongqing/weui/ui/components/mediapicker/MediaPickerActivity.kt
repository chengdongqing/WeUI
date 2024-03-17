package top.chengdongqing.weui.ui.components.mediapicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.enums.MediaType

class MediaPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.getStringExtra("type")?.run { MediaType.valueOf(this) }
        val count = intent.getIntExtra("count", 99)

        setContent {
            WeMediaPicker(type = type, count = count, onCancel = { finish() }) { medias ->
                val intent = Intent().apply {
                    putExtra("medias", medias)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MediaPickerActivity::class.java)
    }
}