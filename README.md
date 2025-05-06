# Backend Sistema de Gestión

Este proyecto es una API REST desarrollada con Spring Boot para el sistema de gestión.

## Requisitos Previos

- Java 17 o superior
- PostgreSQL 12 o superior
- Maven 3.8 o superior
- Node.js 16 o superior (para el frontend)

## Configuración del Entorno

1. Clonar el repositorio:
```bash
git clone <url-del-repositorio>
cd backend-arq
```

2. Configurar las variables de entorno:
   - Crear un archivo `.env` en la raíz del proyecto
   - Copiar el contenido de `.env.example` y ajustar los valores según tu entorno

3. Configurar la base de datos:
```bash
# Crear la base de datos en PostgreSQL
createdb sistemaBdd
```

## Perfiles de Ejecución

El proyecto cuenta con tres perfiles de Spring:

- `dev`: Para desarrollo local (por defecto)
- `test`: Para pruebas
- `prod`: Para producción

Para ejecutar con un perfil específico:
```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## Autenticación y Autorización

El sistema utiliza JWT para la autenticación:

- Access Token: Válido por 10 minutos
- Refresh Token: Válido por 30 minutos

### Endpoints de Autenticación

```
POST /login
Content-Type: application/json

{
    "username": "email@ejemplo.com",
    "password": "contraseña"
}

Respuesta:
{
    "access_token": "...",
    "refresh_token": "...",
    "email": "email@ejemplo.com",
    "roles": ["ROLE_ADMIN", "ROLE_PROFESOR"]
}
```

### Manejo de Errores

El sistema implementa un manejo centralizado de errores que retorna respuestas en el siguiente formato:

```json
{
    "timestamp": "2024-03-21T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Descripción del error"
}
```

## Roles y Permisos

El sistema maneja los siguientes roles:

- `ROLE_ADMIN`: Acceso completo al sistema
- `ROLE_PROFESOR`: Acceso a funcionalidades específicas de profesores

## Variables de Entorno

Las siguientes variables de entorno son necesarias:

```properties
# Base de datos
DATABASE_URL=jdbc:postgresql://localhost:5432/sistemaBdd
DB_USERNAME=postgres
DB_PASSWORD=tu_contraseña

# JWT
JWT_SECRET=tu_clave_secreta
JWT_ACCESS_TOKEN_EXPIRATION=600000
JWT_REFRESH_TOKEN_EXPIRATION=1800000

# Frontend
FRONTEND_URL=http://localhost:5173
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:4200

# Correo
MAIL_HOST=smtp.ejemplo.com
MAIL_PORT=465
MAIL_USERNAME=tu_usuario
MAIL_PASSWORD=tu_contraseña
```

## Desarrollo

Para ejecutar en modo desarrollo:

1. Asegúrate de tener PostgreSQL ejecutándose
2. Configura el archivo `.env`
3. Ejecuta:
```bash
./mvnw spring-boot:run
```

## Pruebas

Para ejecutar las pruebas:
```bash
./mvnw test
```

## Integración con Frontend

El backend está configurado para trabajar con un frontend en React/Angular. Asegúrate de:

1. Configurar los orígenes permitidos en CORS_ALLOWED_ORIGINS
2. El frontend debe incluir el token JWT en el header Authorization:
```javascript
headers: {
    'Authorization': `Bearer ${accessToken}`
}
```

3. Manejar las respuestas de error según el formato estándar:
```typescript
interface ApiError {
    timestamp: string;
    status: number;
    error: string;
    message: string | Record<string, string>;
}
```

## Logging

El sistema utiliza Log4j2 para el logging:

- Desarrollo: Nivel DEBUG para com.arquitectura.proyecto
- Producción: Nivel INFO para com.arquitectura.proyecto

## Monitoreo

Endpoints de Actuator habilitados:
- /actuator/health
- /actuator/info
- /actuator/metrics 