package top.chengdongqing.weui.feature.form.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.form.screens.ButtonScreen
import top.chengdongqing.weui.feature.form.screens.CheckboxScreen
import top.chengdongqing.weui.feature.form.screens.InputScreen
import top.chengdongqing.weui.feature.form.screens.PickerScreen
import top.chengdongqing.weui.feature.form.screens.RadioScreen
import top.chengdongqing.weui.feature.form.screens.RateScreen
import top.chengdongqing.weui.feature.form.screens.SliderScreen
import top.chengdongqing.weui.feature.form.screens.SwitchScreen

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