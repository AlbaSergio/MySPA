var empleados = [];

function inicializarModulo()
{
    //Codigo para implementar el filtro en la tabla
    $("#txtBuscar").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbodyEmpleado tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    setDetalleEmpleadoVisible(false);
    refrescarTabla();
}

function imprimir() {
    var print = document.getElementById('divTablaEmpleado').innerHTML;
    var contenido = document.body.innerHTML;

    document.body.innerHTML = print;
    window.print();
    document.body.innerHTML = contenido;
}
/*
 * Esta función nos sirve para mostrar y ocultar el área que contiene 
 * el detalle de un empleado.
 * 
 * Si el valor es true, mostramos el area del detalle del empleado.
 * Si el valor es false, ocultamos el area del detalle del empleado.
 */
function setDetalleEmpleadoVisible(valor)
{
    if (valor)
    {
        $('#divTablaEmpleado').removeClass("col-12");
        $('#divTablaEmpleado').addClass("col-7");
        $('#divDetalleEmpleado').show();
    } else
    {
        $('#divTablaEmpleado').removeClass("col-7");
        $('#divTablaEmpleado').addClass("col-12");
        $('#divDetalleEmpleado').hide();
    }
}

function guardar() {
//Creamos un nuevo objeto
    var empleado = new Object();
    empleado.persona = new Object();
    empleado.usuario = new Object();
  
    empleado.id = 0;
    empleado.persona.nombre = $('#txtNombre').val();
    empleado.persona.apellidoPaterno = $('#txtApellidoPaterno').val();
    empleado.persona.apellidoMaterno = $('#txtApellidoMaterno').val();
    empleado.persona.domicilio = $('#txtDomicilio').val();
    empleado.persona.genero = $('#cmbGenero').val();
    empleado.persona.rfc = $('#txtRfc').val();
    empleado.persona.telefono = $('#txtTelefono').val();
    empleado.usuario.nombreUsuario = $('#txtUsuario').val();
    empleado.usuario.contrasenia = $('#txtContra').val();
    empleado.usuario.rol = "Empleado";
    empleado.numeroEmpleado = $('#txtNumeroEmpleado').val();
    empleado.puesto = $('#txtPuesto').val();
    empleado.estatus = 1;
    empleado.foto = $('#txtCodigoImagen').val();
    empleado.rutaFoto = $('#txtCodigoImagen').val();

//Revisamos si hay un ID previsto:
    if ($('#txtCodigoEmpleado').val().length >= 0)
    {
        empleado.id = parseInt($('#txtCodigoEmpleado').val());
        empleado.persona.id = parseInt($('#txtCodigoPersona').val());
        empleado.usuario.id = parseInt($('#txtCodigoUsuario').val());
    }
    $.ajax({
        type: "POST",
        async: true,
        url: "api/empleado/save",
        data: {empleado: JSON.stringify(empleado)}
    })
            .done(function (data) {
                if (data.error != null)
                {
                    Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
                } 
                else
                {
                    refrescarTabla();
                    empleado = data;    
                    $('#txtCodigoEmpleado').val(empleado.id);
                    $('#txtCodigoPersona').val(empleado.persona.id);
                    $('#txtCodigoUsuario').val(empleado.usuario.id);
                    $('#txtNumeroEmpleado').val(empleado.numeroEmpleado);
                    Swal.fire('Movimiento realizado', 'Los datos de empleado se han guardado correctamente.', 'success');
                    limpiarFormulario();
                }
            });
        
}
function eliminar()
{
    //Obtenemos el ID del Empleado a eliminar
    var idEmpleado = 0;

    if ($('#txtCodigoEmpleado').val().length > 0) {

        idEmpleado = parseInt($('#txtCodigoEmpleado').val());
        $.ajax({
            type: "POST",
            url: "api/empleado/delete",
            async: true,
            data: {id: idEmpleado}
        })
                .done(function (data) {
                    if (data.error != null) {
                        Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
                    } else
                    {
                        Swal.fire('Empleado eliminado', data.result, 'success');
                        limpiarFormulario();
                        refrescarTabla();
                    }
                });
    } else {
         Swal.fire('¡¡¡ Alerta !!!', '¡No se ha seleccionado ningún registro de empleado para eliminarlo!.', 'warning');
    }
}

function refrescarTabla()
{
    var contenido = "";
    //Hacemos la petición al Servicio REST que nos consulta los empleados:
    $.ajax({
        type: "GET",
        url: "api/empleado/getAll"
    })
            .done(function (data) {
                //Revisamos si hubo algun error:
                if (data.error != null)
                {
                    Swal.fire('Error', data.error, 'warning');
                } else {
                    //Recorremos el arreglo de Empleado posición por posición:
                    for (var i = 0; i < data.length; i++)
                    {
                        empleados = data;
                        //Agregamos un nuevo renglo a la tabla contenido
                        // sus respectivas columnas y valores:
                        contenido = contenido + '<tr>' +
                                '<td>' + empleados [i].numeroEmpleado + '</td>' +
                                '<td>' + empleados [i].persona.nombre + ' ' +
                                empleados [i].persona.apellidoPaterno + ' ' +
                                empleados [i].persona.apellidoMaterno + ' ' +
                                '</td>' +
                                '<td>' + empleados [i].persona.domicilio + '</td>' +
                                '<td>' + empleados [i].puesto + '</td>' +
                                '<td>' + empleados [i].estatus + '</td>' +
                                '<td><a href="#" onclick="mostrarDetalle(' + empleados[i].id + ');"><i class="far fa-eye"></i></a>' + '</td>' +
                                '</tr>';
                    }
                    //Insertamos el contenido generado previamente dentro del cuerpo de la tabla:
                    $('#tbodyEmpleado').html(contenido);
                }
            });
}


function mostrarDetalle(idEmpleado) {
    var pos = buscarEmpleadoPorID(idEmpleado);
    var e = null;

    if (pos < 0)
        return;
    //Obbtenemos el objeto empleado en la posición requerida:
    e = empleados[pos];
    //Llenamos los controles del formulariode detalle:
    $('#txtCodigoEmpleado').val(e.id);
    $('#txtCodigoPersona').val(e.persona.id);
    $('#txtCodigoUsuario').val(e.usuario.id);
    
    $('#txtNombre').val(e.persona.nombre);
    $('#txtApellidoPaterno').val(e.persona.apellidoPaterno);
    $('#txtApellidoMaterno').val(e.persona.apellidoMaterno);
    $('#txtDomicilio').val(e.persona.domicilio);
    $('#cmbGenero').val(e.persona.genero);
    $('#txtRfc').val(e.persona.rfc);
    $('#txtTelefono').val(e.persona.telefono);
    
    $('#txtUsuario').val(e.usuario.nombreUsuario);
    $('#txtContra').val(e.usuario.contrasenia);
    $('#txtRol').val(e.usuario.rol);
    
    $('#txtNumeroEmpleado').val(e.numeroEmpleado);
    $('#txtPuesto').val(e.puesto);
    $('#txtCodigoImagen').val(e.rutaFoto);
    $('#imgEmpleadoFoto').prop('src','data:image/png;base64,' + e.foto);

    setDetalleEmpleadoVisible(true);
}


function buscarEmpleadoPorID(id) {
    for (var i = 0; i < empleados.length; i++) {
        if (empleados[i].id === id) {
            return i;
        }
    }
    return -1;
}

function limpiarFormulario() {
    $('#txtCodigoEmpleado').val('');
    $('#txtCodigoPersona').val('');
    $('#txtCodigoUsuario').val('');
    $('#txtNombre').val('');
    $('#txtApellidoPaterno').val('');
    $('#txtApellidoMaterno').val('');
    $('#txtTelefono').val('');
    $('#txtDomicilio').val('');
    $('#txtUsuario').val('');
    $('#txtContra').val('');
    $('#txtRol').val('');
    $('#txtRfc').val('');
    $('#cmbGenero').val('');
    $('#txtNumeroEmpleado').val('');
    $('#txtPuesto').val('');
    $('#txtCodigoImagen').val('');
    $('#imgEmpleadoFoto').prop('src', 'media/img/avatar.png');
    var inputFile = document.getElementById('inputFileFoto');
    inputFile.value = '';
}

function cargarFotografia(){
    //Recuperamos el input de tipo File donde se selecciona la foto
    var inputFile = document.getElementById('inputFileFoto');
    //Revisamos que el usuario haya seleccionado un archivo:
    if (inputFile.files && inputFile.files[0]) {
        //Creamos el objeto que leerá la imagen
        var reader = new FileReader();
        //Agregamos un oyente al lector del archivo para que en cuanto el usuario cargue la imagen,
        //esta se lea y se convierta de forma automatica en una cadena de Base64:
        reader.onload = function (e){
            var fotoB64 = e.target.result;
            $("#imgEmpleadoFoto").attr("src",fotoB64);
            $("#txtCodigoImagen").val(fotoB64.substring(22,fotoB64.length));
        };
        
        //Leemos el archivo que selecciono el usuario y lo convertimos en una cadena con la Base64
        reader.readAsDataURL(inputFile.files[0]);
    }
}


