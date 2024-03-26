package top.chengdongqing.weui.core.ui.components.mediapicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.core.ui.components.actionsheet.rememberActionSheetState
import top.chengdongqing.weui.core.ui.components.button.ButtonSize
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.mediapreview.previewMedias
import top.chengdongqing.weui.core.utils.RequestMediaPermission

@Composable
fun WeMediaPicker(
    type: VisualMediaType,
    count: Int,
    onCancel: () -> Unit,
    onConfirm: (Array<MediaItem>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        RequestMediaPermission(onRevoked = onCancel) {
            val state = rememberMediaPickerState(type, count)

            TopBar(state, onCancel)
            if (state.isLoading) {
                WeLoadMore()
            } else {
                MediaGrid(state)
                BottomBar(state) {
                    onConfirm(state.selectedMediaList.toTypedArray())
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    state: MediaPickerState,
    onCancel: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val actionSheetState = rememberActionSheetState()
    val typeOptions = remember {
        listOf(
            ActionSheetItem("选择图片", value = VisualMediaType.IMAGE),
            ActionSheetItem("选择视频", value = VisualMediaType.VIDEO),
            ActionSheetItem("图片和视频", value = VisualMediaType.IMAGE_AND_VIDEO)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 6.dp, bottom = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "返回",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 16.dp)
                .size(28.dp)
                .clickable {
                    onCancel()
                }
        )
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.outline)
                .clickable(enabled = state.isTypeEnabled) {
                    actionSheetState.show(typeOptions) { index ->
                        coroutineScope.launch {
                            state.refresh(typeOptions[index].value as VisualMediaType)
                        }
                    }
                }
                .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = typeOptions.find { it.value == state.type }?.label!!,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
            if (state.isTypeEnabled) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowDropDownCircle,
                    contentDescription = "更多",
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun BottomBar(state: MediaPickerState, onConfirm: () -> Unit) {
    val context = LocalContext.current
    val selectedCount = state.selectedMediaList.size
    val countDescription = if (selectedCount > 0) "($selectedCount)" else ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "预览$countDescription",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            modifier = Modifier
                .alpha(if (selectedCount > 0) 1f else 0.6f)
                .clickable(enabled = selectedCount > 0) {
                    context.previewMedias(state.selectedMediaList)
                }
        )
        WeButton(
            text = "确定$countDescription",
            size = ButtonSize.SMALL,
            disabled = selectedCount == 0
        ) {
            onConfirm()
        }
    }
}