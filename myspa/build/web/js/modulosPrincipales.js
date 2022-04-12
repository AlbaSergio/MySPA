function cargarModuloLogin() {
    $.ajax({
        context : document.body,
        url: "gestion/login/login.html"
  
    }).done(function(data){
    //  document.getElementById("contenedorPrincipal").innerHTML=data;
        $("#contenedorInicio").html(data);
    }); 
}

function cargarContacto() {
  $.ajax({
      context : document.body,
      url: "gestion/login/bienvenida/contacto.html"
  }).done(function(data){
  //  document.getElementById("contenedorPrincipal").innerHTML=data;
    $("#contenedorInicio").html(data);
  }); 
}

function cargarConocenos() {
  $.ajax({
      context : document.body,
      url: "gestion/login/bienvenida/acercaDe.html"
  }).done(function(data){
  //  document.getElementById("contenedorPrincipal").innerHTML=data;
    $("#contenedorInicio").html(data);
  }); 
}

function cargarCrearCuenta() {
    $.ajax({
        context: document.body,
        url: "gestion/login/crearUsuarioCliente.html"
    }).done(function (data) {
        //  document.getElementById("contenedorPrincipal").innerHTML=data;
        $("#contenedorInicio").html(data);
    });
}