package top.chengdongqing.weui.feature.media.screens.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyGridState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.ui.components.mediapicker.rememberPickMediasLauncher
import top.chengdongqing.weui.core.ui.components.mediapreview.previewMedias
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.theme.DangerColorLight
import top.chengdongqing.weui.feature.media.R

@Composable
fun MediaPickerScreen() {
    // 基础状态
    val data = rememberSaveable { mutableStateOf<List<MediaItem>>(emptyList()) }
    val windowInfo = LocalWindowInfo.current
    val screenHeight = windowInfo.containerSize.height
    val density = LocalDensity.current
    val navigationBarsHeight = WindowInsets.navigationBars.getBottom(density)
    val bottomBarHeight = remember { mutableIntStateOf(0) }
    val gridTopOffset = remember { mutableIntStateOf(0) }

    // 重新排序状态
    val state = rememberReorderableLazyGridState(
        canDragOver = { p1, _ -> p1.key != -1 },
        onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    // 计算是否进入删除感应区
    val isReadyToRemove by remember {
        derivedStateOf {
            val draggingIndex = state.draggingItemIndex ?: return@derivedStateOf false
            val layoutInfo = state.gridState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == draggingIndex } ?: return@derivedStateOf false

            // 实时计算被拖拽图片底部Y坐标
            val currentBottomY =
                gridTopOffset.intValue + layoutInfo.offset.y + state.draggingItemTop + layoutInfo.size.height

            // 判定条件：底部超过了（屏幕高度 - 导航栏高度 - 删除拦高度）
            currentBottomY >= (screenHeight - navigationBarsHeight - bottomBarHeight.intValue)
        }
    }

    // 处理删除
    val pickerState = rememberMediaPickerReorderState(state) { item ->
        data.value = data.value.filter { it.uri != item.uri }
    }
    LaunchedEffect(state.draggingItemIndex, isReadyToRemove) {
        pickerState.update(isReadyToRemove, data.value)
    }

    WeScreen(
        title = "MediaPicker",
        description = "媒体选择器",
        containerColor = MaterialTheme.colorScheme.surface,
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        val pickMedia = rememberPickMediasLauncher { items ->
            val currentList = data.value.toMutableList()
            items.forEach {
                if (!data.value.contains(it)) {
                    currentList.add(it)
                }
            }
            data.value = currentList
        }

        Box {
            // 图片列表
            PictureGrid(
                state,
                data,
                gridTopOffset,
                onChooseMedia = pickMedia
            )

            // 底部删除栏
            BottomDeleteBar(
                visible = state.draggingItemIndex != null,
                isReady = isReadyToRemove
            ) {
                bottomBarHeight.intValue = it
            }
        }
    }
}

@Composable
private fun PictureGrid(
    state: ReorderableLazyGridState,
    data: MutableState<List<MediaItem>>,
    gridTopOffset: MutableIntState,
    onChooseMedia: (type: VisualMediaType, count: Int) -> Unit
) {
    val context = LocalContext.current

    LazyVerticalGrid(
        state = state.gridState,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxSize()
            .reorderable(state)
            .onGloballyPositioned {
                gridTopOffset.intValue = it.positionInRoot().y.toInt()
            },
    ) {
        itemsIndexed(data.value, key = { _, item -> item.uri }) { index, item ->
            ReorderableItem(state, key = item.uri) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .shadow(elevation)
                        .background(MaterialTheme.colorScheme.onSurface)
                        .detectReorderAfterLongPress(state)
                        .clickable { context.previewMedias(data.value, index) }
                ) {
                    AsyncImage(
                        model = item.uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }
        if (data.value.size < 9) {
            item(key = -1) {
                PlusButton {
                    onChooseMedia(VisualMediaType.IMAGE, 9 - data.value.size)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BottomDeleteBar(
    visible: Boolean,
    isReady: Boolean,
    onHeightChange: (Int) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    // 触发震动反馈
    LaunchedEffect(isReady) {
        if (isReady) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    AnimatedVisibility(
        visible,
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = slideInVertically(
            animationSpec = tween(150),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(150),
            targetOffsetY = { it }
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(DangerColorLight.copy(alpha = 0.8f))
                .padding(top = 8.dp, bottom = 12.dp)
                .onSizeChanged {
                    onHeightChange(it.height)
                },
        ) {
            Icon(
                imageVector = if (isReady) {
                    Icons.Outlined.DeleteSweep
                } else {
                    Icons.Outlined.Delete
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isReady) "松开即可删除" else "拖动到此处删除",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = "添加",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun rememberMediaPickerReorderState(
    reorderableState: ReorderableLazyGridState,
    onRemove: (MediaItem) -> Unit
): MediaPickerReorderState {
    return remember(reorderableState) {
        MediaPickerReorderState(reorderableState, onRemove)
    }
}

class MediaPickerReorderState(
    val reorderableState: ReorderableLazyGridState,
    private val onRemove: (MediaItem) -> Unit
) {
    var draggingItem by mutableStateOf<MediaItem?>(null)
        private set

    private var isSettledReadyToDelete = false

    fun update(isReady: Boolean, data: List<MediaItem>) {
        val currentIndex = reorderableState.draggingItemIndex
        if (currentIndex != null) {
            // 拖拽中：同步状态
            draggingItem = data.getOrNull(currentIndex)
            isSettledReadyToDelete = isReady
        } else {
            // 拖拽结束：结算
            if (isSettledReadyToDelete && draggingItem != null) {
                onRemove(draggingItem!!)
            }
            // 重置
            draggingItem = null
            isSettledReadyToDelete = false
        }
    }
}