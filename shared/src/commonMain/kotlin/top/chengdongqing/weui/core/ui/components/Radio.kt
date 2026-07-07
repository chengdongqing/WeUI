package top.chengdongqing.weui.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.onTap
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_check

@Composable
fun <T> WeRadioGroup(
    options: List<Pair<String, T>>,
    value: T?,
    onChange: (T) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WeTheme.colorScheme.surface)
    ) {
        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .onTap { onChange(option.second) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.first,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    color = WeTheme.colorScheme.textPrimary
                )

                if (option.second == value) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = "已选中",
                        tint = WeTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (index < options.lastIndex) {
                WeDivider(modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}