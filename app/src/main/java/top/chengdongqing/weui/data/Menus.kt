package top.chengdongqing.weui.data

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
        "操作反馈", R.drawable.icon_nav_feedback,
        listOf(
            MenuItem("ActionSheet", "action-sheet"),
            MenuItem("Dialog", "dialog"),
            MenuItem("Popup", "popup"),
            MenuItem("Picker", "picker"),
            MenuItem("Toast", "toast"),
            MenuItem("InformationBar", "information-bar")
        )
    ),
    MenuGroup(
        "媒体组件", R.drawable.icon_nav_media,
        listOf(
            MenuItem("Gallery", "gallery"),
            MenuItem("FileBrowser", "file-browser"),
            MenuItem("MediaPicker", "media-picker"),
            MenuItem("VoiceRecorder", "voice-recorder"),
            MenuItem("ImageCropper", "image-cropper"),
            MenuItem("Camera", "camera"),
            MenuItem("Audio", "audio"),
            MenuItem("Video", "video"),
            MenuItem("LivePlayer", "live-player"),
            MenuItem("LivePusher", "live-pusher")
        )
    ),
    MenuGroup(
        "设备接口", R.drawable.icon_nav_nav,
        listOf(
            MenuItem("DeviceInfo", "device-info"),
            MenuItem("SystemStatus", "system-status"),
            MenuItem("Database", "database"),
            MenuItem("WiFi", "wifi"),
            MenuItem("Bluetooth", "bluetooth"),
            MenuItem("NFC", "nfc"),
            MenuItem("Battery", "battery"),
            MenuItem("Flashlight", "flashlight"),
            MenuItem("Vibrate", "vibrate"),
            MenuItem("Infrared", "infrared"),
            MenuItem("GPS", "gps"),
            MenuItem("Gyroscope", "gyroscope"),
            MenuItem("Compass", "compass"),
            MenuItem("Accelerometer", "accelerometer"),
            MenuItem("Clipboard", "clipboard"),
            MenuItem("ScreenBrightness", "screen-brightness"),
            MenuItem("PhoneContact", "phone-contact"),
            MenuItem("Sms", "sms"),
            MenuItem("Keyboard", "keyboard"),
            MenuItem("PhoneCalendar", "phone-calendar")
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
            MenuItem("SearchBar", "search-bar")
        )
    ),
    MenuGroup(
        "层级规范", R.drawable.icon_nav_zindex,
        path = "z-index"
    )
)