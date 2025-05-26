package com.inventorymanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.inventorymanagement.ui.product.AddEditProductScreen
import com.inventorymanagement.ui.product.ProductListScreen
import com.inventorymanagement.ui.product.SellProductScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") {
            ProductListScreen(navController)
        }
        composable("addEdit/{id}?", arguments = listOf(
            navArgument("id") {
                type = NavType.LongType; defaultValue = -1L
            }
        )) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: -1L
            AddEditProductScreen(navController, id)
        }
        composable("sell/{id}", arguments = listOf(
            navArgument("id") { type = NavType.LongType }
        )) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")!!
            SellProductScreen(navController, id)
        }
    }
}