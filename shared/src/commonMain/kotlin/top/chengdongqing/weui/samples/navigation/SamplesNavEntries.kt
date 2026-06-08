package top.chengdongqing.weui.samples.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.navigation.SamplesNavKey
import top.chengdongqing.weui.samples.ui.ClockScreen
import top.chengdongqing.weui.samples.ui.NotificationBarScreen
import top.chengdongqing.weui.samples.ui.PanoramicImageScreen
import top.chengdongqing.weui.samples.ui.SolarSystemScreen

fun EntryProviderScope<NavKey>.samplesNavEntries(onBack: () -> Unit) {
    entry<SamplesNavKey.Clock> {
        ClockScreen(onBack)
    }
    entry<SamplesNavKey.NotificationBar> {
        NotificationBarScreen(onBack)
    }
    entry<SamplesNavKey.PanoramicImage> {
        PanoramicImageScreen(onBack)
    }
    entry<SamplesNavKey.SolarSystem> {
        SolarSystemScreen(onBack)
    }
}