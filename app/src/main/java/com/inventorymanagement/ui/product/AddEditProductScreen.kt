package com.inventorymanagement.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddEditProductScreen(navController: NavController, productId: Long) {
    val vm: ProductViewModel = koinViewModel()
    val product by vm.getProductById(productId).observeAsState()
    var name by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var price by remember { mutableIntStateOf(0) }

    LaunchedEffect(product) {
        product?.let {
            name = it.name
            sku = it.sku
            quantity = it.quantity
            price = it.price
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = sku,
            onValueChange = { sku = it },
            label = { Text("SKU") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = { quantity = it.toIntOrNull() ?: 0 },
            label = { Text("Quantity") })
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = price.toString(),
            onValueChange = { price = it.toIntOrNull() ?: 0 },
            label = { Text("Price") })

        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = {
            vm.save(
                Product(
                    id = if (productId == -1L) 0 else productId,
                    name = name,
                    sku = sku,
                    quantity = quantity,
                    price = price
                )
            )
            navController.popBackStack()
        }) {
            Text(if (productId == -1L) "Add Product" else "Update Product")
        }
    }
}