package com.example.sanaescencia.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sanaescencia.NavTarget
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.viewmodel.SanaesenciaViewModel
import com.example.sanaescencia.model.RoleType
@Composable
fun CarritoScreen(viewModel: SanaesenciaViewModel = viewModel()) {
    // Aquí puedes implementar la lógica y la interfaz de usuario para la pantalla del carrito de compras.
    // Por ejemplo, mostrar los productos en el carrito, permitir eliminar productos, etc.
    // Puedes acceder al usuario actual y a la lista de productos en el carrito según sea necesario.
    // Por ahora, solo mostramos un mensaje de ejemplo.
    // Ejemplo de mensaje:
    // Text(text = "Esta es la pantalla del carrito de compras.")
    val currentUser: AuthUser? by viewModel.currentUser.collectAsState() // Aquí deberías obtener el usuario actual desde tu ViewModel o estado global
    val user : Boolean = currentUser?.role == RoleType.PACIENTE;// Aquí deberías obtener el usuario actual desde tu ViewModel o estado global
    val currentScreen2: NavTarget = NavTarget.CARRITO // Aquí deberías obtener
    val cartItems: Map<String, Int> = emptyMap() // Aquí deberías obtener la lista de productos en el carrito desde tu ViewModel o estado global
    var searchQuery by remember { mutableStateOf("") } // Aquí deberías obtener la consulta de búsqueda desde tu ViewModel o estado global

    var filteredProducts by remember { mutableStateOf(emptyList<Any>()) } // Aquí deberías obtener la lista de productos filtrados desde tu ViewModel o estado global

    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()

    ) {
        Column(modifier = Modifier.width(12.dp).padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Column{
                        if (user) {
                            androidx.compose.material3.Text(
                                text = "Carrito de Compras",
                                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            androidx.compose.material3.Text(
                                text = "Bienvenido, ${currentUser?.name ?: "Cliente"}",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                                    .width(12.dp)
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                items(filteredProducts) { product ->

                                // Aquí puedes mostrar cada producto en el carrito, por ejemplo, usando un Card o Row.
                                    // Asegúrate de personalizar la visualización según tus necesidades.
                                }
                                // Aquí puedes agregar los elementos del carrito de compras
                                // Por ejemplo, puedes usar items() para iterar sobre la lista de productos en el carrito
                                // y mostrar cada producto en un Card o Row.
                                // Asegúrate de obtener la lista de productos desde tu ViewModel o estado global.) {

                            }
                        } else {
                            androidx.compose.material3.Text(
                                text = "Acceso Denegado",
                                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            androidx.compose.material3.Text(
                                text = "Lo sentimos, esta sección está disponible solo para pacientes.",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }

            }
        }

    }
}

