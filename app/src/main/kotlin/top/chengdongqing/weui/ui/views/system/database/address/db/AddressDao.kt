package top.chengdongqing.weui.ui.views.system.database.address.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AddressDao {

    @Query("SELECT * FROM address")
    fun loadAll(): LiveData<List<Address>>

    @Query("select * from address where id = :id")
    suspend fun loadById(id: Int): Address?

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)
}
