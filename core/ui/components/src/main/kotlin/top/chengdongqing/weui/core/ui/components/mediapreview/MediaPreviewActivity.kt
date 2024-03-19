package top.chengdongqing.weui.core.ui.components.mediapreview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.ui.components.R

class MediaPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val medias = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayExtra("medias", MediaItem::class.java)
        } else {
            @Suppress("DEPRECATION", "UNCHECKED_CAST")
            intent.getParcelableArrayExtra("medias") as? Array<MediaItem>
        } ?: emptyArray()
        val current = intent.getIntExtra("current", 0)

        setContent {
            MediaPreviewScreen(medias, current)
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