package top.chengdongqing.weui.core.ui.components.location.picker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.core.ui.theme.WeUITheme

class LocationPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeUITheme {
                WeLocationPicker(onCancel = { finish() }) { location ->
                    val intent = Intent().apply {
                        putExtra("location", location)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LocationPickerActivity::class.java)
    }
}