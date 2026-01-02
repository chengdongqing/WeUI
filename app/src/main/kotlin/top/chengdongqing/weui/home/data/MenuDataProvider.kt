package top.chengdongqing.weui.home.data

import top.chengdongqing.weui.R
import top.chengdongqing.weui.home.data.model.MenuGroup
import top.chengdongqing.weui.home.data.model.MenuItem

internal object MenuDataProvider {
    val menuGroups = listOf(
        MenuGroup(
            "基础组件", R.drawable.ic_nav_layout,
            listOf(
                MenuItem("Badge", "badge"),
                MenuItem("Loading", "loading"),
                MenuItem("LoadMore", "load_more"),
                MenuItem("Progress", "progress"),
                MenuItem("Steps", "steps"),
                MenuItem("Swiper", "swiper"),
                MenuItem("RefreshView", "refresh_view"),
                MenuItem("TabView", "tab_view"),
                MenuItem("SwipeAction", "swipe_action"),
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
                MenuItem("Camera", "camera"),
                MenuItem("MediaPicker", "media_picker"),
                MenuItem("AudioRecorder", "audio_recorder"),
                MenuItem("AudioPlayer", "audio_player"),
                MenuItem("Gallery", "gallery"),
                MenuItem("ImageCropper", "image_cropper"),
                MenuItem("PanoramicImage", "panoramic_image")
            )
        ),
        MenuGroup(
            "操作反馈", R.drawable.ic_nav_feedback,
            listOf(
                MenuItem("ActionSheet", "action_sheet"),
                MenuItem("Dialog", "dialog"),
                MenuItem("Popup", "popup"),
                MenuItem("Toast", "toast"),
                MenuItem("InformationBar", "information_bar"),
                MenuItem("ContextMenu", "context_menu")
            )
        ),
        MenuGroup(
            "系统服务", R.drawable.ic_nav_layout,
            listOf(
                MenuItem("Contacts", "contacts"),
                MenuItem("Clipboard", "clipboard"),
                MenuItem("CalendarEvents", "calendar_events"),
                MenuItem("DeviceInfo", "device_info"),
                MenuItem("Downloader", "downloader"),
                MenuItem("Database", "database"),
                MenuItem("SystemStatus", "system_status"),
                MenuItem("SMS", "sms"),
                MenuItem("InstalledApps", "installed_apps"),
                MenuItem("Keyboard", "keyboard"),
                MenuItem("Notification", "notification")
            )
        ),
        MenuGroup(
            "网络服务", R.drawable.ic_nav_search,
            listOf(
                MenuItem("HttpRequest", "http_request"),
                MenuItem("FileUpload", "file_upload"),
                MenuItem("FileDownload", "file_download"),
                MenuItem("WebSocket", "web_socket")
            )
        ),
        MenuGroup(
            "硬件接口", R.drawable.ic_nav_nav,
            listOf(
                MenuItem("Screen", "screen"),
                MenuItem("Flashlight", "flashlight"),
                MenuItem("Vibration", "vibration"),
                MenuItem("WiFi", "wifi"),
                MenuItem("Bluetooth", "bluetooth"),
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
                MenuItem("BarChart", "bar_chart"),
                MenuItem("LineChart", "line_chart"),
                MenuItem("PieChart", "pie_chart")
            )
        ),
        MenuGroup(
            "二维码", R.drawable.ic_nav_form, listOf(
                MenuItem("QrCodeScanner", "qrcode_scanner"),
                MenuItem("QrCodeGenerator", "qrcode_generator")
            )
        ),
        MenuGroup(
            "地图组件", R.drawable.ic_nav_feedback,
            listOf(
                MenuItem("LocationPreview", "location_preview"),
                MenuItem("LocationPicker", "location_picker")
            )
        ),
        MenuGroup(
            "扩展示例", R.drawable.ic_nav_search,
            listOf(
                MenuItem("Calendar", "calendar"),
                MenuItem("Clock", "clock"),
                MenuItem("DropCard", "drop_card"),
                MenuItem("SearchBar", "search_bar"),
                MenuItem("FileBrowser", "file_browser"),
                MenuItem("Paint", "paint"),
                MenuItem("IndexedList", "indexed_list"),
                MenuItem("Reorderable", "reorderable"),
                MenuItem("DividingRule", "dividing_rule"),
                MenuItem("OrgTree", "org_tree"),
                MenuItem("DigitalRoller", "digital_roller"),
                MenuItem("DigitalKeyboard", "digital_keyboard"),
                MenuItem("CubicBezier", "cubic_bezier"),
                MenuItem("NotificationBar", "notification_bar"),
                MenuItem("VideoChannel", "video_channel"),
                MenuItem("SolarSystem", "solar_system")
            )
        ),
        MenuGroup(
            "层级规范",
            R.drawable.ic_nav_zindex,
            path = "layers"
        )
    )
}