package top.chengdongqing.weui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.ui.screens.form.ButtonScreen
import top.chengdongqing.weui.ui.screens.form.CheckboxScreen
import top.chengdongqing.weui.ui.screens.form.InputScreen
import top.chengdongqing.weui.ui.screens.form.PickerScreen
import top.chengdongqing.weui.ui.screens.form.RadioScreen
import top.chengdongqing.weui.ui.screens.form.RateScreen
import top.chengdongqing.weui.ui.screens.form.SliderScreen
import top.chengdongqing.weui.ui.screens.form.SwitchScreen

fun NavGraphBuilder.addFormGraph() {
    composable("button") {
        ButtonScreen()
    }
    composable("checkbox") {
        CheckboxScreen()
    }
    composable("radio") {
        RadioScreen()
    }
    composable("switch") {
        SwitchScreen()
    }
    composable("slider") {
        SliderScreen()
    }
    composable("picker") {
        PickerScreen()
    }
    composable("input") {
        InputScreen()
    }
    composable("Rate") {
        RateScreen()
    }
}