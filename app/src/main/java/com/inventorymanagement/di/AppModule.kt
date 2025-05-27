package com.inventorymanagement.di

import androidx.room.Room
import com.inventorymanagement.data.local.AppDatabase
import com.inventorymanagement.data.repository.ProductRepository
import com.inventorymanagement.ui.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "inventory_db")
            .build()
    }
    single { get<AppDatabase>().productDao() }
    single { ProductRepository(get()) }
    viewModel { ProductViewModel(get()) }
}