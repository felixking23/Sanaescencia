package com.example.sanaescencia.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sanaescencia.model.*
import com.example.sanaescencia.repository.SanaesenciaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SanaesenciaViewModel : ViewModel() {

    // Usuario Actualmente Autenticado
    private val _currentUser = MutableStateFlow<AuthUser?>(SanaesenciaRepository.demoUsers[0])
    val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // Datos del Repositorio
    val appointments: StateFlow<List<Appointment>> = SanaesenciaRepository.appointments
    val boxes: StateFlow<List<BoxStatus>> = SanaesenciaRepository.boxes
    val patientContinuityList: StateFlow<List<PatientContinuity>> = SanaesenciaRepository.patientContinuityList
    val predictiveAlerts: StateFlow<List<PredictiveAlert>> = SanaesenciaRepository.predictiveAlerts



    // Inicio de Sesión por Credenciales
    fun login(emailInput: String, passwordInput: String): Boolean {
        val user = SanaesenciaRepository.demoUsers.find {
            it.email.equals(emailInput.trim(), ignoreCase = true) && it.passwordHash == passwordInput.trim()
        }
        return if (user != null) {
            _currentUser.value = user
            _loginError.value = null
            true
        } else {
            _loginError.value = "Credenciales incorrectas. Prueba con los accesos directos de demo."
            false
        }
    }

    // Acceso Rápido por Rol
    fun quickLogin(role: RoleType) {
        val user = SanaesenciaRepository.demoUsers.firstOrNull { it.role == role }
        _currentUser.value = user
        _loginError.value = null
    }

    fun logout() {
        _currentUser.value = null
        _loginError.value = null
    }

    fun switchRole(newRole: RoleType) {
        quickLogin(newRole)
    }

    fun bookAppointment(
        specialty: Specialty,
        date: String,
        timeSlot: String,
        modality: AppointmentModality,
        boxNumber: Int?
    ) {
        val user = _currentUser.value
        val patientName = user?.name ?: "Paciente Invitado"
        val patientEmail = user?.email ?: "contacto@paciente.cl"

        val newAppt = Appointment(
            id = "appt_${System.currentTimeMillis()}",
            patientName = patientName,
            patientEmail = patientEmail,
            specialtyName = specialty.name,
            professionalName = specialty.professional,
            date = date,
            timeSlot = timeSlot,
            modality = modality,
            boxNumber = if (modality == AppointmentModality.PRESENCIA_LOS_VILOS) boxNumber ?: 1 else null,
            status = AppointmentStatus.AGENDADA
        )
        SanaesenciaRepository.addAppointment(newAppt)
    }

    fun sendPromotionToPatient(patientId: String) {
        SanaesenciaRepository.sendDesertionPromotion(patientId)
    }

    fun dismissAlert(alertId: String) {
        SanaesenciaRepository.dismissAlert(alertId)
    }

    //efe




}
