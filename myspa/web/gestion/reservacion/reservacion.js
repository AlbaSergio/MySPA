var reservaciones = [];
var clientes = [];
var empleados = [];
var sucursales = [];
var salas = [];
var horarios = [];
var tratamientos = [];
var productos = [];
var servicioActual;
var servicioTratamientoActual;

function inicializarModulo() {
    //Codigo para implementar el filtro en la tabla
    $("#txtBuscar").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbodyReservacion tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $("#txtBuscarProducto").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbDP tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    $("#divTablaClientes").hide();
    setDetalleReservacionVisible(false);
    $("#divDetalleServicio").hide();
    $("#divProductoTratamiento").hide();
    refrescarTabla();
}


/*
 * Esta función nos sirve para mostrar y ocultar el área que contiene 
 * el detalle de una reservación.
 * 
 * Si el valor es true, mostramos el area del detalle de la reservación.
 * Si el valor es false, ocultamos el area del detalle de la reservación.
 */
function setDetalleReservacionVisible(valor) {
    if (valor) {
        $('#divTablaReservacion').removeClass("col-12");
        $('#divTablaReservacion').addClass("col-7");
        $('#divDetalleReservacion').show();

    } else {
        $('#divTablaReservacion').removeClass("col-7");
        $('#divTablaReservacion').addClass("col-12");
        $('#divDetalleReservacion').hide();
    }
}

function eliminar() {

}

function buscarClienteReservacion() {
    var contenido = "";

    // Elemento visual de la búsqueda del cliente para la reservación txtSearch:
    var buscarCliente = $('#txtBuscarCliente').val();

    $.ajax({
        type: "GET",
        url: "api/cliente/findCliente",
        data: {nombre: buscarCliente,
            token: sessionStorage.getItem("token")}

    }).done(function (data) {
        clientes = data;

        //Recorremos el arreglo de clientes posición por posición:
        for (var i = 0; i < data.length; i++) {
            var nombreCompleto = clientes[i].persona.nombre + " " + clientes[i].persona.apellidoPaterno + " " +
                    clientes[i].persona.apellidoMaterno;

            // Agregamos un nuevo renglon a la tabla contenido sus respectivas columnas y valores:
            contenido = contenido + '<tr>' +
                    '<td>' + nombreCompleto + '</td>' +
                    '<td><button class="btn btn-outline-primary" onclick="seleccionarCliente(' + clientes[i].id + ');"><i class="fas fa-plus"></i>&nbspElegir</button>' + '</td>' +
                    '</tr>';
        }
        $('#tbodyClientes').html(contenido);
        $('#divTablaClientes').show();
        consultarSucursales();
    });
}

function seleccionarCliente(idCliente) {
    var pos = buscarPosicionClientePorID(idCliente);

    var nombreCliente = clientes[pos].persona.nombre + " " + clientes[pos].persona.apellidoPaterno + " " +
            clientes[pos].persona.apellidoMaterno;

    $('#txtNombreCliente').val(nombreCliente);
    $('#txtCodigoCliente').val(clientes[pos].id);

}

function buscarPosicionClientePorID(id) {
    // Recorremos el arreglo posición por posición:
    for (var i = 0; i < clientes.length; i++) {

        // Comparamos si el ID del empleado en la posición actual es el mismo que el buscado:
        if (clientes[i].id === id) {
            return i;
        }
    }
    // Si llegamos a este punto significa que no encontramos un empleado con el ID especificado, 
    // en cuyo caso devolvemos el valor -1:
    return -1;
}

function consultarSucursales() {
    // Esta variable contendrá el contenido HTML del nombre de las sucursales.
    var contenido = '';

    // Hacemos la petición al servicio REST que nos consulta las sucursales:
    $.ajax({
        type: "GET",
        url: "api/sucursal/getAll",
        data: {token: sessionStorage.getItem("token")}

    }).done(function (data) {
        sucursales = data;

        // Recorreremos el arreglo de productos posición por posición:
        for (var i = 0; i < sucursales.length; i++) {
            // Agregamos el valor en el combobox del respectivo valor del nombre de las sucursales:
            contenido = contenido + '<option value="' + sucursales[i].id + '">' + sucursales[i].nombre +
                    '</option>';
        }
        // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
        $('#cmbSucursales').html(contenido);
    });
}

function consultarSalas() {
    var idSucursal = $('#cmbSucursales').val();

    // Esta variable contendrá el contenido HTML de la tabla
    var contenido = '';

    // Hacemos la peticion al servicio REST que nos consulta las sucursales y salas:
    $.ajax({
        type: "GET",
        url: "api/sala/getAllBySucursal",
        data: {
            idSucursal: idSucursal,
            token: sessionStorage.getItem("token")
        }

    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {
            salas = data;

            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < salas.length; i++) {
                // Agregamos un nuevo renglón a la tabla contenido sus respectivas columnas y valores:
                contenido = contenido + '<option value="' + salas[i].id + '">' + salas[i].nombre +
                        '</option>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#cmbSalas').html(contenido);
        }
    });
}

function consultarHorarios() {
    var fecha = $('#txtFechaReservacion').val();
    var idSala = parseInt($('#cmbSalas').val());

    // Esta variable contendrá el contenido HTML de la tabla
    var contenido = '';

    // Hacemos la peticion al servicio REST que nos consulta las sucursales y salas:
    $.ajax({
        type: "GET",
        url: "api/reservacion/getAllHorarios",
        data: {
            fecha: fecha,
            idSala: idSala,
            token: sessionStorage.getItem("token")
        }

    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {
            horarios = data;

            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < horarios.length; i++) {
                // Agregamos un nuevo renglón a la tabla contenido sus respectivas columnas y valores:
                contenido = contenido + '<option value="' + horarios[i].id + '">' + horarios[i].horaInicio + " - " + horarios[i].horaFin +
                        '</option>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#cmbHorarios').html(contenido);
        }
    });
}

function guardar() {
    var estatus = 1;
    var idCliente = $('#txtCodigoCliente').val();
    var idSala = $('#cmbSalas').val();
    var fecha = $('#txtFechaReservacion').val();
    var idHorario = $('#cmbHorarios').val();

    $.ajax({
        type: "GET",
        url: "api/reservacion/insert",
        data: {
            estatus: estatus,
            idCliente: idCliente,
            idSala: idSala,
            fecha: fecha,
            idHorario: idHorario,
            token: sessionStorage.getItem("token")
        }

    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else if (data.exception != null) {
            Swal.fire('Error', data.exception, 'warning');

        } else if (data.result != null) {
            refrescarTabla();
            Swal.fire('Movimiento realizado', data.result, 'success');
            limpiarFormulario();
            setDetalleReservacionVisible(false);
        }
    });
}

function refrescarTabla() {
    var contenido = "";

    // Hacemos la peticion al servicio REST que nos consulta las sucursales y salas:
    $.ajax({
        type: "GET",
        url: "api/reservacion/getAll",
        data: {token: sessionStorage.getItem("token")}

    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {
            reservaciones = data;

            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < reservaciones.length; i++) {
                // Agregamos un nuevo renglón a la tabla contenido sus respectivas columnas y valores:
                contenido = contenido + '<tr>' +
                        '<td>' + reservaciones[i].fecha + '</td>' +
                        '<td>' + reservaciones[i].horario.horaInicio + '</td>' +
                        '<td>' + reservaciones[i].horario.horaFin + '</td>' +
                        '<td>' + reservaciones[i].cliente.persona.nombre + " " + reservaciones[i].cliente.persona.apellidoPaterno + " " +
                        reservaciones[i].cliente.persona.apellidoMaterno + '</td>' +
                        '<td>' + reservaciones[i].sala.nombre + '</td>' +
                        '<td><button class="btn btn-outline-success" onclick="atenderReservacion(' + i + ')" id="estiloInputText"><i class="fa fa-edit mx-1"></i>Atender</button>' + '</td>' +
                        '<td><button class="btn btn-outline-danger" onclick ="cancelarReservacion(' + reservaciones[i].id + ')" id="estiloInputText"><i class="fa fa-trash  mx-1"></i>Cancelar</button>' + '</td>';
                '</tr>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#tbodyReservacion').html(contenido);
        }
    });
}
function refrescarAtendida() {
    var contenido = "";

    // Hacemos la peticion al servicio REST que nos consulta las sucursales y salas:
    $.ajax({
        type: "GET",
        url: "api/reservacion/getAllAtendida",
        data: {token: sessionStorage.getItem("token")}

    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {
            reservaciones = data;

            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < reservaciones.length; i++) {
                // Agregamos un nuevo renglón a la tabla contenido sus respectivas columnas y valores:
                contenido = contenido + '<tr>' +
                        '<td>' + reservaciones[i].fecha + '</td>' +
                        '<td>' + reservaciones[i].horario.horaInicio + '</td>' +
                        '<td>' + reservaciones[i].horario.horaFin + '</td>' +
                        '<td>' + reservaciones[i].cliente.persona.nombre + " " + reservaciones[i].cliente.persona.apellidoPaterno + " " +
                        reservaciones[i].cliente.persona.apellidoMaterno + '</td>' +
                        '<td>' + reservaciones[i].sala.nombre + '</td>' +
                        '<td><span>Atendida</span>' + '</td>' +
                        '<td><i class="fa fa-eye  mx-1"></i>' + '</td>';
                '</tr>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#tbodyReservacion').html(contenido);
        }
    });
}
function refrescarCancelada() {
    var contenido = "";

    // Hacemos la peticion al servicio REST que nos consulta las sucursales y salas:
    $.ajax({
        type: "GET",
        url: "api/reservacion/getAllCancelada",
        data: {token: sessionStorage.getItem("token")}

    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {
            reservaciones = data;

            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < reservaciones.length; i++) {
                // Agregamos un nuevo renglón a la tabla contenido sus respectivas columnas y valores:
                contenido = contenido + '<tr>' +
                        '<td>' + reservaciones[i].fecha + '</td>' +
                        '<td>' + reservaciones[i].horario.horaInicio + '</td>' +
                        '<td>' + reservaciones[i].horario.horaFin + '</td>' +
                        '<td>' + reservaciones[i].cliente.persona.nombre + " " + reservaciones[i].cliente.persona.apellidoPaterno + " " +
                        reservaciones[i].cliente.persona.apellidoMaterno + '</td>' +
                        '<td>' + reservaciones[i].sala.nombre + '</td>' +
                        '<td><span>Cancelada</span>' + '</td>' +
                        
                '</tr>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#tbodyReservacion').html(contenido);
        }
    });
}


function cerrar() {
    $('#divTablaClientes').hide();
}


function buscarReservacionPorID(id) {
    for (var i = 0; i < reservaciones.length; i++) {
        if (reservaciones[i].id === id) {
            return i;
        }
    }
    return -1;
}


function mostrarDetalle(idReservacion) {
    //Buscamos la posicion del producto
    var pos = buscarReservacionPorID(idReservacion);

    //Llenamos los campos del formulario
    $('#txtCodigo').val(reservaciones[pos].id);
    $('#txtCodigoCliente').val(reservaciones[pos].idCliente);
    $('#txtSalaReservada').val(reservaciones[pos].salaReservacion);
    $('#fecha').val(reservaciones[pos].fecha);
    if (reservaciones[pos].estatus === "P") {
        $('#rbtnPendiente').prop('checked', true);
        $('#rbtnAtendida').prop('checked', false);
        $('#rbtnCancelada').prop('checked', false);

    } else if (reservaciones[pos].estatus === "A") {
        $('#rbtnPendiente').prop('checked', false);
        $('#rbtnAtendida').prop('checked', true);
        $('#rbtnCancelada').prop('checked', false);

    } else {
        $('#rbtnPendiente').prop('checked', false);
        $('#rbtnAtendida').prop('checked', false);
        $('#rbtnCancelada').prop('checked', true);
    }
    setDetalleReservacionVisible(true);
}

function limpiarFormulario() {
    $('#txtBuscarCliente').val('');
    $('#txtNombreCliente').val('');
    $('#txtCodigoCliente').val('');
    $('#cmbSucursales').val('');
    $('#cmbSalas').val('');
    $('#txtFechaReservacion').val('');
    $('#cmbHorarios').val('');
}

function cancelarReservacion(idReservacion) {

    var idR = parseInt(idReservacion);
    //alert(idR);
    $.ajax({
        type: "GET",
        url: "api/reservacion/delete",
        data: {
            idReservacion: idR
        }
    })

            .done(function (data) {
                //Revisamos si se llegó a producir algún error:
                if (data.error != null) {
                    Swal.fire('Error', data.error, 'warning');

                } else {
                    Swal.fire('Reservacion Cancelada', data.result, 'success');
                    resfrescarTabla();
                    limpiarFormulario();

                }
            });

}

function  atenderReservacion(i)
{
    $("#divTablaReservacion").hide();
    $("#divDetalleReservacion").hide();
    var datosReservacion = reservaciones[i].cliente.persona.nombre + " " +
            reservaciones[i].cliente.persona.apellidoPaterno + " " +
            reservaciones[i].cliente.persona.apellidoMaterno + "\n " +
            reservaciones[i].sala.nombre + "\n " +
            reservaciones[i].fecha + "\n " +
            reservaciones[i].horario.horaInicio + " - " + reservaciones[i].horario.horaFin;
    $("#txaReservacion").html(datosReservacion);
    //Despliega el div con los datos del servicio
    cargarDetalleServicio();

    //Definimos que el servicio actual es un objeto
    servicioActual = new Object();
    //Declaramos un atributo que es de tipo objeto
    servicioActual.reservacion = new Object();
    // Se le asigna el objeto de la reservacion seleccionada al objeto-atributo reservacion
    servicioActual.reservacion = reservaciones[i];

}

function cargarDetalleServicio()
{
    $("#divDetalleServicio").show();
}
function cerrarDetalleServicio()
{
    $("#divDetalleServicio").hide();
}
function buscarEmpleadoReservacion() {
    var contenido = "";

    // Elemento visual de la búsqueda del cliente para la reservación txtSearch:
    var buscarEmpleado = $('#txtBuscarEmpleado').val();
    var data = {"palabra": buscarEmpleado};

    $.ajax({
        type: "GET",
        url: "api/empleado/getAll",
        data:  data

    }).done(function (data) {
        empleados = data;

        //Recorremos el arreglo de clientes posición por posición:
        for (var i = 0; i < empleados.length; i++) {
            var nombreCompleto = empleados[i].persona.nombre + " " + empleados[i].persona.apellidoPaterno + " " +
                    empleados[i].persona.apellidoMaterno;

            // Agregamos un nuevo renglon a la tabla contenido sus respectivas columnas y valores:
            contenido = contenido + '<tr>' +
                    '<td>' + nombreCompleto + '</td>' +
                    '<td><button class="btn btn-outline-primary" onclick="seleccionarEmpleado(' + i + ');"><i class="fas fa-plus"></i>&nbspElegir</button>' + '</td>' +
                    '</tr>';
        }
        $('#tbodyEmpleados').html(contenido);
        $('#divTablaEmpleados').show();
        
    });
}
function seleccionarEmpleado(i) {
   var nomC = empleados[i].persona.nombre+ " "+empleados[i].persona.apellidoPaterno + " " + empleados[i].persona.apellidoMaterno;
     $("#txtEmpleado").val(nomC);
     $("#txtIdEmpleado").val(empleados[i].id);
     //Primero declaramos el nombre y tipo del atributo
     servicioActual.empleado =new Object();
     //Asignamos el valor
     servicioActual.empleado = empleados[i];
     //alert(JSON.stringify(servicioActual));

}
function buscarPosicionEmpleadoPorID(id) {
    // Recorremos el arreglo posición por posición:
    for (var i = 0; i < empleados.length; i++) {

        // Comparamos si el ID del empleado en la posición actual es el mismo que el buscado:
        if (empleados[i].id === id) {
            return i;
        }
    }
    // Si llegamos a este punto significa que no encontramos un empleado con el ID especificado, 
    // en cuyo caso devolvemos el valor -1:
    return -1;
}
function cargarTratamiento()
{
    var data = {"t": sessionStorage.getItem("token")};

    $.ajax(
            {
                type: "GET",
                url: "api/tratamiento/getAll",
                async: true,
                data: data
            }).done(function (data)
    {
        tratamientos = data;
        var contenido = "";
        for (var i = 0; i < tratamientos.length; i++)
        {
            contenido = contenido + '<tr>' +
                    '<td>' + tratamientos[i].id + " - " + tratamientos[i].nombre +
                    " - " + tratamientos[i].costo + '</td>' +
                    //'<td>'+'<button class="btn btn-outline-primary" onclick = "agregarT(" + i + ")" ><i class="fas fa-plus"></i>Agregar</button>'+'</td>'+
                    '<td><button class="btn btn-outline-primary" id="estiloInputText" onclick="agregarTratamiento(' + i + ');"><i class="fas fa-plus"></i>Agregar</button>' + '</td>' +
                    '</tr>';
        }
        //alert(contenido);
        $("#tbodyTrat").html(contenido);
    });
    servicioActual.servicioTratamiento = [];
    $("#divTablaTrat").show();
    
}
function agregarTratamiento(posicion)
{
    var t = tratamientos[posicion];
    var i = servicioActual.servicioTratamiento.length;
    var tr = '<tr>' +
            '<td>' + t.id + '</td>' +
            '<td>' + t.nombre + '</td>' +
            '<td class="text-right">' + "$" + t.costo + '</td>' +
            '<td>' +
            '<a href="#" class="text-primary" id="estiloInputText" onclick = "cargarPT(' + t.id + ');">' +
            '<i class="fa fa-cog"></i>&nbsp;Detalle' +
            '</a>' + '</td>' + '<td>' +
            '<a href="#" class="text-danger" id="estiloInputText" onclick = "eliminarTratamiento(' + t.id + ');">' +
            '<i class="fa fa-trash"></i>&nbsp;Quitar' +
            '</a>' +
            '</td>' + '</tr>';
    servicioActual.servicioTratamiento[i] = new Object();
    servicioActual.servicioTratamiento[i].tratamiento = new Object();
    servicioActual.servicioTratamiento[i].tratamiento = t;
    servicioActual.servicioTratamiento[i].productos = [];
    $('#tbTratamiento').append(tr);
    calcularTotal();
}
function eliminarTratamiento(idTratamiento)
{
    //alert(idTratamiento)
    for (var i = 0; i < servicioActual.servicioTratamiento.length; i++)
    {

        if (servicioActual.servicioTratamiento[i].tratamiento.id === idTratamiento)
        {
            servicioActual.servicioTratamiento.splice(i, 1);
            $('#tbTratamiento tr').eq(i).remove();
            calcularTotal();
            return;

        }
    }

    cargarTratamiento();


}
function cargarPT(idTratamiento) {
    //alert(idTratamiento);
    var content = '';
    servicioTratamientoActual = null;

    //Buscamos el tratamiento asociado al servicio actual:
    servicioTratamientoActual = buscarServicioTratamiento(idTratamiento);

    if (servicioTratamientoActual != null)
    {
        //Cargamos el listado de productos que han sido agregados:
        for (var i = 0; i < servicioTratamientoActual.productos.length; i++)
        {
            content += '<tr id="tr_' + idTratamiento + '_'
                    + servicioTratamientoActual.productos[i].id + '">' +
                    '<td>' + servicioTratamientoActual.productos[i].id + '</td>' +
                    '<td>' + servicioTratamientoActual.productos[i].nombre + '</td>' +
                    '<td>' + servicioTratamientoActual.productos[i].precioUso + '</td>' +
                    '<td>' +
                    '<a href="#" class="text-danger"' +
                    'onclick="deleteProducto(' + idTratamiento + ',' +
                    servicioTratamientoActual.productos[i].id + ')">' +
                    '<i class="fa fa-trash"></i>&nbsp;Eliminar' +
                    '</a>' +
                    '</td>' +
                    '</tr>';
        }
        $('#tbProductosT').html(content);
        $('#spnNombreTratamiento').html(servicioTratamientoActual.tratamiento.nombre);
        $("#divProductoTratamiento").show();
        //setDetalleProductoVisible(true);  
    }
}
function cerrarProductoT()
{
    $("#divProductoTratamiento").hide();
}
function buscarServicioTratamiento(idTratamiento)
{
    for (var i = 0; i < servicioActual.servicioTratamiento.length; i++)
        if (servicioActual.servicioTratamiento[i].tratamiento.id === idTratamiento)
            return servicioActual.servicioTratamiento[i];
    return null;

}
function cargarProductos() {
    //Esta variable contendrá el contenido HTML de la tabla
    var contenido = "";

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "api/producto/getAll"
    })

            .done(function (data) {
                // Revisamos si hubo un error.
                if (data.error != null) {
                    Swal.fire('Error', data.error, 'warning');

                } else {
                    productos = data;

                    // Recorremos el arreglo JSON de productos:
                    for (var i = 0; i < productos.length; i++) {
                        contenido += '<tr>';
                        contenido += '<td class="text-right">' + productos[i].id + '</td>';
                        contenido += '<td>' + productos[i].nombre + ' $' + productos[i].precioUso + '</td>';
                        contenido += '<td>';
                        contenido += '<a href="#" class="text-primary" onclick="agregarProductos(' + i + ');">';
                        contenido += '<i class="fa fa-plus"></i>';
                        contenido += '</a>';
                        contenido += '</td>';
                        contenido += '</tr>';
                    }
                    // Insertamos el contenido de la tabla dentro de su cuerpo:
                    $('#tbDP').html(contenido);
                }
            });
}
function agregarProductos(index)
{
    var p = productos[index];
    var i = 0;
    var tr = null;
    var t = null;
    if (servicioTratamientoActual == null)
    {
        Swal.fire('Se debe seleccionar un tratamiento', "", 'error');
        return;
    }
    i = servicioTratamientoActual.productos.length;
    t = servicioTratamientoActual.tratamiento;
    servicioTratamientoActual.productos[i] = p;

    //Cada vez que agrega un producto, debo identificar el TR para
    //poderlo eliminar cuando quito productos de un tratamiento
    tr = '<tr id="tr_' + t.id + '_' + p.id + '">' +
            '<td>' + p.id + '</td>' +
            '<td>' + p.nombre + '</td>' +
            '<td>' + p.precioUso + '</td>' +
            '<td>' +
            '<a href="#" class="text-danger" ' +
            'onclick="eliminarProductoS(' + t.id + ', ' + p.id + ');">' +
            '<i class="fa fa-trash"></i>&nbsp;Quitar' +
            '</a>' +
            '</td>' +
            '</tr>';

    $("#tbProductosT").append(tr);
    calcularTotal();
}
function calcularTotal()
{
    var subtotal = 0;
    var total = 0;
    var st = null;

    for (var i = 0; i < servicioActual.servicioTratamiento.length; i++)
    {
        subtotal = 0;
        st = servicioActual.servicioTratamiento[i];
        subtotal = st.tratamiento.costo;
        for (var j = 0; j < st.productos.length; j++)
            subtotal += st.productos[j].precioUso;

        total += subtotal;
    }

    $("#txtTotal").val("$" + total);
}

function eliminarProductoS(idTratamiento, idProducto)
{
    for (var i = 0; i < servicioActual.servicioTratamiento.length; i++)
    {
        if (servicioActual.servicioTratamiento[i].tratamiento.id === idTratamiento)
        {
            for (var j = 0; j < servicioActual.servicioTratamiento[i].productos.length; j++)
            {
                if (servicioActual.servicioTratamiento[i].productos[j].id === idProducto)
                {
                    servicioActual.servicioTratamiento[i].productos.splice(j, 1);
                    $("#tr_" + servicioActual.servicioTratamiento[i].tratamiento.id + '_' + idProducto).remove();
                    calcularTotal();

                }
            }
        }
    }
}

function guardarServicio()
{
    var d = new Date();
    var di = d.getDate();
    var m = d.getMonth() + 1;
    var y = d.getFullYear();
    var hoy = y + '/' + m + '/' + di;
    servicioActual.fecha = hoy;
    
    alert(JSON.stringify(servicioActual));
    var error = servicio_validarDatos();
    
    if(error != null)
    {
        Swal.fire(error.titulo, error.descripcion, error.tipo);
        return;
    }
    
    var data = {"s": JSON.stringify(servicioActual)};
    $.ajax({
        "type": "POST",
        "dataType": "json",
        "async": true,
        "url": "api/servicio/insert",
        "data": data
    }).done(function (data){
        if (data.error != null)
        {
            Swal.fire('Error', data.error, 'error');
            return;
        }
        if (data.exception != null)
        {
            Swal.fire('Error', data.error, 'error');
            return;
        }
        
        Swal.fire('Movimiento Realizado.', '', 'success');        
    });
}

function servicio_validarDatos()
{
    var sinProductos = false;
    
    if(servicioActual == null)
        return {titulo: "", descripcion: "Error desconocido.", tipo: "error"};
    
    if(servicioActual.reservacion == null)
        return {titulo: "Verificar Datos", descripcion: "Seleccione Una Reservación", tipo: "warning"};
    
    if(servicioActual.servicioTratamiento.length < 1)
        return {titulo: "Verificar Datos", descripcion: "Debe asociar por lo menos un tratamiento al servicio.", tipo: "warning"};
    
    for(var i = 0; i < servicioActual.servicioTratamiento.length; i++)
    {
        if(servicioActual.servicioTratamiento[i].productos.length < 1)
        {
            sinProductos = true;
            i = servicioActual.servicioTratamiento.length + 1;
        }
    }
    
    if(sinProductos)
        return {titulo: "Verificar Datos.", descripcion: "Verifique que todos los tratamientos tienen por lo menos un producto asociado", tipo:"warning"};
    
    return null;
}