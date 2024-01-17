package top.chengdongqing.weui.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.LinkColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

enum class InformationBarType(
    val backgroundColor: Color,
    val iconColor: Color = Color.White,
    val textColor: Color = Color.White,
    val linkColor: Color = Color.White,
    val closeIconColor: Color = Color.White
) {
    WARN_STRONG(backgroundColor = Color(0xFFFA5151)),
    WARN_WEAK(
        backgroundColor = Color(0x1AFA5151),
        iconColor = Color(0xFFFA5151),
        textColor = FontColo1,
        closeIconColor = FontColo1
    ),
    INFO(backgroundColor = Color(0f, 0f, 0f, 0.3f)),
    TIPS_STRONG(backgroundColor = Color(0xFFFA9D3B)),
    TIPS_WEAK(
        backgroundColor = Color.White,
        iconColor = FontColo1,
        textColor = FontColo1,
        linkColor = LinkColor,
        closeIconColor = FontColo1
    ),
    SUCCESS(backgroundColor = PrimaryColor)
}

@Composable
fun WeInformationBar(
    content: String,
    type: InformationBarType = InformationBarType.SUCCESS,
    linkText: String? = null,
    onLink: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(type.backgroundColor)
            .padding(16.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (type == InformationBarType.SUCCESS) Icons.Outlined.Check else Icons.Outlined.Info,
            contentDescription = null,
            tint = type.iconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = content, fontSize = 14.sp, color = type.textColor)
        Spacer(modifier = Modifier.weight(1f))
        linkText?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = type.linkColor,
                modifier = Modifier.clickableWithoutRipple {
                    onLink?.invoke()
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        onClose?.let {
            Icon(
                Icons.Outlined.Close,
                contentDescription = null,
                tint = type.closeIconColor,
                modifier = Modifier.clickableWithoutRipple {
                    it()
                }
            )
        }
    }
}