var productos = [];

function imprimir() {
    var print = document.getElementById('divTablaProductos').innerHTML;
    var contenido = document.body.innerHTML;

    document.body.innerHTML = print;
    window.print();
    document.body.innerHTML = contenido;
}
function inicializarModulo() {

    //Codigo para implementar el filtro en la tabla
    $("#txtBuscar").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbodyProductos tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    setDetalleProductoVisible(false);
    refrescarTabla();
}
/*
 * Esta funcion nos sirve para ocultar y mostrar el area
 * que contiene el detalle producto.
 * 
 * Si el valor es true, mostramos el area del detalle del producto.
 * Si el valor es false, ocultamos el area del detalle del producto
 */
function  setDetalleProductoVisible(valor) {
    if (valor) {
        $('#divTablaProductos').removeClass("col-12");
        $('#divTablaProductos').addClass("col-7");
        $('#divDetalleProducto').show();
    } else
    {
        $('#divTablaProductos').removeClass("col-7");
        $('#divTablaProductos').addClass("col-12");
        $('#divDetalleProducto').hide();
    }
}

function guardar() {
    var producto = new Object();
    
    if ($('#txtCodigo').val().length < 1) 
        producto.id = 0;
    
    else 
    producto.id = parseInt($('#txtCodigo').val());
    producto.nombre = ($('#txtNombre').val());
    producto.marca = ($('#txtMarca').val());
    producto.precioUso = parseFloat(($('#txtPrecioUso').val()));
    producto.estatus = 1;
  
    $.ajax({
                type  : "POST",
                url   : "api/producto/save",
                data  : {
                            idProducto  : producto.id,
                            nombre      : producto.nombre,
                            marca       : producto.marca,
                            precioUso   : producto.precioUso
                        }
            })
            
    .done(function (data){
        if(data.error != null) {
            Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
            
        } else {
                resfrescarTabla();
                Swal.fire('Movimiento realizado', 'Los datos del producto se han guardado correctamente.', 'success');
                limpiarFormulario();
                
            }
        });
}


function eliminar() {
    //Declaramos la variable para obtener el ID del producto a eliminar
    var id = 0;

    if ($('#txtCodigo').val().length > 0) {
        id = parseInt($('#txtCodigo').val());
    
        $.ajax({
                    type  : "POST",
                    url   : "api/producto/delete",
                    data  : {
                                idProducto : id
                            }
                })

        .done(function (data){
            //Revisamos si se llegó a producir algún error:
            if(data.error != null) {
                Swal.fire('Error', data.error, 'warning');

            } else {
                resfrescarTabla();
                Swal.fire('Producto eliminado', data.result, 'success');
                limpiarFormulario();
                
            }
        });
        
    } else {
        Swal.fire('¡¡¡ Alerta !!!', '¡No se ha seleccionado ningún registro de producto para eliminarlo!', 'warning');
    }
}

function resfrescarTabla() {
    //Esta variable contendrá el contenido HTML de la tabla
    var contenido = "";
    
    $.ajax({
                type  : "GET",
                url   : "api/producto/getAll"
            })
            
    .done(function (data) {
        // Revisamos si hubo un error.
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {   
            productos = data;
            
            // Recorremos el arreglo JSON de productos:
            for (var i = 0; i < productos.length; i++) {
                contenido = contenido + '<tr>' +
                                            '<td>' + productos[i].id + '</td>' +
                                            '<td>' + productos[i].nombre + '</td>' +
                                            '<td>' + productos[i].marca + '</td>' +
                                            '<td>' + productos[i].precioUso + '</td>' +
                                            '<td>' + productos[i].estatus + '</td>' +
                                            '<td><a href="#" onclick="mostrarDetalle('+ productos[i].id +');"><i class="fa fa-eye"></i> </a>' +'</td>'+
                                        '</tr>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo:
            $('#tbodyProductos').html(contenido);
        }
    });
}
/*
 * Esta funcion sirve para  buscar la posicion de un producto 
 * dentro del arrreglo de productos, con base en un ID especificado
 * 
 * El metodo devolvera la posicion donde se encuentra el objeto con el ID 
 * coincidente. En caso de que no se encuentre un producto
 * con el ID especificado, la funcion devolvera el valor -1
 */
function buscarPosicionProductoPorID(id)
{
    //Recorremos el arreglo posicion por posicion
    for (var i = 0; i < productos.length; i++) {
        //Comparamos si el ID del producto en la posicion actual es el mismo que el buscado
        if (productos[i].id === id)
        {
            //Devolvemos la posicion del producto
            return i;
        }
    }
    return -1;
}

function mostrarDetalle(idProducto) {
    //Buscamos la posicion del producto
    var pos = buscarPosicionProductoPorID(idProducto);
    //Llenamos los campos del formulario
    $('#txtCodigo').val(productos[pos].id);
    $('#txtNombre').val(productos[pos].nombre);
    $('#txtMarca').val(productos[pos].marca);
    $('#txtPrecioUso').val(productos[pos].precioUso);

    setDetalleProductoVisible(true);
}
function limpiarFormulario()
{
    $('#txtCodigo').val('');
    $('#txtNombre').val('');
    $('#txtMarca').val('');
    $('#txtPrecioUso').val('');
}

