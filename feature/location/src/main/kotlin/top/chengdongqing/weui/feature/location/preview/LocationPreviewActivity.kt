package top.chengdongqing.weui.feature.location.preview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.feature.location.data.model.LocationPreviewItem

class LocationPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("location", LocationPreviewItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("location") as? LocationPreviewItem
        }!!

        setContent {
            WeUITheme {
                WeLocationPreview(location)
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LocationPreviewActivity::class.java)
    }
}