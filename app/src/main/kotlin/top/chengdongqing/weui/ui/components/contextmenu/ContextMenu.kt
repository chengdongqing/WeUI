package top.chengdongqing.weui.ui.components.contextmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

@Composable
fun WeContextMenu(
    position: IntOffset,
    options: List<String>,
    onCancel: () -> Unit,
    onTap: (index: Int) -> Unit
) {
    Popup(offset = position, onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .width(180.dp)
                .shadow(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White)
        ) {
            options.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(56.dp)
                        .clickable { onTap(index) }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item, color = MaterialTheme.colorScheme.onPrimary, fontSize = 17.sp)
                }
            }
        }
    }
}

@Stable
interface ContextMenuState {
    /**
     * 是否显示
     */
    val visible: Boolean

    /**
     * 偏移位置
     */
    val position: IntOffset

    /**
     * 菜单列表
     */
    val options: List<String>

    /**
     * 当前被点击项的在列表中的索引
     */
    val listIndex: Int

    /**
     * 显示菜单
     */
    fun show(position: IntOffset, options: List<String>, currentIndex: Int)

    /**
     * 隐藏菜单
     */
    fun hide()
}

@Composable
fun rememberContextMenuState(onTap: (listIndex: Int, menuIndex: Int) -> Unit): ContextMenuState {
    val state = remember { ContextMenuStateImpl() }

    if (state.visible) {
        WeContextMenu(
            position = state.position,
            options = state.options,
            onCancel = { state.hide() },
        ) { menuIndex ->
            onTap(state.listIndex, menuIndex)
            state.hide()
        }
    }

    return state
}

private class ContextMenuStateImpl : ContextMenuState {
    override val visible: Boolean get() = _visible
    override val position: IntOffset get() = _position
    override val options: List<String> get() = _options
    override val listIndex: Int get() = _listIndex

    override fun show(position: IntOffset, options: List<String>, currentIndex: Int) {
        _listIndex = currentIndex
        _position = position
        _options = options
        _visible = true
    }

    override fun hide() {
        _visible = false
    }

    private var _visible by mutableStateOf(false)
    private var _position by mutableStateOf(IntOffset.Zero)
    private var _options by mutableStateOf<List<String>>(emptyList())
    private var _listIndex by mutableIntStateOf(0)
}