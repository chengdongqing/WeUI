package top.chengdongqing.weui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.R
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.theme.FontColorDark
import top.chengdongqing.weui.core.ui.theme.InvertColorMatrix
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.home.data.MenuDataProvider
import top.chengdongqing.weui.home.data.model.MenuGroup
import top.chengdongqing.weui.home.data.model.MenuItem

@Composable
fun HomeScreen(onNavigateTo: (route: String) -> Unit) {
    var current by rememberSaveable { mutableStateOf<Int?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item { HomeHeader() }
        itemsIndexed(MenuDataProvider.menuGroups) { index, item ->
            MenuGroup(
                item,
                expanded = index == current,
                onNavigateTo
            ) {
                current = if (current == index) null else index
            }
        }
        item {
            Spacer(Modifier.height(60.dp))
            HomeFooter()
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(Modifier.padding(40.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "WeUI",
            colorFilter = if (MaterialTheme.homeColorScheme.iconColor != Color.Unspecified) ColorFilter.tint(
                MaterialTheme.homeColorScheme.iconColor
            ) else null,
            modifier = Modifier.height(21.dp)
        )
        Spacer(modifier = Modifier.height(19.dp))
        Text(
            text = "WeUI 是一套同微信原生视觉体验一致的基础样式库，由微信官方设计团队为微信内网页和微信小程序量身设计，令用户的使用感知更加统一。",
            color = MaterialTheme.homeColorScheme.headerColor,
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
            colorFilter = if (MaterialTheme.homeColorScheme.iconColor != Color.Unspecified) {
                ColorFilter.colorMatrix(InvertColorMatrix)
            } else {
                null
            },
            modifier = Modifier.size(84.dp, 19.dp)
        )
    }
}

@Composable
private fun MenuGroup(
    group: MenuGroup,
    expanded: Boolean,
    onNavigateTo: (route: String) -> Unit,
    onToggleExpand: () -> Unit
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        MenuGroupHeader(group, expanded) {
            if (group.path != null) {
                onNavigateTo(group.path)
            } else {
                onToggleExpand()
            }
        }
        if (group.children != null) {
            AnimatedVisibility(visible = expanded) {
                Column {
                    val children = remember { group.children.sortedBy { it.label } }
                    children.forEachIndexed { index, item ->
                        MenuGroupItem(item, onNavigateTo)
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
            color = MaterialTheme.homeColorScheme.fontColor,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = group.iconId),
            contentDescription = null,
            colorFilter = if (MaterialTheme.homeColorScheme.iconColor != Color.Unspecified) ColorFilter.tint(
                MaterialTheme.homeColorScheme.iconColor
            ) else null,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun MenuGroupItem(item: MenuItem, onNavigateTo: (route: String) -> Unit) {
    Row(
        Modifier
            .clickable {
                onNavigateTo(item.route)
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.label,
            color = MaterialTheme.homeColorScheme.fontColor,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = MaterialTheme.homeColorScheme.arrowColor
        )
    }
}

private data class HomeColors(
    val iconColor: Color,
    val headerColor: Color,
    val fontColor: Color,
    val arrowColor: Color
)

private val MaterialTheme.homeColorScheme: HomeColors
    @Composable
    get() = HomeColors(
        iconColor = if (isSystemInDarkTheme()) FontColorDark else Color.Unspecified,
        headerColor = colorScheme.onSecondary,
        fontColor = colorScheme.onPrimary,
        arrowColor = colorScheme.onSecondary
    )