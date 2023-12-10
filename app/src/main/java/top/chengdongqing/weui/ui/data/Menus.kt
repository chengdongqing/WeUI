package top.chengdongqing.weui.ui.data

import androidx.annotation.DrawableRes
import top.chengdongqing.weui.R

data class MenuItem(
    val label: String,
    val path: String
)

data class MenuGroup(
    val title: String,
    @DrawableRes
    val iconId: Int,
    val children: List<MenuItem>? = null,
    val path: String? = null
)

val menus = listOf(
    MenuGroup(
        "表单", R.drawable.icon_nav_form,
        listOf(
            MenuItem("Button", "button"),
            MenuItem("Form", "form"),
            MenuItem("Checkbox", "checkbox"),
            MenuItem("Radio", "radio"),
            MenuItem("Switch", "switch"),
            MenuItem("Slider", "slider"),
            MenuItem("List", "list"),
            MenuItem("Uploader", "uploader")
        )
    ),
    MenuGroup(
        "基础组件", R.drawable.icon_nav_layout,
        listOf(
            MenuItem("MediaPicker", "mediaPicker"),
            MenuItem("Article", "article"),
            MenuItem("Badge", "badge"),
            MenuItem("Flex", "flex"),
            MenuItem("Footer", "footer"),
            MenuItem("Gallery", "gallery"),
            MenuItem("Grid", "grid"),
            MenuItem("Icons", "icons"),
            MenuItem("Loading", "loading"),
            MenuItem("LoadMore", "loadMore"),
            MenuItem("Panel", "panel"),
            MenuItem("Preview", "preview"),
            MenuItem("Progress", "progress"),
            MenuItem("Steps", "steps")
        )
    ),
    MenuGroup(
        "操作反馈", R.drawable.icon_nav_feedback,
        listOf(
            MenuItem("ActionSheet", "actionSheet"),
            MenuItem("Dialog", "dialog"),
            MenuItem("Popup", "popup"),
            MenuItem("Msg", "msg"),
            MenuItem("Picker", "picker"),
            MenuItem("Toast", "toast"),
            MenuItem("Information Bar", "informationBar")
        )
    ),
    MenuGroup(
        "导航相关", R.drawable.icon_nav_nav,
        listOf(
            MenuItem("Navbar", "navbar"),
            MenuItem("TabBar", "tabBar")
        )
    ),
    MenuGroup(
        "搜索相关", R.drawable.icon_nav_search,
        listOf(
            MenuItem("Search Bar", "searchBar")
        )
    ),
    MenuGroup(
        "层级规范", R.drawable.icon_nav_zindex,
        path = "/z-index"
    )
)