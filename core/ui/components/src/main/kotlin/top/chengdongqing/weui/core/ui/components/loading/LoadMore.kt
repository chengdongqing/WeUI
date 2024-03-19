package top.chengdongqing.weui.core.ui.components.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.divider.WeDivider

enum class LoadMoreType {
    LOADING,
    EMPTY_DATA,
    ALL_LOADED
}

@Composable
fun WeLoadMore(modifier: Modifier = Modifier, type: LoadMoreType = LoadMoreType.LOADING) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (type) {
            LoadMoreType.LOADING -> {
                WeLoading()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "正在加载...",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )
            }

            LoadMoreType.EMPTY_DATA -> {
                WeDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "暂无数据",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                WeDivider(modifier = Modifier.weight(1f))
            }

            LoadMoreType.ALL_LOADED -> {
                WeDivider(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(4.dp)
                        .background(MaterialTheme.colorScheme.outline, CircleShape)
                )
                WeDivider(modifier = Modifier.weight(1f))
            }
        }
    }
}