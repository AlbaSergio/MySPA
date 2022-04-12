//Esta variable es mi mapa central
var map = null;
var marker = null;
var sucursales = [];

function imprimir() {
    var print = document.getElementById('divTablaSucursal').innerHTML;
    var contenido = document.body.innerHTML;

    document.body.innerHTML = print;
    window.print();
    document.body.innerHTML = contenido;
}
function inicializarModulo()
{
    //Codigo para implementar el filtro en la tabla
    $("#txtBuscar").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbodySucursal tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    setDetalleSucursalVisible(false);
    refrescarTabla();
}
/*
 * Esta función nos sirve para mostrar y ocultar el área que contiene 
 * el detalle de un producto.
 * 
 * Si el valor es true, mostramos el area del detalle del producto.
 * Si el valor es false, ocultamos el area del detalle del producto.
 */
function setDetalleSucursalVisible(valor)
{
    if (valor)
    {
        $('#divTablaSucursal').removeClass("col-12");
        $('#divTablaSucursal').addClass("col-7");
        $('#divDetalleSucursal').show();
    } else
    {
        $('#divTablaSucursal').removeClass("col-7");
        $('#divTablaSucursal').addClass("col-12");
        $('#divDetalleSucursal').hide();
    }

}

function guardar()
{
    var sucursal = new Object();
    if ($('#txtCodigo').val().length < 1)
        sucursal.id = 0;
    else
    sucursal.id = parseInt($('#txtCodigo').val());
    sucursal.nombre = ($('#txtNombre').val());
    sucursal.domicilio = ($('#txtDomicilio').val());
    sucursal.latitud = parseFloat($('#txtLatitud').val());
    sucursal.longitud = parseFloat($('#txtLongitud').val());
    sucursal.estatus = 1;

    $.ajax({
        type: "POST",
        url: "api/sucursal/save",
        data: {
            idSucursal: sucursal.id,
            nombre: sucursal.nombre,
            domicilio: sucursal.domicilio,
            latitud: sucursal.latitud,
            longitud: sucursal.longitud
        }
    })
            .done(function (data) {
                //Revisamos si hubo algun error:
                if (data.error != null) {
                    Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');

                } else {
                    refrescarTabla();
                    sucursal = data;
                    $('#txtCodigo').val(sucursal.id);
                    Swal.fire('Movimiento realizado', 'Los datos de la sucursal se han guardado correctamente.', 'success');
                     limpiarFormulario();
                }
            });
}



function eliminar() { 
   //Declaramos la variable para obtener el ID de la sala a eliminar
    var id = 0;
    
    if ($('#txtCodigo').val().length > 0) {
        id = parseInt($('#txtCodigo').val());
    
        $.ajax({
            type: "POST",
            url: "api/sucursal/delete",
            data: {
                idSucursal: id
            }
        })
                .done(function (data) {
                    //Revisamos si hubo algun error:
                    if (data.error != null) {
                        Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
                    } else {
                        refrescarTabla();
                        Swal.fire('Sucursal eliminada', data.result, 'success');
                        limpiarFormulario();
                    } 
        });
        
    } else {
        Swal.fire('¡¡¡ Alerta !!!', '¡No se ha seleccionado ningún registro de sucursal para eliminarlo!', 'warning');
    }
}

function refrescarTabla() {
    var contenido = "";
    
    //Hacemos la petición al Servicio REST que nos consulta las sucursales:
    $.ajax({
        type  : "GET",
        url   : "api/sucursal/getAll",
        data  : {"token" : sessionStorage.getItem("token")}
                
    }).done(function (data) {
                //Revisamos si hubo algun error:
                if (data.error != null)
                {
                    Swal.fire('Error', data.error, 'warning');
                } else {
                    //Recorremos el arreglo de productos posición por posición:
                    for (var i = 0; i < data.length; i++)
                    {
                        sucursales = data;
                        //Agregamos un nuevo renglo a la tabla contenido
                        // sus respectivas columnas y valores:
                        contenido = contenido + '<tr>' +
                                '<td>' + sucursales [i].id + '</td>' +
                                '<td>' + sucursales [i].nombre + '</td>' +
                                '<td>' + sucursales [i].domicilio + '</td>' +
                                '<td>' + sucursales [i].estatus + '</td>' +
                                '<td><a href="#" onclick="mostrarDetalle(' + sucursales[i].id + '); "><i class="far fa-eye"></i></a>' + '</td>' +
                                '</tr>';
                    }
                    //Insertamos el contenido generado previamente dentro del cuerpo de la tabla:
                    $('#tbodySucursal').html(contenido);
                }
            });
}

function mostrarDetalle(idSucursal)
{

    var pos = buscarSucursalPorID(idSucursal);
    $('#txtCodigo').val(sucursales[pos].id);
    $('#txtNombre').val(sucursales[pos].nombre);
    $('#txtDomicilio').val(sucursales[pos].domicilio);
    $('#txtLongitud').val(sucursales[pos].longitud);
    $('#txtLatitud').val(sucursales[pos].latitud);
    $('#locSucursal').val(sucursales[pos].localizacion);
    setDetalleSucursalVisible(true);
    localizarSucursal(sucursales[pos]);
}


function buscarSucursalPorID(id)
{

    for (var i = 0; i < sucursales.length; i++)
    {
        if (sucursales[i].id === id)
        {
            return i;
        }
    }
    return -1;
}

function iniciarMap() {
    var latitude = 21.12094669292484;
    var longitude = -101.68294915705076;
    var coord = {lat: latitude, lng: longitude};
    map = new google.maps.Map(document.getElementById('divMapa'), {
        zoom: 15,
        center: coord
    });
    // navigator.geolocation.getCurrentPosition(localizacion);

}

function localizarSucursal(sucursal)
{
    var loc = {lat: sucursal.latitud, lng: sucursal.longitud};
    if (marker != null)
    {
        marker.setMap(null);
    }
    marker = new google.maps.Marker({
        position: loc,
        map: map
    });
    map.setCenter(loc);
    //alert("hola");
}


function limpiarFormulario()
{
    $('#txtCodigo').val('');
    $('#txtNombre').val('');
    $('#txtDomicilio').val('');
    $('#txtLongitud').val('');
    $('#txtLatitud').val('');
    $('#locSucursal').prop('src', '');
    $('#divMapa').val('');
}