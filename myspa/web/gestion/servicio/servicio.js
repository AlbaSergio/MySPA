var servicios = [
    {
        id: 1,
        idReservacion: 2,
        idTratamiento: 3,
        idEmpleado: 1,
        fecha: "12/10/2021",
        hora: "01:20",
        costoServicio: 49.99,
        costoTratamiento: 50.00,
        costoProducto: 78.47
    },
    {
        id: 2,
        idReservacion: 3,
        idTratamiento: 1,
        idEmpleado: 2,
        fecha: "12/10/2021",
        hora: "02:50",
        costoServicio: 75.43,
        costoTratamiento: 34.67,
        costoProducto: 75.57
    },
    {
        id: 3,
        idReservacion: 1,
        idTratamiento: 1,
        idEmpleado: 1,
        fecha: "12/10/2021",
        hora: "03:10",
        costoServicio: 346.46,
        costoTratamiento: 346.65,
        costoProducto: 34.99
    }
];

function inicializarModulo() {

    //Codigo para implementar el filtro en la tabla
    $("#txtBuscar").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbodyServicio tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    setDetalleServicioVisible(false);
    refrescarTabla();
}
/*
 * Esta funcion nos sirve para ocultar y mostrar el area
 * que contiene el detalle servicio.
 * 
 * Si el valor es true, mostramos el area del detalle del servicio.
 * Si el valor es false, ocultamos el area del detalle del servicio
 */
function  setDetalleServicioVisible(valor)
{
    if (valor) {
        $('#divTablaServicio').show();
    } 
    else
    {
        $('#divTablaServicio').hide();
    }
}

function guardar() {

    var pos = -1;

    //Generamos un nuevo objeto 
    var servicio = new Object();

    //Definimos sus propiedades y sus valores:
    servicio.id = parseInt($('#txtCodigo').val());
    servicio.idReservacion = parseInt($('#txtCodigoReservacion').val());
    servicio.idTratamiento = parseInt($('#txtCodigoTratamiento').val());
    servicio.idEmpleado = parseInt($('#txtCodigoEmpleado').val());
    servicio.fecha = $('#fecha').val();
    servicio.hora = $('#hora').val();
    servicio.costoServicio = parseFloat($('#txtCostoServicio').val());
    servicio.costoTratamiento = parseFloat($('#txtCostoTratamiento').val());
    servicio.costoProducto = parseFloat($('#txtCostoProducto').val());

    //Buscamos la posicion del producto con base en su ID para saber
    //si ya existe previamente
    pos = buscarServicioPorID(servicio.id);
    if (pos === -1)
    {
        //Lo insertamos dentro del arreglo
        servicios.push(servicio);
    } else
    {
        //Reemplazamos el producto en la posicion que encontramos
        servicios[pos] = servicio;
    }
    //Mostramos un mensaje al usuario;
    Swal.fire('Movimiento realizado', 'Servicio agregado correctamente', 'success');
    
    //Refrescamos la tabla productos:
    resfrescarTabla();
}

function eliminar()
{
    //Obtenemos el ID del producto a eliminar
    var idServicio = parseInt($('#txtCodigo').val());

    //Buscamos la posicion del producto
    var pos = buscarServicioPorID(idServicio);

    //Verificamos si es una posicion valida
    if (pos >= 0)
    {
        //Quitamos el elemento del arreglo
        servicios.splice(pos, 1);
        //Limpiamos el formulario
        limpiarFormulario();
        //Notificamos al usuario
        Swal.fire('', 'Sirvicio eliminado correctamente.', 'success');
        //Refrescamos la tabla productos
        resfrescarTabla();
    } else {
        Swal.fire('Atencion', 'No se encontró el servicio seleccionado', 'warning')
    }
}

function resfrescarTabla() {

    //Esta variable contendra el contenido HTML de la tabla
    var contenido = '';
    //Recorremos el arreglo JSON de productos
    for (var i = 0; i < servicios.length; i++) {
        contenido = contenido + '<tr>' +
                '<td>' + servicios[i].id + '</td>' +
                '<td>' + servicios[i].idReservacion + '</td>' +
                '<td>' + servicios[i].idTratamiento + '</td>' +
                '<td>' + servicios[i].idEmpleado + '</td>' +
                '<td>' + servicios[i].fecha + '</td>' +
                '<td>' + servicios[i].hora + '</td>' +
                '<td>' + servicios[i].costoServicio + '</td>' +
                '<td>' + servicios[i].costoTratamiento + '</td>' +
                '<td>' + servicios[i].costoProducto + '</td>' +
                '<td><a href="#" onclick="mostrarDetalle(' + servicios[i].id + ');"><i class="fa fa-eye"></i> </a>' + '</td>' +
                '</tr>';
    }
    //Insertamos el contenido de la tabla dentro de su cuerpo
    $('#tbodyServicio').html(contenido);
}

function buscarServicioPorID(id)
{

    for (var i = 0; i < servicios.length; i++)
    {
        if (servicios[i].id === id)
        {
            return i;
        }
    }
    return -1;
}

function mostrarDetalle(idServicio)
{
    //Buscamos la posicion del producto
    var pos = buscarServicioPorID(idServicio);

    //Llenamos los campos del formulario
    $('#txtCodigo').val(servicios[pos].id);
    $('#txtCodigoReservacion').val(servicios[pos].idReservacion);
    $('#txtCodigoTratamiento').val(servicios[pos].idTratamiento);
    $('#txtCodigoEmpleado').val(servicios[pos].idEmpleado);
    $('#fecha').val(servicios[pos].fecha);
    $('#hora').val(servicios[pos].hora);
    $('#txtCostoServicio').val(servicios[pos].costoServicio);
    $('#txtCostoTratamiento').val(servicios[pos].costoTratamiento);
    $('#txtCostoProducto').val(servicios[pos].costoProducto);

    setDetalleServicioVisible(true);
}
function limpiarFormulario()
{
    $('#txtCodigo').val('');
    $('#txtCodigoReservacion').val('');
    $('#txtCodigoTratamiento').val('');
    $('#txtCodigoEmpleado').val('');
    $('#fecha').val('');
    $('#hora').val('');
    $('#txtCostoServicio').val('');
    $('#txtCostoTratamiento').val('');
    $('#txtCostoProducto').val('');
}
