var clientes = [];

function imprimir() {
    var print = document.getElementById('divTablaCliente').innerHTML;
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
        $("#tbodyCliente tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    setDetalleClienteVisible(false);
    refrescarTabla();
}

function setDetalleClienteVisible(valor)
{
    if (valor)
    {
        $('#divTablaCliente').removeClass("col-12");
        $('#divTablaCliente').addClass("col-7");
        $('#divDetalleCliente').show();
    } else
    {
        $('#divTablaCliente').removeClass("col-7");
        $('#divTablaCliente').addClass("col-12");
        $('#divDetalleCliente').hide();
    }
}

function guardar()
{
    //Generamos un nuevo objeto
    var cliente = new Object();
    cliente.persona = new Object();
    cliente.usuario = new Object();
    
    cliente.id = 0;
    cliente.persona.nombre = $('#txtNombre').val();
    cliente.persona.apellidoPaterno = $('#txtApellidoPaterno').val();
    cliente.persona.apellidoMaterno = $('#txtApellidoMaterno').val();
    cliente.correo = $('#txtCorreo').val();
    cliente.persona.telefono = parseInt($('#txtTelefono').val());
    cliente.persona.genero = $('#cmbGenero').val();
    cliente.persona.rfc = ($('#txtRfc').val());
    cliente.persona.domicilio = $('#txtDomicilio').val();
    cliente.usuario.nombreUsuario = $('#txtUsuario').val();
    cliente.usuario.contrasenia = $('#txtContrasenia').val();
    cliente.usuario.rol = "Cliente";
    cliente.numeroUnico = $('#txtNumeroCliente').val();
    cliente.estatus = 1;
    cliente.foto = $('#txtCodigoImagen').val();
    cliente.rutaFoto = $('#txtCodigoImagen').val();
   
    //Revisamos si hay un ID previo:
    if ($('#txtCodigoCliente').val().length > 0) {
        cliente.id = parseInt($('#txtCodigoCliente').val());
        cliente.persona.id = parseInt($('#txtCodigoPersona').val());
        cliente.usuario.id = parseInt($('#txtCodigoUsuario').val());
    }
    
    $.ajax({
        type: "POST",
        async: true,
        url: "api/cliente/save",
        data: {cliente: JSON.stringify(cliente),
                token : sessionStorage.getItem("token")}
    })
            .done(function (data) {
                //Revisamos si hubo algun error:
                if (data.error != null)
                {
                    Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
                } else {
                    refrescarTabla();
                    cliente = data;
                    $('#txtCodigoCliente').val(cliente.id);
                    $('#txtCodigoPersona').val(cliente.persona.id);
                    $('#txtCodigoUsuario').val(cliente.usuario.id);
                    $('#txtNumeroCliente').val(cliente.numeroUnico);

                    Swal.fire('Movimiento realizado', 'Los datos del cliente se han guardado correctamente.', 'success');
                     limpiarFormulario();
                }
            });
}

function eliminar() {
    // Obtenemos el ID del Empleado a eliminar
    var idCliente = 0;

    if ($('#txtCodigoCliente').val().length > 0) {

        idCliente = parseInt($('#txtCodigoCliente').val());
        $.ajax({
            type: "POST",
            url: "api/cliente/delete",
            async: true,
            data: { id: idCliente, 
                    token : sessionStorage.getItem("token")
                  }
        })
                .done(function (data) {
                    if (data.error != null) {
                        Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
                    } else
                    {
                        Swal.fire('Cliente eliminado', data.result, 'success');
                        limpiarFormulario();
                        refrescarTabla();
                    }
                });
    } else {
        Swal.fire('¡¡¡ Alerta !!!', '¡No se ha seleccionado ningún registro de cliente para eliminarlo!', 'warning');
    }
}



function refrescarTabla() {
    var contenido = "";
    //Hacemos la petición al Servicio REST que nos consulta los clientes:
    
    $.ajax({
        type: "GET",
        url: "api/cliente/getAll",
        data  : {token : sessionStorage.getItem("token")}
        
    })
            .done(function (data) {
                //Revisamos si hubo algun error:
                if (data.error != null)
                {
                    Swal.fire('Error', data.error, 'warning');
                } else {
                    //Recorremos el arreglo de clientes posición por posición:
                    for (var i = 0; i < data.length; i++) {     
                        clientes = data;             
                        //Agregamos un nuevo renglon a la tabla contenido
                        // sus respectivas columnas y valores:
                        contenido = contenido + '<tr>' +
                                '<td>' + clientes[i].numeroUnico + '</td>' +
                                '<td>' + clientes[i].persona.nombre + ' ' +
                                clientes[i].persona.apellidoPaterno + ' ' +
                                clientes[i].persona.apellidoMaterno + ' ' +
                                '</td>' +
                                '<td>' + clientes[i].persona.rfc + '</td>' +
                                '<td>' + clientes[i].persona.domicilio + '</td>' +
                                '<td>' + clientes[i].estatus + '</td>' +
                                '<td><a href="#" onclick="mostrarDetalle(' + clientes[i].id + ');"><i class="far fa-eye"></i></a>' + '</td>' +
                                '</tr>';
                    }
                    //Insertamos el contenido generado previamente dentro del cuerpo de la tabla:
                    $('#tbodyCliente').html(contenido);
                }
            });
}

function mostrarDetalle(idCliente) {

    var pos = buscarClientePorID(idCliente);
    var c = null;

    if (pos < 0)
        return;
    //Obtenemos el objeto cliente en la posición requerida:
    c = clientes[pos];

    //Llenamos los controles del formulariode detalle:
        //Llenamos los controles del formulariode detalle:
    $('#txtCodigoCliente').val(c.id);
    $('#txtCodigoPersona').val(c.persona.id);
    $('#txtCodigoUsuario').val(c.usuario.id);
        
    $('#txtNombre').val(c.persona.nombre);
    $('#txtApellidoPaterno').val(c.persona.apellidoPaterno);
    $('#txtApellidoMaterno').val(c.persona.apellidoMaterno);
    $('#txtDomicilio').val(c.persona.domicilio);
    $('#txtTelefono').val(c.persona.telefono);
    $('#txtRfc').val(c.persona.rfc);
    $('#cmbGenero').val(c.persona.genero);
    
    //Usuario
    $('#txtUsuario').val(c.usuario.nombreUsuario);
    $('#txtContrasenia').val(c.usuario.contrasenia);
    $('#txtRol').val(c.usuario.rol);

    //Cliente
    $('#txtCorreo').val(c.correo);
    $('#txtNumeroCliente').val(c.numeroUnico);
    
    $('#txtCodigoImagen').val(c.rutaFoto);
    $('#imgClienteFoto').prop('src','data:image/png;base64,' + c.foto);
    
  
    setDetalleClienteVisible(true);
}


//    setDetalleClienteVisible(true);
//}
function limpiarFormulario()
{
    $('#txtCodigoCliente').val('');
    $('#txtCodigoPersona').val('');
    $('#txtCodigoUsuario').val('');
    $('#txtRol').val('');
    $('#txtNumeroCliente').val('');
    $('#txtNombre').val('');
    $('#txtApellidoPaterno').val('');
    $('#txtApellidoMaterno').val('');
    $('#txtCorreo').val('');
    $('#txtTelefono').val('');
    $('#txtDomicilio').val('');
    $('#txtUsuario').val('');
    $('#txtContrasenia').val('');
    $('#txtRfc').val('');
    $('#txtCodigoImagen').val('');
    $('#imgClienteFoto').prop('src', 'media/img/avatar.png');
    var inputFile = document.getElementById('inputFileFoto');
    inputFile.value = '';
    $('#cmbGenero').val('');

}


function buscarClientePorID(id) {
    for (var i = 0; i < clientes.length; i++) {
        if (clientes[i].id === id) {
            return i;
        }
    }
    return -1;
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
            $("#imgClienteFoto").attr("src",fotoB64);
            $("#txtCodigoImagen").val(fotoB64.substring(22,fotoB64.length));
        };
        
        //Leemos el archivo que selecciono el usuario y lo convertimos en una cadena con la Base64
        reader.readAsDataURL(inputFile.files[0]);
    }
}



