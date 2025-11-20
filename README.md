# 🤖 Chatbot Hexagonal – Spring Boot + Gemini + N8N

Este proyecto implementa un **chatbot modular** utilizando **Arquitectura Hexagonal por Features**, integrando un **motor interno de IA (Gemini)** y un **motor externo (N8N)**, además de una base de datos de **productos** que el chatbot utiliza para responder preguntas inteligentes.

El objetivo es demostrar:
- Diseño limpio por features (chatbot, products)
- Separación domain / application / infrastructure
- Memoria de conversación persistente
- Motor IA intercambiable (Gemini ↔ N8N)
- Consultas inteligentes sobre productos almacenados en BD

---

# 🧱 Arquitectura

El proyecto utiliza **Arquitectura Hexagonal por Features**, donde cada módulo tiene:

```
domain/
application/
infrastructure/
```

### Domain Layer
- Modelos del negocio
- Value Objects
- Excepciones
- Sin dependencias externas

### Application Layer
- Use cases
- Ports in/out
- DTOs
- Coordinación de lógica (sin mappers)

### Infrastructure Layer
- Controllers
- Adapters IA (Gemini / N8N)
- JPA Repositories
- Entities
- Persistence mapper

---

# 📦 Features Principales

## 1️⃣ Feature: Chatbot

Encargado de:

- Crear sesión si no existe
- Cargar historial desde la base de datos
- Guardar mensajes de usuario y asistente
- Seleccionar el motor IA activo
- Integrarse con Gemini 1.5 Flash
- Integrarse con un workflow de N8N

### Estructura

```
chatbot/
 ├── domain/
 │    ├── model/
 │    ├── exception/
 │    └── valueobject/
 │
 ├── application/
 │    ├── dto/
 │    ├── port/in
 │    ├── port/out
 │    └── usecase/
 │
 └── infrastructure/
      ├── in/rest
      └── out/adapter/
            ├── InternalChatbotAdapter (Gemini)
            └── ExternalN8nAdapter
```

---

## 2️⃣ Feature: Products

Los productos se insertan **exclusivamente por SQL** y el chatbot los usa como fuente de conocimiento para responder:

- Producto más caro
- Producto más barato
- Lista de productos disponibles
- Consultas generales

### Estructura

```
product/
 ├── domain/
 │    ├── model/
 │    ├── valueobject/
 │    └── exception/
 │
 ├── application/
 │    ├── dto/
 │    ├── port/in
 │    ├── port/out
 │    └── usecase/
 │
 └── infrastructure/
      └── out/jpa
            ├── ProductEntity
            ├── ProductJpaRepository
            ├── ProductJpaMapper
            └── ProductRepositoryAdapter
```

---

# 🤖 Flujo del Chatbot

1. El usuario envía un mensaje a:
   ```
   POST /api/chatbot/send
   ```
2. Si la sesión no existe → **se crea automáticamente**
3. Se carga el historial de mensajes
4. Se cargan los productos desde la base de datos
5. Se construye un **context prompt** para Gemini:
   - Lista de productos
   - Instrucciones sobre cómo responder
6. Se envía mensaje + contexto a Gemini 1.5 Flash
7. Se guarda la respuesta del asistente
8. Se devuelve el resultado al usuario

---

# 🔌 Integración con Gemini (Internal AI)

El chatbot usa el modelo gratuito:

```
gemini-1.5-flash
```

Configuración en `application.properties`:

```properties
gemini.api.key=TU_API_KEY
gemini.model=gemini-1.5-flash
gemini.url=https://generativelanguage.googleapis.com/v1beta/models/${gemini.model}:generateContent?key=${gemini.api.key}
```

---

# 🔌 Integración con N8N

El usuario puede cambiar entre motores IA:

```
POST /api/chatbot/change-model
```

Valores permitidos:
- `INTERNAL_AI`
- `N8N`

El motor externo llama un workflow configurado en tu servidor N8N.

---

# 📚 Endpoints del Chatbot

## 1️⃣ Enviar mensaje
```
POST /api/chatbot/send
```

### Body:
```json
{
  "sessionKey": "user-123",
  "messageText": "Hola chatbot"
}
```

---

## 2️⃣ Cambiar de modelo IA
```
POST /api/chatbot/change-model
```

### Body:
```json
{
  "sessionKey": "user-123",
  "newModel": "INTERNAL_AI"
}
```

---

# 💾 Base de Datos

### Tabla de productos:

```sql
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL
);
```

El proyecto incluye un script SQL con **30 productos** precargados.

---

# 🚀 Instalación

## 1️⃣ Clonar el proyecto
```
git clone https://github.com/tu-repo/chatbot-hexagonal.git
```

## 2️⃣ Configurar PostgreSQL
En `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/chatbot
spring.datasource.username=postgres
spring.datasource.password=root
```

## 3️⃣ Configurar Gemini API Key
```properties
gemini.api.key=TU_API_KEY
```

## 4️⃣ Ejecutar
```
mvn spring-boot:run
```

---

# 🧪 Postman Collection

El proyecto se prueba fácilmente importando:

```
ChatbotRoutes.postman_collection.json
```

Permite:

- Enviar mensajes
- Cambiar modelo IA
- Probar memoria
- Consultar productos por IA

---

# 🎉 Resultado Final

Este proyecto demuestra una arquitectura moderna, modular y escalable:

- 🔹 Arquitectura hexagonal por features
- 🔹 Integración real con IA (Gemini 1.5 Flash)
- 🔹 Integración con N8N
- 🔹 Persistencia de sesiones y mensajes
- 🔹 Consultas inteligentes basadas en BD real
- 🔹 Modelo IA intercambiable en tiempo real

---

```
Desarrollado con ❤️ usando Spring Boot, PostgreSQL, Gemini y Arquitectura Hexagonal.
```
