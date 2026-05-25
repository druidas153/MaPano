# MaPaño

Aplicación Android de puntos de interés en Zaragoza con mapa interactivo, 
datos abiertos del Ayuntamiento y lista de deseos personalizada.

## 📋 Descripción

MaPaño es una aplicación móvil que permite a los usuarios explorar puntos de 
interés en la ciudad de Zaragoza. Muestra restaurantes, eventos culturales, 
farmacias y paradas de taxi en un mapa interactivo, con datos reales 
proporcionados por la API de datos abiertos del Ayuntamiento de Zaragoza.

## 🚀 Funcionalidades

- **Mapa interactivo** con osmdroid (OpenStreetMap)
- **4 categorías** de puntos de interés: Restaurantes, Eventos, Farmacias, Taxis
- **Datos reales** de la API de datos abiertos del Ayuntamiento de Zaragoza
- **Lista de deseos** con CRUD completo (añadir, editar, eliminar, marcar visitado)
- **Multimedia**: Captura de fotos con la cámara y galería de imágenes
- **Geolocalización**: Ubicación real del usuario con punto azul en el mapa
- **Bubble personalizado**: Información detallada al pulsar un marcador
- **Modo offline**: Datos mock almacenados en Room como respaldo

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|------------|-----|
Java: Lenguaje principal
Android SDK : (API 24-34)
Plataforma: Moviles
osmdroid: Mapa OpenStreetMap
Room: Base de datos local (SQLite)
Retrofit: Comunicación HTTP con APIs
Gson: Parseo de JSON
LiveData + ViewModel: Arquitectura MVVM
Material Design 3: Interfaz de usuario
JUnit: Pruebas unitarias

## 📡 APIs utilizadas

| API | URL |
|-----|-----|

Restaurantes: https://www.zaragoza.es/sede/servicio/restaurante.json |
Eventos: https://www.zaragoza.es/sede/servicio/cultura/evento/list.json |
Farmacias: https://www.zaragoza.es/sede/servicio/farmacia.json |
Taxis: https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/equipamiento/parada-taxi.json |

Todas las APIs usan el parámetro `srsname=wgs84` para obtener coordenadas 
en formato latitud/longitud directamente.

## 📱 Requisitos

- Android 7.0 (API 24) o superior
- Conexión a Internet (para datos de la API)
- GPS (para geolocalización)
- Cámara (para multimedia)

## 🔧 Compilación

1. Clonar el repositorio: `git clone https://github.com/druidas153/MaPano.git`
2. Abrir en Android Studio
3. Sincronizar Gradle
4. Ejecutar en dispositivo o emulador

## 🧪 Pruebas unitarias

| Clase de test | Qué verifica |
|---------------|--------------|

RestauranteTest: Creación, tenedores visual, valores por defecto
DatosComunesTest: Getters/setters, coordenadas válidas 
DeseoLugarTest: Creación, estado visitado, cambio de estado 

### 🧪 Pruebas manuales

Dispositivo : Huawei P20 , HMD Global Nokia 7 plus

Mapa con marcadores ✅ 
Cambio de categorías ✅
Bubble personalizado ✅
Añadir a lista de deseos ✅
Editar/eliminar deseos ✅
Cámara y galería ✅
Geolocalización ✅
4 APIs sincronizadas ✅
Modo offline (sin Internet) ✅
Denegar permiso GPS ✅

## 🔮 Mejoras futuras

- Clustering de marcadores para mostrar más de 50 sin afectar rendimiento
- Sistema de búsqueda por nombre de lugar
- Navegación/rutas hasta el punto de interés
- Sistema de valoraciones propias del usuario
- Notificaciones cuando el usuario esté cerca de un lugar de su lista
- Modelo premium para negocios (restaurantes destacados, reservas)
- Modo oscuro
- Soporte multiidioma (español, inglés, francés)
- Widget de escritorio con lugares cercanos
   
## 👤 Autor

César García - Desarrollo de Aplicaciones Multiplataforma (DAM)

## 📄 Licencia

Proyecto académico - Todos los derechos reservados
