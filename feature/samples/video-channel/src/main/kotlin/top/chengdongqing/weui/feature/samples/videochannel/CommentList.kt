package top.chengdongqing.weui.feature.samples.videochannel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import top.chengdongqing.weui.core.ui.theme.FontLinkColor
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.feature.samples.videochannel.data.model.VideoComment
import top.chengdongqing.weui.feature.samples.videochannel.data.model.VideoItem

@Composable
internal fun CommentList(visible: Boolean, video: VideoItem, onClose: () -> Unit) {
    CommentPopup(visible, title = "${video.comments}条评论", onClose) {
        LazyColumn {
            item {
                VideoIntroduction(video)
            }
            items(video.commentList) {
                Box(modifier = Modifier.padding(12.dp)) {
                    CommentItem(it)
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: VideoComment) {
    Row {
        AsyncImage(
            model = comment.userAvatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comment.username,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${comment.region} ${comment.time}",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 13.sp
                    )
                }

                var liked by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickableWithoutRipple {
                        liked = !liked
                    }
                ) {
                    Text(
                        text = (comment.likes + (if (liked) 1 else 0)).toString(),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = if (liked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "赞",
                        tint = if (liked) Color.Red else MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = comment.content,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )

            comment.replayList.forEach {
                Box(modifier = Modifier.padding(vertical = 12.dp)) {
                    CommentItem(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VideoIntroduction(video: VideoItem) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        AsyncImage(
            model = video.userAvatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.username,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 15.sp
                )
                Text(
                    text = "作者",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = "${video.region} ${video.time}",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 13.sp
                )
            }

            Text(
                text = video.content,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            val tags = remember {
                listOf(
                    "乡愁记忆",
                    "农村老家",
                    "农村生活",
                    "童年回忆",
                    "从小长大的地方"
                )
            }
            FlowRow {
                tags.forEach {
                    Text(
                        text = "#$it",
                        color = FontLinkColor,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clickable { }
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
