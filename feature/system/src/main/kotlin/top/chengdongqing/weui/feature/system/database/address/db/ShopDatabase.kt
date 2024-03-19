package top.chengdongqing.weui.feature.system.database.address.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Address::class], version = 1)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun addressDao(): AddressDao

    companion object {
        /*@Volatile关键字用于标记INSTANCE变量，确保其值不会被本地线程缓存，所有的读写都直接在主内存中进行。
        这意味着，当一个线程修改了INSTANCE变量的值，这个改变对其他所有线程来说是立即可见的。
        这有助于保持INSTANCE的值在多线程环境中的一致性和可见性。*/
        @Volatile
        private var INSTANCE: ShopDatabase? = null

        fun getInstance(context: Context): ShopDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ShopDatabase::class.java,
                        "shop_database"
                    )
                        //.fallbackToDestructiveMigration() // 如果发现版本不一致（即实体结构与数据库中的结构不符），将重新创建数据库表，这意味着原有数据会丢失
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
