package top.chengdongqing.weui.ui.views.media.file

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.formatTime
import java.io.File

@Composable
fun FileBrowserPage() {
    Page(title = "FileBrowser", description = "文件浏览器", backgroundColor = Color.White) {
        FileBrowser("/storage/emulated/0")
    }
}

@Composable
fun FileBrowser(folderPath: String) {
    val folder = File(folderPath)
    val files = remember(folderPath) {
        folder.listFiles()?.toList() ?: emptyList()
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(files) {
            FileListItem(it)
        }
    }
}

@Composable
fun FileListItem(file: File) {
    Row(
        modifier = Modifier.clickableWithoutRipple {
            if (file.isDirectory) {
                // navigate into the folder
            } else {
                // open the file
            }
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (file.isDirectory) {
            Image(
                painter = painterResource(id = R.drawable.ic_folder),
                contentDescription = "文件夹",
                modifier = Modifier.size(48.dp)
            )
        } else {
            Icon(imageVector = Icons.Default.List, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = file.name,
                color = FontColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row {
                Text(
                    text = buildAnnotatedString {
                        append(formatTime(file.lastModified()))
                        append(" | ")
                        append((file.listFiles()?.size ?: 0).toString() + "项")
                    },
                    color = FontColo1,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(painterResource(id = R.drawable.ic_arrow_right), contentDescription = "进入")
    }
}