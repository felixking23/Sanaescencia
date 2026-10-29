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

    val cartItems: StateFlow<Map<String, Int>> = SanaesenciaRepository.cartItems
    val specialties: StateFlow<List<Specialty>> = SanaesenciaRepository.specialties
    val products: StateFlow<List<NaturalProduct>> = SanaesenciaRepository.products

    // Compatibilidad para referencias existentes
    val availableSpecialties = specialties
    val availableProducts = products

    fun addToCart(itemId: String) {
        SanaesenciaRepository.addItemToCart(itemId)
    }

    fun removeFromCart(itemId: String) {
        SanaesenciaRepository.removeItemFromCart(itemId)
    }

    fun clearCart() {
        SanaesenciaRepository.clearCart()
    }

    fun getCartTotal(): Int {
        var total = 0
        cartItems.value.forEach { (id, qty) ->
            val specialtyPrice = specialties.value.find { it.id == id }?.price
            val productPrice = products.value.find { it.id == id }?.price
            total += ((specialtyPrice ?: productPrice ?: 0) * qty)
        }
        return total
    }

    fun addProduct(name: String, type: ProductType, price: Int, description: String, volume: String) {
        val prod = NaturalProduct(
            id = "prod_${System.currentTimeMillis()}",
            name = name,
            type = type,
            price = price,
            description = description,
            volume = volume
        )
        SanaesenciaRepository.addProduct(prod)
    }

    fun updateProduct(product: NaturalProduct) {
        SanaesenciaRepository.updateProduct(product)
    }

    fun deleteProduct(productId: String) {
        SanaesenciaRepository.deleteProduct(productId)
    }

    fun addSpecialty(name: String, professional: String, description: String, price: Int) {
        val spec = Specialty(
            id = "spec_${System.currentTimeMillis()}",
            name = name,
            professional = professional,
            description = description,
            price = price
        )
        SanaesenciaRepository.addSpecialty(spec)
    }

    fun updateSpecialty(specialty: Specialty) {
        SanaesenciaRepository.updateSpecialty(specialty)
    }

    fun deleteSpecialty(specialtyId: String) {
        SanaesenciaRepository.deleteSpecialty(specialtyId)
    }
}
