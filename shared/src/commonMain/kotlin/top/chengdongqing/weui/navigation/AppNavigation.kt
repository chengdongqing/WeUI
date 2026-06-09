package top.chengdongqing.weui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import top.chengdongqing.weui.charts.navigation.chartsNavEntries
import top.chengdongqing.weui.home.HomeScreen
import top.chengdongqing.weui.layers.LayersScreen
import top.chengdongqing.weui.samples.navigation.samplesNavEntries
import top.chengdongqing.weui.system.navigation.systemNavEntries

@Composable
fun AppNavigation() {
    val backStack = remember {
        mutableStateListOf<NavKey>(HomeNavKey)
    }

    val goBack: () -> Unit = {
        backStack.removeLastOrNull()
    }

    NavDisplay(
        backStack = backStack,
        onBack = goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        transitionSpec = { createEnterTransition() },
        popTransitionSpec = { createExitTransition() },
        predictivePopTransitionSpec = { createExitTransition() },
        entryProvider = entryProvider {
            entry<HomeNavKey> {
                HomeScreen {
                    backStack.add(it)
                }
            }
            entry<LayersNavKey> {
                LayersScreen(onBack = goBack)
            }

            systemNavEntries(backStack = backStack, onBack = goBack)
            chartsNavEntries(onBack = goBack)
            samplesNavEntries(onBack = goBack)
        }
    )
}

/**
 * 默认动画配置
 */
private const val TRANSITION_DURATION_MILLISECOND = 300
private val TRANSITION_ANIMATION_SPEC = tween<IntOffset>(
    durationMillis = TRANSITION_DURATION_MILLISECOND
)

private fun createEnterTransition() = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = TRANSITION_ANIMATION_SPEC
) togetherWith slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = TRANSITION_ANIMATION_SPEC
)

private fun createExitTransition() = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = TRANSITION_ANIMATION_SPEC
) togetherWith slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = TRANSITION_ANIMATION_SPEC
)