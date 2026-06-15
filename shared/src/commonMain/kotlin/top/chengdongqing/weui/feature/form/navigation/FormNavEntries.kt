package top.chengdongqing.weui.feature.form.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.feature.form.ui.ButtonScreen
import top.chengdongqing.weui.feature.form.ui.CheckboxScreen
import top.chengdongqing.weui.feature.form.ui.InputScreen
import top.chengdongqing.weui.feature.form.ui.PickerScreen
import top.chengdongqing.weui.feature.form.ui.RadioScreen
import top.chengdongqing.weui.feature.form.ui.RateScreen
import top.chengdongqing.weui.feature.form.ui.SliderScreen
import top.chengdongqing.weui.feature.form.ui.SwitchScreen
import top.chengdongqing.weui.navigation.FormNavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.formNavEntries(onBack: () -> Unit) {
    entry<FormNavKey.Button>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ButtonScreen(onBack)
    }
    entry<FormNavKey.Checkbox>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        CheckboxScreen(onBack)
    }
    entry<FormNavKey.Input>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        InputScreen(onBack)
    }
    entry<FormNavKey.Picker>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        PickerScreen(onBack)
    }
    entry<FormNavKey.Radio>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        RadioScreen(onBack)
    }
    entry<FormNavKey.Rate>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        RateScreen(onBack)
    }
    entry<FormNavKey.Slider>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SliderScreen(onBack)
    }
    entry<FormNavKey.Switch>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SwitchScreen(onBack)
    }
}