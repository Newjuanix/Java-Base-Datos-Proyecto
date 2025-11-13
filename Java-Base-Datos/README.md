"# Base-de-Datos" 
# 📢 Portal de Avisos Universitario

Sistema CRUD desarrollado en **Java** con conexión a **MySQL**, que permite gestionar avisos académicos entre profesores y estudiantes.

---

## 🚀 Descripción

El **Portal de Avisos** busca mejorar la comunicación entre profesores y estudiantes de una institución educativa.  
Cada profesor puede publicar, editar o eliminar avisos, mientras que los estudiantes solo pueden visualizar los existentes.

Incluye:
- Inicio de sesión por correo institucional.
- Roles diferenciados (`profesor` / `estudiante`).
- CRUD completo de avisos.
- Interfaz gráfica con **Swing**.
- Conexión a base de datos mediante **MySQL JDBC Connector**.

---


---

## ⚙️ Requisitos

- **Java JDK 17** o superior  
- **MySQL Server**  
- **MySQL Workbench**  
- **Visual Studio Code** o **NetBeans**  
- Conector JDBC de MySQL (`mysql-connector-j-9.5.0.jar`)

---

## 🧩 Configuración de la Base de Datos

Ejecuta en **MySQL Workbench**:

ESTA ES LA BASE DE DATOS: (de igual manera se encuentra en el archivo hola.txt)

-- eliminar base de datos si existe
drop database if exists portal_avisos;
create database portal_avisos;
use portal_avisos;

-- tabla profesor
create table profesor (
  id_profesor int auto_increment primary key,
  nombre_profesor varchar(100) not null,
  apellido_profesor varchar(100) not null,
  correo_profesor varchar(100) not null unique
);

-- tabla estudiante
create table estudiante (
  id_estudiante int auto_increment primary key,
  nombre_estudiante varchar(100) not null,
  apellido_estudiante varchar(100) not null,
  correo_estudiante varchar(100) not null unique
);

-- tabla aviso
create table avisos (
  id_aviso int auto_increment primary key,
  titulo varchar(150) not null,
  contenido text not null,
  fecha_publicacion timestamp default current_timestamp,
  id_profesor int not null,
  foreign key (id_profesor) references profesor(id_profesor)
);

-- insertar profesores
insert into profesor (nombre_profesor, apellido_profesor, correo_profesor) values
('juan', 'perez', 'juan.perez@uni.com'),
('maria', 'gomez', 'maria.gomez@uni.com');

-- insertar estudiantes
insert into estudiante (nombre_estudiante, apellido_estudiante, correo_estudiante) values
('ana', 'lopez', 'ana.lopez@uni.com'),
('carlos', 'diaz', 'carlos.diaz@uni.com');

-- insertar avisos
insert into avisos (titulo, contenido, id_profesor) values
('bienvenida al semestre', 'recuerden revisar el calendario académico.', 1),
('tarea 1 disponible', 'la primera tarea está publicada en la plataforma.', 2);

select * from avisos; -- ver el ingreso de datos

🖥️ Ejecución del Proyecto
1️⃣ Compilar

Desde la raíz del proyecto:

para cmd:  javac -encoding UTF-8 -cp "lib/*;src" -d bin src/**/*.java
para powershell: javac -encoding UTF-8 -cp ".\lib\*;.\src" -d .\bin (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

2️⃣ Ejecutar
para cmd: java -cp "lib/*;bin" ui.LoginGUI
para powershell: java -cp ".\lib\*;.\bin" ui.LoginGUI

🧰 Tecnologías Utilizadas:

☕ Java 17
🧩 JDBC (Java Database Connectivity)
🗄️ MySQL 8+
🖥️ Swing (Interfaz gráfica)
🧠 Modelo DAO (Data Access Object)
