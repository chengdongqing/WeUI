package top.chengdongqing.weui.feature.samples.ui.filebrowser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.refreshview.WeRefreshView
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.feature.samples.data.model.FileNode
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FileListWrapper(
    fileNode: FileNode,
    viewModel: FileListViewModel = viewModel(factory = viewModelFactory {
        initializer {
            FileListViewModel(fileNode)
        }
    }),
    onNavigateToFolder: (FileNode) -> Unit
) {
    val scope = rememberCoroutineScope()

    WeRefreshView(onRefresh = {
        delay(1000.milliseconds)
        viewModel.getFileList(fileNode)
    }) {
        FileList(
            viewModel = viewModel,
            onNavigateToFolder = onNavigateToFolder,
            onRefresh = {
                scope.launch {
                    viewModel.getFileList(fileNode)
                }
            }
        )
    }
}

@Composable
private fun FileList(
    viewModel: FileListViewModel,
    onNavigateToFolder: (FileNode) -> Unit,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fileList = uiState.fileList

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WeTheme.colorScheme.surface),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when {
            uiState.isLoading -> {
                item {
                    WeLoadMore()
                }
            }

            fileList.isEmpty() -> {
                item {
                    WeLoadMore(type = LoadMoreType.AllLoaded)
                }
            }

            else -> {
                items(fileList, key = { it.node.id }) { fileItem ->
                    FileListItem(
                        file = fileItem,
                        onFolderClick = {
                            onNavigateToFolder(fileItem.node)
                        },
                        onFileClick = {
                            scope.launch {
                                viewModel.openFile(fileItem)
                            }
                        },
                        onDelete = {
                            scope.launch {
                                viewModel.deleteFile(fileItem.node)
                                onRefresh()
                            }
                        }
                    )
                }
            }
        }
    }
}