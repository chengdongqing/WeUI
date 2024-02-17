package top.chengdongqing.weui.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import top.chengdongqing.weui.R
import top.chengdongqing.weui.data.menus
import top.chengdongqing.weui.extensions.clickableWithoutRipple
import top.chengdongqing.weui.model.MenuGroup
import top.chengdongqing.weui.model.MenuItem
import top.chengdongqing.weui.ui.components.divider.WeDivider
import top.chengdongqing.weui.ui.theme.FontColor1

@Composable
fun HomePage(navController: NavHostController) {
    var current by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        LazyColumn(Modifier.padding(horizontal = 16.dp)) {
            item { HomeHeader() }
            itemsIndexed(menus) { index, item ->
                MenuGroup(
                    item,
                    expanded = index == current,
                    navController
                ) {
                    current = if (current == index) null else index
                }
                Spacer(Modifier.height(8.dp))
            }
            item {
                Spacer(Modifier.height(60.dp))
                HomeFooter()
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(Modifier.padding(40.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "WeUI",
            modifier = Modifier.height(21.dp)
        )
        Spacer(modifier = Modifier.height(19.dp))
        Text(
            text = "WeUI 是一套同微信原生视觉体验一致的基础样式库，由微信官方设计团队为微信内网页和微信小程序量身设计，令用户的使用感知更加统一。",
            color = FontColor1,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun HomeFooter() {
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

@Composable
private fun MenuGroup(
    group: MenuGroup,
    expanded: Boolean,
    navController: NavController,
    onToggleExpand: () -> Unit
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        MenuGroupHeader(group, expanded) {
            if (group.path != null) {
                navController.navigate(group.path)
            } else {
                onToggleExpand()
            }
        }
        if (group.children != null) {
            AnimatedVisibility(visible = expanded) {
                Column {
                    val children = remember { group.children.sortedBy { it.label } }
                    children.forEachIndexed { index, item ->
                        MenuGroupItem(item, navController)
                        if (index < group.children.lastIndex) {
                            WeDivider(Modifier.padding(horizontal = 20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuGroupHeader(group: MenuGroup, expanded: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .alpha(if (expanded) 0.5f else 1f)
            .clickableWithoutRipple { onClick() }
            .padding(20.dp),
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
}

@Composable
private fun MenuGroupItem(item: MenuItem, navController: NavController) {
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
}