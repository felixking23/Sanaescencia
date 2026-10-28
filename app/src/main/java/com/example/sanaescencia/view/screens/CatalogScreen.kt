package com.example.sanaescencia.view.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sanaescencia.model.ProductType
import com.example.sanaescencia.viewmodel.SanaesenciaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: SanaesenciaViewModel = viewModel()) {
    val cartItems by viewModel.cartItems.collectAsState()
    val products = viewModel.availableProducts
    val specialties = viewModel.availableSpecialties

    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showCartSummary by remember { mutableStateOf(false) }

    val filteredProducts = products.filter {
        (selectedFilter == null || selectedFilter == it.type.name) &&
                (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
    }

    val filteredSpecialties = specialties.filter {
        (selectedFilter == null || selectedFilter == "TERAPIA") &&
                (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text("Catálogo de Prestaciones e Insumos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Centralización de servicios terapéuticos y catálogo natural", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar servicio o producto...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(selected = selectedFilter == null, onClick = { selectedFilter = null }, label = { Text("Todos") }) }
                item { FilterChip(selected = selectedFilter == "TERAPIA", onClick = { selectedFilter = "TERAPIA" }, label = { Text("Terapias Integrativas") }) }
                items(ProductType.entries.toTypedArray()) { type ->
                    FilterChip(selected = selectedFilter == type.name, onClick = { selectedFilter = type.name }, label = { Text(type.label) })
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (filteredSpecialties.isNotEmpty()) {
                    items(filteredSpecialties) { spec ->
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(spec.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                    Text("Profesional: ${spec.professional}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                    Text("$${spec.price}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                }
                                IconButton(
                                    onClick = { viewModel.addToCart(spec.id) },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
                                ) { Icon(Icons.Default.AddShoppingCart, contentDescription = null) }
                            }
                        }
                    }
                }

                if (filteredProducts.isNotEmpty()) {
                    items(filteredProducts) { prod ->
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(prod.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                    Text("${prod.type.label} · ${prod.volume}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text("$${prod.price}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                }
                                IconButton(
                                    onClick = { viewModel.addToCart(prod.id) },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
                                ) { Icon(Icons.Default.AddShoppingCart, contentDescription = null) }
                            }
                        }
                    }
                }
            }
        }

        if (cartItems.isNotEmpty()) {
            ExtendedFloatingActionButton(
                onClick = { showCartSummary = !showCartSummary },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                text = { Text("Ver Canasta (${cartItems.values.sum()})") },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            )
        }

        AnimatedVisibility(
            visible = showCartSummary,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.45f), tonalElevation = 10.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Canasta de Solicitudes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        cartItems.forEach { (id, qty) ->
                            val labelName = specialties.find { it.id == id }?.name ?: products.find { it.id == id }?.name ?: ""
                            val labelPrice = specialties.find { it.id == id }?.price ?: products.find { it.id == id }?.price ?: 0
                            item {
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("$labelName (x$qty)", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                                    Row {
                                        Text("$${labelPrice * qty}", fontWeight = FontWeight.SemiBold)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        IconButton(onClick = { viewModel.removeFromCart(id) }, modifier = Modifier.size(20.dp)) {
                                            Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Total Estimado: $${viewModel.getCartTotal()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { viewModel.clearCart(); showCartSummary = false }) { Text("Vaciar") }
                        Button(onClick = { viewModel.clearCart(); showCartSummary = false }) { Text("Confirmar Registro") }
                    }
                }
            }
        }
    }
}
