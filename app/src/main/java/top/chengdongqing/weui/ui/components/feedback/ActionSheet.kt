package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColo1

data class ActionSheetItem(
    val label: String,
    val description: String? = null,
    val color: Color? = null
)

/**
 * 弹出式菜单
 *
 * @param visible 是否显示
 * @param title 标题
 * @param options 菜单列表
 * @param onCancel 取消事件
 * @param onChange 菜单选中事件
 */
@Composable
fun WeActionSheet(
    visible: Boolean,
    title: String? = null,
    options: List<ActionSheetItem>,
    onCancel: () -> Unit,
    onChange: (index: Int) -> Unit
) {
    WePopup(visible, onCancel, PaddingValues(0.dp)) {
        Column {
            title?.also {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(56.dp)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        color = FontColo1,
                        fontSize = 12.sp
                    )
                }
            }

            options.forEachIndexed { index, item ->
                Divider(thickness = 0.5.dp, color = Color(0f, 0f, 0f, 0.1f))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(56.dp)
                        .clickable {
                            onCancel()
                            onChange(index)
                        }
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.label,
                        color = item.color ?: Color.Unspecified,
                        fontSize = 17.sp
                    )
                    item.description?.also {
                        Text(text = it, color = FontColo1, fontSize = 12.sp)
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(Color(0XFFF7F7F7))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        onCancel()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "取消",
                    fontSize = 17.sp
                )
            }
        }
    }
}