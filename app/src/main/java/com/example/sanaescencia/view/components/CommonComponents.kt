package com.example.sanaescencia.view.components

import android.telephony.TelephonyCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanaescencia.model.AlertType
import com.example.sanaescencia.model.AuthUser
import com.example.sanaescencia.model.PredictiveAlert
import com.example.sanaescencia.model.RoleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRoleBar(
    currentUser: AuthUser?,
    onSwitchRole: (RoleType) -> Unit,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Column {
                Text(
                    text = "Sanaesencia",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = currentUser?.name ?: "Sin sesión",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        },
        actions = {
            currentUser?.let { user ->
                AssistChip(
                    onClick = { showMenu = true },
                    label = { Text(user.role.name) },
                    leadingIcon = {
                        Icon(
                            imageVector = when (user.role) {
                                RoleType.PACIENTE -> Icons.Default.Person
                                RoleType.ADMINISTRACION -> Icons.Default.AdminPanelSettings
                            },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
                )

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    Text(
                        text = " Cambiar Perfil (Demo)",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()
                    RoleType.entries.forEach { role ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(role.displayName, fontWeight = FontWeight.SemiBold)
                                    Text(role.description, style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = {
                                showMenu = false
                                onSwitchRole(role)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (role) {
                                        RoleType.PACIENTE -> Icons.Default.Person
                                        RoleType.ADMINISTRACION -> Icons.Default.AdminPanelSettings
                                    },
                                    contentDescription = null
                                )
                            }
                        )
                    }
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            onLogout()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun comp(
    currentUser: AuthUser?

){

}
@Composable
fun GpsLocationCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Sede Los Vilos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SuggestionChip(
                        onClick = {},
                        label = { Text("8 Boxes", fontSize = 10.sp) },
                        modifier = Modifier.height(24.dp)
                    )
                }
                Text(
                    text = "Caupolicán 220, Los Vilos",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " GPS Activo: A 1.8 km de tu ubicación",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PredictiveAlertCard(
    alert: PredictiveAlert,
    onDismiss: (String) -> Unit
) {
    val backgroundColor = when (alert.alertType) {
        AlertType.RECORDATORIO_CITA -> MaterialTheme.colorScheme.surfaceVariant
        AlertType.ALERTA_DESERCION -> Color(0xFFFFF3E0)
        AlertType.PROMOCION_REENGANCHE -> Color(0xFFE8F5E9)
    }

    val icon = when (alert.alertType) {
        AlertType.RECORDATORIO_CITA -> Icons.Default.NotificationsActive
        AlertType.ALERTA_DESERCION -> Icons.Default.Warning
        AlertType.PROMOCION_REENGANCHE -> Icons.Default.LocalOffer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alert.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = alert.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(
                onClick = { onDismiss(alert.id) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
