package top.chengdongqing.weui.feature.animation.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.core.ui.theme.WeUITheme
import top.chengdongqing.weui.feature.animation.ui.ClockScreen
import top.chengdongqing.weui.feature.animation.ui.ConstellationScreen
import top.chengdongqing.weui.feature.animation.ui.FibonacciScreen
import top.chengdongqing.weui.feature.animation.ui.RadialParticleScreen
import top.chengdongqing.weui.feature.animation.ui.RoseCurveScreen
import top.chengdongqing.weui.feature.animation.ui.SolarSystemScreen
import top.chengdongqing.weui.feature.animation.ui.SymmetryParticleScreen
import top.chengdongqing.weui.navigation.AnimationNavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.animationNavEntries(onBack: () -> Unit) {
    entry<AnimationNavKey.Clock>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ClockScreen(onBack)
    }
    entry<AnimationNavKey.SolarSystem>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        WeUITheme(isDark = true) {
            SolarSystemScreen(onBack)
        }
    }
    entry<AnimationNavKey.Fibonacci>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        FibonacciScreen(onBack)
    }
    entry<AnimationNavKey.RoseCurve>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        RoseCurveScreen(onBack)
    }
    entry<AnimationNavKey.SymmetryParticle>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        SymmetryParticleScreen(onBack)
    }
    entry<AnimationNavKey.Constellation>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        ConstellationScreen(onBack)
    }
    entry<AnimationNavKey.RadialParticle>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        RadialParticleScreen(onBack)
    }
}