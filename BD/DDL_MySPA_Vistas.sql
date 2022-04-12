/********************************************************
 *      BASE DE DATOS myspa                             *
 *                                                      *
 *      Archivo de Definicion de Datos (DDL)            *
 *      Vistas - Views                                  *
 *******************************************************/
 
 /*
    Version:        1.0
    Fecha:          11/04/2018 07:48:00
    Autor:          Miguel Angel Gil Rios
    Email:          angel.grios@gmail.com
    Comentarios:    Esta es la primera version de las vistas
                    principales.
 */
 
 USE myspa;
 
--  Vista que consulta todos los datos de un Empleado
DROP VIEW IF EXISTS v_empleados;
CREATE VIEW v_empleados AS
    SELECT  P.*,
            E.idEmpleado,
            E.numeroEmpleado,
            E.puesto,
            E.estatus,
            E.foto,
            E.rutaFoto,
            U.*
    FROM    persona P
            INNER JOIN empleado E ON E.idPersona = P.idPersona
            INNER JOIN usuario U ON U.idUsuario = E.idUsuario;
            
--  Vista que consulta todos los datos de un Cliente
DROP VIEW IF EXISTS v_clientes;
CREATE VIEW v_clientes AS
    SELECT  P.*,
            C.idCliente,
            C.correo,
            C.numeroUnico,
            C.estatus,
			C.foto,
			C.rutaFoto,
            U.*
    FROM    persona P
            INNER JOIN cliente C ON C.idPersona = P.idPersona
            INNER JOIN usuario U ON U.idUsuario = C.idUsuario;

--  Vista que consulta todos los datos de una Reservacion:
DROP VIEW IF EXISTS v_reservacion;
CREATE VIEW v_reservacion AS
    SELECT  P.*,
            C.idCliente,
            C.correo,
            C.numeroUnico,
            R.idReservacion,
            DATE_FORMAT(R.fechaHoraInicio, '%d/%m/%Y %H:%i:%s') AS fechaHoraInicio,
            DATE_FORMAT(R.fechaHoraFin, '%d/%m/%Y %H:%i:%s') AS fechaHoraFin,
            R.estatus,
            S.idSala,
            S.nombre AS nombreSala,
            S.descripcion,
            S.foto AS fotoSala,
            S.rutaFoto AS rutaFotoSala,
            S.estatus AS estatusSala
    FROM    persona P
            INNER JOIN cliente C ON C.idPersona = P.idPersona
            INNER JOIN reservacion R ON R.idCliente = C.idCliente
            INNER JOIN sala S ON S.idSala = R.idSala;

--  Vista que consulta todas las sucursales y sus salas respectivas:
DROP VIEW IF EXISTS v_sucursales_salas;
CREATE VIEW v_sucursales_salas AS
    SELECT  SU.*,
            SA.idSala,
            SA.nombre AS nombreSala,
            SA.descripcion,
            SA.foto,
            SA.rutaFoto,
            SA.estatus AS estatusSala
    FROM    sucursal SU
            INNER JOIN sala SA ON SA.idSucursal = SU.idSucursal;



DROP VIEW IF EXISTS v_servicios_tratamientos;
CREATE VIEW v_servicios_tratamientos AS 
    SELECT  STP.idServicioTratamiento,
            ST.idServicio,
            SUM(STP.precioUso) AS totalProductos,
            SUM(T.costo) AS totalTratamientos
    FROM    servicio_tratamiento ST
            INNER JOIN servicio_tratamiento_producto STP
            ON  STP.idServicioTratamiento = ST.idServicioTratamiento
            INNER JOIN tratamiento T ON T.idTratamiento = ST.idTratamiento;
            
--  Vista que consulta los datos generales de los servicios, incluido el costo total del mismo.
DROP VIEW IF EXISTS v_servicios;
CREATE VIEW v_servicios AS
    SELECT  S.idServicio,
        S.fechaSala,
        R.idReservacion,
        R.fechaHoraInicio,
        R.fechaHoraFin,
        C.idCliente,
        C.numeroUnico,
        PC.nombre AS nombreCliente,
        PC.apellidoPaterno AS apellidoPaternoCliente,
        PC.apellidoMaterno AS apellidoMaternoCliente,
        SA.idSala,
        SA.nombre AS nombreSala,
        E.idEmpleado,
        E.numeroEmpleado,
        PE.nombre AS nombreEmpleado,
        PE.apellidoPaterno AS apellidoPaternoEmpleado,
        PE.apellidoMaterno AS apellidoMaternoEmpleado,
        Q2.totalProductos,
        Q2.totalTratamientos
        FROM    servicio S
                INNER JOIN reservacion R ON S.idReservacion = R.idReservacion
                INNER JOIN sala SA ON R.idSala = SA.idSala
                INNER JOIN cliente C ON R.idCliente = C.idCliente
                INNER JOIN persona PC ON C.idPersona = PC.idPersona
                INNER JOIN empleado E ON S.idEmpleado = E.idEmpleado
                INNER JOIN persona PE ON PE.idPersona = E.idPersona
/*                
                INNER JOIN  (
                                SELECT  STP.idServicioTratamiento,
                                        ST.idServicio,
                                        SUM(STP.precioUso) AS total
                                FROM    servicio_tratamiento ST
                                        INNER JOIN servicio_tratamiento_producto STP
                                        ON  STP.idServicioTratamiento = ST.idServicioTratamiento
                            )Q2 ON S.idServicio = Q2.idServicio;
*/
                INNER JOIN v_servicios_tratamientos Q2 ON S.idServicio = Q2.idServicio;