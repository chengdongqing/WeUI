package top.chengdongqing.weui.ui.views.system.database.address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeInput
import top.chengdongqing.weui.ui.components.form.WeTextarea

@Composable
fun AddressFormPage(id: String?) {
    println("id = $id")

    WePage(
        title = "AddressForm",
        description = "${if (id == null) "新增" else "编辑"}收货地址",
        backgroundColor = Color.White
    ) {
        val address = remember { mutableStateMapOf<String, String>() }
        val hasAllEntered by remember {
            derivedStateOf {
                address.filter { it.value.isNotEmpty() }.size == 3
            }
        }

        Column {
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
            WeButton(
                text = "确定",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                disabled = !hasAllEntered
            ) {

            }
        }
    }
}