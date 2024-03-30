package top.chengdongqing.weui.feature.network.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.toast.ToastIcon
import top.chengdongqing.weui.core.ui.components.toast.rememberToastState

@Composable
fun HttpRequestScreen(viewModel: CartViewModel = viewModel()) {
    WeScreen(title = "HttpRequest", description = "Http请求", scrollEnabled = false) {
        val toast = rememberToastState()
        val coroutineScope = rememberCoroutineScope()
        var content by remember { mutableStateOf<String?>(null) }
        var loading by remember { mutableStateOf(false) }

        WeButton(
            "查询推荐的商品",
            loading = loading,
            width = 200.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            loading = true
            coroutineScope.launch {
                val res = viewModel.fetchRecommendProducts()
                loading = false

                if (res?.code == 200) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    content = gson.toJson(res)
                } else {
                    toast.show("请求失败", ToastIcon.FAIL)
                }
            }
        }

        content?.let {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(6.dp)
                    )
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
            }
        }
    }
}