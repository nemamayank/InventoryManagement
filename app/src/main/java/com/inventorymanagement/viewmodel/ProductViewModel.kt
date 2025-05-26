package com.inventorymanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    val products: LiveData<List<Product>> = repo.getAllProducts()

    fun save(product: Product) {
        viewModelScope.launch { repo.addOrUpdate(product) }
    }

    fun delete(product: Product) {
        viewModelScope.launch { repo.remove(product) }
    }

    fun sell(product: Product, amount: Int) {
        if (product.quantity >= amount) {
            save(product.copy(quantity = product.quantity - amount))
        }
    }

    fun getProductById(id: Long): LiveData<Product?> = repo.getProductById(id)
}