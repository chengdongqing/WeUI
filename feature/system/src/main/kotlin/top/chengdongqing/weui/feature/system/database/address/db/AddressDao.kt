package top.chengdongqing.weui.feature.system.database.address.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM address")
    fun loadAll(): Flow<List<Address>>

    @Query("select * from address where id = :id")
    fun loadById(id: Int): Flow<Address?>

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)
}
