package top.chengdongqing.weui.model

import androidx.annotation.DrawableRes

data class MenuItem(
    val label: String,
    val route: String
)

data class MenuGroup(
    val title: String,
    @DrawableRes
    val iconId: Int,
    val children: List<MenuItem>? = null,
    val path: String? = null
)