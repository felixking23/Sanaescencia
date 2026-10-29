# Centro Integrativo Sanaesencia - MVP

Solución móvil desarrollada para nuestro cliente, el Centro Integrativo Sanaesencia. La aplicación centraliza la trazabilidad de usuarios, la gestión de sesiones y el catálogo de prestaciones integrativas.

## 👥 Integrantes
- Tomás Octavio Acuña Maldonado
- Felipe Olguín
- Benjamín Manzur

## 🛠️ Arquitectura y Tecnologías
- **Lenguaje:** Kotlin
- **Diseño UI:** Jetpack Compose con Material Design 3
- **Patrón de Arquitectura:** MVVM (Model-View-ViewModel)
- **Persistencia Local:** Room Database con SQLite
- **Componentes Reactivos:** Manejo de estados mediante `mutableStateOf` y flujos asíncronos desacoplados de la interfaz visual.

## 📱 Módulo de Gestión de Cuentas
- **Autenticación y Control de Roles:** Interfaces reactivas para inicio de sesión y registro segregando flujos para roles `PACIENTE` y `ADMINISTRACION`.
- **Modo Demo Integrado:** Acceso rápido en la pantalla principal para facilitar la evaluación directa de flujos por parte del docente.
- **Uso de Recursos Nativos:** Integración mediante Intents explícitos e implícitos para llamadas de soporte técnico y geolocalización por GPS redirigida a Google Maps hacia la sucursal física en Los Vilos (Caupolicán 220).
