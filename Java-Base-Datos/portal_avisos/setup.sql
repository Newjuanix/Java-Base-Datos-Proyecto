DROP DATABASE IF EXISTS portal_avisos;
CREATE DATABASE portal_avisos;
USE portal_avisos;

CREATE TABLE profesor (
  id_profesor INT AUTO_INCREMENT PRIMARY KEY,
  nombre_profesor VARCHAR(100) NOT NULL,
  apellido_profesor VARCHAR(100) NOT NULL,
  correo_profesor VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE estudiante (
  id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
  nombre_estudiante VARCHAR(100) NOT NULL,
  apellido_estudiante VARCHAR(100) NOT NULL,
  correo_estudiante VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE avisos (
  id_aviso INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  contenido TEXT NOT NULL,
  fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  id_profesor INT NOT NULL,
  FOREIGN KEY (id_profesor) REFERENCES profesor(id_profesor)
);

INSERT INTO profesor (nombre_profesor, apellido_profesor, correo_profesor) VALUES
('juan', 'perez', 'juan.perez@uni.com'),
('maria', 'gomez', 'maria.gomez@uni.com');

INSERT INTO estudiante (nombre_estudiante, apellido_estudiante, correo_estudiante) VALUES
('ana', 'lopez', 'ana.lopez@uni.com'),
('carlos', 'diaz', 'carlos.diaz@uni.com');

INSERT INTO avisos (titulo, contenido, id_profesor) VALUES
('bienvenida al semestre', 'recuerden revisar el calendario academico.', 1),
('tarea 1 disponible', 'la primera tarea esta publicada en la plataforma.', 2);

# Para que me funcionara use:  cd "c:\Users\sisca\OneDrive\Escritorio\Base-de-Datos-rama_juan\portal_avisos"; javac -encoding UTF-8 -d bin -cp "lib/flatlaf-3.6.2.jar;lib/mysql-connector-j-8.0.33.jar" src/dao/*.java src/database/*.java src/main/*.java src/models/*.java src/ui/*.java; java -cp "lib/flatlaf-3.6.2.jar;lib/mysql-connector-j-8.0.33.jar;bin" ui.LoginGUI