package com.inventorymanagement.data.repository

import androidx.lifecycle.LiveData
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.data.local.ProductDao

class ProductRepository(private val dao: ProductDao) {

    fun getAllProducts(): LiveData<List<Product>> = dao.getAll()
    fun getProductById(id: Long): LiveData<Product?> = dao.getById(id)
    suspend fun addOrUpdate(product: Product) = dao.insert(product)
    suspend fun remove(product: Product) = dao.delete(product)
}