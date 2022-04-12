var tratamientos = [];
function imprimir()
{
    var print = document.getElementById('divTablaTratamiento').innerHTML;
    var contenido = document.body.innerHTML;

    document.body.innerHTML = print;
    window.print();
    document.body.innerHTML = contenido;
}

function guardar() {
    var tratamiento = new Object();
    
    tratamiento.id = 0;
    tratamiento.nombre = ($('#txtNombreT').val());
    tratamiento.costo = parseFloat(($('#txtCosto').val()));
    tratamiento.descripcion = ($('#txtDescripcionT').val());
    tratamiento.estatus =1;
    if($('#txtCodigoT').val().length > 0)
    {
        tratamiento.id = parseInt($('#txtCodigoT').val());
    }
    
   
    $.ajax({
        type: "POST",
        async: true,
        url: "api/tratamiento/save",
        data: {
            idTratamiento: tratamiento.id,
            nombre: tratamiento.nombre,
            costo: tratamiento.costo,
            descripcion: tratamiento.descripcion
              }
    }).done(function (data) {
                if (data.error != null) {
                    Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');

                } else {
                    resfrescarTabla();
                    tratamiento= data;
                    $('#txtCodigoT').val(tratamiento.id);
                    Swal.fire('Movimiento realizado', 'El tratamiento se guardo correctamente.', 'success');
                    limpiarFormulario();

                }
            });
}

function eliminar()
{

    
    
 var id = parseInt($('#txtCodigoT').val());

        $.ajax({
            type: "POST",
            url: "api/tratamiento/delete",
            async: true,
            data: {
                idTratamiento: id
            }
        }).done(function (data) {
                    //Revisamos si se llegó a producir algún error:
                    if (data.error != null) {
                        Swal.fire('Error', data.error, 'warning');

                    } else {
                        resfrescarTabla();
                        tratamiento = data;
                       $('#txtCodigoT').val(id);
                        Swal.fire('Tratamiento eliminado', data.result, 'success');
                        limpiarFormulario();

                    }
                });
}

function resfrescarTabla() {
var contenido = '';
        $.ajax({
        type: "GET",
        url: "api/tratamiento/getAll"
        })

        .done(function (data) {
        //Revisamos si hubo algun error:
        if (data.error != null)
        {
        Swal.fire('Error', data.error, 'warning');
        } else {
        tratamientos = data;
                //Recorremos el arreglo JSON de tratamiento
        for (var i = 0; i < tratamientos.length; i++) {
        contenido = contenido + '<tr>' +
                '<td>' + tratamientos[i].id + '</td>' +
                '<td>' + tratamientos[i].nombre + '</td>' +
                '<td>'+'$'+ tratamientos[i].costo + '</td>' +
                '<td>' + tratamientos[i].descripcion + '</td>' +
                '<td>' + tratamientos[i].estatus + '</td>' +
                '<td><a href="#" onclick="mostrarDetalle(' + tratamientos[i].id + ');"><i class="fa fa-eye"></i> </a>' + '</td>' +
                '</tr>';
        }
        //Insertamos el contenido de la tabla dentro de su cuerpo
        $('#tbodyTratamiento').html(contenido);
                }
          });
        }
        /*
         * Esta funcion sirve para  buscar la posicion de un tratamiento
         * dentro del arrreglo de tratamiento, con base en un ID especificado
         * 
         * El metodo devolvera la posicion donde se encuentra el objeto con el ID 
         * coincidente. En caso de que no se encuentre un tratamiento
         * con el ID especificado, la funcion devolvera el valor -1
         */
        function buscarPosicionTratamientoPorID(id)
                {
                //Recorremos el arreglo posicion por posicion
                for (var i = 0; i < tratamientos.length; i++) {
                //Comparamos si el ID del tratamiento en la posicion actual es el mismo que el buscado
                if (tratamientos[i].id === id)
                {
                //Devolvemos la posicion del tratamiento
                return i;
                }
                }
                /*
                 * Si llegamos a este punto significa que no encontramos un
                 * tratamiento con el ID especificado, en cuyo caso devolvemos 
                 * el valor -1:
                 */
                return - 1;
               }

        function mostrarDetalle(idTratamiento){
        //Buscamos la posicion del Horario
        var pos = buscarPosicionTratamientoPorID(idTratamiento);
                //Llenamos los campos del formulario
                $('#txtCodigoT').val(tratamientos[pos].id);
                $('#txtNombreT').val(tratamientos[pos].nombre);
                $('#txtCosto').val(tratamientos[pos].costo);
                $('#txtDescripcionT').val(tratamientos[pos].descripcion);
                setDetalleTratamientoVisible(true);
                }
                
        function limpiarFormulario()
                {
                        $('#txtCodigoT').val('');
                        $('#txtNombreT').val('');
                        $('#txtCosto').val('');
                        $('#txtDescripcionT').val('');
                        }
        function inicializarModulo(){
        //Codigo para implementar el filtro en la tabla
        $("#txtBuscar").on("keyup", function() {
        var value = $(this).val().toLowerCase();
                $("#tbodyTratamiento tr").filter(function() {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > - 1)
        });
        });
                setDetalleTratamientoVisible(false);
                refrescarTabla();
                }
        /*
         * Esta funcion nos sirve para ocultar y mostrar el area
         * que contiene el detalle tratamiento.
         * 
         * Si el valor es true, mostramos el area del detalle de tratamiento.
         * Si el valor es false, ocultamos el area del detalle de tratamiento.
         */
        function  setDetalleTratamientoVisible(valor){
        if (valor){
        $('#divTablaTratamiento').removeClass("col-12");
                $('#divTablaTratamiento').addClass("col-7");
                $('#divDetalleTratamiento').show();
                }
    else
                {
                $('#divTablaTratamiento').removeClass("col-7");
                        $('#divTablaTratamiento').addClass("col-12");
                        $('#divDetalleTratamiento').hide();
                        }
        }




