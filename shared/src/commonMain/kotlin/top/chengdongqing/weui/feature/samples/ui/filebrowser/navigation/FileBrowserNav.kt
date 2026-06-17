package top.chengdongqing.weui.feature.samples.ui.filebrowser.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import top.chengdongqing.weui.feature.samples.ui.filebrowser.components.FileListWrapper
import top.chengdongqing.weui.navigation.createEnterTransition
import top.chengdongqing.weui.navigation.createExitTransition

@Composable
fun FileBrowserNav(
    backStack: List<NavKey>,
    rootPath: String,
    onNavigateToFolder: (String) -> Unit,
    onBack: () -> Unit
) {
    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { createEnterTransition() },
        popTransitionSpec = { createExitTransition() },
        predictivePopTransitionSpec = { createExitTransition() },
        entryProvider = entryProvider {
            entry<FileBrowserNavKey> {
                FileListWrapper(
                    filePath = it.path ?: rootPath,
                    onNavigateToFolder = onNavigateToFolder
                )
            }
        }
    )
}

@Serializable
data class FileBrowserNavKey(
    val path: String?
) : NavKey