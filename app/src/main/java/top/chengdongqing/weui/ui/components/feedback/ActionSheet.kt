package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColo1

data class ActionSheetItem(
    val label: String,
    val description: String? = null,
    val color: Color? = null,
    val disabled: Boolean = false,
    val icon: (@Composable () -> Unit)? = null
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
    WePopup(visible, padding = PaddingValues(0.dp), onClose = onCancel) {
        Column {
            title?.let {
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
                        .alpha(if (item.disabled) 0.4f else 1f)
                        .then(if (!item.disabled) {
                            Modifier.clickable {
                                onCancel()
                                onChange(index)
                            }
                        } else Modifier)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        item.icon?.let {
                            it()
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = item.label,
                            color = item.color ?: Color.Unspecified,
                            fontSize = 17.sp
                        )
                    }
                    item.description?.let {
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

@Composable
fun rememberWeActionSheet(): WeActionSheetState {
    val visible = remember {
        mutableStateOf(false)
    }
    var localState by remember {
        mutableStateOf(ActionSheet(null, listOf()) {})
    }

    WeActionSheet(
        visible = visible.value,
        title = localState.title,
        options = localState.options,
        onCancel = { visible.value = false },
        onChange = localState.onChange
    )

    return WeActionSheetState(visible) {
        localState = it
    }
}

class WeActionSheetState(
    private val visible: MutableState<Boolean>,
    private val setLocalState: (ActionSheet) -> Unit
) {
    fun visible(): Boolean {
        return visible.value
    }

    fun show(
        title: String? = null,
        options: List<ActionSheetItem>,
        onChange: (index: Int) -> Unit
    ) {
        setLocalState(ActionSheet(title, options, onChange))
        visible.value = true
    }

    fun hide() {
        visible.value = false
    }
}

data class ActionSheet(
    val title: String?,
    val options: List<ActionSheetItem>,
    val onChange: (index: Int) -> Unit
)