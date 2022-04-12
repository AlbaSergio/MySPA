/************************************************
 *      BASE DE DATOS myspa                     *
 *                                              *
 *      Archivo de Definicion de Datos (DDL)    *
 ***********************************************/
 
 /*
    Version:        1.0
    Fecha:          10/04/2018 14:00:00
    Autor:          Miguel Angel Gil Rios
    Email:          angel.grios@gmail.com
    Comentarios:    Esta es la primera version de la base de datos
                    con las instrucciones necesarias para
                    generar las tablas.
 */
 
  /*
    Version:        1.2
    Fecha:          11/10/2019 10:40:00
    Autor:          Miguel Angel Gil Rios
    Email:          angel.grios@gmail.com
    Comentarios:    Se modifico la tabla [tratamiento]: Se agrego el campo
                    [precio], en cumplimiento con el requerimiento <<ERF 0801>>.
 */


DROP DATABASE IF EXISTS myspa;

CREATE DATABASE myspa;

USE myspa;

--  Esta tabla guarda los datos de una persona
CREATE TABLE persona
(
    idPersona           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre              VARCHAR(64) NOT NULL DEFAULT '',
    apellidoPaterno     VARCHAR(64) NOT NULL DEFAULT '',
    apellidoMaterno     VARCHAR(64) NOT NULL DEFAULT '',
    genero              VARCHAR(2)  NOT NULL DEFAULT 'O',
    domicilio           VARCHAR(200) NOT NULL DEFAULT '',
    telefono            VARCHAR(25) NOT NULL DEFAULT '',
    rfc                 VARCHAR(14) NOT NULL DEFAULT ''
);

--  Esta tabla guarda los datos del usuario.
CREATE TABLE usuario
(
    idUsuario           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombreUsuario       VARCHAR(48) NOT NULL,
    contrasenia         VARCHAR(48) NOT NULL DEFAULT '',
    token               TEXT,
    rol                 VARCHAR(24) NOT NULL DEFAULT '' -- INT NOT NULL DEFAULT 1 -- 1: Administrador; 2 Empleado; 3 Cliente
);

--  Esta tabla guarda los datos del empleado, incluidos
--  sus datos personales (dentro de la tabla persona).
CREATE TABLE empleado
(
    idEmpleado          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    numeroEmpleado      VARCHAR(65),
    puesto              VARCHAR(20),
    estatus             INT NOT NULL DEFAULT 1, -- 1: Activo; 2: Inactivo
    foto                LONGTEXT,
    rutaFoto            TEXT,
    idPersona           INT NOT NULL,
    idUsuario           INT NOT NULL,
    CONSTRAINT  fk_empleado_persona  FOREIGN KEY (idPersona) 
                REFERENCES persona(idPersona) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_empleado_usuario  FOREIGN KEY (idUsuario) 
                REFERENCES usuario(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE
);

--  Esta tabla guarda los datos de un cliente, incluidos
--  sus datos personales (dentro de la tabla persona).
CREATE TABLE cliente
(
    idCliente       INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    numeroUnico     VARCHAR(70) NOT NULL,
    correo          VARCHAR(200) NOT NULL DEFAULT '',
    estatus         INT NOT NULL DEFAULT 1, -- 1: Activo; 2: Inactivo
    idPersona       INT NOT NULL,
    idUsuario       INT NOT NULL,
    CONSTRAINT  fk_cliente_persona  FOREIGN KEY (idPersona) 
                REFERENCES persona(idPersona) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_cliente_usuario  FOREIGN KEY (idUsuario) 
                REFERENCES usuario(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE	
);

--  Esta tabla guarda los productos ofrecidos por el SPA:
CREATE TABLE producto
(
    idProducto      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre          VARCHAR(255) NOT NULL DEFAULT '',
    marca           VARCHAR(255) NOT NULL DEFAULT '',
    estatus         INT NOT NULL DEFAULT 1, -- 1: Activo; 2: Inactivo
    precioUso       FLOAT NOT NULL DEFAULT 0.0 
);

--  Esta tabla guarda los datos de los tratamientos.
CREATE TABLE tratamiento
(
    idTratamiento   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre          VARCHAR(65) NOT NULL DEFAULT '',
    descripcion     TEXT NOT NULL,
    costo           FLOAT NOT NULL DEFAULT 0,
    estatus         NOT NULL DEFAULT 1
);

--	Esta tabla guarda los datos de las sucursales:
CREATE TABLE sucursal
(
    idSucursal      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre          VARCHAR(129) NOT NULL DEFAULT '',
    domicilio       VARCHAR(255),
    latitud         DOUBLE NOT NULL DEFAULT 0.0,
    longitud        DOUBLE NOT NULL DEFAULT 0.0,
    estatus         INT NOT NULL DEFAULT 1    
);

--  Esta tabla guarda los datos de la sala.
CREATE TABLE sala
(
    idSala          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre          VARCHAR(129) NOT NULL DEFAULT '',
    descripcion     TEXT,
    foto            LONGTEXT,
    rutaFoto        TEXT,
    estatus         INT NOT NULL DEFAULT 1, -- 1: Activo; 2: Inactivo
    idSucursal      INT NOT NULL,
    CONSTRAINT  fk_sala_sucursal  FOREIGN KEY (idSucursal) 
                REFERENCES sucursal(idSucursal) ON DELETE CASCADE ON UPDATE CASCADE
);

--  Esta tabla guarda diferentes horarios identificados
--  por dia y por hora de inicio y hora de termino.
CREATE TABLE horario
(
    idHorario       INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    horaInicio      VARCHAR(10),
    horaFin         VARCHAR(10)
);

--  Esta tabla guarda datos para saber
--  en que horarios se encuentra disponible una sala.
CREATE TABLE sala_horario
(
    idSala      INT NOT NULL,
    idHorario   INT NOT NULL,
    CONSTRAINT  fk_salahorario_sala      FOREIGN KEY (idSala) 
                REFERENCES sala(idSala) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_salahorario_horario   FOREIGN KEY (idHorario) 
                REFERENCES horario(idHorario) ON DELETE CASCADE ON UPDATE CASCADE	
);

--  Esta tabla guarda datos de reservaciones incluidos:
--      1. La fecha y la hora de la reservacion.
--      2. El estatus de la reservacion.
--      3. El cliente que realiza la reservacion.
--      4. La sala reservada.
CREATE TABLE reservacion
(
    idReservacion   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fechaHoraInicio DATETIME,
    fechaHoraFin    DATETIME,
    estatus         INT,	-- 0: Cancelada; 1: Activa; 2: Atendida;
    idCliente       INT NOT NULL,
    idSala          INT NOT NULL,
    CONSTRAINT  fk_reservacion_cliente   FOREIGN KEY (idCliente)
                REFERENCES cliente(idCliente) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_reservacion_sala      FOREIGN KEY (idSala) 
                REFERENCES sala(idSala) ON DELETE CASCADE ON UPDATE CASCADE    
);

--  Esta tabla guarda los datos de las reservaciones atendidas a traves
--  de los servicios que brindan los empleados del SPA.
CREATE TABLE servicio
(
    idServicio      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fecha           DATETIME NOT NULL,
    idReservacion   INT NOT NULL,
    idEmpleado      INT NOT NULL,
    CONSTRAINT  fk_servicio_reservacion  FOREIGN KEY (idReservacion) 
                REFERENCES reservacion(idReservacion) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_servicio_empleado  FOREIGN KEY (idEmpleado) 
                REFERENCES empleado(idEmpleado) ON DELETE CASCADE ON UPDATE CASCADE
);

--  Esta tabla guarda los datos de los tratamientos adquiridos en cada
--  servicio.
CREATE TABLE servicio_tratamiento
(
    idServicioTratamiento   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    idTratamiento           INT NOT NULL,
    idServicio              INT NOT NULL,
    CONSTRAINT  fk_serviciotratamiento_tratamiento  FOREIGN KEY (idTratamiento) 
                REFERENCES tratamiento(idTratamiento) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_tratamientoservicio_servicio  FOREIGN KEY (idServicio) 
                REFERENCES servicio(idServicio) ON DELETE CASCADE ON UPDATE CASCADE
);

--  Esta tabla guarda la informacion de los servicios, los tratamientos 
--  realizados así como el detalle de los productos (con cantidades y precios)
--  de cada tratamiento.
--  La información de esta tabla es redundante, sin embargo, es necesario
--  dejarla así para no perder la integridad de los datos históricos.
CREATE TABLE servicio_tratamiento_producto
(
    idServicioTratamiento       INT NOT NULL,
    idProducto                  INT NOT NULL,
    precioUso                   FLOAT NOT NULL DEFAULT 0.0,
    CONSTRAINT  fk_serviciotratamientoproducto_serviciotratamiento  FOREIGN KEY (idServicioTratamiento) 
                REFERENCES servicio_tratamiento (idServicioTratamiento) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  fk_serviciotratamientoproducto_producto  FOREIGN KEY (idProducto) 
                REFERENCES producto(idProducto) ON DELETE CASCADE ON UPDATE CASCADE
);
