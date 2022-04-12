
package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.scd.myspa.model.Tratamiento;
import com.systemComunity.myspa.controller.ControllerTratamiento;
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

/**
 *
 * @author DanielAbrahamSanchez
 */
@Path("tratamiento")
public class RESTTratamiento {
     @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) throws Exception {
        ControllerTratamiento ct = new ControllerTratamiento();
        List<Tratamiento> tratamientos = null;
        String out = null;
        try {
            tratamientos = ct.getAll(filtro);
            out = new Gson().toJson(tratamientos);
     }
     catch (Exception e)
             {
             e.printStackTrace();
             out = "{\"error\":\""+ e.toString() + "\"}";
             }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("idTratamiento") @DefaultValue("0") int id,
                         @FormParam("nombre") @DefaultValue("") String nombre,
                         @FormParam("costo") @DefaultValue("0") float costo,
                         @FormParam("descripcion") @DefaultValue("") String descripcion) 
    {
        
        // Creamos un objeto de tipo ControllerTratamiento:
        ControllerTratamiento ct = new ControllerTratamiento();
        String out = null;
        
        // Creamos un objeto de tipo Tratamiento:
        Tratamiento tratamiento = new Tratamiento();
        
        try {
            // Llenamos las propiedades del método Tratamiento:
            tratamiento.setId(id);
            tratamiento.setNombre(nombre);
            tratamiento.setCosto(costo);
            tratamiento.setDescripcion(descripcion);
            
            // Evaluamos si hacemos un INSERT o un UPDATE con base en el ID del Tratamiento:
            if (tratamiento.getId() == 0) {
                ct.insert(tratamiento);

            } 
            else{ 
                 ct.update(tratamiento);
            // Devolvemos como respuesta todos los datos del Tratamiento:
            out = new Gson().toJson(tratamiento);
            }
        } 
        catch (Exception e) {
            // Imprimimos el Error en la consola del servidor:
            e.printStackTrace();
            
           //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡No se ha seleccionado ningún registro de Tratamiento!.\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idTratamiento") @DefaultValue("0") int id) throws Exception {
        
        // Creamos un objeto de tipo ControllerTratamiento:
        ControllerTratamiento ct = new ControllerTratamiento();
        String out = null;
        
        try {      
            if (id >= 1) {
                // Mandamos el parámetro de ID al método de DELETE:
                ct.delete(id);

                // Devolvemos como respuesta un result de que el registro se ha eliminado correctamente:
              out = "{\"result\":\"Movimiento realizado. Se elimino de manera correcta el registro.\"}";
            }
            else{ 
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"Algo salió mal. Intenta nuevamente.\"}";
            
            }
            
        } catch (Exception e) {
            // Imprimimos el Error en la consola del servidor:
            e.printStackTrace();
            
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"Algo salió mal. Intenta nuevamente.\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
