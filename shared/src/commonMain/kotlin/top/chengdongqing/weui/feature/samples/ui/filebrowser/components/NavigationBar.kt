package top.chengdongqing.weui.feature.samples.ui.filebrowser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.weClickable
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_arrow_right

@Composable
internal fun NavigationBar(
    folders: MutableList<String>,
    onBack: () -> Unit
) {
    val levels = folders.size
    val listState = rememberLazyListState()
    LaunchedEffect(levels) {
        listState.animateScrollToItem(folders.lastIndex)
    }

    Row {
        FolderLabel("内部存储", levels == 1) {
            if (folders.size > 1) {
                repeat(folders.size - 1) {
                    onBack()
                }
                folders.subList(1, levels).clear()
            }
        }
        if (levels > 1) {
            FolderArrowIcon()
            LazyRow(state = listState) {
                folders.slice(1..folders.lastIndex).forEachIndexed { index, item ->
                    val label = item.slice(item.lastIndexOf("/") + 1..item.lastIndex)
                    val isActive = index + 2 == levels

                    item {
                        FolderLabel(label, isActive) {
                            if (!isActive) {
                                val toRemoveFolders = folders.slice(index + 2..folders.lastIndex)
                                folders.removeAll(toRemoveFolders)
                                repeat(toRemoveFolders.size) {
                                    onBack()
                                }
                            }
                        }
                        if (index < levels - 2) {
                            FolderArrowIcon()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FolderLabel(label: String, isActive: Boolean, onClick: () -> Unit) {
    Text(
        text = label,
        color = if (isActive) {
            WeTheme.colorScheme.primary
        } else {
            WeTheme.colorScheme.textSecondary
        },
        fontSize = 13.sp,
        modifier = Modifier
            .background(
                color = if (isActive) {
                    WeTheme.colorScheme.primary.copy(0.1f)
                } else {
                    WeTheme.colorScheme.surfaceVariant
                },
                shape = RoundedCornerShape(16.dp)
            )
            .weClickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 12.dp)
    )
}

@Composable
private fun FolderArrowIcon() {
    Icon(
        painter = painterResource(Res.drawable.ic_arrow_right),
        contentDescription = null,
        tint = WeTheme.colorScheme.textSecondary,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .size(16.dp)
    )
}