package com.example.sanaescencia.view.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.model.RoleType
import com.example.sanaescencia.model.base64ToBitmap
import com.example.sanaescencia.model.bitmapToBase64

@Composable
fun ProfileScreen(
    currentUser: AuthUser?,
    onSwitchRole: (RoleType) -> Unit,
    onLogout: () -> Unit,
    onUpdateProfile: (originalEmail: String, name: String, rut: String, email: String, phone: String, password: String, role: RoleType, profileImageUri: String) -> Boolean,
    isEditingProfile: Boolean,
    onEditingProfileChanged: (Boolean) -> Unit
) {
    var profileBitmap by remember(currentUser?.profileImageUri) {
        mutableStateOf(currentUser?.profileImageUri?.let { base64ToBitmap(it) })
    }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(profileBitmap) {
        if (profileBitmap != null && currentUser != null) {
            val base64 = bitmapToBase64(profileBitmap!!)
            if (base64 != currentUser.profileImageUri) {
                onUpdateProfile(
                    currentUser.email,
                    currentUser.name,
                    currentUser.rut,
                    currentUser.email,
                    currentUser.phone,
                    currentUser.passwordHash,
                    currentUser.role,
                    base64
                )
            }
        }
    }

    // Launcher para la Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            profileBitmap = bitmap
        }
    }

    // Launcher para la Galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            runCatching {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                profileBitmap = ImageDecoder.decodeBitmap(source)
            }
        }
    }

    if (isEditingProfile && currentUser != null) {
        EditUserAdminScreen(
            user = currentUser,
            onUpdateUser = { origEmail, name, rut, email, phone, password, role, imageUri ->
                val success = onUpdateProfile(origEmail, name, rut, email, phone, password, role, imageUri)
                if (success) {
                    onEditingProfileChanged(false)
                }
                success
            },
            onBack = { onEditingProfileChanged(false) }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Avatar del Perfil interactivo con insignia de lápiz completamente visible
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { showImagePickerDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileBitmap != null) {
                        Image(
                            bitmap = profileBitmap!!.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = when (currentUser?.role) {
                                RoleType.PACIENTE -> Icons.Default.Person
                                RoleType.ADMINISTRACION -> Icons.Default.AdminPanelSettings
                                null -> Icons.Default.AccountCircle
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                // Insignia superpuesta en la esquina completamente visible
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Cambiar Foto",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre y Correo
            Text(
                text = currentUser?.name ?: "Usuario",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentUser?.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Detalles de la Cuenta
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Información de la Cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoRow(
                        icon = Icons.Default.Person,
                        label = "Nombre Completo",
                        value = currentUser?.name ?: "Usuario"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileInfoRow(
                        icon = Icons.Default.Badge,
                        label = "RUT",
                        value = if (!currentUser?.rut.isNullOrBlank()) currentUser.rut else "Sin RUT registrado"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Teléfono / WhatsApp",
                        value = if (!currentUser?.phone.isNullOrBlank()) currentUser.phone else "Sin teléfono registrado"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileInfoRow(
                        icon = Icons.Default.Email,
                        label = "Correo Electrónico",
                        value = currentUser?.email ?: "No registrado"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileInfoRow(
                        icon = Icons.Default.VpnKey,
                        label = "Contraseña de Acceso",
                        value = currentUser?.passwordHash ?: "••••••"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Cambio Rápido de Rol (Demo)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cambiar Perfil Activo (Modo Demo)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    RoleType.entries.forEach { role ->
                        if (role != currentUser?.role) {
                            OutlinedButton(
                                onClick = { onSwitchRole(role) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = if (role == RoleType.PACIENTE) Icons.Default.Person else Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cambiar a ${role.displayName}")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Editar Perfil
            Button(
                onClick = { onEditingProfileChanged(true) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Cerrar Sesión
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Diálogo de Selección de Origen de la Imagen (Cámara o Galería)
    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = {
                Text(
                    text = "Foto de Perfil",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿De dónde deseas seleccionar tu foto de perfil?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Opción 1: Cámara
                    OutlinedButton(
                        onClick = {
                            showImagePickerDialog = false
                            cameraLauncher.launch(null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Tomar foto con Cámara", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Opción 2: Galería
                    OutlinedButton(
                        onClick = {
                            showImagePickerDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Elegir de Galería", fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImagePickerDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}
