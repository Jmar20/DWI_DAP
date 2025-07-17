-- Flyway Migration: Agregar columna cultivo_id a tabla alerta
-- Fecha: 2025-07-15
-- Descripción: Añade soporte para alertas automáticas relacionadas directamente con cultivos

-- Agregar la columna cultivo_id como nullable para mantener compatibilidad
ALTER TABLE alerta 
ADD COLUMN cultivo_id INT NULL;

-- Crear clave foránea con eliminación en cascada
ALTER TABLE alerta 
ADD CONSTRAINT FK_alerta_cultivo 
    FOREIGN KEY (cultivo_id) 
    REFERENCES cultivo(id) 
    ON DELETE CASCADE;

-- Crear índice para mejorar el rendimiento en consultas
CREATE INDEX idx_alerta_cultivo_id ON alerta(cultivo_id);
