# Plataforma de Gestión de Historial Médico

Este proyecto implementa un sistema de gestión de historial médico utilizando **Spring Boot** y **MongoDB** como base de datos NoSQL. Los servicios están expuestos mediante una **API REST** en formato **JSON**.

## Requisitos Previos

1. **Java 11** o superior.
2. **Maven** para gestionar dependencias.
3. **MongoDB** en ejecución (por defecto, se conecta a `mongodb://localhost:27017`).
4. **Git** para la clonación del repositorio.

## Configuración del Proyecto

### Clonar el Repositorio

```bash
git clone git@github.com:facundoa17IT/NoSQL-GestionHistorialMedico.git
cd NoSQL-GestionHistorialMedico
```

### Configuración de MongoDB

Asegúrate de que MongoDB está ejecutándose en `localhost` en el puerto `27017`. Si necesitas cambiar esta configuración, modifica el archivo `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/historialMedicoDB
```

## Compilación e Inicio del Proyecto

Usa Maven para compilar y ejecutar el proyecto:

```bash
mvn clean install
mvn spring-boot:run
```

El servidor debería estar disponible en `http://localhost:8080`.

## Endpoints de la API

A continuación, se describen los endpoints principales junto con ejemplos de comandos `curl` para realizar pruebas.

### 1. Agregar Paciente

- **Endpoint**: `POST /api/pacientes`
- **Descripción**: Agrega un nuevo paciente.

**Ejemplo de JSON**:

```json
{
  "ci": "12345678",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fechaNacimiento": "1985-05-15",
  "sexo": "Masculino"
}
```

**Comando curl**:

```bash
curl -X POST http://localhost:8080/api/pacientes \
-H "Content-Type: application/json" \
-d '{"ci": "12345678", "nombre": "Juan", "apellido": "Pérez", "fechaNacimiento": "1985-05-15", "sexo": "Masculino"}'
```

### 2. Agregar Registro Médico

- **Endpoint**: `POST /api/pacientes/{ci}/registros`
- **Descripción**: Agrega un registro médico para un paciente.

**Ejemplo de JSON**:

```json
{
  "fecha": "2024-11-10",
  "tipo": "Consulta",
  "diagnostico": "Hipertensión",
  "medico": "Dr. López",
  "institucion": "Hospital Central",
  "descripcion": "Presión elevada",
  "medicacion": "Enalapril"
}
```

**Comando curl**:

```bash
curl -X POST http://localhost:8080/api/pacientes/12345678/registros \
-H "Content-Type: application/json" \
-d '{"fecha": "2024-11-10", "tipo": "Consulta", "diagnostico": "Hipertensión", "medico": "Dr. López", "institucion": "Hospital Central", "descripcion": "Presión elevada", "medicacion": "Enalapril"}'
```

### 3. Consultar Historial Médico (Paginado)

- **Endpoint**: `GET /api/pacientes/{ci}/historial`
- **Descripción**: Obtiene el historial médico de un paciente de forma paginada.

**Parámetros de URL**:
- `page`: Número de la página (empieza en 0).
- `size`: Cantidad de registros por página.

**Comando curl**:

```bash
curl -X GET "http://localhost:8080/api/pacientes/12345678/historial?page=0&size=5"
```

### 4. Obtener Registros por Criterio

- **Endpoint**: `GET /api/pacientes/{ci}/historial`
- **Descripción**: Filtra el historial médico del paciente por criterios opcionales como tipo, diagnóstico, médico o institución.

**Ejemplo de URL**:

```bash
curl -X GET "http://localhost:8080/api/pacientes/12345678/historial?tipo=Consulta&medico=Dr.%20López"
```

## Ejecución en Docker

Este proyecto incluye un `Dockerfile` y un `docker-compose.yml` para desplegar tanto la aplicación como MongoDB en contenedores Docker.

### Construir la Imagen de la Aplicación

Ejecuta el siguiente comando en el directorio donde se encuentra el `Dockerfile`:

```bash
docker build -t historial-medico-app .
```

### Usar Docker Compose

Asegúrate de que `docker-compose.yml` tiene el siguiente contenido:

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    depends_on:
      - mongo
    environment:
      - SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/historialMedicoDB

  mongo:
    image: mongo:latest
    ports:
      - "27017:27017"
    volumes:
      - mongo_data:/data/db

volumes:
  mongo_data:
```

Luego, ejecuta los contenedores con:

```bash
docker-compose up --build
```

## Documentación y Pruebas

- **Casos de Prueba en Postman**: Puedes usar los comandos `curl` anteriores o importarlos en Postman para ejecutar las pruebas.
- **Manejo de Errores**: El sistema retorna errores específicos (401 para paciente ya existente, 404 para paciente no encontrado).

## Notas Adicionales

- **Justificación del Modelo**: MongoDB permite almacenar estructuras JSON fácilmente, lo cual facilita la integración con APIs REST y soporta escalabilidad en NoSQL.