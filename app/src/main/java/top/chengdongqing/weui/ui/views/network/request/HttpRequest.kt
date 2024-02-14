package top.chengdongqing.weui.ui.views.network.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.WeToastOptions
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun HttpRequestPage(viewModel: CartViewModel = viewModel()) {
    WePage(title = "HttpRequest", description = "Http请求") {
        val toast = rememberWeToast()
        val coroutineScope = rememberCoroutineScope()
        var content by remember { mutableStateOf<String?>(null) }
        var loading by remember { mutableStateOf(false) }

        Column {
            WeButton(
                "查询购物车数量",
                loading = loading,
                width = 200.dp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                loading = true
                coroutineScope.launch {
                    val res = viewModel.fetchCount().also {
                        loading = false
                    }
                    if (res.isSuccessful && res.body() != null) {
                        content = res.body().toString()
                    } else {
                        toast.show(WeToastOptions("请求失败", ToastIcon.FAIL))
                    }
                }
            }

            content?.let {
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(20.dp)
                ) {
                    Text(text = it, fontSize = 12.sp)
                }
            }
        }
    }
}