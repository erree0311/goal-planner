# 🎯 Goal Planner

Aplicación web que genera un plan de acción paso a paso para cualquier meta personal o de negocio — mudarte a otro país, comprar un vehículo, abrir un negocio, un viaje, etc. — investigando en internet los requisitos y trámites actuales.

## Cómo funciona

1. Escribes tu meta en el formulario.
2. La app llama a la API de Claude (Anthropic) con búsqueda web en tiempo real para investigar requisitos, documentos, costos y tiempos actuales.
3. Devuelve un plan ordenado y accionable, con imágenes de referencia relacionadas con la meta.

## Stack

- Java 21
- Spring Boot 3 (Web + Thymeleaf)
- Claude API (Anthropic Java SDK) con búsqueda web y "adaptive thinking"
- Maven

## Cómo correrlo localmente

### Requisitos

- JDK 21+
- Maven
- Una API key de Anthropic ([console.anthropic.com](https://console.anthropic.com))

### Pasos

```bash
# Configura tu API key como variable de entorno
export ANTHROPIC_API_KEY=tu-api-key

# Compila y corre
mvn clean package
java -jar target/goal-planner-1.0.0.jar
```

Abre `http://localhost:8080` en tu navegador.

## Nota de seguridad

La API key nunca se guarda en el código — se lee desde la variable de entorno `ANTHROPIC_API_KEY` (`AnthropicOkHttpClient.fromEnv()`).
