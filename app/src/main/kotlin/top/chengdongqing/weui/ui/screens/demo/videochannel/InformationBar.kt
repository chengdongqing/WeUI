package top.chengdongqing.weui.ui.screens.demo.videochannel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.theme.PlainColor

@Composable
internal fun InformationBar(video: Video, onCommentsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            AsyncImage(
                model = video.userAvatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = video.username,
                color = PlainColor,
                fontSize = 17.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconAction(icon = Icons.Outlined.ThumbUp, count = video.likes) {}
            IconAction(icon = Icons.Outlined.IosShare, count = video.forwards) {}
            IconAction(icon = Icons.Outlined.FavoriteBorder, count = video.favorites) {}
            IconAction(icon = Icons.Outlined.ModeComment, count = video.comments) {
                onCommentsClick()
            }
        }
    }
}

@Composable
private fun IconAction(icon: ImageVector, count: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "点赞",
            tint = PlainColor
        )
        Text(
            text = count.toString(),
            color = PlainColor,
            fontSize = 12.sp
        )
    }
}