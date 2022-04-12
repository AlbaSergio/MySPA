package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.systemComunity.myspa.controller.ControllerEmpleado;
import com.scd.myspa.model.Empleado;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("empleado")
public class RESTEmpleado {
  
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("empleado") @DefaultValue("{}") String jsonEmpleado) {
        String out = null;
        ControllerEmpleado ce = new ControllerEmpleado();
        Empleado empleado = null;
        
        try {
            if (jsonEmpleado == null || jsonEmpleado.equals("{}")) {
                out = "{\"error\":\"¡No se recibieron datos del cliente para guardar!\"}";
                
            } else {
                empleado = new Gson().fromJson(jsonEmpleado, Empleado.class);
                if (empleado.getFoto() == null) {
                    empleado.setFoto("");
                }
                if (empleado.getRutaFoto() == null) {
                    empleado.setRutaFoto("");
                }
                if (empleado.getId() == 0) {
                    ce.insert(empleado);
                } else {
                    ce.update(empleado);
                }
                out = new Gson().toJson(empleado);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡No se ha seleccionado ningún registro de empleado!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("id") @DefaultValue("0") int idEmpleado) {
        String out = null;
        ControllerEmpleado ce = new ControllerEmpleado();
        try {
            ce.delete(idEmpleado);
            out = "{\"result\":\"Movimiento realizado. Se eliminó de manera correcta el registro.\"}";
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";

        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) throws Exception {
        String out = null;
        ControllerEmpleado ce = new ControllerEmpleado();
        List<Empleado> empleados = null;

        Gson gson = new Gson();

        try {
            empleados = ce.getAll(filtro);
            out = gson.toJson(empleados);

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";

        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
