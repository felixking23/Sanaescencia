package com.example.sanaescencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sanaescencia.model.RoleType
import com.example.sanaescencia.viewmodel.SanaesenciaViewModel
import com.example.sanaescencia.view.components.TopRoleBar
import com.example.sanaescencia.view.screens.*
import com.example.sanaescencia.ui.theme.SanaescenciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SanaescenciaTheme {
                SanaesenciaApp()
            }
        }
    }
}

enum class NavTarget(val title: String, val icon: ImageVector) {
    INICIO("Inicio", Icons.Default.Home),
    CATALOGO("Catálogo", Icons.Default.Storefront),
    USUARIOS("Usuarios", Icons.Default.Group),
    PERFIL("Perfil", Icons.Default.Person)
}

@Composable
fun SanaesenciaApp(viewModel: SanaesenciaViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val users by viewModel.users.collectAsState()

    var currentScreen by remember { mutableStateOf(NavTarget.INICIO) }
    var isCreatingUserAdmin by remember { mutableStateOf(false) }
    var isEditingUserAdmin by remember { mutableStateOf(false) }
    var isEditingOwnProfile by remember { mutableStateOf(false) }

    if (currentUser == null) {
        var isRegistering by remember { mutableStateOf(false) }

        if (isRegistering) {
            RegisterScreen(
                onRegisterClick = { name, rut, email, phone, password ->
                    val success = viewModel.registerPatient(name, rut, email, phone, password)
                    if (success) {
                        currentScreen = NavTarget.INICIO
                    }
                    success
                },
                onBackToLogin = {
                    isRegistering = false
                }
            )
        } else {
            // Pantalla de Inicio de Sesión
            LoginScreen(
                onLoginClick = { email, password ->
                    viewModel.login(email, password)
                },
                onQuickLogin = { role ->
                    viewModel.quickLogin(role)
                    currentScreen = when (role) {
                        RoleType.PACIENTE -> NavTarget.INICIO
                        RoleType.ADMINISTRACION -> NavTarget.PERFIL
                    }
                },
                onNavigateToRegister = {
                    isRegistering = true
                },
                errorMessage = loginError
            )
        }
    } else {
        // App Principal con Barra de Navegación y Header
        Scaffold(
            topBar = {
                TopRoleBar(
                    currentUser = currentUser,
                    onSwitchRole = { role ->
                        viewModel.switchRole(role)
                        isEditingOwnProfile = false
                        currentScreen = when (role) {
                            RoleType.PACIENTE -> NavTarget.INICIO
                            RoleType.ADMINISTRACION -> NavTarget.PERFIL
                        }
                    },
                    onLogout = {
                        isEditingOwnProfile = false
                        viewModel.logout()
                    }
                )
            },
            bottomBar = {
                val hideBottomBar = (currentScreen == NavTarget.USUARIOS && (isCreatingUserAdmin || isEditingUserAdmin)) ||
                        (currentScreen == NavTarget.PERFIL && isEditingOwnProfile)

                if (!hideBottomBar) {
                    NavigationBar {
                        val userRole = currentUser?.role ?: RoleType.PACIENTE
                        val availableTabs = when (userRole) {
                            RoleType.PACIENTE -> listOf(NavTarget.INICIO, NavTarget.CATALOGO, NavTarget.PERFIL)
                            RoleType.ADMINISTRACION -> listOf(NavTarget.INICIO, NavTarget.CATALOGO, NavTarget.USUARIOS, NavTarget.PERFIL)
                        }

                        availableTabs.forEach { tab ->
                            NavigationBarItem(
                                selected = currentScreen == tab,
                                onClick = {
                                    currentScreen = tab
                                    if (tab != NavTarget.USUARIOS) {
                                        isCreatingUserAdmin = false
                                        isEditingUserAdmin = false
                                    }
                                    if (tab != NavTarget.PERFIL) {
                                        isEditingOwnProfile = false
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.title) },
                                label = { Text(tab.title, textAlign = TextAlign.Center) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    NavTarget.INICIO -> HomeScreen(
                        currentUser = currentUser,
                        onNavigateToCatalog = { currentScreen = NavTarget.CATALOGO }
                    )

                    NavTarget.CATALOGO -> CatalogScreen()

                    NavTarget.USUARIOS -> UsersManagementScreen(
                        users = users,
                        onDeleteUser = { user ->
                            viewModel.deleteUser(user)
                        },
                        onCreateUser = { name, rut, email, phone, password, role ->
                            viewModel.createUserByAdmin(name, rut, email, phone, password, role)
                        },
                        onUpdateUser = { originalEmail, name, rut, email, phone, password, role, imageUri ->
                            viewModel.updateUser(originalEmail, name, rut, email, phone, password, role, imageUri)
                        },
                        isCreatingUser = isCreatingUserAdmin,
                        onCreatingUserChanged = { isCreatingUserAdmin = it },
                        isEditingUser = isEditingUserAdmin,
                        onEditingUserChanged = { isEditingUserAdmin = it }
                    )

                    NavTarget.PERFIL -> ProfileScreen(
                        currentUser = currentUser,
                        onSwitchRole = { role ->
                            viewModel.switchRole(role)
                            isEditingOwnProfile = false
                            currentScreen = when (role) {
                                RoleType.PACIENTE -> NavTarget.INICIO
                                RoleType.ADMINISTRACION -> NavTarget.PERFIL
                            }
                        },
                        onLogout = {
                            isEditingOwnProfile = false
                            viewModel.logout()
                        },
                        onUpdateProfile = { originalEmail, name, rut, email, phone, password, role, imageUri ->
                            viewModel.updateUser(originalEmail, name, rut, email, phone, password, role, imageUri)
                        },
                        isEditingProfile = isEditingOwnProfile,
                        onEditingProfileChanged = { isEditingOwnProfile = it }
                    )
                }
            }
        }
    }
}
