package com.example.sanaescencia.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.model.RoleType
import com.example.sanaescencia.model.base64ToBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersManagementScreen(
    users: List<AuthUser>,
    onDeleteUser: (AuthUser) -> Unit,
    onCreateUser: (name: String, rut: String, email: String, phone: String, password: String, role: RoleType) -> Boolean,
    onUpdateUser: (originalEmail: String, name: String, rut: String, email: String, phone: String, password: String, role: RoleType, profileImageUri: String) -> Boolean,
    isCreatingUser: Boolean,
    onCreatingUserChanged: (Boolean) -> Unit,
    isEditingUser: Boolean,
    onEditingUserChanged: (Boolean) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf<RoleType?>(null) }
    var selectedUserForDetails by remember { mutableStateOf<AuthUser?>(null) }
    var userToDelete by remember { mutableStateOf<AuthUser?>(null) }
    var userToEdit by remember { mutableStateOf<AuthUser?>(null) }

    LaunchedEffect(userToEdit) {
        onEditingUserChanged(userToEdit != null)
    }

    if (userToEdit != null) {
        EditUserAdminScreen(
            user = userToEdit!!,
            onUpdateUser = { origEmail, name, rut, email, phone, password, role, imageUri ->
                val success = onUpdateUser(origEmail, name, rut, email, phone, password, role, imageUri)
                if (success) {
                    userToEdit = null
                }
                success
            },
            onBack = { userToEdit = null }
        )
    } else if (isCreatingUser) {
        CreateUserAdminScreen(
            onCreateUser = { name, rut, email, phone, password, role ->
                val success = onCreateUser(name, rut, email, phone, password, role)
                if (success) {
                    onCreatingUserChanged(false)
                }
                success
            },
            onBack = { onCreatingUserChanged(false) }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            val filteredUsers = users.filter { user ->
                val matchesSearch = user.name.contains(searchQuery, ignoreCase = true) ||
                        user.rut.contains(searchQuery, ignoreCase = true) ||
                        user.email.contains(searchQuery, ignoreCase = true) ||
                        user.detail.contains(searchQuery, ignoreCase = true)
                val matchesRole = selectedRoleFilter == null || user.role == selectedRoleFilter
                matchesSearch && matchesRole
            }

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
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Filtro y Búsqueda
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar", color = Color.Gray) },
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
                        label = { Text("Todos") }
                    )
                    FilterChip(
                        selected = selectedRoleFilter == RoleType.PACIENTE,
                        onClick = { selectedRoleFilter = RoleType.PACIENTE },
                        label = { Text("Clientes") }
                    )
                    FilterChip(
                        selected = selectedRoleFilter == RoleType.ADMINISTRACION,
                        onClick = { selectedRoleFilter = RoleType.ADMINISTRACION },
                        label = { Text("Admins") }
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
                            Text(
                                text = "Tabla de usuarios",
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
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Encabezados de la Tabla
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Nombre",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(0.9f)
                                    )
                                    Text(
                                        text = "Rol",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(0.6f)
                                    )
                                    Spacer(modifier = Modifier.weight(1.8f))
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
                                            .fillMaxWidth()
                                            .background(backgroundColor)
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Nombre
                                        Text(
                                            text = user.name,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            modifier = Modifier.weight(0.9f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        // Rol
                                        Text(
                                            text = when (user.role) {
                                                RoleType.ADMINISTRACION -> "Admin"
                                                RoleType.PACIENTE -> "Cliente"
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black,
                                            modifier = Modifier.weight(0.6f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        // Botones de Acción ('Borrar', 'Editar' y 'Ver')
                                        Row(
                                            modifier = Modifier.weight(1.8f),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val isMainAdmin = user.email.equals("admin@sanaescencia.cl", ignoreCase = true) || user.name.contains("Admin Jefe", ignoreCase = true)
                                            if (!isMainAdmin) {
                                                Button(
                                                    onClick = { userToDelete = user },
                                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                                    shape = RoundedCornerShape(8.dp),
                                                    modifier = Modifier.height(30.dp)
                                                ) {
                                                    Text("Borrar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }

                                                Spacer(modifier = Modifier.width(4.dp))
                                            }

                                            Button(
                                                onClick = { userToEdit = user },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(30.dp)
                                            ) {
                                                Text("Editar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }

                                            Spacer(modifier = Modifier.width(4.dp))

                                            OutlinedButton(
                                                onClick = { selectedUserForDetails = user },
                                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(30.dp)
                                            ) {
                                                Text("Ver", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
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

            // Botón flotante circular con símbolo de más en la zona inferior izquierda
            FloatingActionButton(
                onClick = { onCreatingUserChanged(true) },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear Usuario",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

    // Modal / Diálogo para "Ver Datos del Usuario"
    selectedUserForDetails?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUserForDetails = null },
            confirmButton = {
                Button(
                    onClick = { selectedUserForDetails = null },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cerrar")
                }
            },
            title = {
                Text(
                    text = "Datos de la Cuenta",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto de Perfil / Avatar
                    val userBitmap = remember(user.profileImageUri) { base64ToBitmap(user.profileImageUri) }
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                if (user.role == RoleType.PACIENTE) Color(0xFFE8F5E9) else Color(0xFFEDE7F6)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (userBitmap != null) {
                            Image(
                                bitmap = userBitmap.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = if (user.role == RoleType.PACIENTE) Icons.Default.Person else Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = if (user.role == RoleType.PACIENTE) Color(0xFF2E7D32) else Color(0xFF512DA8),
                                modifier = Modifier.size(46.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Datos ordenados de arriba hacia abajo
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            UserDetailItem(
                                icon = Icons.Default.Person,
                                label = "Nombre Completo",
                                value = user.name
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            UserDetailItem(
                                icon = Icons.Default.Badge,
                                label = "RUT",
                                value = if (user.rut.isNotBlank()) user.rut else "Sin RUT registrado"
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            UserDetailItem(
                                icon = Icons.Default.Email,
                                label = "Correo Electrónico",
                                value = user.email
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            UserDetailItem(
                                icon = Icons.Default.Phone,
                                label = "Teléfono / WhatsApp",
                                value = if (user.phone.isNotBlank()) user.phone else "Sin teléfono registrado"
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            UserDetailItem(
                                icon = Icons.Default.VpnKey,
                                label = "Contraseña",
                                value = user.passwordHash
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            UserDetailItem(
                                icon = if (user.role == RoleType.ADMINISTRACION) Icons.Default.AdminPanelSettings else Icons.Default.AccountCircle,
                                label = "Rol",
                                value = user.role.displayName
                            )
                        }
                    }
                }
            }
        )
    }

    // Modal / Diálogo para "Confirmación de Eliminación"
    userToDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = {
                Text("Eliminar Usuario", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar la cuenta de ${user.name} (${user.email})? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteUser(user)
                        userToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { userToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun UserDetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
