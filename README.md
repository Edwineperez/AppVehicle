# Taller Garaje — Sistema de Gestión de Vehículos

## Descripción general
Aplicación web desarrollada en **Java EE (Jakarta)** utilizando **JSP**, **Servlets** y **GlassFish** con conexión a base de datos **MySQL** mediante un **DataSource (jdbc/myPool)**.

El sistema permite realizar operaciones CRUD sobre vehículos registrados, aplicando reglas de negocio y validaciones en la capa de fachada (`VehiculoFacade`).

---

## Arquitectura general

### Capas
El proyecto sigue una arquitectura **en capas**:

| Capa | Paquete | Descripción |
|------|----------|-------------|
| Modelo | `com.garaje.model` | Contiene las clases de entidades (`Vehiculo`). |
| Persistencia | `com.garaje.persistence` | Contiene los DAO responsables del acceso a datos. |
| Fachada | `com.garaje.facade` | Implementa las reglas de negocio y coordina las operaciones del DAO. |
| Controlador | `com.garaje.controller` | Servlet que recibe las peticiones y actúa como intermediario entre la vista y la lógica. |
| Vista | `src/main/webapp` | JSPs para listar y registrar vehículos (`vehiculos.jsp`, `formVehiculo.jsp`). |

### Flujo general
1. El usuario ingresa a `vehiculos.jsp`.  
2. El `VehiculoServlet` procesa las acciones (`new`, `edit`, `delete`).  
3. La capa `VehiculoFacade` valida las reglas de negocio.  
4. Si pasa las validaciones, el `VehiculoDAO` ejecuta las operaciones SQL.  
5. La vista se actualiza con la lista de vehículos.

---

## Convenciones de nombres

| Tipo de elemento | Convención usada |
|------------------|------------------|
| Clases | PascalCase → `VehiculoServlet`, `VehiculoDAO` |
| Métodos y variables | camelCase → `buscarVehiculo`, `insertarVehiculo` |
| Paquetes | minúsculas y separadas por punto → `com.garaje.controller` |
| JSPs | nombres simples en minúscula → `vehiculos.jsp`, `formVehiculo.jsp` |

---

## Ejecución del sistema

### Requisitos previos
- Java JDK 17  
- Apache NetBeans
- GlassFish 7  
- MySQL 8 o superior  
- Conector `mysql-connector-j-9.4.0.jar` (copiado en `glassfish/domains/domain1/lib/`)

## Despliegue

- Configurar base de datos MySQL:

   ```sql
   CREATE DATABASE garage;

   USE garage;

   CREATE TABLE vehiculos (
     id INT AUTO_INCREMENT PRIMARY KEY,
     placa VARCHAR(20) NOT NULL,
     marca VARCHAR(30) NOT NULL,
     modelo VARCHAR(30) NOT NULL,
     color VARCHAR(20),
     propietario VARCHAR(50) NOT NULL
   ); 

### Configuración del DataSource
En la consola de GlassFish:

**JDBC Connection Pool**

- Name: myPool
- Datasource Classname: com.mysql.cj.jdbc.MysqlDataSource

Properties:
- serverName = localhost
- portNumber = 3306
- databaseName = garage
- user = root
- password = (tu clave)
- URL = jdbc:mysql://localhost:3306/garage?useSSL=false&serverTimezone=UTC

**JDBC Resource**
- JNDI Name: jdbc/myPool
- Pool Name: myPool

---

### Ejecución
1. Iniciar GlassFish (`domain1`).  
2. En NetBeans → Ejecutar proyecto.  
3. Acceder a:  
   - http://localhost:8080/AppVehicle/vehiculos

---

## Reglas de negocio implementadas

| # | Regla | Descripción |
|---|--------|-------------|
| 1 | Placa única | No se permite agregar ni actualizar un vehículo con placa duplicada. |.
| 2 | Propietario válido | No puede estar vacío ni tener menos de 5 caracteres. |.
| 3 | Longitud mínima | Marca, modelo y placa ≥ 3 caracteres. |
| 4 | Color permitido | Solo se aceptan: Rojo, Blanco, Negro, Azul, Gris. |
| 5 | Antigüedad máxima | El modelo no puede tener más de 20 años. |.
| 6 | Placa global única | Las placas deben ser únicas en toda la base. |
| 7 | Eliminar restringido | No se puede eliminar si el propietario es “Administrador”. |
| 8 | Actualizar existente | Solo se actualiza si el vehículo existe. |
| 9 | Anti-SQL Injection | Se bloquean patrones sospechosos (`'`, `--`, `drop`, etc.). |.
| 10 | Marca especial | Si la marca es “Ferrari”, se muestra notificación simulada. |.

---

# Pruebas de Reglas de Negocio

### Caso 1 — Placa duplicada y placa única
**Entrada:**

`Vehiculo("ABC123", "Toyota", "2020", "Rojo", "Juan Pérez")`

`Vehiculo("ABC123", "Nissan", "2021", "Azul", "Carlos Ruiz")`

**Resultado esperado:** ❌ Excepción  
> "Ya existe un vehículo con la placa ABC123"

---

### Caso 2 — Propietario corto
**Entrada:**  
`Vehiculo("ZZZ999", "Ford", "2020", "Gris", "Ana")`

**Resultado:** ❌ Excepción  
> "El propietario debe tener al menos 5 caracteres."

---

### Caso 3 — Longitud mínima
**Entrada:**  
`Vehiculo("AB", "Toyota", "2020", "Rojo", "Juan Pérez")`

`Vehiculo("ABC123", "Ni", "2021", "Azul", "Carlos Ruiz")`

`Vehiculo("ZZZ999", "Ford", "20", "Gris", "Ana")`

**Resultado:** ❌ Excepción  
> "La placa debe tener al menos 3 caracteres."

> "La marca debe tener al menos 3 caracteres."

> "El modelo debe tener al menos 3 caracteres."

---

### Caso 4 — Color permitido
**Entrada:**  
`Vehiculo("ZZZ999", "Ford", "2020", "Amarillo", "Ana")`

**Resultado:** ❌ Excepción  
> "Color inválido. Solo se permiten: [Rojo, Blanco, Negro, Azul, Gris]"

---

### Caso 5 — Eliminar restringido
**Entrada:**  
`Vehiculo("ABC123", "Nissan", "2021", "Azul", "Administrador")`

**Resultado:** ❌ Excepción  
> "No se puede eliminar un vehículo del propietario 'Administrador'."

---

### Caso 6 — Modelo antiguo
**Entrada:**  
`Vehiculo("TTT777", "Chevrolet", "1999", "Rojo", "Pedro Díaz")`

**Resultado:** ❌ Excepción  
> "El modelo es demasiado antiguo (1999)."

---

### Caso 7 — Marca Ferrari
**Entrada:**  
`Vehiculo("FER911", "Ferrari", "2024", "Negro", "Juliana López")`

**Resultado:** ✅ Registro exitoso  
> Consola: "🚗 [Notificación] Nuevo Ferrari registrado: FER911"

---

### Caso 8 — SQL Injection
**Entrada:**  
`Vehiculo("AAA111", "Mazda", "2020", "Azul", "DROP TABLE vehiculos;")`

**Resultado:** ❌ Excepción  
> "Entrada inválida detectada (posible SQL Injection)."


## Autores
- **Edwin Pérez**
- **Jefferson Prieto**
- **Sebastian Rodriguez** 
  Estudiante de Tecnología en Desarrollo de Sistemas Informáticos.
