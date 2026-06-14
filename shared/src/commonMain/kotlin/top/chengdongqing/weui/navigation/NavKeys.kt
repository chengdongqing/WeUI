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
}

@Serializable
sealed interface SystemNavKey {
    @Serializable
    data object AddressList : NavKey

    @Serializable
    data class AddressDetail(val addressId: Int?) : NavKey
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