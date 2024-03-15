package top.chengdongqing.weui.ui.components.mediapicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import top.chengdongqing.weui.ui.components.button.ButtonSize
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.theme.WeUITheme
import top.chengdongqing.weui.utils.SetupStatusBarStyle

@Composable
fun WeMediaPicker(
    navController: NavController,
    pickerViewModel: MediaPickerViewModel = viewModel(
        factory = MediaPickerViewModelFactory(LocalContext.current)
    )
) {
    SetupStatusBarStyle(isDark = false)
    WeUITheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopBar(navController)
            MediaGrid(pickerViewModel)
            BottomBar()
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 16.dp, top = 6.dp, bottom = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "返回",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    navController.popBackStack()
                }
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.outline)
                .clickable { }
                .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "图片和视频", color = MaterialTheme.colorScheme.onPrimary, fontSize = 17.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDownCircle,
                contentDescription = "更多",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun BottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "预览(14)", color = MaterialTheme.colorScheme.onPrimary, fontSize = 17.sp)
        WeButton(text = "发送", size = ButtonSize.SMALL)
    }
}