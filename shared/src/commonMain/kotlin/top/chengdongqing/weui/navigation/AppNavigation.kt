package top.chengdongqing.weui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import top.chengdongqing.weui.feature.animation.navigation.animationNavEntries
import top.chengdongqing.weui.feature.basic.navigation.basicNavEntries
import top.chengdongqing.weui.feature.charts.navigation.chartsNavEntries
import top.chengdongqing.weui.feature.feedback.navigation.feedbackNavEntries
import top.chengdongqing.weui.feature.form.navigation.formNavEntries
import top.chengdongqing.weui.feature.home.HomeScreen
import top.chengdongqing.weui.feature.layers.LayersScreen
import top.chengdongqing.weui.feature.samples.navigation.samplesNavEntries
import top.chengdongqing.weui.feature.system.navigation.systemNavEntries

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
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
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategies = listOf(
            rememberListDetailSceneStrategy()
        ),
        transitionSpec = { createEnterTransition() },
        popTransitionSpec = { createExitTransition() },
        predictivePopTransitionSpec = { createExitTransition() },
        entryProvider = entryProvider {
            entry<HomeNavKey>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                HomeScreen {
                    backStack.removeAll { key -> key !is HomeNavKey }
                    backStack.add(it)
                }
            }
            entry<LayersNavKey>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                LayersScreen(goBack)
            }

            basicNavEntries(goBack)
            formNavEntries(goBack)
            feedbackNavEntries(goBack)
            systemNavEntries(backStack, goBack)
            chartsNavEntries(goBack)
            samplesNavEntries(goBack)
            animationNavEntries(goBack)
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

@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = enabled,
        onBackCompleted = onBack
    )
}