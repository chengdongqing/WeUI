package top.chengdongqing.weui.ui.data

import androidx.annotation.DrawableRes
import top.chengdongqing.weui.R

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

val menus = listOf(
    MenuGroup(
        "表单组件", R.drawable.icon_nav_form,
        listOf(
            MenuItem("Button", "button"),
            MenuItem("Checkbox", "checkbox"),
            MenuItem("Radio", "radio"),
            MenuItem("Switch", "switch"),
            MenuItem("Slider", "slider")
        )
    ),
    MenuGroup(
        "基础组件", R.drawable.icon_nav_layout,
        listOf(
            MenuItem("Badge", "badge"),
            MenuItem("Footer", "footer"),
            MenuItem("Loading", "loading"),
            MenuItem("LoadMore", "load-more"),
            MenuItem("Progress", "progress"),
            MenuItem("Steps", "steps")
        )
    ),
    MenuGroup(
        "媒体组件", R.drawable.icon_nav_media,
        listOf(
            MenuItem("Gallery", "gallery"),
            MenuItem("File Browser", "file-browser"),
            MenuItem("Media Picker", "media-picker"),
            MenuItem("Voice Recorder", "voice-recorder"),
            MenuItem("Image Cropper", "image-cropper"),
            MenuItem("Camera", "camera"),
            MenuItem("Audio", "audio"),
            MenuItem("Video", "video"),
            MenuItem("Live Player", "live-player"),
            MenuItem("Live Pusher", "live-pusher")
        )
    ),
    MenuGroup(
        "操作反馈", R.drawable.icon_nav_feedback,
        listOf(
            MenuItem("ActionSheet", "action-sheet"),
            MenuItem("Dialog", "dialog"),
            MenuItem("Popup", "popup"),
            MenuItem("Picker", "picker"),
            MenuItem("Toast", "toast"),
            MenuItem("Information Bar", "information-bar")
        )
    ),
    MenuGroup(
        "地图组件", R.drawable.icon_nav_feedback,
        listOf(
            MenuItem("Map", "map"),
        )
    ),
    MenuGroup(
        "导航相关", R.drawable.icon_nav_nav,
        listOf(
            MenuItem("NavBar", "nav-bar"),
            MenuItem("TabBar", "tab-bar")
        )
    ),
    MenuGroup(
        "搜索相关", R.drawable.icon_nav_search,
        listOf(
            MenuItem("Search Bar", "search-bar")
        )
    ),
    MenuGroup(
        "层级规范", R.drawable.icon_nav_zindex,
        path = "z-index"
    )
)