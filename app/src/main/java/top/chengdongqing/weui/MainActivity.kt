package top.chengdongqing.weui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.chengdongqing.weui.ui.components.WeDialogHolder
import top.chengdongqing.weui.ui.theme.WeUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeUITheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        WeDialogHolder(title = "弹窗标题", content = "确定要删除吗？", onOk = {
                            it.value = false
                        }, onCancel = {
                            it.value = false
                        }) {
                            Button(onClick = { it.value = true }) {
                                Text(text = "打开弹窗")
                            }
                        }
                    }
                }
            }
        }
    }
}

