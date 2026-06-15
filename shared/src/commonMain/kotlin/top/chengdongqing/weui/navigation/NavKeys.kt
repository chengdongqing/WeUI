package top.chengdongqing.weui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeNavKey : NavKey

@Serializable
data object LayersNavKey : NavKey

@Serializable
sealed interface BasicNavKey {
    @Serializable
    data object Badge : NavKey

    @Serializable
    data object Loading : NavKey

    @Serializable
    data object LoadMore : NavKey

    @Serializable
    data object Progress : NavKey

    @Serializable
    data object RefreshView : NavKey

    @Serializable
    data object Skeleton : NavKey

    @Serializable
    data object Steps : NavKey

    @Serializable
    data object SwipeAction : NavKey

    @Serializable
    data object Swiper : NavKey

    @Serializable
    data object TabView : NavKey

    @Serializable
    data object Tree : NavKey
}

@Serializable
sealed interface FormNavKey {
    @Serializable
    data object Button : NavKey

    @Serializable
    data object Checkbox : NavKey

    @Serializable
    data object Input : NavKey

    @Serializable
    data object Picker : NavKey

    @Serializable
    data object Radio : NavKey

    @Serializable
    data object Rate : NavKey

    @Serializable
    data object Slider : NavKey

    @Serializable
    data object Switch : NavKey
}

@Serializable
sealed interface FeedbackNavKey {
    @Serializable
    data object ActionSheet : NavKey

    @Serializable
    data object ContextMenu : NavKey

    @Serializable
    data object Dialog : NavKey

    @Serializable
    data object InformationBar : NavKey

    @Serializable
    data object Popup : NavKey

    @Serializable
    data object Toast : NavKey
}

@Serializable
sealed interface SystemNavKey {
    @Serializable
    data object AddressList : NavKey

    @Serializable
    data class AddressDetail(val addressId: Int?) : NavKey

    @Serializable
    data object Clipboard : NavKey
}

@Serializable
sealed interface ChartsNavKey {
    @Serializable
    data object Bar : NavKey

    @Serializable
    data object Line : NavKey

    @Serializable
    data object Pie : NavKey
}

@Serializable
sealed interface SamplesNavKey {
    @Serializable
    data object NotificationBar : NavKey

    @Serializable
    data object PanoramicImage : NavKey

    @Serializable
    data object CubicBezier : NavKey

    @Serializable
    data object DigitalKeyboard : NavKey

    @Serializable
    data object DigitalRoller : NavKey

    @Serializable
    data object DividingRule : NavKey

    @Serializable
    data object DropCard : NavKey

    @Serializable
    data object OrgTree : NavKey

    @Serializable
    data object IndexedList : NavKey

    @Serializable
    data object SearchBar : NavKey

    @Serializable
    data object Reorderable : NavKey

    @Serializable
    data object Paint : NavKey

    @Serializable
    data object Calendar : NavKey
}

@Serializable
sealed interface AnimationNavKey {
    @Serializable
    data object Clock : NavKey

    @Serializable
    data object SolarSystem : NavKey

    @Serializable
    data object Fibonacci : NavKey

    @Serializable
    data object RoseCurve : NavKey

    @Serializable
    data object SymmetryParticle : NavKey

    @Serializable
    data object RadialParticle : NavKey

    @Serializable
    data object Constellation : NavKey
}