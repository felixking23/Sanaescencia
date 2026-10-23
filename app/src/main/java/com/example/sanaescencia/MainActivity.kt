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
    PERFIL("Perfil", Icons.Default.Person)
}

@Composable
fun SanaesenciaApp(viewModel: SanaesenciaViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val loginError by viewModel.loginError.collectAsState()

    var currentScreen by remember { mutableStateOf(NavTarget.INICIO) }

    if (currentUser == null) {
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
            errorMessage = loginError
        )
    } else {
        // App Principal con Barra de Navegación y Header
        Scaffold(
            topBar = {
                TopRoleBar(
                    currentUser = currentUser,
                    onSwitchRole = { role ->
                        viewModel.switchRole(role)
                        currentScreen = when (role) {
                            RoleType.PACIENTE -> NavTarget.INICIO
                            RoleType.ADMINISTRACION -> NavTarget.PERFIL
                        }
                    },
                    onLogout = {
                        viewModel.logout()
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val userRole = currentUser?.role ?: RoleType.PACIENTE
                    val availableTabs = when (userRole) {
                        RoleType.PACIENTE -> listOf(NavTarget.INICIO, NavTarget.CATALOGO, NavTarget.PERFIL)
                        RoleType.ADMINISTRACION -> listOf(NavTarget.PERFIL)
                    }

                    availableTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentScreen == tab,
                            onClick = { currentScreen = tab },
                            icon = { Icon(tab.icon, contentDescription = tab.title) },
                            label = { Text(tab.title, textAlign = TextAlign.Center) }
                        )
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

                    NavTarget.PERFIL -> ProfileScreen(
                        currentUser = currentUser,
                        onSwitchRole = { role ->
                            viewModel.switchRole(role)
                            currentScreen = when (role) {
                                RoleType.PACIENTE -> NavTarget.INICIO
                                RoleType.ADMINISTRACION -> NavTarget.PERFIL
                            }
                        },
                        onLogout = {
                            viewModel.logout()
                        }
                    )
                }
            }
        }
    }
}
