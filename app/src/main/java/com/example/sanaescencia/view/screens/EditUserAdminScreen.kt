package com.example.sanaescencia.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.model.RoleType
import com.example.sanaescencia.model.formatRut
import com.example.sanaescencia.model.isValidEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserAdminScreen(
    user: AuthUser,
    onUpdateUser: (originalEmail: String, name: String, rut: String, email: String, phone: String, password: String, role: RoleType) -> Boolean,
    onBack: () -> Unit
) {
    val nameParts = user.name.split(" ", limit = 2)
    var firstName by remember { mutableStateOf(nameParts.getOrNull(0) ?: "") }
    var lastName by remember { mutableStateOf(nameParts.getOrNull(1) ?: "") }
    val initialRut = formatRut(user.rut)
    var rutValue by remember { mutableStateOf(TextFieldValue(initialRut, selection = TextRange(initialRut.length))) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var password by remember { mutableStateOf(user.passwordHash) }
    var confirmPassword by remember { mutableStateOf(user.passwordHash) }
    var selectedRole by remember { mutableStateOf(user.role) }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Usuario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Modificar Datos del Usuario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selector de Rol (Cliente o Admin)
            Text(
                text = "Rol",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { selectedRole = RoleType.PACIENTE },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedRole == RoleType.PACIENTE) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cliente")
                }

                OutlinedButton(
                    onClick = { selectedRole = RoleType.ADMINISTRACION },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedRole == RoleType.ADMINISTRACION) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Admin")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = rutValue,
                onValueChange = { input ->
                    val formatted = formatRut(input.text)
                    rutValue = TextFieldValue(
                        text = formatted,
                        selection = TextRange(formatted.length)
                    )
                },
                label = { Text("RUT (ej: 12.345.678-9)") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.filterNot { c -> c.isWhitespace() } },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono / WhatsApp") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        firstName.isBlank() -> {
                            errorMessage = "Por favor ingresa el nombre."
                        }
                        lastName.isBlank() -> {
                            errorMessage = "Por favor ingresa el apellido."
                        }
                        rutValue.text.isBlank() -> {
                            errorMessage = "Por favor ingresa el RUT."
                        }
                        email.isBlank() -> {
                            errorMessage = "Por favor ingresa el correo electrónico."
                        }
                        !isValidEmail(email) -> {
                            errorMessage = "El correo electrónico debe ser válido (ejemplo: usuario@correo.com)."
                        }
                        password.isBlank() -> {
                            errorMessage = "Por favor ingresa una contraseña."
                        }
                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden."
                        }
                        else -> {
                            errorMessage = null
                            val fullName = "$firstName $lastName".trim()
                            val success = onUpdateUser(user.email, fullName, rutValue.text, email, phone, password, selectedRole)
                            if (!success) {
                                errorMessage = "No se pudo actualizar el usuario."
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Guardar Cambios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
