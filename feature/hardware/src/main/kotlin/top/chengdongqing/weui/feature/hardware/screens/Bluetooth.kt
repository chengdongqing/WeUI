package top.chengdongqing.weui.feature.hardware.screens

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.cardList
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.dbmToPercentage
import top.chengdongqing.weui.core.utils.showToast
import top.chengdongqing.weui.feature.hardware.components.DiscoveryLoading

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothScreen() {
    WeScreen(title = "Bluetooth", description = "蓝牙", scrollEnabled = false) {
        val context = LocalContext.current
        val permissionState = rememberMultiplePermissionsState(
            remember {
                mutableListOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        add(Manifest.permission.BLUETOOTH_CONNECT)
                        add(Manifest.permission.BLUETOOTH_SCAN)
                    }
                }
            }
        )
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val bluetoothAdapter = bluetoothManager?.adapter
        val launchBluetooth = rememberBluetoothLauncher()
        val bluetoothList = rememberBluetoothDevices()
        val sortedList by remember {
            derivedStateOf {
                bluetoothList.sortedByDescending { it.percentage }
            }
        }
        val (observing, setObserving) = remember { mutableStateOf(false) }

        WeButton(text = "扫描蓝牙") {
            if (permissionState.allPermissionsGranted) {
                if (bluetoothAdapter == null) {
                    context.showToast("此设备不支持蓝牙")
                } else if (!bluetoothAdapter.isEnabled) {
                    launchBluetooth()
                } else {
                    bluetoothList.clear()
                    bluetoothAdapter.startDiscovery()
                    setObserving(true)
                }
            } else {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        if (observing) {
            Spacer(modifier = Modifier.height(40.dp))
            BluetoothList(sortedList)
        }
    }
}

@Composable
private fun BluetoothList(bluetoothList: List<BluetoothInfo>) {
    if (bluetoothList.isNotEmpty()) {
        LazyColumn(modifier = Modifier.cardList()) {
            itemsIndexed(bluetoothList, key = { _, item -> item.mac }) { index, device ->
                BluetoothListItem(device)
                if (index < bluetoothList.lastIndex) {
                    WeDivider()
                }
            }
        }
    } else {
        DiscoveryLoading("正在扫描蓝牙设备...")
    }
}

@Composable
private fun BluetoothListItem(bluetooth: BluetoothInfo) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = bluetooth.name,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = buildString {
                appendLine("信号强度：${bluetooth.rssi}dBm （${bluetooth.percentage}%）")
                appendLine("MAC地址：${bluetooth.mac}")
                appendLine("蓝牙类型：${bluetooth.type}")
                append("绑定状态：${bluetooth.bondState}")
            },
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun rememberBluetoothDevices(): SnapshotStateList<BluetoothInfo> {
    val context = LocalContext.current
    val bluetoothList = remember { mutableStateListOf<BluetoothInfo>() }

    DisposableEffect(Unit) {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }

        val receiver = BluetoothBroadcastReceiver { info ->
            val index = bluetoothList.indexOfFirst { item ->
                item.mac == info.mac
            }
            if (index == -1) {
                bluetoothList.add(info)
            } else {
                bluetoothList[index] = info
            }
        }
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    return bluetoothList
}

@Composable
private fun rememberBluetoothLauncher(): () -> Unit {
    val context = LocalContext.current
    val bluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            context.showToast("蓝牙已打开")
        }
    }

    return {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        bluetoothLauncher.launch(intent)
    }
}

private class BluetoothBroadcastReceiver(val onFound: (info: BluetoothInfo) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                if (device?.name?.isNotEmpty() == true) {
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE)
                    onFound(device.buildBluetoothInfo(rssi.toInt()))
                }
            }
        }
    }
}

private fun BluetoothDevice.buildBluetoothInfo(rssi: Int): BluetoothInfo {
    return BluetoothInfo(
        name = this.name,
        mac = this.address,
        type = getBluetoothType(this.type),
        bondState = getBluetoothBondState(this.bondState),
        rssi = rssi,
        percentage = dbmToPercentage(rssi)
    )
}

/**
 * 获取蓝牙类型
 */
private fun getBluetoothType(type: Int): String {
    return when (type) {
        BluetoothDevice.DEVICE_TYPE_CLASSIC -> "经典蓝牙"
        BluetoothDevice.DEVICE_TYPE_LE -> "低功耗蓝牙"
        BluetoothDevice.DEVICE_TYPE_DUAL -> "双模蓝牙"
        else -> "未知类型"
    }
}

/**
 * 获取蓝牙绑定状态
 */
private fun getBluetoothBondState(type: Int): String {
    return when (type) {
        BluetoothDevice.BOND_NONE -> "未绑定"
        BluetoothDevice.BOND_BONDED -> "已绑定"
        BluetoothDevice.BOND_BONDING -> "绑定中"
        else -> "未知状态"
    }
}

private data class BluetoothInfo(
    val name: String,       // 设备名称
    val mac: String,    // mac地址
    val type: String,       // 蓝牙类型
    val bondState: String,  // 绑定状态
    val rssi: Int,          // 信号强度（dBm）
    val percentage: Int    // 信号强度（百分比）
)
