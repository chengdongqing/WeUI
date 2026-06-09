package top.chengdongqing.weui.system.address.repository

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@Database(
    entities = [Address::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AddressDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AddressDatabase> {
    override fun initialize(): AddressDatabase
}