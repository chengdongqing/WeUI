package top.chengdongqing.weui.feature.system.address.repository

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@Database(entities = [Address::class], version = 1, exportSchema = false)
@ConstructedBy(AddressDatabaseConstructor::class)
abstract class AddressDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao

    companion object {
        fun getRoomDatabase(
            builder: Builder<AddressDatabase>
        ): AddressDatabase {
            return builder
                .fallbackToDestructiveMigration(true)
                .build()
        }
    }
}

@Suppress("KotlinNoActualForExpect")
expect object AddressDatabaseConstructor : RoomDatabaseConstructor<AddressDatabase> {
    override fun initialize(): AddressDatabase
}