package top.chengdongqing.weui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.chengdongqing.weui.feature.basic.navigation.addBasicGraph
import top.chengdongqing.weui.feature.charts.navigation.addChartGraph
import top.chengdongqing.weui.feature.feedback.navigation.addFeedbackGraph
import top.chengdongqing.weui.feature.form.navigation.addFormGraph
import top.chengdongqing.weui.feature.hardware.navigation.addHardwareGraph
import top.chengdongqing.weui.feature.media.navigation.addMediaGraph
import top.chengdongqing.weui.feature.network.navigation.addNetworkGraph
import top.chengdongqing.weui.feature.qrcode.navigation.addQrCodeGraph
import top.chengdongqing.weui.feature.samples.navigation.addSamplesGraph
import top.chengdongqing.weui.feature.system.navigation.addSystemGraph
import top.chengdongqing.weui.home.HomeScreen
import top.chengdongqing.weui.layers.LayersScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val locationNav = remember { LocationNavImpl() }

    NavHost(
        navController,
        startDestination = "home",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("home") {
            HomeScreen {
                navController.navigate(it)
            }
        }
        addBasicGraph()
        addFormGraph()
        addFeedbackGraph()
        addMediaGraph(navController)
        addSystemGraph(navController)
        addNetworkGraph()
        addHardwareGraph()
        addChartGraph()
        addQrCodeGraph()
        addSamplesGraph()
        composable("layers") {
            LayersScreen()
        }

        // 动态添加地图模块
        locationNav.addLocationGraph(this)
    }
}

/**
 * 为什么官方文档不建议直接将 NavController 传递给可组合项（Composable）?
 *
 * 主要是基于架构和测试的考量。直接在可组合项中操作导航可能导致几个问题：
 *
 * 1. 耦合性增加：直接使用 NavController 会让你的可组合项和导航逻辑紧密耦合。这意味着可组合项需要知道导航的具体细节，比如导航路由的名称。这样一来，你的可组合项不仅要负责显示 UI，还要管理导航逻辑，违反了单一职责原则。
 *
 * 2. 测试困难：当可组合项直接依赖 NavController 时，单元测试会更加困难。你需要模拟 NavController 或创建一个环境，其中包含 NavController 的实例。这增加了测试的复杂性，尤其是当导航逻辑变得复杂时。
 *
 * 3. 重组问题：在可组合函数中直接调用 NavController.navigate() 可能会在每次重组时触发导航动作。由于 Compose 会根据状态变化频繁重组，这可能导致意料之外的导航行为或性能问题。
 *
 * 4. 灵活性减少：将导航逻辑紧密绑定到可组合项中，减少了组件的可重用性。如果将来你想要在不同的上下文中重用同一个可组合项，但是导航目标不同，你可能需要重写或调整可组合项以适应新的需求。
 *
 * 5. 遵循最佳实践：Jetpack Compose 提倡使用事件回调来处理用户交互和相关的UI逻辑，而将应用逻辑（如导航）留给组件的调用者来处理。这样做不仅使得你的UI组件更加通用和可测试，也使得架构更加清晰，因为它强制进行了逻辑分层。
 *
 * 尽管直接传递 NavController 到组件可能在初期看起来没有问题，但随着应用规模的增长和需求的变化，上述问题可能会逐渐显现，导致代码难以维护和扩展。因此，遵循官方的建议可以使你的代码更加健壮，易于测试和维护。
 *
 *
 *
 * NavHost提供了四种主要的动画配置：enterTransition、exitTransition、popEnterTransition和popExitTransition。
 *
 * 1. enterTransition（进入转换）：当导航到一个新的组件时，这个新组件的进入动画。简而言之，当你从组件A导航到组件B时，组件B的`enterTransition`定义了它是如何出现在屏幕上的。
 *
 * 2. exitTransition（退出转换）：当从当前组件导航到另一个组件时，当前组件的退出动画。当你从组件A导航到组件B时，组件A的`exitTransition`定义了它是如何从屏幕上消失的。
 *
 * 3. popEnterTransition（弹出进入转换）：当从当前组件回退到前一个组件时，那个前一个组件的进入动画。如果你在组件B中按下返回按钮，预期回到组件A，那么组件A的`popEnterTransition`定义了它是如何重新出现在屏幕上的。
 *
 * 4. popExitTransition（弹出退出转换）：当通过回退操作退出当前组件时，当前组件的退出动画。如果你在组件B中按下返回按钮回到组件A，那么组件B的`popExitTransition`定义了它是如何从屏幕上消失的。
 */