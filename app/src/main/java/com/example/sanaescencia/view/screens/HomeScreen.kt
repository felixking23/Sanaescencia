package com.example.sanaescencia.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.view.components.GpsLocationCard

@Composable
fun HomeScreen(
    currentUser: AuthUser?,
    onNavigateToCatalog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Saludo
        Text(
            text = "¡Hola, ${currentUser?.name ?: "Cliente"}!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Bienvenido al Centro Integrativo Sanaesencia",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjeta Ubicación GPS Sede Los Vilos
        GpsLocationCard()

        Spacer(modifier = Modifier.height(20.dp))

        // Acceso Principal
        Text(
            text = "Acciones Rápidas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNavigateToCatalog,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Storefront, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Explorar Catálogo de Productos Naturales", fontWeight = FontWeight.Bold)
        }
    }
}
