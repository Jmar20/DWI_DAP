-- Migración para actualizar la tabla actividad
-- Ejecutar en la base de datos Azure MySQL

USE pydwi_db;

-- 1. Primero agregar las nuevas columnas
ALTER TABLE actividad 
ADD COLUMN nombre VARCHAR(255) NOT NULL DEFAULT 'Actividad',
ADD COLUMN descripcion VARCHAR(500) NOT NULL DEFAULT 'Descripción de actividad',
ADD COLUMN fecha_ejecucion DATE NOT NULL DEFAULT '2025-01-01',
ADD COLUMN prioridad VARCHAR(50) NOT NULL DEFAULT 'MEDIA',
ADD COLUMN realizada BOOLEAN NOT NULL DEFAULT FALSE;

-- 2. Migrar datos de la columna fecha a fecha_ejecucion
UPDATE actividad SET fecha_ejecucion = fecha WHERE fecha IS NOT NULL;

-- 3. Migrar datos del campo tipo a nombre
UPDATE actividad SET nombre = tipo WHERE tipo IS NOT NULL;

-- 4. Eliminar las columnas antiguas (opcional, comentado por seguridad)
-- ALTER TABLE actividad DROP COLUMN fecha;
-- ALTER TABLE actividad DROP COLUMN tipo;

-- 5. Verificar la estructura final
DESCRIBE actividad;

-- 6. Ver algunos registros para verificar
SELECT * FROM actividad LIMIT 5;
