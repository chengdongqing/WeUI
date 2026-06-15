package top.chengdongqing.weui.feature.system.address

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.ToastIcon
import top.chengdongqing.weui.core.ui.components.WeButton
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.input.WeInput
import top.chengdongqing.weui.core.ui.components.input.WeTextarea
import top.chengdongqing.weui.core.ui.components.rememberToastState
import top.chengdongqing.weui.feature.system.address.repository.Address
import top.chengdongqing.weui.feature.system.address.repository.AddressRepositoryImpl
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AddressFormScreen(
    id: Int?,
    onBack: () -> Unit,
    addressViewModel: AddressViewModel = viewModel(factory = viewModelFactory {
        initializer {
            AddressViewModel(AddressRepositoryImpl())
        }
    })
) {
    WeScreen(
        title = "AddressForm",
        description = "${if (id == null) "新增" else "编辑"}收货地址",
        onBack = onBack
    ) {
        val address = remember { mutableStateMapOf<String, String>() }
        val isAllFilled by remember {
            derivedStateOf {
                address.filter { it.value.isNotEmpty() }.size == 3
            }
        }

        LaunchedEffect(id) {
            if (id != null) {
                addressViewModel.loadById(id)?.let {
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
            val toast = rememberToastState()

            WeButton(
                text = "确定",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                enabled = isAllFilled
            ) {
                val value = Address(
                    name = address["name"]!!,
                    phone = address["phone"]!!,
                    addressDetail = address["addressDetail"]!!
                )
                coroutineScope.launch {
                    if (id == null) {
                        addressViewModel.insert(value)
                        toast.show("添加成功", ToastIcon.Success)
                    } else {
                        addressViewModel.update(value.copy(id = id))
                        toast.show("修改成功", ToastIcon.Success)
                    }
                    delay(1000.milliseconds)

                    onBack()
                }
            }
        }
    }
}