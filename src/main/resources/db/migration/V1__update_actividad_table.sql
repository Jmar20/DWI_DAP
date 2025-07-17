-- Migración para actualizar la tabla actividad
-- Las nuevas columnas necesarias para la entidad Actividad

-- Agregar las nuevas columnas si no existen
ALTER TABLE actividad 
ADD COLUMN IF NOT EXISTS nombre VARCHAR(255) NOT NULL DEFAULT 'Actividad',
ADD COLUMN IF NOT EXISTS descripcion VARCHAR(500) NOT NULL DEFAULT 'Descripción de actividad',
ADD COLUMN IF NOT EXISTS fecha_ejecucion DATE NULL,
ADD COLUMN IF NOT EXISTS prioridad VARCHAR(50) NOT NULL DEFAULT 'MEDIA',
ADD COLUMN IF NOT EXISTS realizada BOOLEAN NOT NULL DEFAULT FALSE;

-- Migrar datos existentes
UPDATE actividad SET 
    fecha_ejecucion = COALESCE(fecha, CURDATE()),
    nombre = COALESCE(tipo, 'Actividad')
WHERE fecha_ejecucion IS NULL OR nombre = 'Actividad';

-- Hacer fecha_ejecucion NOT NULL después de migrar los datos
ALTER TABLE actividad MODIFY COLUMN fecha_ejecucion DATE NOT NULL;
