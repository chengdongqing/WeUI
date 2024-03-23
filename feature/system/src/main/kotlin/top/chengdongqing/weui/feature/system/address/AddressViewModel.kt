package top.chengdongqing.weui.feature.system.address

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import top.chengdongqing.weui.feature.system.address.repository.Address
import top.chengdongqing.weui.feature.system.address.repository.AddressRepositoryImpl

@Suppress("UNCHECKED_CAST")
class AddressViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddressViewModel(context) as T
    }
}

class AddressViewModel(context: Context) : ViewModel() {
    private val addressRepository by lazy {
        AddressRepositoryImpl(context)
    }
    val addressList by lazy { addressRepository.addressList }

    suspend fun insert(address: Address) {
        return addressRepository.insert(address)
    }

    suspend fun update(address: Address) {
        return addressRepository.update(address)
    }

    suspend fun delete(address: Address) {
        return addressRepository.delete(address)
    }

    suspend fun loadById(id: Int): Address? {
        return addressRepository.loadById(id)
    }
}