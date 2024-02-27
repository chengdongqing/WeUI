package top.chengdongqing.weui.ui.components.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.BorderColorLight
import top.chengdongqing.weui.ui.theme.FontSecondaryColorLight

enum class LoadMoreType {
    LOADING,
    EMPTY_DATA,
    ALL_LOADED
}

@Composable
fun WeLoadMore(type: LoadMoreType = LoadMoreType.LOADING) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (type) {
            LoadMoreType.LOADING -> {
                WeLoading()
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "正在加载...", color = FontSecondaryColorLight, fontSize = 14.sp)
            }

            LoadMoreType.EMPTY_DATA -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(0.5.dp)
                        .background(BorderColorLight)
                )
                Text(
                    text = "暂无数据",
                    color = FontSecondaryColorLight,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(0.5.dp)
                        .background(BorderColorLight)
                )
            }

            LoadMoreType.ALL_LOADED -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(0.5.dp)
                        .background(BorderColorLight)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(4.dp)
                        .background(BorderColorLight, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(0.5.dp)
                        .background(BorderColorLight)
                )
            }
        }
    }
}