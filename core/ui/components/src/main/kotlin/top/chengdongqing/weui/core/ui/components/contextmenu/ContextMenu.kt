package top.chengdongqing.weui.core.ui.components.contextmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import top.chengdongqing.weui.core.utils.toIntOffset

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
                .width(160.dp)
                .shadow(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.onBackground)
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
     * 显示菜单
     */
    fun show(position: IntOffset, options: List<String>, listIndex: Int)

    /**
     * 隐藏菜单
     */
    fun hide()
}

@Composable
fun rememberContextMenuState(onTap: (listIndex: Int, menuIndex: Int) -> Unit): ContextMenuState {
    val state = remember { ContextMenuStateImpl() }

    if (state.visible) {
        state.props?.let { props ->
            WeContextMenu(
                position = props.position,
                options = props.options,
                onCancel = { state.hide() },
            ) { menuIndex ->
                onTap(props.listIndex, menuIndex)
                state.hide()
            }
        }
    }

    return state
}

private class ContextMenuStateImpl : ContextMenuState {
    override var visible by mutableStateOf(false)
    var props by mutableStateOf<ContextMenuProps?>(null)

    override fun show(position: IntOffset, options: List<String>, listIndex: Int) {
        props = ContextMenuProps(position, options, listIndex)
        visible = true
    }

    override fun hide() {
        visible = false
    }
}

private data class ContextMenuProps(
    val position: IntOffset,
    val options: List<String>,
    val listIndex: Int
)

@Composable
fun Modifier.contextMenu(onLongPress: (IntOffset) -> Unit): Modifier {
    var offset by remember { mutableStateOf(Offset.Zero) }

    return this
        .onGloballyPositioned {
            offset = it.positionInParent()
        }
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                onLongPress((offset + it).toIntOffset())
            })
        }
}