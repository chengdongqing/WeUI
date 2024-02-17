package top.chengdongqing.weui.ui.views.template.gallery.preview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import top.chengdongqing.weui.R
import java.io.File

class MediaPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uris = intent.extras?.getStringArray("uris")?.map { File(it).toUri() }
        val current = intent.extras?.getInt("current", 0)
        setContent {
            MediaPreviewPage(uris ?: emptyList(), current ?: 0)
        }
    }

    override fun finish() {
        super.finish()

        // 设置退出动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.fade_out)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(0, R.anim.fade_out)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MediaPreviewActivity::class.java)
    }
}