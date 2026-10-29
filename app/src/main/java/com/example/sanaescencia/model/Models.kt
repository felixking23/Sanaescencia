package com.example.sanaescencia.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

enum class RoleType(val displayName: String, val description: String) {
    PACIENTE("Cliente / Paciente", "Explora el catálogo de productos, terapias y agenda sus atenciones"),
    ADMINISTRACION("Administrador", "Gestiona el catálogo, la ocupación de los 8 boxes y alertas de deserción")
}

data class AuthUser(
    val email: String,
    val passwordHash: String = "123456",
    val name: String,
    val rut: String = "",
    val phone: String = "",
    val role: RoleType,
    val detail: String,
    val profileImageUri: String = ""
)

data class Specialty(
    val id: String,
    val name: String,
    val professional: String,
    val description: String,
    val price: Int,
    val durationMinutes: Int = 45,
    val isPresencialAvailable: Boolean = true,
    val isOnlineAvailable: Boolean = true,
    val category: String = "Salud Mental y Bienestar"
)

enum class ProductType(val label: String) {
    KIT("Kit de Bienestar"),
    ROLL_ON("Roll-on Aromaterapia"),
    BRUMA("Bruma Natural")
}

data class NaturalProduct(
    val id: String,
    val name: String,
    val type: ProductType,
    val price: Int,
    val description: String,
    val volume: String
)

enum class AppointmentModality(val label: String) {
    PRESENCIA_LOS_VILOS("Presencial - Los Vilos (Caupolicán 220)"),
    ONLINE_MEDILINK("Online - Plataforma MEDIlink")
}

enum class AppointmentStatus(val label: String) {
    AGENDADA("Agendada"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada")
}

data class Appointment(
    val id: String,
    val patientName: String,
    val patientEmail: String,
    val specialtyName: String,
    val professionalName: String,
    val date: String,
    val timeSlot: String,
    val modality: AppointmentModality,
    val boxNumber: Int? = null,
    val status: AppointmentStatus = AppointmentStatus.AGENDADA,
    val notes: String = ""
)

data class BoxStatus(
    val boxNumber: Int,
    val boxName: String = "Box $boxNumber",
    val isOccupied: Boolean = false,
    val assignedSpecialty: String? = null,
    val currentPatient: String? = null,
    val nextSchedule: String = "Disponible"
)

data class PatientContinuity(
    val patientId: String,
    val patientName: String,
    val patientEmail: String,
    val therapyName: String,
    val completedSessions: Int,
    val daysSinceLastSession: Int,
    val isAtRiskOfDesertion: Boolean,
    val promotionSent: Boolean = false
)

enum class AlertType(val title: String) {
    RECORDATORIO_CITA("Recordatorio de Cita"),
    ALERTA_DESERCION("Alerta Predictiva de Deserción"),
    PROMOCION_REENGANCHE("Promoción Especial Reenganche")
}

data class PredictiveAlert(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val alertType: AlertType,
    val isDismissed: Boolean = false
)

fun formatRut(rawInput: String): String {
    val cleaned = rawInput.filter { it.isDigit() || it == 'k' || it == 'K' }
    if (cleaned.isEmpty()) return ""

    val trimmed = cleaned.take(9)
    if (trimmed.length == 1) return trimmed.uppercase()

    val body = trimmed.dropLast(1)
    val dv = trimmed.last().uppercaseChar()
    if (body.isEmpty()) return dv.toString()

    val formattedBody = StringBuilder()
    var count = 0
    for (i in body.length - 1 downTo 0) {
        formattedBody.append(body[i])
        count++
        if (count == 3 && i != 0) {
            formattedBody.append('.')
            count = 0
        }
    }

    return "${formattedBody.reverse()}-$dv"
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return email.trim().matches(emailRegex)
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        if (base64Str.isBlank()) return null
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}
