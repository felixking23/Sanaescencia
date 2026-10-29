package com.example.sanaescencia.view.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.sanaescencia.model.RoleType
import com.example.sanaescencia.model.Specialty
import com.example.sanaescencia.model.NaturalProduct
import com.example.sanaescencia.viewmodel.SanaesenciaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: SanaesenciaViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isAdmin = currentUser?.role == RoleType.ADMINISTRACION

    val cartItems by viewModel.cartItems.collectAsState()
    val products by viewModel.products.collectAsState()
    val specialties by viewModel.specialties.collectAsState()

    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showCartSummary by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var editingSpecialty by remember { mutableStateOf<Specialty?>(null) }
    var editingProduct by remember { mutableStateOf<NaturalProduct?>(null) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Catálogo de Prestaciones e Insumos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        if (isAdmin) "Modo Administrador: Gestión de Catálogo" else "Centralización de servicios terapéuticos y catálogo natural",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isAdmin) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }

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
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (isAdmin) {
                                        IconButton(onClick = { editingSpecialty = spec }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                        }
                                        IconButton(onClick = { viewModel.deleteSpecialty(spec.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    } else {
                                        IconButton(
                                            onClick = { viewModel.addToCart(spec.id) },
                                            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
                                        ) { Icon(Icons.Default.AddShoppingCart, contentDescription = null) }
                                    }
                                }
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
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (isAdmin) {
                                        IconButton(onClick = { editingProduct = prod }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                        }
                                        IconButton(onClick = { viewModel.deleteProduct(prod.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    } else {
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
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Ítem")
            }
        } else if (cartItems.isNotEmpty()) {
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
                        Button(onClick = { showCartSummary = false; showCheckoutDialog = true }) { Text("Pagar con Tarjeta") }
                    }
                }
            }
        }

        if (showCheckoutDialog) {
            var cardNumber by remember { mutableStateOf("") }
            var cardExpiry by remember { mutableStateOf("") }
            var cardCvv by remember { mutableStateOf("") }
            var cardName by remember { mutableStateOf("") }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = { showCheckoutDialog = false },
                title = { Text("Datos de la Tarjeta", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Total a pagar: $${viewModel.getCartTotal()}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Número de tarjeta") },
                            placeholder = { Text("4532 1234 5678 9010") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = cardExpiry,
                                onValueChange = { cardExpiry = it },
                                label = { Text("Exp. (MM/AA)") },
                                placeholder = { Text("12/26") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = cardCvv,
                                onValueChange = { cardCvv = it },
                                label = { Text("CVV") },
                                placeholder = { Text("123") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        OutlinedTextField(
                            value = cardName,
                            onValueChange = { cardName = it },
                            label = { Text("Nombre del titular") },
                            placeholder = { Text("Juan Pérez") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        errorMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (cardNumber.isBlank() || cardExpiry.isBlank() || cardCvv.isBlank() || cardName.isBlank()) {
                                errorMessage = "Por favor completa todos los datos de la tarjeta."
                            } else {
                                viewModel.clearCart()
                                showCheckoutDialog = false
                                showSuccessDialog = true
                            }
                        }
                    ) {
                        Text("Pagar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCheckoutDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp)) },
                title = { Text("¡Compra Exitosa!", fontWeight = FontWeight.Bold) },
                text = { Text("¡Tu compra se hizo con éxito! Los detalles han sido registrados correctamente.", style = MaterialTheme.typography.bodyMedium) },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }

        if (showAddDialog) {
            var itemType by remember { mutableStateOf("TERAPIA") }
            var name by remember { mutableStateOf("") }
            var secondaryField by remember { mutableStateOf("") }
            var priceStr by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var productTypeEnum by remember { mutableStateOf(ProductType.KIT) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Agregar Nuevo Ítem al Catálogo", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected = itemType == "TERAPIA", onClick = { itemType = "TERAPIA" }, label = { Text("Terapia") })
                            FilterChip(selected = itemType == "PRODUCTO", onClick = { itemType = "PRODUCTO" }, label = { Text("Producto Natural") })
                        }

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre del servicio o producto") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        if (itemType == "TERAPIA") {
                            OutlinedTextField(
                                value = secondaryField,
                                onValueChange = { secondaryField = it },
                                label = { Text("Profesional a cargo") },
                                placeholder = { Text("Dra. María Pérez") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        } else {
                            var expanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Tipo: ${productTypeEnum.label}")
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    ProductType.entries.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type.label) },
                                            onClick = {
                                                productTypeEnum = type
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = secondaryField,
                                onValueChange = { secondaryField = it },
                                label = { Text("Volumen / Presentación") },
                                placeholder = { Text("10 ml / Pack de 2") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it },
                            label = { Text("Precio ($)") },
                            placeholder = { Text("25000") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        errorMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = priceStr.toIntOrNull()
                            if (name.isBlank() || price == null || description.isBlank()) {
                                errorMessage = "Por favor completa todos los campos obligatorios."
                            } else {
                                if (itemType == "TERAPIA") {
                                    viewModel.addSpecialty(
                                        name = name,
                                        professional = if (secondaryField.isBlank()) "Especialista Sanaesencia" else secondaryField,
                                        description = description,
                                        price = price
                                    )
                                } else {
                                    viewModel.addProduct(
                                        name = name,
                                        type = productTypeEnum,
                                        price = price,
                                        description = description,
                                        volume = if (secondaryField.isBlank()) "Estándar" else secondaryField
                                    )
                                }
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
                }
            )
        }

        editingSpecialty?.let { spec ->
            var name by remember { mutableStateOf(spec.name) }
            var professional by remember { mutableStateOf(spec.professional) }
            var priceStr by remember { mutableStateOf(spec.price.toString()) }
            var description by remember { mutableStateOf(spec.description) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = { editingSpecialty = null },
                title = { Text("Editar Terapia / Prestación", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = professional, onValueChange = { professional = it }, label = { Text("Profesional") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val price = priceStr.toIntOrNull()
                        if (name.isBlank() || price == null) {
                            errorMessage = "Verifica los datos."
                        } else {
                            viewModel.updateSpecialty(spec.copy(name = name, professional = professional, price = price, description = description))
                            editingSpecialty = null
                        }
                    }) { Text("Actualizar") }
                },
                dismissButton = { TextButton(onClick = { editingSpecialty = null }) { Text("Cancelar") } }
            )
        }

        editingProduct?.let { prod ->
            var name by remember { mutableStateOf(prod.name) }
            var volume by remember { mutableStateOf(prod.volume) }
            var priceStr by remember { mutableStateOf(prod.price.toString()) }
            var description by remember { mutableStateOf(prod.description) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = { editingProduct = null },
                title = { Text("Editar Producto Natural", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = volume, onValueChange = { volume = it }, label = { Text("Volumen / Presentación") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val price = priceStr.toIntOrNull()
                        if (name.isBlank() || price == null) {
                            errorMessage = "Verifica los datos."
                        } else {
                            viewModel.updateProduct(prod.copy(name = name, volume = volume, price = price, description = description))
                            editingProduct = null
                        }
                    }) { Text("Actualizar") }
                },
                dismissButton = { TextButton(onClick = { editingProduct = null }) { Text("Cancelar") } }
            )
        }
    }
}
