package top.chengdongqing.weui.ui.views.system.database

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.basic.WeDivider
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.ActionSheetItem
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.feedback.rememberWeDialog
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.theme.BackgroundColor
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.LinkColor

@Composable
fun DatabasePage() {
    WePage(title = "Database", description = "数据库（SQLite+Room）", padding = PaddingValues(0.dp)) {
        AddressManager()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddressManager() {
    val addresses = remember {
        mutableStateListOf(
            Address(1, "张三", "1912525497", "重庆市 江北区 江夏村"),
            Address(2, "李四", "1912525497", "重庆市 江北区 江夏村"),
            Address(3, "王五", "1912525497", "重庆市 江北区 江夏村")
        )
    }
    val actionSheet = rememberWeActionSheet()
    val dialog = rememberWeDialog()
    val toast = rememberWeToast()

    LazyColumn {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "收货地址管理", fontSize = 22.sp)
            }
        }
        itemsIndexed(addresses) { index, item ->
            AddressListItem(item,
                onClick = {

                },
                onLongClick = {
                    actionSheet.open(
                        options = listOf(
                            ActionSheetItem("编辑"),
                            ActionSheetItem("删除"),
                            ActionSheetItem("复制")
                        )
                    ) { action ->
                        when (action) {
                            0 -> {

                            }

                            1 -> {
                                dialog.open("确定删除该地址吗？") {
                                    addresses.removeAt(index)
                                    toast.open("删除成功", ToastIcon.SUCCESS)
                                }
                            }

                            2 -> {

                            }
                        }
                    }
                })
        }
        item {
            AddAddressButton()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddressListItem(address: Address, onClick: () -> Unit, onLongClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 14.dp, horizontal = 26.dp)
    ) {
        Text(text = "${address.name}  ${address.phone}", fontSize = 17.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = address.addressDetail,
            color = FontColo1,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
    WeDivider(Modifier.padding(horizontal = 26.dp))
}

@Composable
fun AddAddressButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }
            .padding(vertical = 18.dp, horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.AddCircleOutline,
            contentDescription = null,
            modifier = Modifier.size(27.dp),
            tint = LinkColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "添加地址", color = LinkColor, fontSize = 17.sp)
    }
}