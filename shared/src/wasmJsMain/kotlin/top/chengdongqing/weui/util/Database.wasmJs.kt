package top.chengdongqing.weui.util

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import org.w3c.dom.Worker
import top.chengdongqing.weui.feature.system.address.repository.AddressDatabase

actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<AddressDatabase> {
    return Room.databaseBuilder<AddressDatabase>("my_room.db")
        .setDriver(WebWorkerSQLiteDriver(jsWorker()))
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsWorker(): Worker =
    js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")