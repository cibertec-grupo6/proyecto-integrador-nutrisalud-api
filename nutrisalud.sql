-- 1. Crear la base de datos
drop database nutrisalud_db;
CREATE DATABASE IF NOT EXISTS nutrisalud_db;
USE nutrisalud_db;

-- 2. Tabla de Usuarios (Para HU13 - Autenticación y Roles)
-- Roles: ADMIN, EVALUADOR_SALUD, EVALUADOR_FISICO, MEDICO
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Aquí guardaremos el hash (BCrypt)
    rol VARCHAR(30) NOT NULL, 
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Empresas Clientas (Para HU12)
CREATE TABLE empresa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    razon_social VARCHAR(150) NOT NULL,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    sector VARCHAR(100),
    direccion VARCHAR(200),
    correo_contacto VARCHAR(100),
    telefono VARCHAR(20),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla de Trabajadores (Para HU01)
-- Son los empleados de las empresas clientas que vienen al EMO
CREATE TABLE trabajador (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(10) DEFAULT 'DNI',
    documento_identidad VARCHAR(15) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL,
    cargo VARCHAR(100),
    area VARCHAR(100),
    empresa_id BIGINT NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

-- 5. Tabla de Evaluaciones IMC (Para HU02 y HU03)
CREATE TABLE evaluacion_imc (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trabajador_id BIGINT NOT NULL,
    peso_kg DECIMAL(5,2) NOT NULL,
    talla_m DECIMAL(4,2) NOT NULL,
    imc_calculado DECIMAL(5,2) NOT NULL,
    clasificacion VARCHAR(50) NOT NULL, -- Ej: 'Normal', 'Sobrepeso'
    fecha_evaluacion DATE NOT NULL,
    evaluador_id BIGINT, -- Quién hizo la evaluación (usuario del sistema)
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trabajador_id) REFERENCES trabajador(id),
    FOREIGN KEY (evaluador_id) REFERENCES usuario(id)
);

-- 6. Datos de prueba (Opcional, para que puedas probar el login y las empresas)
-- Contraseña: 123456 (Hash BCrypt para Spring Security)
INSERT INTO usuario (nombres, apellidos, correo, password, rol) VALUES 
('Roger', 'Admin', 'admin@nutrisalud.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');

INSERT INTO empresa (razon_social, ruc, sector, correo_contacto) VALUES 
('Minera Las Bambas', '20504532109', 'Minería', 'rrhh@bambas.com'),
('Constructora Graña', '20100070970', 'Construcción', 'seguridad@grana.com');

select * from trabajador;