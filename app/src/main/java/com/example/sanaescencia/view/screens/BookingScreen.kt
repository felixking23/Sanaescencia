package com.example.sanaescencia.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sanaescencia.model.AppointmentModality
import com.example.sanaescencia.model.BoxStatus
import com.example.sanaescencia.model.Specialty
import com.example.sanaescencia.repository.SanaesenciaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    preselectedSpecialty: Specialty? = null,
    boxes: List<BoxStatus>,
    onBookSuccess: (Specialty, String, String, AppointmentModality, Int?) -> Unit
) {
    val specialties = SanaesenciaRepository.specialties
    var selectedSpecialty by remember { mutableStateOf(preselectedSpecialty ?: specialties[0]) }
    var selectedModality by remember { mutableStateOf(AppointmentModality.PRESENCIA_LOS_VILOS) }
    var selectedBoxNumber by remember { mutableStateOf(1) }
    var selectedDate by remember { mutableStateOf("Hoy") }
    var selectedTimeSlot by remember { mutableStateOf("15:00 hrs") }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val dateOptions = listOf("Hoy", "Mañana", "En 2 días", "Próximo Lunes")
    val timeOptions = listOf("09:00 hrs", "11:00 hrs", "15:00 hrs", "17:00 hrs", "18:30 hrs")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Agendar Nueva Consulta",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Selecciona la especialidad, modalidad y horario de atención",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Paso 1: Especialidad
        Text(text = "1. Selecciona la Especialidad Terapéutica", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        var specialtyDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = specialtyDropdownExpanded,
            onExpandedChange = { specialtyDropdownExpanded = !specialtyDropdownExpanded }
        ) {
            OutlinedTextField(
                value = "${selectedSpecialty.name} (${selectedSpecialty.professional})",
                onValueChange = {},
                readOnly = true,
                label = { Text("Especialidad Terapéutica") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = specialtyDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = specialtyDropdownExpanded,
                onDismissRequest = { specialtyDropdownExpanded = false }
            ) {
                specialties.forEach { spec ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(spec.name, fontWeight = FontWeight.SemiBold)
                                Text("Profesional: ${spec.professional} · $${spec.price}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        },
                        onClick = {
                            selectedSpecialty = spec
                            specialtyDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Paso 2: Modalidad
        Text(text = "2. Selecciona la Modalidad de Atención", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedModality == AppointmentModality.PRESENCIA_LOS_VILOS,
                onClick = { selectedModality = AppointmentModality.PRESENCIA_LOS_VILOS },
                label = { Text("Presencial Los Vilos") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedModality == AppointmentModality.ONLINE_MEDILINK,
                onClick = { selectedModality = AppointmentModality.ONLINE_MEDILINK },
                label = { Text("Online MEDIlink") },
                leadingIcon = { Icon(Icons.Default.Computer, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.weight(1f)
            )
        }

        if (selectedModality == AppointmentModality.PRESENCIA_LOS_VILOS) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Asignación de Box Presencial (Los Vilos · Caupolicán 220):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (1..8).take(4).forEach { boxNum ->
                            FilterChip(
                                selected = selectedBoxNumber == boxNum,
                                onClick = { selectedBoxNumber = boxNum },
                                label = { Text("Box $boxNum") }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (5..8).forEach { boxNum ->
                            FilterChip(
                                selected = selectedBoxNumber == boxNum,
                                onClick = { selectedBoxNumber = boxNum },
                                label = { Text("Box $boxNum") }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Paso 3: Fecha y Horario
        Text(text = "3. Fecha y Bloque Horario", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Día:", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dateOptions.forEach { dateOpt ->
                FilterChip(
                    selected = selectedDate == dateOpt,
                    onClick = { selectedDate = dateOpt },
                    label = { Text(dateOpt) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Hora:", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeOptions.take(3).forEach { timeOpt ->
                FilterChip(
                    selected = selectedTimeSlot == timeOpt,
                    onClick = { selectedTimeSlot = timeOpt },
                    label = { Text(timeOpt) }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Botón Confirmar
        Button(
            onClick = { showConfirmationDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Confirmar Agendamiento ($${selectedSpecialty.price})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmar Cita Terapéutica") },
            text = {
                Column {
                    Text("• Especialidad: ${selectedSpecialty.name}")
                    Text("• Profesional: ${selectedSpecialty.professional}")
                    Text("• Modalidad: ${selectedModality.label}")
                    if (selectedModality == AppointmentModality.PRESENCIA_LOS_VILOS) {
                        Text("• Lugar: Sede Los Vilos · Box $selectedBoxNumber")
                    }
                    Text("• Horario: $selectedDate a las $selectedTimeSlot")
                    Text("• Valor: $${selectedSpecialty.price}")
                }
            },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                    onBookSuccess(
                        selectedSpecialty,
                        selectedDate,
                        selectedTimeSlot,
                        selectedModality,
                        if (selectedModality == AppointmentModality.PRESENCIA_LOS_VILOS) selectedBoxNumber else null
                    )
                }) {
                    Text("Agendar Ahora")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
