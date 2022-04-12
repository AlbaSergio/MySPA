function correoEnviado(){
    //Mostramos un mensaje al usuario;
 const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    confirmButton: 'btn btn-success mx-4',
    cancelButton: 'btn btn-danger mx-4'
  },
  buttonsStyling: false
})

swalWithBootstrapButtons.fire({
  title: 'El mensaje ser&aacute; enviado',
  text: "¿Estás seguro?",
  icon: 'warning',
  showCancelButton: true,
  confirmButtonText: 'Si',
  cancelButtonText: 'No',
  reverseButtons: true
}).then((result) => {
  if (result.isConfirmed) {
    swalWithBootstrapButtons.fire(
      'Enviado',
      'Tu correo se envi&oacute; con &eacute;xito',
      'success'
    )
  } else if (
    result.dismiss === Swal.DismissReason.cancel
  ) {
    swalWithBootstrapButtons.fire(
      'Envi&oacute; cancelado',
      'Tu correo no se envi&oacute;',
      'error'
    )
  }
})
}


