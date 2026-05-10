# Historial de Cambios Realizados

## Fecha: 10 de Mayo 2026

---

## 1. docker-compose.yml

### Agregado: Servicio db-auth y ms-auth

**Antes:**
- Solo existían: db-users, db-products, ms-users, ms-products, ms-discovery, ms-gateway

**Después:**
- Se agregó: `db-auth` (MySQL 8.0 en puerto 3309)
- Se agregó: `ms-auth` (8090) depended de db-auth y ms-discovery

---

## 2. ms-auth/src/main/resources/application.yaml

### Configuración de Base de Datos

**Antes:**
```yaml
datasource:
  url: jdbc:mysql://mysql-db:3306/morfik_auth_db?createDatabaseIfNotExist=true
  username: root
  password: root
```

**Después:**
```yaml
datasource:
  url: jdbc:mysql://db-auth:3306/auth_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
  username: camilo
  password: mypassword
```

### DDL Auto

**Antes:** `ddl-auto: none`
**Después:** `ddl-auto: update` (para crear tablas automáticamente)

---

## 3. ms-auth/pom.xml

### Dependencias Agregadas

**Antes:**
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-cloud-starter-netflix-eureka-client
- jjwt 0.12.5

**Agregado:**
- spring-boot-starter-security (para PasswordEncoder/BCrypt)

### Corrections
- spring-boot-starter-webmvc → spring-boot-starter-web
- spring-boot-starter-webmvc-test → spring-boot-starter-test

---

## 4. ms-auth/src/main/java/com/gallego/ms_auth/config/

### AppConfig.java

**Nuevo archivo creado** (antes existía pero sin imports)
- Provee PasswordEncoder (BCrypt)
- Ubicación: `src/main/java/com/gallego/ms_auth/config/AppConfig.java`

### AppConfig.Java → AppConfig.java

**Cambio:** Renombrado de mayúscula a minúscula
- Archivo original: `AppConfig.Java`
- Archivo actual: `AppConfig.java`

### SecurityConfig.java

**Nuevo archivo creado**
- Ubicación: `src/main/java/com/gallego/ms_auth/config/SecurityConfig.java`
- Función: Permitir acceso público a `/auth/**` (register, login, refresh)

---

## 5. ms-auth/src/main/java/com/gallego/ms_auth/services/

### AuthService.java

**Cambio:** Encriptar contraseña al registrar

**Antes:**
```java
user.setPassword(requestDTO.getPassword());
```

**Después:**
```java
user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
```

---

## 6. ms-auth/src/main/java/com/gallego/ms_auth/services/AuthService.java

**Cambio:** Eliminar import innecesario

**Antes:**
```java
import java.lang.foreign.Linker.Option;
```

**Eliminado:** Este import causaba errores de compilación

---

## 7. ms-auth/pom.xml - Limpieza

### Etiquetas eliminadas
- `<licenses><license/></licenses>`
- `<developers><developer/></developers>`
- `<scm>...</scm>`

### Valores agregados
- `<name>ms-auth</name>`
- `<description>Microservicio de Autenticación</description>`

---

## Resumen de Archivos Modificados/Creados

| Archivo | Acción |
|---------|--------|
| docker-compose.yml | Modificado - agregado db-auth y ms-auth |
| ms-auth/src/main/resources/application.yaml | Modificado - configuración DB |
| ms-auth/pom.xml | Modificado - agregado security |
| ms-auth/src/main/java/.../config/AppConfig.java | Modificado - renombrado + imports |
| ms-auth/src/main/java/.../config/SecurityConfig.java | Creado |
| ms-auth/src/main/java/.../services/AuthService.java | Modificado - encriptar password |
| ms-auth/src/main/java/.../services/AuthService.java | Modificado - eliminar import |

---

## 8. Cambio: ms-auth usa db-users en lugar de db-auth

**Fecha: 10 Mayo 2026**

### Problema Original
- ms-auth guardaba usuarios en db-auth (propia DB)
- ms-users guardaba usuarios en db-users
- Esto duplicaba datos y violaba el principio de una sola fuente de verdad

### Solución
- ms-auth ahora guarda usuarios en db-users (3307)

### Cambios Realizados

**A) ms-auth/src/main/resources/application.yaml**

**Antes:**
```yaml
datasource:
  url: jdbc:mysql://db-auth:3306/auth_db?...
```

**Después:**
```yaml
datasource:
  url: jdbc:mysql://db-users:3306/users_db?...
```

**B) docker-compose.yml**

**Eliminado:**
- servicio `db-auth` (mysql:3309)
- Dependencia de ms-auth hacia db-auth

**Modificado:**
- ms-auth ahora depende de db-users

### Resultado
- Los datos de usuarios se guardan en db-users (mysql:3307)
- ms-auth y ms-users comparten la misma base de datos

---

## 9. Fix: Agregar columnas a tabla user

**Fecha: 10 Mayo 2026**

### Problema
- La tabla `user` en db-users no tenía columnas `age` ni `rol_id`
- Ms-auth necesita estas columnas para guardar usuarios

### Solución
```sql
ALTER TABLE user ADD COLUMN age BIGINT, ADD COLUMN rol_id BIGINT;
```