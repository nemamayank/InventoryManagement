package com.inventorymanagement.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorymanagement.R
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddEditProductScreen(navController: NavController, productId: Long) {
    val vm: ProductViewModel = koinViewModel()
    val product by vm.getProductById(productId).observeAsState()
    val coroutineScope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
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

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snack) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colorResource(id = R.color.purple_top),
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
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
                coroutineScope.launch {
                    when {
                        name.isBlank() -> {
                            snack.showSnackbar("Product name cannot be blank")
                            return@launch
                        }

                        sku.isBlank() -> {
                            snack.showSnackbar("SKU cannot be blank")
                            return@launch
                        }

                        quantity <= 0 -> {
                            snack.showSnackbar("Invalid Quantity!")
                            return@launch
                        }

                        price <= 0 -> {
                            snack.showSnackbar("Invalid Price")
                            return@launch
                        }

                        else -> {

                            val existing = withContext(Dispatchers.IO) {
                                vm.getProductByNameOrSku(name.trim(), sku.trim())
                            }
                            if (existing != null && existing.id != productId) {
                                snack.showSnackbar("A product with this name / sku already exists.")
                                return@launch
                            }

                            vm.save(
                                Product(
                                    id = if (productId == -1L) 0 else productId,
                                    name = name,
                                    sku = sku,
                                    quantity = quantity,
                                    price = price
                                )
                            )
                            snack.showSnackbar("Product: $name added successfully.")
                            navController.popBackStack()
                        }
                    }
                }
            }) {
                Text(if (productId == -1L) "Add Product" else "Update Product")
            }
        }
    }
}