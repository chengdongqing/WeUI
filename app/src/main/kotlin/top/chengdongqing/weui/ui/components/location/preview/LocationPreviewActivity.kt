package top.chengdongqing.weui.ui.components.location.preview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.ui.theme.WeUITheme

class LocationPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val zoom = intent.getFloatExtra("zoom", 16f)
        val name = intent.getStringExtra("name") ?: "位置"
        val address = intent.getStringExtra("address")

        setContent {
            WeUITheme {
                WeLocationPreview(latitude, longitude, zoom, name, address)
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LocationPreviewActivity::class.java)
    }
}