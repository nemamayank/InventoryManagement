package com.inventorymanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    val products: LiveData<List<Product>> = repo.getAllProducts()

    fun save(product: Product) = viewModelScope.launch { repo.addOrUpdate(product) }

    fun getProductByNameOrSku(name: String, sku: String): Product? = repo.getProductByNameOrSku(name, sku)

    fun sell(product: Product, amount: Int) = save(product.copy(quantity = product.quantity - amount))

    fun getProductById(id: Long): LiveData<Product?> = repo.getProductById(id)

}