package top.chengdongqing.weui.system.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import top.chengdongqing.weui.navigation.SystemNavKey
import top.chengdongqing.weui.system.address.AddressFormScreen
import top.chengdongqing.weui.system.ui.DatabaseScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.systemNavEntries(
    backStack: SnapshotStateList<NavKey>,
    onBack: () -> Unit
) {
    entry<SystemNavKey.AddressList>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        DatabaseScreen(
            onBack = onBack,
            onNavigateToAddressForm = { addressId ->
                backStack.add(SystemNavKey.AddressDetail(addressId))
            }
        )
    }
    entry<SystemNavKey.AddressDetail>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {
        AddressFormScreen(
            id = it.addressId,
            onBack = onBack
        )
    }
}