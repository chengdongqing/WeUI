package top.chengdongqing.weui.feature.samples.filebrowser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.theme.R
import top.chengdongqing.weui.core.utils.clickableWithoutRipple

@Composable
internal fun NavigationBar(folders: MutableList<String>) {
    val levels = folders.size
    val lazyListState = rememberLazyListState()
    LaunchedEffect(levels) {
        if (levels > 1) {
            lazyListState.animateScrollToItem(levels - 1)
        }
    }

    Row {
        FolderLabel("内部存储", levels == 1) {
            folders.subList(1, levels).clear()
        }
        if (levels > 1) {
            FolderArrowIcon()
            LazyRow(state = lazyListState) {
                folders.slice(1..folders.lastIndex).forEachIndexed { index, item ->
                    val label = item.slice(item.lastIndexOf("/") + 1..item.lastIndex)
                    val isActive = index + 2 == levels

                    item {
                        FolderLabel(label, isActive) {
                            if (!isActive) {
                                folders.retainAll(folders.subList(0, index + 2))
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
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSecondary
        },
        fontSize = 14.sp,
        modifier = Modifier
            .background(
                color = if (isActive) {
                    MaterialTheme.colorScheme.primary.copy(0.1f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickableWithoutRipple { onClick() }
            .padding(vertical = 4.dp, horizontal = 12.dp)
    )
}

@Composable
private fun FolderArrowIcon() {
    Icon(
        painterResource(id = R.drawable.ic_arrow_right),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .size(16.dp)
    )
}