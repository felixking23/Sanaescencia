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

🛒 Módulo de Catálogo Centralizado y Canasta Reactiva

- **Catálogo Unificado de Prestaciones:** Interfaz gráfica optimizada para el rol `PACIENTE` que consolida la oferta de terapias clínicas (Psicología, Fonoaudiología, Terapia Ocupacional) e insumos naturales de bienestar de la línea propia Sanaesencia.
- **Buscador y Filtros Dinámicos (LazyRow):** Implementación de un motor de búsqueda predictiva por texto y chips de filtrado categórico por tipo de producto/servicio para agilizar la navegación de la demanda.
- **Canasta Reactiva Desacoplada (MVVM + StateFlow):** Lógica de negocio y persistencia temporal del carrito delegadas mediante flujos asíncronos (`StateFlow`) en el `ViewModel` y `Repository`, desvinculando por completo el estado de las transacciones de la interfaz visual.
- **Micro-animaciones Interactivas (Material Design 3):** Panel desplegable animado mediante `AnimatedVisibility` que emerge de forma fluida desde la zona inferior al accionar el botón adaptativo de control de solicitudes, optimizando la UX de confirmación o descarte de prestaciones.
