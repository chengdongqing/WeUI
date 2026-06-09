package top.chengdongqing.weui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeNavKey : NavKey

@Serializable
data object LayersNavKey : NavKey

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
    data object Clock : NavKey

    @Serializable
    data object NotificationBar : NavKey

    @Serializable
    data object PanoramicImage : NavKey

    @Serializable
    data object SolarSystem : NavKey
}