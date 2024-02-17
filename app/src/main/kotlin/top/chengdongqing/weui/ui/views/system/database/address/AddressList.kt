package top.chengdongqing.weui.ui.views.system.database.address

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.actionsheet.ActionSheetItem
import top.chengdongqing.weui.ui.components.actionsheet.WeActionSheetOptions
import top.chengdongqing.weui.ui.components.actionsheet.rememberWeActionSheet
import top.chengdongqing.weui.ui.components.dialog.WeDialogOptions
import top.chengdongqing.weui.ui.components.dialog.rememberWeDialog
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LinkColor
import top.chengdongqing.weui.ui.views.system.database.address.db.Address
import top.chengdongqing.weui.ui.views.system.database.address.db.ShopDatabase
import top.chengdongqing.weui.ui.views.system.setClipboardData

@Composable
fun AddressList(navController: NavController) {
    val context = LocalContext.current
    val addressDao = remember { ShopDatabase.getInstance(context).addressDao() }
    val addresses by remember { addressDao.loadAll() }.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val actionSheet = rememberWeActionSheet()
    val dialog = rememberWeDialog()
    val toast = rememberWeToast()
    val actions = remember {
        listOf(
            ActionSheetItem("编辑"),
            ActionSheetItem("删除"),
            ActionSheetItem("复制")
        )
    }

    fun navigateToForm(id: Int? = null) {
        navController.navigate(buildString {
            append("address-form")
            if (id != null) {
                append("?id=${id}")
            }
        })
    }

    LazyColumn {
        items(addresses) { item ->
            AddressListItem(item,
                onLongClick = {
                    actionSheet.show(WeActionSheetOptions(actions) { action ->
                        when (action) {
                            0 -> {
                                navigateToForm(item.id)
                            }

                            1 -> {
                                dialog.show(WeDialogOptions(title = "确定删除该地址吗？") {
                                    coroutineScope.launch {
                                        addressDao.delete(item)
                                        toast.show(WeToastOptions("删除成功", ToastIcon.SUCCESS))
                                    }
                                })
                            }

                            2 -> {
                                setClipboardData(context, buildString {
                                    appendLine("联系人: ${item.name}")
                                    appendLine("手机号: ${item.phone}")
                                    append("详细地址: ${item.addressDetail}")
                                })
                                toast.show(WeToastOptions("已复制", ToastIcon.SUCCESS))
                            }
                        }
                    })
                }) {
                navigateToForm(item.id)
            }
        }
        item {
            AddAddressButton {
                navigateToForm()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddressListItem(address: Address, onLongClick: () -> Unit, onClick: () -> Unit) {
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
            color = FontColor1,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
    WeDivider(Modifier.padding(horizontal = 26.dp))
}

@Composable
private fun AddAddressButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
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