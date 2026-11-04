package com.example.sanaescencia.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sanaescencia.repository.SanaesenciaRepository.cartItems
import com.example.sanaescencia.viewmodel.SanaesenciaViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState


@Composable
fun CarritoScreen(
    viewModel: SanaesenciaViewModel = viewModel(),
    isNotEmpty: StateFlow<Map<String, Int>>.() -> Boolean,

    ) {

    Box(modifier = Modifier.fillMaxSize()) {

        if (cartItems.isNotEmpty()) {
            Text(
                text = "No hay productos en el carrito",
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems.value.size) { index ->
                    val item = cartItems.collectAsState().value.keys.elementAt(index)
                    val qty = cartItems.value[item] ?: 0

                }
            }
        }


    }
}




