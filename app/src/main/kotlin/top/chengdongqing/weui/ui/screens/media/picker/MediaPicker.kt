package top.chengdongqing.weui.ui.screens.media.picker

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyGridState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import top.chengdongqing.weui.R
import top.chengdongqing.weui.enums.MediaType
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.DangerColorLight
import top.chengdongqing.weui.utils.detectDragGesturesAfterLongPressWithoutConsume
import top.chengdongqing.weui.utils.previewMedias
import top.chengdongqing.weui.utils.rememberMediaPicker

@Composable
fun MediaPickerScreen() {
    WeScreen(
        title = "MediaPicker",
        description = "媒体选择器",
        containerColor = MaterialTheme.colorScheme.surface,
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        val data = rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
        val state = rememberReorderableLazyGridState(onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }, canDragOver = { p1, _ ->
            p1.key != -1
        })
        val pickMedia = rememberMediaPicker {
            it.forEach { item ->
                if (!data.value.contains(item)) {
                    data.value += item
                }
            }
        }

        val density = LocalDensity.current
        val configuration = LocalConfiguration.current
        val screenHeight = remember { density.run { configuration.screenHeightDp.dp.toPx() } }
        val bottomBarHeight = remember { mutableIntStateOf(0) }
        val currentItemHeight = remember { mutableIntStateOf(0) }
        val currentPositionY = remember { mutableFloatStateOf(0f) }

        Box {
            PictureGrid(
                state,
                data,
                screenHeight,
                currentItemHeight,
                currentPositionY,
                bottomBarHeight,
                pickMedia
            )

            val isReadying by remember {
                derivedStateOf {
                    screenHeight - currentPositionY.floatValue - currentItemHeight.intValue <= bottomBarHeight.intValue
                }
            }
            DeleteActionBottomBar(
                visible = state.draggingItemIndex != null,
                isReadying
            ) {
                bottomBarHeight.intValue = it
            }
        }
    }
}

@Composable
private fun PictureGrid(
    state: ReorderableLazyGridState,
    data: MutableState<List<String>>,
    screenHeight: Float,
    currentItemHeight: MutableIntState,
    currentPositionY: MutableFloatState,
    bottomBarHeight: MutableIntState,
    onChooseMedia: (type: MediaType, count: Int) -> Unit
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
    ) {
        itemsIndexed(data.value, key = { _, item -> item }) { index, item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                var positionY by remember { mutableFloatStateOf(0f) }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .onSizeChanged {
                            if (currentItemHeight.intValue == 0) {
                                currentItemHeight.intValue = it.height
                            }
                        }
                        .onGloballyPositioned {
                            positionY = it.positionInRoot().y
                        }
                        .shadow(elevation)
                        .background(MaterialTheme.colorScheme.onSurface)
                        .detectReorderAfterLongPress(state)
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPressWithoutConsume(onDragEnd = {
                                if (positionY >= screenHeight - currentItemHeight.intValue - bottomBarHeight.intValue) {
                                    data.value -= item
                                }
                            }) { _, _ ->
                                currentPositionY.floatValue = positionY
                            }
                        }
                        .clickable { context.previewMedias(data.value, index) }
                ) {
                    AsyncImage(
                        model = item,
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
                    onChooseMedia(MediaType.IMAGE, 9 - data.value.size)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.DeleteActionBottomBar(
    visible: Boolean,
    isReadying: Boolean,
    onHeightChange: (Int) -> Unit
) {
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
                imageVector = if (isReadying) {
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
                text = if (isReadying) "松开即可删除" else "拖动到此处删除",
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