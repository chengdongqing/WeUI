package top.chengdongqing.weui.ui.views.system.database.address

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AddressDao {

    @Query("SELECT * FROM Address")
    fun loadAll(): List<Address>

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)
}
