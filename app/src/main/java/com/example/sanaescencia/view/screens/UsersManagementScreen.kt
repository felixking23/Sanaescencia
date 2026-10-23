package com.example.sanaescencia.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.model.RoleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersManagementScreen(
    users: List<AuthUser>
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf<RoleType?>(null) }

    val filteredUsers = users.filter { user ->
        val matchesSearch = user.name.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true) ||
                user.detail.contains(searchQuery, ignoreCase = true)
        val matchesRole = selectedRoleFilter == null || user.role == selectedRoleFilter
        matchesSearch && matchesRole
    }

    val totalPatients = users.count { it.role == RoleType.PACIENTE }
    val totalAdmins = users.count { it.role == RoleType.ADMINISTRACION }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gestión de Usuarios",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Listado completo de clientes y administradores registrados en Sanaesencia",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjetas Resumen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Total Usuarios", style = MaterialTheme.typography.labelMedium)
                    Text("${users.size}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Registrados", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Clientes/Pacientes", style = MaterialTheme.typography.labelMedium)
                    Text("$totalPatients", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text("Clientes Activos", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Administradores", style = MaterialTheme.typography.labelMedium)
                    Text("$totalAdmins", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF512DA8))
                    Text("Personal Centro", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Filtro y Búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por nombre, correo o detalle...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filtro por Rol
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = selectedRoleFilter == null,
                onClick = { selectedRoleFilter = null },
                label = { Text("Todos (${users.size})") }
            )
            FilterChip(
                selected = selectedRoleFilter == RoleType.PACIENTE,
                onClick = { selectedRoleFilter = RoleType.PACIENTE },
                label = { Text("Clientes ($totalPatients)") }
            )
            FilterChip(
                selected = selectedRoleFilter == RoleType.ADMINISTRACION,
                onClick = { selectedRoleFilter = RoleType.ADMINISTRACION },
                label = { Text("Admin ($totalAdmins)") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabla de Usuarios
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Título de la Tabla
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tabla de Usuarios Creados (${filteredUsers.size})",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                HorizontalDivider()

                if (filteredUsers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron usuarios con el criterio de búsqueda.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    // Tabla desplazable horizontalmente para visualización perfecta
                    val horizontalScrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        // Encabezados de la Tabla
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nombre",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(160.dp)
                            )
                            Text(
                                text = "RUT",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(130.dp)
                            )
                            Text(
                                text = "Correo Electrónico",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(200.dp)
                            )
                            Text(
                                text = "Contraseña",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(130.dp)
                            )
                            Text(
                                text = "Rol / Perfil",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(140.dp)
                            )
                            Text(
                                text = "Detalle de Cuenta",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(220.dp)
                            )
                        }

                        HorizontalDivider()

                        // Filas de la Tabla
                        filteredUsers.forEachIndexed { index, user ->
                            val backgroundColor = if (index % 2 == 0) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            }

                            Row(
                                modifier = Modifier
                                    .background(backgroundColor)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Nombre
                                Row(
                                    modifier = Modifier.width(160.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (user.role == RoleType.PACIENTE) Icons.Default.Person else Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = if (user.role == RoleType.PACIENTE) Color(0xFF2E7D32) else Color(0xFF512DA8),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = user.name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // RUT
                                Text(
                                    text = if (user.rut.isNotBlank()) user.rut else "Sin RUT",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray,
                                    modifier = Modifier.width(130.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                // Correo
                                Text(
                                    text = user.email,
                                    fontSize = 13.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.width(200.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                // Contraseña
                                Row(
                                    modifier = Modifier.width(130.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VpnKey,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = user.passwordHash,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // Rol
                                Box(modifier = Modifier.width(140.dp)) {
                                    Surface(
                                        color = if (user.role == RoleType.PACIENTE) Color(0xFFE8F5E9) else Color(0xFFEDE7F6),
                                        contentColor = if (user.role == RoleType.PACIENTE) Color(0xFF2E7D32) else Color(0xFF512DA8),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = if (user.role == RoleType.PACIENTE) "Paciente" else "Administrador",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                // Detalle
                                Text(
                                    text = user.detail,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.width(220.dp),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (index < filteredUsers.size - 1) {
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}
