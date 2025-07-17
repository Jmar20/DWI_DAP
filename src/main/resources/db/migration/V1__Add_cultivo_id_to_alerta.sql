-- Agregar columna cultivo_id a la tabla alerta para soportar alertas automáticas
-- Esta columna permitirá alertas relacionadas directamente con cultivos

ALTER TABLE alerta 
ADD COLUMN cultivo_id INT NULL,
ADD CONSTRAINT FK_alerta_cultivo 
    FOREIGN KEY (cultivo_id) 
    REFERENCES cultivo(id) 
    ON DELETE CASCADE;

-- Crear índice para mejorar rendimiento en consultas
CREATE INDEX idx_alerta_cultivo_id ON alerta(cultivo_id);

-- Comentario para documentar el cambio
COMMENT ON COLUMN alerta.cultivo_id IS 'ID del cultivo para alertas automáticas. NULL para alertas manuales relacionadas con actividades.';
