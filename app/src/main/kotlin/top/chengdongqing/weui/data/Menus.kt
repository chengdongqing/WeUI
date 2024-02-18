package top.chengdongqing.weui.data

import top.chengdongqing.weui.R
import top.chengdongqing.weui.model.MenuGroup
import top.chengdongqing.weui.model.MenuItem

val menus = listOf(
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
            MenuItem("TabView", "tab-view")
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
            MenuItem("Input", "input")
        )
    ),
    MenuGroup(
        "媒体组件", R.drawable.ic_nav_media,
        listOf(
            MenuItem("MediaPicker", "media-picker"),
            MenuItem("Camera", "camera"),
            MenuItem("AudioRecorder", "audio-recorder"),
            MenuItem("VideoRecorder", "video-recorder"),
            MenuItem("AudioPlayer", "audio-player"),
            MenuItem("VideoPlayer", "video-player"),
            MenuItem("ImageCropper", "image-cropper"),
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
            MenuItem("Socket", "socket")
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
        "地图组件", R.drawable.ic_nav_feedback,
        listOf(
            MenuItem("LocationPreview", "location-preview"),
            MenuItem("LocationPicker", "location-picker")
        )
    ),
    MenuGroup(
        "案例", R.drawable.ic_nav_search,
        listOf(
            MenuItem("Calendar", "calendar"),
            MenuItem("Clock", "clock"),
            MenuItem("SearchBar", "search-bar"),
            MenuItem("Gallery", "gallery"),
            MenuItem("FileBrowser", "file-browser"),
            MenuItem("Paint", "paint"),
            MenuItem("IndexedList", "indexed-list")
        )
    ),
    MenuGroup(
        "层级规范", R.drawable.ic_nav_zindex,
        path = "layers"
    )
)