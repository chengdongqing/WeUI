package top.chengdongqing.weui.home.data.model

import androidx.annotation.DrawableRes

internal data class MenuGroup(
    val title: String,
    @DrawableRes
    val iconId: Int,
    val children: List<MenuItem>? = null,
    val path: String? = null
)