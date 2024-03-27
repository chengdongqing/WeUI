package top.chengdongqing.weui.feature.samples.filebrowser.filelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.Result
import top.chengdongqing.weui.core.ui.components.loading.LoadMoreType
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.refreshview.WeRefreshView
import top.chengdongqing.weui.core.utils.openFile
import top.chengdongqing.weui.feature.samples.filebrowser.data.model.FileItem
import java.io.File

@Composable
fun FileListScreen(
    path: String,
    fileViewModel: FileListViewModel = viewModel(),
    navigateToFolder: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(path) {
        if (fileViewModel.fileListResult == Result.Loading) {
            fileViewModel.refresh(path)
        }
    }

    WeRefreshView(onRefresh = {
        fileViewModel.refresh(path)
    }) {
        FileList(
            fileListResult = fileViewModel.fileListResult,
            navigateToFolder = {
                navigateToFolder(it)
            }
        ) {
            coroutineScope.launch {
                fileViewModel.refresh(path)
            }
        }
    }
}

@Composable
private fun FileList(
    fileListResult: Result<List<FileItem>>,
    navigateToFolder: (String) -> Unit,
    onDeleted: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when (fileListResult) {
            is Result.Loading -> {
                item {
                    WeLoadMore()
                }
            }

            is Result.Success -> {
                val fileList = fileListResult.data
                if (fileList.isEmpty()) {
                    item {
                        WeLoadMore(type = LoadMoreType.ALL_LOADED)
                    }
                }
                if (fileList.isNotEmpty()) {
                    items(fileList, key = { it.path }) { item ->
                        FileListItem(
                            item,
                            onFolderClick = {
                                navigateToFolder(item.path)
                            },
                            onFileClick = {
                                context.openFile(File(item.path), item.mimeType ?: "*/*")
                            },
                            onDeleted
                        )
                    }
                }
            }

            else -> {}
        }
    }
}