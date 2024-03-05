package top.chengdongqing.weui.ui.screens.home

import androidx.annotation.DrawableRes
import top.chengdongqing.weui.R

internal data class MenuItem(
    val label: String,
    val route: String
)

internal data class MenuGroup(
    val title: String,
    @DrawableRes
    val iconId: Int,
    val children: List<MenuItem>? = null,
    val path: String? = null
)

internal val menus = listOf(
    MenuGroup(
        "基础组件", R.drawable.ic_nav_layout,
        listOf(
            MenuItem("Badge", "badge"),
            MenuItem("Loading", "loading"),
            MenuItem("LoadMore", "load-more"),
            MenuItem("Progress", "progress"),
            MenuItem("Steps", "steps"),
            MenuItem("Swiper", "swiper"),
            MenuItem("ScrollView", "scroll-view"),
            MenuItem("TabView", "tab-view"),
            MenuItem("SwipeAction", "swipe-action"),
            MenuItem("Skeleton", "skeleton"),
            MenuItem("Tree", "tree")
        )
    ),
    MenuGroup(
        "表单组件", R.drawable.ic_nav_form,
        listOf(
            MenuItem("Button", "button"),
            MenuItem("Checkbox", "checkbox"),
            MenuItem("Radio", "radio"),
            MenuItem("Switch", "switch"),
            MenuItem("Slider", "slider"),
            MenuItem("Picker", "picker"),
            MenuItem("Input", "input"),
            MenuItem("Rate", "rate")
        )
    ),
    MenuGroup(
        "媒体组件", R.drawable.ic_nav_search,
        listOf(
            MenuItem("MediaPicker", "media-picker"),
            MenuItem("Camera", "camera"),
            MenuItem("AudioRecorder", "audio-recorder"),
            MenuItem("VideoRecorder", "video-recorder"),
            MenuItem("AudioPlayer", "audio-player"),
            MenuItem("VideoPlayer", "video-player"),
            MenuItem("LivePlayer", "live-player"),
            MenuItem("LivePusher", "live-pusher")
        )
    ),
    MenuGroup(
        "操作反馈", R.drawable.ic_nav_feedback,
        listOf(
            MenuItem("ActionSheet", "action-sheet"),
            MenuItem("Dialog", "dialog"),
            MenuItem("Popup", "popup"),
            MenuItem("Toast", "toast"),
            MenuItem("InformationBar", "information-bar"),
            MenuItem("ContextMenu", "context-menu")
        )
    ),
    MenuGroup(
        "系统服务", R.drawable.ic_nav_layout,
        listOf(
            MenuItem("Contacts", "contacts"),
            MenuItem("Clipboard", "clipboard"),
            MenuItem("CalendarEvents", "calendar-events"),
            MenuItem("DeviceInfo", "device-info"),
            MenuItem("Downloader", "downloader"),
            MenuItem("Database", "database"),
            MenuItem("SystemStatus", "system-status"),
            MenuItem("SMS", "sms"),
            MenuItem("InstalledApps", "installed-apps"),
            MenuItem("Keyboard", "keyboard"),
            MenuItem("Notification", "notification")
        )
    ),
    MenuGroup(
        "网络服务", R.drawable.ic_nav_search,
        listOf(
            MenuItem("HTTPRequest", "http-request"),
            MenuItem("FileUpload", "file-upload"),
            MenuItem("FileDownload", "file-download"),
            MenuItem("WebSocket", "web-socket")
        )
    ),
    MenuGroup(
        "硬件接口", R.drawable.ic_nav_nav,
        listOf(
            MenuItem("Screen", "screen"),
            MenuItem("Flashlight", "flashlight"),
            MenuItem("Vibration", "vibration"),
            MenuItem("Wi-Fi", "wifi"),
            MenuItem("Bluetooth", "bluetooth"),
            MenuItem("NFC", "nfc"),
            MenuItem("GNSS", "gnss"),
            MenuItem("Infrared", "infrared"),
            MenuItem("Gyroscope", "gyroscope"),
            MenuItem("Compass", "compass"),
            MenuItem("Accelerometer", "accelerometer"),
            MenuItem("Hygrothermograph", "hygrothermograph"),
            MenuItem("Fingerprint", "fingerprint")
        )
    ),
    MenuGroup(
        "图表组件", R.drawable.ic_nav_layout,
        listOf(
            MenuItem("BarChart", "bar-chart"),
            MenuItem("LineChart", "line-chart"),
            MenuItem("PieChart", "pie-chart")
        )
    ),
    MenuGroup(
        "二维码", R.drawable.ic_nav_form, listOf(
            MenuItem("QrCodeScanner", "qrcode-scanner"),
            MenuItem("QrCodeGenerator", "qrcode-generator")
        )
    ),
    MenuGroup(
        "地图", R.drawable.ic_nav_feedback,
        listOf(
            MenuItem("LocationPreview", "location-preview"),
            MenuItem("LocationPicker", "location-picker")
        )
    ),
    MenuGroup(
        "扩展示例", R.drawable.ic_nav_search,
        listOf(
            MenuItem("Calendar", "calendar"),
            MenuItem("Clock", "clock"),
            MenuItem("DropCard", "drop-card"),
            MenuItem("SearchBar", "search-bar"),
            MenuItem("Gallery", "gallery"),
            MenuItem("FileBrowser", "file-browser"),
            MenuItem("Paint", "paint"),
            MenuItem("IndexedList", "indexed-list"),
            MenuItem("DragSorter", "drag-sorter"),
            MenuItem("DividingRule", "dividing-rule"),
            MenuItem("OrgTree", "org-tree"),
            MenuItem("DigitalRoller", "digital-roller"),
            MenuItem("DigitalKeyboard", "digital-keyboard"),
            MenuItem("CubicBezier", "cubic-bezier"),
            MenuItem("NotificationBar", "notification-bar"),
            MenuItem("ImageCrop", "image-crop"),
            MenuItem("VideoChannel", "video-channel"),
            MenuItem("SolarSystem", "solar-system")
        )
    ),
    MenuGroup("层级规范", R.drawable.ic_nav_zindex, path = "layers")
)