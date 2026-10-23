package com.example.sanaescencia.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanaescencia.model.BoxStatus
import com.example.sanaescencia.model.PatientContinuity

@Composable
fun AdminDashboardScreen(
    boxes: List<BoxStatus>,
    patientContinuityList: List<PatientContinuity>,
    onSendPromotion: (String) -> Unit
) {
    val occupiedBoxesCount = boxes.count { it.isOccupied }
    val occupancyPercentage = (occupiedBoxesCount * 100) / 8
    val atRiskPatients = patientContinuityList.filter { it.isAtRiskOfDesertion }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado Admin Centrado
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dashboard de Administración",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Control de 8 Boxes Presenciales & Control de Deserción",
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
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Ocupación de Boxes", style = MaterialTheme.typography.labelMedium)
                    Text("$occupancyPercentage%", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("$occupiedBoxesCount de 8 boxes en uso", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Riesgo Deserción", style = MaterialTheme.typography.labelMedium)
                    Text("${atRiskPatients.size} Pacientes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    Text(">21 días sin sesión", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ocupación de los 8 Boxes (Caupolicán 220, Los Vilos)
        Text(
            text = "Estado en Vivo de los 8 Boxes (Los Vilos)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Planificación y asignación de infraestructura física",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            boxes.chunked(2).forEach { rowBoxes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowBoxes.forEach { box ->
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (box.isOccupied) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = box.boxName,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Badge(
                                        containerColor = if (box.isOccupied) Color.Red else Color(0xFF2E7D32)
                                    ) {
                                        Text(if (box.isOccupied) "Ocupado" else "Disponible", color = Color.White, fontSize = 9.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                if (box.isOccupied) {
                                    Text("Especialidad: ${box.assignedSpecialty ?: "General"}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    Text("Paciente: ${box.currentPatient ?: "Asignado"}", style = MaterialTheme.typography.bodySmall)
                                } else {
                                    Text("Espacio disponible para agendamiento", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Motor de Alertas Predictivas y Deserción
        Text(
            text = "Motor Predictivo de Deserción y Reenganche",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Detección temprana de inactividad para estabilizar la demanda",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        atRiskPatients.forEach { patient ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = patient.patientName,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text("${patient.daysSinceLastSession} días inactivo", fontSize = 11.sp, color = Color(0xFFD84315)) },
                            leadingIcon = { Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFD84315)) }
                        )
                    }

                    Text(text = "Tratamiento: ${patient.therapyName} · Sesiones realizadas: ${patient.completedSessions}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(10.dp))

                    if (patient.promotionSent) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Promoción de reenganche enviada correctamente", color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { onSendPromotion(patient.patientId) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Enviar Promoción de Reenganche (20% Off)")
                        }
                    }
                }
            }
        }
    }
}
