
function crearUsuarioCliente() {
    //Mostramos un mensaje al usuario;
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success mx-4',
            cancelButton: 'btn btn-danger mx-4'
        },
        buttonsStyling: false
    })

    swalWithBootstrapButtons.fire({
        title: '¿Estás seguro?',
        text: "",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si',
        cancelButtonText: 'No',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            swalWithBootstrapButtons.fire(
                    'Nuevo usuario creado',
                    'Bienvenido a MySPA',
                    'success'
                    )
        } else if (
                result.dismiss === Swal.DismissReason.cancel
                ) {
            swalWithBootstrapButtons.fire(
                    'Usuario no creado',
                    'Puedes volver a intentarlo',
                    'error'
                    )
        }
    })
}