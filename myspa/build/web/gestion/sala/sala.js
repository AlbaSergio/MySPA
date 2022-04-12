var salas = [];
var sucursales=[];


function inicializarModulo() {
    setDetalleSalaVisible(false);
    consultarSucursales();
    
    // Agregamos un oyente que se dispara cuando seleccionamos una sucursal
    // desde el combobox de sucursales:
    $('#cmbSucursales').on('change',function(){        
        refrescarTabla();
    });
}

/*
 * Esta función nos sirve para mostrar y ocultar el área que contiene 
 * el detalle de una sala.
 * 
 * Si el valor es true, mostramos el area del detalle de la sala.
 * Si el valor es false, ocultamos el area del detalle de la sala.
 */
function setDetalleSalaVisible(valor) {
    if (valor) {
        $('#divTablaSala').removeClass("col-12");
        $('#divTablaSala').addClass("col-7");
        $('#divDetalleSala').show();
        
    } else {
        $('#divTablaSala').removeClass("col-7");
        $('#divTablaSala').addClass("col-12");
        $('#divDetalleSala').hide();
    }
}

function sanitizar(text, valid) {
    text = text.replaceAll("(", "");
    text = text.replaceAll(")", "");
    text = text.replaceAll("#", "");
    text = text.replaceAll("%", "");
    text = text.replaceAll("$", "");
    text = text.replaceAll("&", "");
    text = text.replaceAll("*", "");
    text = text.replaceAll("+", "");
    text = text.replaceAll("}", "");
    text = text.replaceAll("{", "");
    text = text.replaceAll("]", "");
    text = text.replaceAll("[", "");
    text = text.replaceAll("_", "");
    text = text.replaceAll("´", "");
    text = text.replaceAll("\"", "");
    text = text.replaceAll("/", "");
    text = text.replaceAll("“", "");
    text = text.replaceAll("¿", "");
    text = text.replaceAll("?", "");
    text = text.replaceAll("¡", "");
    text = text.replaceAll("!", "");
    text = text.replaceAll("'", "");
    text = text.replaceAll("”", "");
    text = text.replaceAll(":", "");
    text = text.replaceAll(";", "");
    text = text.replaceAll("-", "");
    text = text.replaceAll(".", "");
    
    valid; 
    
    switch(valid) {
        case 1:
            text = text.replaceAll(",", "");
            return text;
            break;
        
        case 2:          
            text = text.replaceAll(",,", ", ");
            return text;
            break;
    }
    return text;
}

function normalizar(text) {
    text = text.replaceAll(0, "");
    text = text.replaceAll(1, "");
    text = text.replaceAll(2, "");
    text = text.replaceAll(3, "");
    text = text.replaceAll(4, "");
    text = text.replaceAll(5, "");
    text = text.replaceAll(6, "");
    text = text.replaceAll(7, "");
    text = text.replaceAll(8, "");
    text = text.replaceAll(9, "");
    text = text.replaceAll("Á", "A");
    text = text.replaceAll("É", "E");
    text = text.replaceAll("Í", "I");
    text = text.replaceAll("Ó", "O");
    text = text.replaceAll("Ú", "U");
    text = text.replaceAll("á", "a");
    text = text.replaceAll("é", "e");
    text = text.replaceAll("í", "i");
    text = text.replaceAll("ó", "o");
    text = text.replaceAll("ú", "u");
    
    if (text.charAt(0).toUpperCase() === "A" || text.charAt(0).toUpperCase() === "B" || text.charAt(0).toUpperCase() === "C" ||
            text.charAt(0).toUpperCase() === "D" || text.charAt(0).toUpperCase() === "E" || text.charAt(0).toUpperCase() === "F" ||
            text.charAt(0).toUpperCase() === "G" || text.charAt(0).toUpperCase() === "H" || text.charAt(0).toUpperCase() === "I" ||
            text.charAt(0).toUpperCase() === "J" || text.charAt(0).toUpperCase() === "K" || text.charAt(0).toUpperCase() === "L" ||
            text.charAt(0).toUpperCase() === "M" || text.charAt(0).toUpperCase() === "N" || text.charAt(0).toUpperCase() === "Ñ" ||
            text.charAt(0).toUpperCase() === "O" || text.charAt(0).toUpperCase() === "P" || text.charAt(0).toUpperCase() === "Q" ||
            text.charAt(0).toUpperCase() === "R" || text.charAt(0).toUpperCase() === "S" || text.charAt(0).toUpperCase() === "T" ||
            text.charAt(0).toUpperCase() === "U" || text.charAt(0).toUpperCase() === "V" || text.charAt(0).toUpperCase() === "W" ||
            text.charAt(0).toUpperCase() === "X" || text.charAt(0).toUpperCase() === "Y" || text.charAt(0).toUpperCase() === "Z") {
        
        text = text.charAt(0).toUpperCase().concat(text.toLowerCase().substring(1, text.length));
    
    } 
    return text;
}

function limpiar(text, valid) {   
    text = sanitizar(text, valid);
    text = normalizar(text);
    
    return text;
}

function guardar() {    
     // Generamos un nuevo Objeto
    var sala = new Object();

    // Definimos sus propiedades y sus valores
    sala.id = 0;
    sala.nombre = limpiar($('#txtNombre').val(), 1);
    sala.descripcion= limpiar($('#txtDescripcion').val(), 2);
    sala.foto = $('#txtCodigoImagen').val();
    sala.rutaFoto =$('#txtCodigoImagen').val();
    sala.estatus = 1;
    sala.idSucursal= $('#cmbNombreSucursales').val();
    
    //Revisamos sin hay un ID previsto:
    if ($('#txtCodigoSala').val().length > 0) {//Mayor 
        sala.id = parseInt($('#txtCodigoSala').val());    
    }
    
    $.ajax({
        type: "POST",
        url: "api/sala/save",
        data: {
                idSala: sala.id,
                nombre: sala.nombre,
                descripcion: sala.descripcion,
                foto: sala.foto,
                rutaFoto: sala.rutaFoto,
                idSucursal: sala.idSucursal,
                token : sessionStorage.getItem("token")
              }

    }).done(function (data) {
        // Revisamos si hubo algún error
        if (data.error != null) {
            Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
        
        } else {
            refrescarTabla();
            sala = data;
            $('#txtCodigoSala').val(sala.id);
            Swal.fire('Movimiento realizado', 'Los datos de la sala se han guardado correctamente', 'success');
        }
    });
}

function eliminar() {
    // Declaramos la variable para obtener el ID de la sala a eliminar
    var id = 0;
    
    if ($('#txtCodigoSala').val().length > 0) {
        id = parseInt($('#txtCodigoSala').val());
        
        $.ajax({
            type: "POST",
            url: "api/sala/delete",
            data: {
                    idSala: id,
                    token : sessionStorage.getItem("token")
                  }

        }).done(function (data) {
            // Revisamos si hubo algún error
            if (data.error != null) {
                Swal.fire('¡¡¡ Alerta !!!', data.error, 'warning');
            
            } else {
                refrescarTabla();
                Swal.fire('Sala eliminada', data.result, 'success');
                limpiarFormulario();
            }
        });
        
    } else {
        Swal.fire('¡¡¡ Alerta !!!', '¡No se ha seleccionado ningún registro de sala para eliminarlo!', 'warning');
    }
}

function refrescarTabla() {
    var idSucursal=$('#cmbSucursales').val();
    
    // Esta variable contendrá el contenido HTML de la tabla
    var contenido = '';

    // Hacemos la peticion al servicio REST que nos consulta las sucursales y salas:
    $.ajax({
        type: "GET",
        url: "api/sala/getAllBySucursal",
        data: {
                idSucursal : idSucursal,
                token      : sessionStorage.getItem("token")
              }
    
    }).done(function (data) {
        //Revisamos si susedio algun error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');

        } else {
            salas = data;
            
            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < data.length; i++) {
                // Agregamos un nuevo renglón a la tabla contenido sus respectivas columnas y valores:
                contenido = contenido + '<tr>' +
                                        '<td>' + salas[i].id + '</td>' +
                                        '<td>' + salas[i].nombre + '</td>' +
                                        '<td >' + salas[i].descripcion + '</td>' +
                                        '<td >' + salas[i].estatus + '</td>' +
                                        '<td><a href="#" onclick="mostrarDetalleSala(' + salas[i].id + ');"><i class="text-info fas fa-eye"></i></a>' + '</td>' +
                                        '</tr>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#tbodySalas').html(contenido);
        }
    });
}

function consultarSucursales() {
    // Esta variable contendrá el contenido HTML del nombre de las sucursales.
    var contenido = '';

    // Hacemos la petición al servicio REST que nos consulta las sucursales:
    $.ajax({
        type: "GET",
        url: "api/sucursal/getAll",
        data  : {token : sessionStorage.getItem("token")}
        
    }).done(function (data) {
        // Revisamos si susedió algún error
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');
           
        } else {
            sucursales = data;
            
            // Recorreremos el arreglo de productos posición por posición:
            for (var i = 0; i < data.length; i++) {
                // Agregamos el valor en el combobox del respectivo valor del nombre de las sucursales:
                contenido = contenido + '<option value="' + sucursales[i].id + '">'+ sucursales[i].nombre+
                                        '</option>';
            }
            // Insertamos el contenido de la tabla dentro de su cuerpo de la tabla
            $('#cmbSucursales').html(contenido);
        }
    });
}

function mostrarDetalleSala(idSala) {
    // Buscamos la posición del empleado:
    var pos = buscarPosicionSalaPorID(idSala);
    
    // Llenamos los campos del formulario:
    $('#txtCodigoSala').val(salas[pos].id);
    $('#txtNombre').val(salas[pos].nombre);
    $('#txtDescripcion').val(salas[pos].descripcion);
    $('#cmbNombreSucursales').val(salas[pos].idSucursal);
    $('#txtCodigoImagen').val(salas[pos].rutaFoto);
    $('#imgSalaFoto').prop('src','data:image/png;base64,' + salas[pos].foto);
    setDetalleSalaVisible(true);
}

// Esta función sirve para buscar la posición de una sala dentro del arreglo de salas, con base en un ID especificado.
// El método devolverá la posición donde se encuentra el objeto con el ID coincidente. En caso de que no se encuentre un
// empleado con el ID especificado, la función devolverá el valor -1.
function buscarPosicionSalaPorID(id) {
    // Recorremos el arreglo posición por posición:
    for(var i = 0; i < salas.length; i++) {
        
        // Comparamos si el ID del empleado en la posición actual es el mismo que el buscado:
        if(salas[i].id === id) {
            return i;
        }
    }
    // Si llegamos a este punto significa que no encontramos un empleado con el ID especificado, 
    // en cuyo caso devolvemos el valor -1:
    return -1;
}

function cargarFotografia(){
    // Recuperamos el input de tipo File donde se selecciona la foto
    var inputFile = document.getElementById('inputFileFoto');
    
    // Revisamos que el usuario haya seleccionado un archivo:
    if (inputFile.files && inputFile.files[0]) {
        // Creamos el objeto que leerá la imagen
        var reader = new FileReader();
        
        // Agregamos un oyente al lector del archivo para que en cuanto el usuario cargue la imagen,
        // esta se lea y se convierta de forma automatica en una cadena de Base64:
        reader.onload = function (e){
            var fotoB64 = e.target.result;
            $("#imgSalaFoto").attr("src",fotoB64);
            $("#txtCodigoImagen").val(fotoB64.substring(22,fotoB64.length));
        };
        
        // Leemos el archivo que selecciono el usuario y lo convertimos en una cadena con la Base64
        reader.readAsDataURL(inputFile.files[0]);
    }
}

function limpiarFormulario() {
    $('#txtCodigoSala').val('');
    $('#txtidSucursal').val('');
    $('#txtNombre').val('');
    $('#txtDescripcion').val('');
    $('#imgSalaFoto').prop('src', 'media/img/salas/S_001.png');
    $('#txtCodigoImagen').val('');
    var inputFile = document.getElementById('inputFileFoto');
    inputFile.value = '';
}

function imprimir() {
    var print = document.getElementById('divTablaSala').innerHTML;
    var contenido = document.body.innerHTML;

    document.body.innerHTML = print;
    window.print();
    document.body.innerHTML = contenido;
}
