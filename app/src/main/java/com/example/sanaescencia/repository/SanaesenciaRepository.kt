package com.example.sanaescencia.repository

import com.example.sanaescencia.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SanaesenciaRepository {

    // --- Credenciales de Demostración y Usuarios del Sistema ---
    val demoUsers = listOf(
        AuthUser(
            email = "paciente@sanaescencia.cl",
            passwordHash = "123456",
            name = "María González",
            rut = "18.452.193-K",
            phone = "+56 9 8765 4321",
            role = RoleType.PACIENTE,
            detail = "Cliente / Paciente Activo"
        ),
        AuthUser(
            email = "admin@sanaescencia.cl",
            passwordHash = "123456",
            name = "Administración Los Vilos",
            rut = "15.820.491-3",
            phone = "+56 9 1234 5678",
            role = RoleType.ADMINISTRACION,
            detail = "Gestión del Catálogo de Productos y 8 Boxes"
        )
    )

    private val _users = MutableStateFlow(demoUsers)
    val users: StateFlow<List<AuthUser>> = _users.asStateFlow()

    fun addUser(user: AuthUser) {
        _users.value = _users.value + user
    }

    // Catálogo de Especialidades Terapéuticas Reales
    val specialties = listOf(
        Specialty("1", "Psicología Infanto-Juvenil", "Dra. Camila Soto", "Atención psicológica integral para niños, adolescentes y orientación familiar.", 35000),
        Specialty("2", "Terapia Ocupacional", "Lic. Roberto Silva", "Evaluación e intervención del desarrollo psicomotor y habilidades de la vida diaria.", 32000),
        Specialty("3", "Fonoaudiología Integrativa", "Lic. Javiera Morales", "Diagnóstico y terapia del lenguaje, habla, voz y deglución.", 30000),
        Specialty("4", "Psicopedagogía Clínica", "Lic. Andrea Castro", "Apoyo en dificultades de aprendizaje, hábitos de estudio y neurodiversidad.", 28000),
        Specialty("5", "Nutrición Integrativa", "Dra. Valentina Palma", "Planes nutricionales enfocados en salud emocional y bienestar digestivo.", 25000),
        Specialty("6", "Matrona y Salud Femenina", "Lic. Sofía Riquelme", "Acompañamiento en etapas de la mujer, salud sexual y reproductiva.", 27000),
        Specialty("7", "Quiropraxia y Postura", "Dr. Matías Vera", "Alineación neuromuscular y alivio de dolor corporal.", 30000),
        Specialty("8", "Masajes Terapéuticos", "Kine. Daniel López", "Masajes descontracturantes y relajación corporal en box equipado.", 25000),
        Specialty("9", "Evaluación del Neurodesarrollo", "Equipo Multidisciplinario", "Evaluación completa para diagnósticos de desarrollo e inclusión escolar.", 60000),
        Specialty("10", "Terapia Floral Bach", "Esp. Patricia Araya", "Esencias florales personalizadas para la gestión de ansiedad y emociones.", 20000),
        Specialty("11", "Aromaterapia Integrativa", "Esp. Marcela Godoy", "Uso clínico de aceites esenciales puros para apoyo emocional.", 22000)
    )

    // Catálogo de Productos Naturales Sanaesencia
    val products = listOf(
        NaturalProduct("p1", "Kit Bienestar Infantil", ProductType.KIT, 24990, "Incluye Roll-on Calma + Bruma Sueño + Guía de uso consciente.", "Pack de 2 productos"),
        NaturalProduct("p2", "Kit Armonía Familiar", ProductType.KIT, 29990, "Línea completa de aromaterapia para espacios y manejo del estrés.", "Pack de 3 productos"),
        NaturalProduct("p3", "Roll-on Calma Infanto-Juvenil", ProductType.ROLL_ON, 8990, "Mezcla de lavanda y manzanilla para reducir estados de ansiedad.", "10 ml"),
        NaturalProduct("p4", "Roll-on Enfoque y Concentración", ProductType.ROLL_ON, 8990, "Con menta y romero para potenciar la atención en sesiones de estudio.", "10 ml"),
        NaturalProduct("p5", "Bruma Sueño Profundo", ProductType.BRUMA, 10990, "Spray ambiental con bergamota y lavanda para favorecer el descanso nocturno.", "100 ml"),
        NaturalProduct("p6", "Bruma Energizante Cítrica", ProductType.BRUMA, 10990, "Aroma tonificante para empezar la jornada con ánimo positivo.", "100 ml")
    )

    // Estado en Vivo de Citas
    private val _appointments = MutableStateFlow(
        listOf(
            Appointment("a1", "María González", "paciente@sanaescencia.cl", "Psicología Infanto-Juvenil", "Dra. Camila Soto", "Hoy", "15:00 hrs", AppointmentModality.PRESENCIA_LOS_VILOS, boxNumber = 1),
            Appointment("a2", "Tomás Tapia", "ttapia@mail.com", "Terapia Ocupacional", "Lic. Roberto Silva", "Mañana", "11:00 hrs", AppointmentModality.PRESENCIA_LOS_VILOS, boxNumber = 2),
            Appointment("a3", "Lucía Fernández", "lfernandez@mail.com", "Fonoaudiología Integrativa", "Lic. Javiera Morales", "15/09", "16:30 hrs", AppointmentModality.ONLINE_MEDILINK)
        )
    )
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    // Ocupación de los 8 Boxes Presenciales en Los Vilos (Caupolicán 220)
    private val _boxes = MutableStateFlow(
        listOf(
            BoxStatus(1, "Box 1 - Psicología", isOccupied = true, assignedSpecialty = "Psicología Infanto-Juvenil", currentPatient = "María González", nextSchedule = "Ocupado hasta 16:00 hrs"),
            BoxStatus(2, "Box 2 - Terapia Ocupacional", isOccupied = true, assignedSpecialty = "Terapia Ocupacional", currentPatient = "Tomás Tapia", nextSchedule = "Ocupado hasta 12:00 hrs"),
            BoxStatus(3, "Box 3 - Fonoaudiología", isOccupied = false, assignedSpecialty = "Fonoaudiología Integrativa", currentPatient = null, nextSchedule = "Disponible desde 14:00 hrs"),
            BoxStatus(4, "Box 4 - Psicopedagogía", isOccupied = false, assignedSpecialty = "Psicopedagogía", currentPatient = null, nextSchedule = "Disponible todo el día"),
            BoxStatus(5, "Box 5 - Nutrición & Matrona", isOccupied = true, assignedSpecialty = "Nutrición Integrativa", currentPatient = "Ignacio Silva", nextSchedule = "Ocupado hasta 17:30 hrs"),
            BoxStatus(6, "Box 6 - Quiropraxia", isOccupied = false, assignedSpecialty = "Quiropraxia", currentPatient = null, nextSchedule = "Disponible desde 16:00 hrs"),
            BoxStatus(7, "Box 7 - Masajes & Kine", isOccupied = false, assignedSpecialty = "Masajes Terapéuticos", currentPatient = null, nextSchedule = "Disponible todo el día"),
            BoxStatus(8, "Box 8 - Terapias Florales", isOccupied = false, assignedSpecialty = "Terapia Floral / Aromaterapia", currentPatient = null, nextSchedule = "Disponible desde 18:00 hrs")
        )
    )
    val boxes: StateFlow<List<BoxStatus>> = _boxes.asStateFlow()

    // Trazabilidad de Pacientes y Alertas de Deserción
    private val _patientContinuityList = MutableStateFlow(
        listOf(
            PatientContinuity("p101", "Felipe Morales", "felipe@mail.com", "Psicología Infanto-Juvenil", 2, 28, isAtRiskOfDesertion = true),
            PatientContinuity("p102", "Constanza Rivas", "crivas@mail.com", "Terapia Ocupacional", 1, 35, isAtRiskOfDesertion = true),
            PatientContinuity("p103", "María González", "paciente@sanaescencia.cl", "Psicología Infanto-Juvenil", 5, 4, isAtRiskOfDesertion = false),
            PatientContinuity("p104", "Benjamín Soto", "bsoto@mail.com", "Psicopedagogía", 3, 12, isAtRiskOfDesertion = false)
        )
    )
    val patientContinuityList: StateFlow<List<PatientContinuity>> = _patientContinuityList.asStateFlow()

    // Alertas Predictivas
    private val _predictiveAlerts = MutableStateFlow(
        listOf(
            PredictiveAlert("alt1", "Recordatorio de Cita Presencial", "Tienes una cita programada en Box 1 (Los Vilos) hoy a las 15:00 hrs con la Dra. Camila Soto.", "Hace 10 min", AlertType.RECORDATORIO_CITA),
            PredictiveAlert("alt2", "Sugerencia de Reenganche", "Felipe Morales acumula 28 días sin retomar su terapia. Se sugiere enviar cupón de 20% dscto.", "Hace 1 hora", AlertType.ALERTA_DESERCION)
        )
    )
    val predictiveAlerts: StateFlow<List<PredictiveAlert>> = _predictiveAlerts.asStateFlow()

    // Métodos de acción
    fun addAppointment(newAppt: Appointment) {
        val current = _appointments.value.toMutableList()
        current.add(0, newAppt)
        _appointments.value = current

        // Si es presencial, actualizar estado del Box correspondiente
        if (newAppt.modality == AppointmentModality.PRESENCIA_LOS_VILOS && newAppt.boxNumber != null) {
            val updatedBoxes = _boxes.value.map { box ->
                if (box.boxNumber == newAppt.boxNumber) {
                    box.copy(
                        isOccupied = true,
                        assignedSpecialty = newAppt.specialtyName,
                        currentPatient = newAppt.patientName,
                        nextSchedule = "Reservado por ${newAppt.patientName}"
                    )
                } else box
            }
            _boxes.value = updatedBoxes
        }
    }

    fun sendDesertionPromotion(patientId: String) {
        val updatedPatients = _patientContinuityList.value.map { patient ->
            if (patient.patientId == patientId) {
                patient.copy(promotionSent = true)
            } else patient
        }
        _patientContinuityList.value = updatedPatients

        val newAlert = PredictiveAlert(
            id = "prom_${System.currentTimeMillis()}",
            title = "Promoción de Reenganche Enviada",
            message = "Se ha enviado un cupón personalizado de reenganche a través de WhatsApp / Email a ${updatedPatients.find { it.patientId == patientId }?.patientName}.",
            timestamp = "Justo ahora",
            alertType = AlertType.PROMOCION_REENGANCHE
        )
        val currentAlerts = _predictiveAlerts.value.toMutableList()
        currentAlerts.add(0, newAlert)
        _predictiveAlerts.value = currentAlerts
    }

    fun dismissAlert(alertId: String) {
        _predictiveAlerts.value = _predictiveAlerts.value.filter { it.id != alertId }
    }
}
