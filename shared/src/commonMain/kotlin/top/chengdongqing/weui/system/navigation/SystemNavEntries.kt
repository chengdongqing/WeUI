package top.chengdongqing.weui.system.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.navigation.SystemNavKey
import top.chengdongqing.weui.system.address.AddressFormScreen
import top.chengdongqing.weui.system.ui.DatabaseScreen

fun EntryProviderScope<NavKey>.systemNavEntries(
    backStack: SnapshotStateList<NavKey>,
    onBack: () -> Unit
) {
    entry<SystemNavKey.AddressList> {
        DatabaseScreen(
            onBack = onBack,
            onNavigateToAddressForm = { addressId ->
                backStack.add(SystemNavKey.AddressDetail(addressId))
            }
        )
    }
    entry<SystemNavKey.AddressDetail> {
        AddressFormScreen(
            id = it.addressId,
            onBack = onBack
        )
    }
}