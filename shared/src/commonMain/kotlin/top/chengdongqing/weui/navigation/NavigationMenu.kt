package top.chengdongqing.weui.navigation

import androidx.navigation3.runtime.NavKey
import org.jetbrains.compose.resources.DrawableResource
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_nav_feedback
import weui_kmp.shared.generated.resources.ic_nav_form
import weui_kmp.shared.generated.resources.ic_nav_layout
import weui_kmp.shared.generated.resources.ic_nav_nav
import weui_kmp.shared.generated.resources.ic_nav_search
import weui_kmp.shared.generated.resources.ic_nav_zindex

data class MenuItem(
    val label: String,
    val navKey: NavKey? = null,
    val iconRes: DrawableResource? = null,
    val children: List<MenuItem>? = null
)

val MenuTreeData = listOf(
    MenuItem(
        label = "基础组件",
        iconRes = Res.drawable.ic_nav_layout,
        children = listOf(
            MenuItem("Badge", BasicNavKey.Badge),
            MenuItem("Loading", BasicNavKey.Loading),
            MenuItem("LoadMore", BasicNavKey.LoadMore),
            MenuItem("Progress", BasicNavKey.Progress),
            MenuItem("Steps", BasicNavKey.Steps),
            MenuItem("Swiper", BasicNavKey.Swiper),
            MenuItem("RefreshView", BasicNavKey.RefreshView),
            MenuItem("TabView", BasicNavKey.TabView),
            MenuItem("SwipeAction", BasicNavKey.SwipeAction),
            MenuItem("Skeleton", BasicNavKey.Skeleton),
            MenuItem("Tree", BasicNavKey.Tree)
        )
    ),
    MenuItem(
        label = "表单组件",
        iconRes = Res.drawable.ic_nav_form,
        children = listOf(
            MenuItem("Button", FormNavKey.Button),
            MenuItem("Checkbox", FormNavKey.Checkbox),
            MenuItem("Radio", FormNavKey.Radio),
            MenuItem("Switch", FormNavKey.Switch),
            MenuItem("Slider", FormNavKey.Slider),
            MenuItem("Picker", FormNavKey.Picker),
            MenuItem("Input", FormNavKey.Input),
            MenuItem("Rate", FormNavKey.Rate)
        )
    ),
    MenuItem(
        label = "媒体组件",
        iconRes = Res.drawable.ic_nav_search,
        children = listOf(
            MenuItem("Camera", LayersNavKey),
            MenuItem("MediaPicker", LayersNavKey),
            MenuItem("AudioRecorder", LayersNavKey),
            MenuItem("AudioPlayer", LayersNavKey),
            MenuItem("Gallery", LayersNavKey),
            MenuItem("ImageCropper", LayersNavKey)
        )
    ),
    MenuItem(
        label = "操作反馈",
        iconRes = Res.drawable.ic_nav_feedback,
        children = listOf(
            MenuItem("ActionSheet", FeedbackNavKey.ActionSheet),
            MenuItem("Dialog", FeedbackNavKey.Dialog),
            MenuItem("Popup", FeedbackNavKey.Popup),
            MenuItem("Toast", FeedbackNavKey.Toast),
            MenuItem("InformationBar", FeedbackNavKey.InformationBar),
            MenuItem("ContextMenu", FeedbackNavKey.ContextMenu)
        )
    ),
    MenuItem(
        label = "系统服务",
        iconRes = Res.drawable.ic_nav_layout,
        children = listOf(
            MenuItem("Contacts", LayersNavKey),
            MenuItem("Clipboard", SystemNavKey.Clipboard),
            MenuItem("CalendarEvents", LayersNavKey),
            MenuItem("DeviceInfo", LayersNavKey),
            MenuItem("Downloader", LayersNavKey),
            MenuItem("Database", SystemNavKey.AddressList),
            MenuItem("SystemStatus", LayersNavKey),
            MenuItem("SMS", LayersNavKey),
            MenuItem("InstalledApps", LayersNavKey),
            MenuItem("Keyboard", LayersNavKey),
            MenuItem("Notification", LayersNavKey)
        )
    ),
    MenuItem(
        label = "网络服务",
        iconRes = Res.drawable.ic_nav_search,
        children = listOf(
            MenuItem("HttpRequest", LayersNavKey),
            MenuItem("FileUpload", LayersNavKey),
            MenuItem("FileDownload", LayersNavKey),
            MenuItem("WebSocket", LayersNavKey)
        )
    ),
    MenuItem(
        label = "硬件接口",
        iconRes = Res.drawable.ic_nav_nav,
        children = listOf(
            MenuItem("Screen", LayersNavKey),
            MenuItem("Flashlight", LayersNavKey),
            MenuItem("Vibration", LayersNavKey),
            MenuItem("WiFi", LayersNavKey),
            MenuItem("Bluetooth", LayersNavKey),
            MenuItem("GNSS", LayersNavKey),
            MenuItem("Infrared", LayersNavKey),
            MenuItem("Gyroscope", LayersNavKey),
            MenuItem("Compass", LayersNavKey),
            MenuItem("Accelerometer", LayersNavKey),
            MenuItem("Hygrothermograph", LayersNavKey),
            MenuItem("Fingerprint", LayersNavKey)
        )
    ),
    MenuItem(
        label = "图表组件",
        iconRes = Res.drawable.ic_nav_layout,
        children = listOf(
            MenuItem("BarChart", ChartsNavKey.Bar),
            MenuItem("LineChart", ChartsNavKey.Line),
            MenuItem("PieChart", ChartsNavKey.Pie)
        )
    ),
    MenuItem(
        label = "二维码",
        iconRes = Res.drawable.ic_nav_form,
        children = listOf(
            MenuItem("QrCodeScanner", LayersNavKey),
            MenuItem("QrCodeGenerator", LayersNavKey)
        )
    ),
    MenuItem(
        label = "地图组件",
        iconRes = Res.drawable.ic_nav_feedback,
        children = listOf(
            MenuItem("LocationPreview", LayersNavKey),
            MenuItem("LocationPicker", LayersNavKey)
        )
    ),
    MenuItem(
        label = "扩展示例",
        iconRes = Res.drawable.ic_nav_search,
        children = listOf(
            MenuItem("Calendar", SamplesNavKey.Calendar),
            MenuItem("DropCard", SamplesNavKey.DropCard),
            MenuItem("SearchBar", SamplesNavKey.SearchBar),
            MenuItem("FileBrowser", SamplesNavKey.FileBrowser),
            MenuItem("Paint", SamplesNavKey.Paint),
            MenuItem("IndexedList", SamplesNavKey.IndexedList),
            MenuItem("Reorderable", SamplesNavKey.Reorderable),
            MenuItem("DividingRule", SamplesNavKey.DividingRule),
            MenuItem("OrgTree", SamplesNavKey.OrgTree),
            MenuItem("DigitalRoller", SamplesNavKey.DigitalRoller),
            MenuItem("DigitalKeyboard", SamplesNavKey.DigitalKeyboard),
            MenuItem("CubicBezier", SamplesNavKey.CubicBezier),
            MenuItem("NotificationBar", SamplesNavKey.NotificationBar),
            MenuItem("PanoramicImage", SamplesNavKey.PanoramicImage),
            MenuItem("VideoChannel", LayersNavKey)
        )
    ),
    MenuItem(
        label = "动画效果",
        iconRes = Res.drawable.ic_nav_nav,
        children = listOf(
            MenuItem("Clock", AnimationNavKey.Clock),
            MenuItem("SolarSystem", AnimationNavKey.SolarSystem),
            MenuItem("Fibonacci", AnimationNavKey.Fibonacci),
            MenuItem("RoseCurve", AnimationNavKey.RoseCurve),
            MenuItem("SymmetryParticle", AnimationNavKey.SymmetryParticle),
            MenuItem("RadialParticle", AnimationNavKey.RadialParticle),
            MenuItem("Constellation", AnimationNavKey.Constellation)
        )
    ),
    MenuItem(
        label = "层级规范",
        iconRes = Res.drawable.ic_nav_zindex,
        navKey = LayersNavKey
    )
)