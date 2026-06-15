package top.chengdongqing.weui.feature.form.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.WeCheckBox
import top.chengdongqing.weui.core.ui.components.WeDivider
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.weClickable

@Composable
fun CheckboxScreen(onBack: () -> Unit) {
    val selectedValues = remember { mutableStateListOf<String>() }

    fun toggleSelection(value: String) {
        if (selectedValues.contains(value)) {
            selectedValues.remove(value)
        } else {
            selectedValues.add(value)
        }
    }

    WeScreen(
        title = "Checkbox",
        description = "复选框",
        onBack = onBack
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
                        .weClickable { toggleSelection(option.second) }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WeCheckBox(selectedValues.contains(option.second))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = option.first,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        color = WeTheme.colorScheme.textPrimary
                    )
                }

                if (index < options.lastIndex) {
                    WeDivider(modifier = Modifier.padding(start = 50.dp))
                }
            }
        }
    }
}

private val options = listOf(
    Pair("中国", "China"),
    Pair("美国", "America"),
    Pair("英国", "Britain"),
    Pair("以色列", "Israel")
)