package top.chengdongqing.weui.ui.views.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.feedback.rememberWeDialog
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WePicker
import java.time.LocalDate

val range = arrayOf(
    (1999..2024).map { "${it}年" },
    (1..12).map { "${it}月" },
    (1..31).map { "${it}日" }
)

@Composable
fun PickerPage() {
    var visible by remember {
        mutableStateOf(false)
    }
    var value by remember {
        val today = LocalDate.now()
        mutableStateOf(
            arrayOf(
                range[0].indexOf("${today.year}年"),
                range[1].indexOf("${today.monthValue}月"),
                range[2].indexOf("${today.dayOfMonth}日")
            )
        )
    }
    val dialog = rememberWeDialog()

    Page(title = "Picker", description = "选择器") {
        WePicker(
            visible,
            range = range,
            value = value,
            onChange = {
                value = it

                val res = range.mapIndexed { index, item ->
                    item[it[index]]
                }.joinToString("")
                dialog.open(res, onCancel = null)
            }, onCancel = {
                visible = false
            })

        WeButton(text = "选择日期") {
            visible = true
        }
    }
}