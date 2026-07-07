package top.chengdongqing.weui.feature.home

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavKey
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.core.ui.components.WeDivider
import top.chengdongqing.weui.core.ui.theme.InvertColorMatrix
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.navigation.MenuItem
import top.chengdongqing.weui.util.onTap
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_arrow_right
import weui_kmp.shared.generated.resources.ic_footer_link
import weui_kmp.shared.generated.resources.ic_logo

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = viewModelFactory {
        initializer {
            HomeViewModel()
        }
    }),
    onNavigateToScreen: (key: NavKey) -> Unit
) {
    val menuTree by viewModel.menuTree.collectAsStateWithLifecycle()
    val expandedIndex by viewModel.expandedIndex.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(WeTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            HomeHeader()
        }

        itemsIndexed(menuTree) { index, group ->
            MenuGroup(
                group = group,
                expanded = index == expandedIndex,
                onToggleExpand = { viewModel.setExpandedIndex(index) },
                onNavigateToScreen = onNavigateToScreen
            )
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
            HomeFooter()
        }
    }
}

@Composable
private fun HomeHeader() {
    val isDark = isSystemInDarkTheme()

    Column(modifier = Modifier.padding(40.dp)) {
        Image(
            painter = painterResource(Res.drawable.ic_logo),
            contentDescription = "WeUI",
            colorFilter = if (isDark) {
                ColorFilter.tint(Color.White.copy(0.8f))
            } else {
                null
            },
            modifier = Modifier.height(21.dp)
        )
        Spacer(modifier = Modifier.height(19.dp))
        Text(
            text = "WeUI 是一套同微信原生视觉体验一致的基础样式库，由微信官方设计团队为微信内网页和微信小程序量身设计，令用户的使用感知更加统一。",
            color = WeTheme.colorScheme.textSecondary,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun HomeFooter() {
    val isDark = isSystemInDarkTheme()

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_footer_link),
            contentDescription = null,
            colorFilter = if (isDark) {
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
    group: MenuItem,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onNavigateToScreen: (key: NavKey) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(WeTheme.colorScheme.surface)
    ) {
        MenuGroupHeader(
            group = group,
            expanded = expanded
        ) {
            if (group.navKey != null) {
                onNavigateToScreen(group.navKey)
            } else {
                onToggleExpand()
            }
        }

        if (group.children != null) {
            AnimatedVisibility(visible = expanded) {
                Column {
                    group.children.forEachIndexed { index, item ->
                        MenuGroupItem(item, onNavigateToScreen)

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
private fun MenuGroupHeader(
    group: MenuItem,
    expanded: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Row(
        Modifier
            .alpha(if (expanded) 0.5f else 1f)
            .onTap { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = group.label,
            color = WeTheme.colorScheme.textPrimary,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f)
        )

        group.iconRes?.let {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                colorFilter = if (isDark) {
                    ColorFilter.tint(Color.White.copy(0.8f))
                } else {
                    null
                },
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun MenuGroupItem(
    item: MenuItem,
    onNavigateToScreen: (key: NavKey) -> Unit
) {
    Row(
        Modifier
            .clickable {
                item.navKey?.let {
                    onNavigateToScreen(it)
                }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.label,
            color = WeTheme.colorScheme.textPrimary,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_right),
            contentDescription = null,
            tint = WeTheme.colorScheme.textSecondary
        )
    }
}