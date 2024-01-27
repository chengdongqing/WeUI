package top.chengdongqing.weui.ui.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import top.chengdongqing.weui.R
import top.chengdongqing.weui.data.menus
import top.chengdongqing.weui.model.MenuGroup
import top.chengdongqing.weui.ui.theme.BackgroundColor
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColo1

@Composable
fun HomePage(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
    ) {
        var current by rememberSaveable { mutableIntStateOf(-1) }
        LazyColumn(Modifier.padding(horizontal = 16.dp)) {
            item {
                Column(
                    Modifier
                        .background(BackgroundColor)
                        .padding(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "WeUI",
                        modifier = Modifier.height(21.dp)
                    )
                    Spacer(modifier = Modifier.height(19.dp))
                    Text(
                        text = "WeUI 是一套同微信原生视觉体验一致的基础样式库，由微信官方设计团队为微信内网页和微信小程序量身设计，令用户的使用感知更加统一。",
                        color = FontColo1,
                        fontSize = 14.sp
                    )
                }
            }
            itemsIndexed(menus) { index, item ->
                MenuGroup(item, index == current, navController) {
                    current = if (current == index) -1 else index
                }
                Spacer(Modifier.height(8.dp))
            }
            item {
                Spacer(Modifier.height(60.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_footer_link),
                        contentDescription = null,
                        modifier = Modifier.size(84.dp, 19.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuGroup(
    group: MenuGroup,
    open: Boolean,
    navController: NavHostController,
    onChange: () -> Unit
) {
    val animatedHeight by animateDpAsState(
        targetValue = if (open && group.children != null) {
            56.5.dp * group.children.size
        } else {
            0.dp
        },
        animationSpec = remember {
            tween(durationMillis = 300)
        },
        label = "MenuGroupHeightAnimation"
    )

    Column(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
    ) {
        Row(
            Modifier
                .clickable(remember {
                    MutableInteractionSource()
                }, null) {
                    if (group.path != null) {
                        navController.navigate(group.path)
                    } else {
                        onChange()
                    }
                }
                .padding(20.dp)
                .alpha(if (open) 0.5f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.title,
                modifier = Modifier.weight(1f),
                fontSize = 17.sp
            )
            Image(
                painter = painterResource(id = group.iconId),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }

        if (group.children != null) {
            Column(Modifier.height(animatedHeight)) {
                group.children.sortedBy { it.label }.forEachIndexed { index, item ->
                    Row(
                        Modifier
                            .clickable {
                                navController.navigate(item.route)
                            }
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.label,
                            modifier = Modifier.weight(1f),
                            fontSize = 17.sp
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null
                        )
                    }
                    if (index < group.children.lastIndex) {
                        Divider(
                            Modifier.padding(horizontal = 20.dp),
                            thickness = 0.5.dp,
                            color = BorderColor
                        )
                    }
                }
            }
        }
    }
}