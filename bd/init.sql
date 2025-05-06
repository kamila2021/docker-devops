-- Crear tabla de ejemplo
CREATE TABLE IF NOT EXISTS usuarios (
                                        id SERIAL PRIMARY KEY,
                                        nombre VARCHAR(100),
    email VARCHAR(100) UNIQUE
    );

-- Insertar datos de prueba
INSERT INTO usuarios (nombre, email) VALUES ('Kamila', 'kamila@ejemplo.com');
