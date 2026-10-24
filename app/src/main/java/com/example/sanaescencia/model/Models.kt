package com.example.sanaescencia.model

enum class RoleType(val displayName: String, val description: String) {
    PACIENTE("Cliente / Paciente", "Explora el catálogo de productos, terapias y agenda sus atenciones"),
    ADMINISTRACION("Administrador del Centro", "Gestiona el catálogo, la ocupación de los 8 boxes y alertas de deserción")
}

data class AuthUser(
    val email: String,
    val passwordHash: String = "123456",
    val name: String,
    val role: RoleType,
    val detail: String
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



