package com.inventorymanagement.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getById(id: Long): LiveData<Product?>

    @Query("SELECT * FROM products WHERE name = :name OR sku = :sku LIMIT 1")
    fun getByNameOrSku(name: String, sku: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)
}