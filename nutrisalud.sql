-- 1. Crear la base de datos
drop database nutrisalud_db;
CREATE DATABASE IF NOT EXISTS nutrisalud_db;
USE nutrisalud_db;


CREATE TABLE cargo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

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
CREATE TABLE IF NOT EXISTS evaluacion_salto (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trabajador_id BIGINT NOT NULL,
    alcance_reposo_cm DECIMAL(5,2) NOT NULL,
    alcance_salto_cm DECIMAL(5,2) NOT NULL,
    altura_salto_cm DECIMAL(5,2) NOT NULL, -- Calculado: alcance_salto - alcance_reposo
    clasificacion_rendimiento VARCHAR(20) NOT NULL, -- DEFICIENTE, REGULAR, BUENO, MUY_BUENO, EXCELENTE
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE', -- PENDIENTE, APROBADO
    fecha_evaluacion DATE NOT NULL,
    evaluador_id BIGINT,
    fecha_registro DATE DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_salto_trabajador 
        FOREIGN KEY (trabajador_id) 
        REFERENCES trabajador(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_salto_evaluador 
        FOREIGN KEY (evaluador_id) 
        REFERENCES usuario(id) 
        ON DELETE SET NULL
);


select * from evaluacion_salto;