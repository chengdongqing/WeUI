package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.views.form.ButtonPage
import top.chengdongqing.weui.ui.views.form.CheckboxPage
import top.chengdongqing.weui.ui.views.form.InputPage
import top.chengdongqing.weui.ui.views.form.PickerPage
import top.chengdongqing.weui.ui.views.form.RadioPage
import top.chengdongqing.weui.ui.views.form.SliderPage
import top.chengdongqing.weui.ui.views.form.SwitchPage

fun NavGraphBuilder.formGraph() {
    composable("button") {
        ButtonPage()
    }
    composable("checkbox") {
        CheckboxPage()
    }
    composable("radio") {
        RadioPage()
    }
    composable("switch") {
        SwitchPage()
    }
    composable("slider") {
        SliderPage()
    }
    composable("picker") {
        PickerPage()
    }
    composable("input") {
        InputPage()
    }
}