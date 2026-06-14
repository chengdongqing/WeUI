package top.chengdongqing.weui.system.address.repository

import kotlinx.coroutines.flow.Flow
import top.chengdongqing.weui.util.getRoomDatabaseBuilder

class AddressRepositoryImpl : AddressRepository {
    private val database by lazy {
        AddressDatabase.getRoomDatabase(getRoomDatabaseBuilder())
    }

    override val addressList: Flow<List<Address>>
        get() = database.addressDao().addressList

    override suspend fun loadById(id: Int): Address? {
        return database.addressDao().loadById(id)
    }

    override suspend fun insert(address: Address) {
        database.addressDao().insert(address)
    }

    override suspend fun update(address: Address) {
        database.addressDao().update(address)
    }

    override suspend fun delete(address: Address) {
        database.addressDao().delete(address)
    }
}