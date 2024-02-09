package top.chengdongqing.weui.ui.views.system.database.address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.WeToastOptions
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeInput
import top.chengdongqing.weui.ui.components.form.WeTextarea
import top.chengdongqing.weui.ui.views.system.database.address.db.Address
import top.chengdongqing.weui.ui.views.system.database.address.db.ShopDatabase

@Composable
fun AddressFormPage(navController: NavController, id: Int?) {
    WePage(
        title = "AddressForm",
        description = "${if (id == null) "新增" else "编辑"}收货地址",
        backgroundColor = Color.White
    ) {
        val context = LocalContext.current
        val addressDao = remember { ShopDatabase.getInstance(context).addressDao() }
        val address = remember { mutableStateMapOf<String, String>() }
        val hasAllEntered by remember {
            derivedStateOf {
                address.filter { it.value.isNotEmpty() }.size == 3
            }
        }

        LaunchedEffect(id) {
            if (id != null) {
                addressDao.loadById(id)?.let {
                    address["name"] = it.name
                    address["phone"] = it.phone
                    address["addressDetail"] = it.addressDetail
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            WeInput(
                value = address["name"],
                label = "联系人",
                placeholder = "请输入"
            ) {
                address["name"] = it
            }
            WeInput(
                value = address["phone"],
                label = "手机号",
                placeholder = "请输入",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            ) {
                address["phone"] = it
            }
            WeTextarea(
                value = address["addressDetail"],
                label = "详细地址",
                placeholder = "请输入"
            ) {
                address["addressDetail"] = it
            }
            Spacer(modifier = Modifier.height(20.dp))

            val coroutineScope = rememberCoroutineScope()
            val toast = rememberWeToast()

            WeButton(
                text = "确定",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                disabled = !hasAllEntered
            ) {
                val value = Address(
                    name = address["name"]!!,
                    phone = address["phone"]!!,
                    addressDetail = address["addressDetail"]!!
                )
                coroutineScope.launch {
                    if (id == null) {
                        addressDao.insert(value)
                        toast.show(WeToastOptions("添加成功", ToastIcon.SUCCESS))
                    } else {
                        addressDao.update(value.copy(id = id))
                        toast.show(WeToastOptions("修改成功", ToastIcon.SUCCESS))
                    }
                    delay(1000)
                    navController.popBackStack()
                }
            }
        }
    }
}