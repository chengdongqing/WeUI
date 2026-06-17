package top.chengdongqing.weui.feature.samples.ui.filebrowser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.feature.samples.ui.filebrowser.components.NavigationBar
import top.chengdongqing.weui.feature.samples.ui.filebrowser.navigation.FileBrowserNav
import top.chengdongqing.weui.feature.samples.ui.filebrowser.navigation.FileBrowserNavKey
import top.chengdongqing.weui.util.RequestStoragePermission
import top.chengdongqing.weui.util.getStorageRootPath

@Composable
fun FileBrowserScreen(onBack: () -> Unit) {
    WeScreen(
        title = "FileBrowser",
        description = "文件浏览器",
        padding = PaddingValues(horizontal = 16.dp),
        containerColor = WeTheme.colorScheme.surface,
        scrollEnabled = false,
        onBack = onBack
    ) {
        RequestStoragePermission {
            val backStack = remember {
                mutableStateListOf<NavKey>(FileBrowserNavKey(null))
            }
            val rootPath = remember { getStorageRootPath() }
            val folders = remember { mutableStateListOf(rootPath) }

            Column {
                NavigationBar(
                    folders = folders,
                    onBack = backStack::removeLast
                )
                Spacer(modifier = Modifier.height(20.dp))
                FileBrowserNav(
                    backStack = backStack,
                    rootPath = rootPath,
                    onNavigateToFolder = {
                        backStack.add(FileBrowserNavKey(it))
                        folders.add(it)
                    },
                    onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeLast()
                            folders.removeLast()
                        } else {
                            onBack()
                        }
                    }
                )
            }
        }
    }
}