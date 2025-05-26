package com.inventorymanagement.ui.product

import android.health.connect.datatypes.units.Length
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorymanagement.R
import com.inventorymanagement.data.local.Product
import com.inventorymanagement.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SellProductScreen(navController: NavController, productId: Long) {
    val vm: ProductViewModel = koinViewModel()
    val productObserve by vm.getProductById(productId).observeAsState()
    var amount by remember { mutableStateOf(1) }
    val snackbar = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbar) { data ->
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
            if (productObserve == null) {
                Text("Loading products...", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                val product = productObserve!!
                Text(text = "Product: ${product.name}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Current Stock: ${product.quantity} units",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(

                    value = amount.toString(),
                    onValueChange = { amount = it.toIntOrNull() ?: 0 },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Quantity to sell") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    keyboardController?.hide()

                    coroutineScope.launch {
                        when {
                            product.quantity <= 0 -> {
                                snackbar.showSnackbar("Cannot sell: Out of Stock!!!")
                            }

                            product.quantity < amount -> {
                                snackbar.showSnackbar("Insufficient stock to sell $amount units, we have only ${product.quantity} units.")
                            }

                            amount <= 0 -> {
                                snackbar.showSnackbar("Please enter a valid quantity to sell")
                            }

                            else -> {
                                vm.sell(product, amount)
                                snackbar.showSnackbar("Sold $amount units.")
                                navController.popBackStack()
                            }
                        }
                    }
                }) {
                    Text("Sell Product")
                }
            }
        }
    }
}