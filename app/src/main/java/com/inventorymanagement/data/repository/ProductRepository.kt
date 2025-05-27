package com.inventorymanagement.data.repository

import androidx.lifecycle.LiveData
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.data.local.ProductDao

class ProductRepository(private val dao: ProductDao) {

    fun getAllProducts(): LiveData<List<Product>> = dao.getAll()
    fun getProductById(id: Long): LiveData<Product?> = dao.getById(id)
    fun getProductByNameOrSku(name: String, sku: String): Product? = dao.getByNameOrSku(name, sku)
    suspend fun addOrUpdate(product: Product) = dao.insert(product)
}