# tiendaApp

API REST de una tienda hecha con **Spring Boot**, **JPA/Hibernate**, **MySQL** y autenticación con **JWT**.
Roles: `ADMIN`, `CLIENTE` y `VENDEDOR`.

## Requisitos

- Java 21
- MySQL 8 corriendo en `localhost:3306`
- Maven (o el wrapper incluido: `mvnw` / `mvnw.cmd`)

## 1. Crear la base de datos

Ejecuta el script `database/tienda_db.sql` en MySQL. Crea las tablas, los 3 roles y el ADMIN inicial.

```bash
mysql -u root -p < database/tienda_db.sql
```

## 2. Configurar las variables de entorno

La aplicación lee sus secretos de variables de entorno (no están escritos en el código).

| Variable         | Obligatoria | Descripción                                          | Ejemplo / valor por defecto |
|------------------|-------------|------------------------------------------------------|-----------------------------|
| `DB_PASSWORD`    | Sí          | Contraseña del usuario de MySQL                      | `mi_clave_mysql`            |
| `DB_USERNAME`    | No          | Usuario de MySQL                                     | `root` (por defecto)        |
| `JWT_SECRET_KEY` | Sí          | Clave secreta para firmar los JWT, en **Base64** (mínimo 32 bytes) | ver abajo |
| `JWT_EXPIRATION` | No          | Duración del token en milisegundos                   | `600000` (10 min)           |

**Generar la clave JWT** (usa una propia, no la compartas ni la subas a git):

```bash
openssl rand -base64 32
```

**Windows (PowerShell):**

```powershell
setx DB_PASSWORD "mi_clave_mysql"
setx JWT_SECRET_KEY "PEGA_AQUI_LA_CLAVE_GENERADA"
```

**Linux / macOS:**

```bash
export DB_PASSWORD="mi_clave_mysql"
export JWT_SECRET_KEY="PEGA_AQUI_LA_CLAVE_GENERADA"
```

> En Windows, `setx` solo aplica a terminales nuevas: cierra y vuelve a abrir la terminal o VS Code.

## 3. Correr el proyecto

**Con Maven:**

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

**Con VS Code:** abre el proyecto y usa la configuración `Spring Boot-TiendaAppApplication` (`.vscode/launch.json`), que toma las variables de entorno de tu sistema.

**Con el .jar:**

```bash
./mvnw clean package
java -jar target/tiendaApp-0.0.1-SNAPSHOT.jar
```

La API queda en: `http://localhost:9090/api/v1/tienda`

## 4. ADMIN inicial

| Campo      | Valor              |
|------------|--------------------|
| Usuario    | `admin`            |
| Email      | `admin@tienda.com` |
| Contraseña | `Admin123*`        |

Iniciar sesión (puedes usar usuario **o** email en `usernameOrEmail`):

```http
POST http://localhost:9090/api/v1/tienda/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "admin",
  "password": "Admin123*"
}
```

La respuesta trae el `jwt`. Envíalo en las demás peticiones:

```
Authorization: Bearer <jwt>
```

⚠️ Esta contraseña es solo para desarrollo/pruebas. Si vas a publicar el proyecto, cámbiala: genera un hash BCrypt nuevo y actualiza la columna `password` del usuario `admin`.

## Endpoints

Todos (excepto `/auth/**`) requieren el header `Authorization: Bearer <jwt>`.

| Método | Ruta                      | Rol                        | Descripción                                   |
|--------|---------------------------|----------------------------|-----------------------------------------------|
| POST   | `/auth/register`          | Público                    | Registro (queda como CLIENTE)                 |
| POST   | `/auth/login`             | Público                    | Inicio de sesión, devuelve el JWT             |
| POST   | `/auth/refreshtoken`      | Público (token vigente)    | Genera un token nuevo                         |
| GET    | `/users/me`               | Todos                      | Mi perfil                                     |
| GET    | `/users`                  | ADMIN                      | Listar usuarios                               |
| POST   | `/users`                  | ADMIN                      | Crear usuario con cualquier rol               |
| PATCH  | `/users/{id}/role`        | ADMIN                      | Cambiar rol                                   |
| PATCH  | `/users/{id}/status`      | ADMIN                      | Activar / desactivar usuario                  |
| GET    | `/categories`             | Todos                      | Listar categorías                             |
| POST   | `/categories`             | ADMIN                      | Crear categoría                               |
| DELETE | `/categories/{id}`        | ADMIN                      | Eliminar categoría (sin productos)            |
| GET    | `/products`               | Autenticado                | Listar productos (`?categoryId=`)             |
| GET    | `/products/{id}`          | Autenticado                | Ver un producto                               |
| GET    | `/products/mine`          | VENDEDOR                   | Mis productos                                 |
| POST   | `/products`               | VENDEDOR                   | Crear producto                                |
| PUT    | `/products/{id}`          | VENDEDOR (dueño) / ADMIN   | Actualizar producto                           |
| DELETE | `/products/{id}`          | VENDEDOR (dueño) / ADMIN   | Eliminar producto (sin pedidos)               |
| POST   | `/orders`                 | CLIENTE                    | Crear pedido (descuenta stock)                |
| GET    | `/orders/mine`            | CLIENTE                    | Mis pedidos                                   |
| GET    | `/orders/sales`           | VENDEDOR                   | Pedidos de mis productos                      |
| GET    | `/orders`                 | ADMIN                      | Todos los pedidos                             |
| PATCH  | `/orders/{id}/status`     | VENDEDOR (dueño) / ADMIN   | Cambiar estado del pedido                     |
| PATCH  | `/orders/{id}/cancel`     | CLIENTE (dueño)            | Cancelar pedido PENDIENTE (devuelve stock)    |

Estados de un pedido: `PENDIENTE`, `ENVIADO`, `ENTREGADO`, `CANCELADO`.

## Problemas comunes

- **`Could not resolve placeholder 'JWT_SECRET_KEY'`**: falta la variable de entorno. Defínela y reinicia la terminal o el IDE.
- **`Access denied for user`**: revisa `DB_USERNAME` y `DB_PASSWORD`.
- **`Unknown database 'tienda_db'`**: no ejecutaste `database/tienda_db.sql`.
- **401 en todas las peticiones**: el token expiró (dura 10 min por defecto) o falta el header `Authorization: Bearer ...`.
