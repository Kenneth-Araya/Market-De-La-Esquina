CREATE SCHEMA IF NOT EXISTS bd_ventas;

CREATE TABLE IF NOT EXISTS bd_ventas.venta (
    id_venta BIGSERIAL PRIMARY KEY,
    fecha_venta TIMESTAMP NOT NULL,
    total_venta DOUBLE PRECISION NOT NULL,
    descripcion_venta VARCHAR(255) NOT NULL,
    monto_venta DOUBLE PRECISION NOT NULL,
    metodo_venta VARCHAR(100) NOT NULL,
    estado_venta VARCHAR(100) NOT NULL,
    codigo_transaccion_venta VARCHAR(255) NOT NULL UNIQUE
);