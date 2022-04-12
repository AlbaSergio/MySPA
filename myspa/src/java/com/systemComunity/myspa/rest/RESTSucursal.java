package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.systemComunity.myspa.controller.ControllerLogin;
import com.systemComunity.myspa.controller.ControllerSucursal;
import com.scd.myspa.model.Sucursal;
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

@Path("sucursal")
public class RESTSucursal {    
    
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save (@FormParam("idSucursal") @DefaultValue("0") int id,
                         @FormParam("nombre") @DefaultValue("") String nombre,
                         @FormParam("domicilio") @DefaultValue("") String domicilio,
                         @FormParam("latitud") @DefaultValue("0") double latitud,
                         @FormParam("longitud") @DefaultValue("0") double longitud){
        String out = null;
        ControllerSucursal cs = new ControllerSucursal();//Creamos un objeto de tipo controller Sucursal
        Sucursal sucursal = new Sucursal(); //Creamos un objeto de tipo sucursal
        try
        {
            //LLenamos las propiedades 
            sucursal.setDomicilio(domicilio);
            sucursal.setId(id);
            sucursal.setLatitud(latitud);
            sucursal.setLongitud(longitud);
            sucursal.setNombre(nombre);
            
            //Evaluamos si hacemos un INSERT o un UPDATE 
            //Con base en el ID de la sucursal 
            if (sucursal.getId() == 0) {
                cs.insert(sucursal);
               
            }
            else
                cs.update(sucursal);
            
            //Devolvemos como respuesta Todos los datos de la sucursal
            out = new Gson().toJson(sucursal);
        }
        catch(Exception e)
        {
            //Imprimimos  el Error en la consola de servidor
            e.printStackTrace();
            
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡No se recibieron datos de la sucursal para guardar!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idSucursal") @DefaultValue("0") int id) throws Exception {
        ControllerSucursal cs = new ControllerSucursal();
        String out = null;
        try { 
            if(id >= 1){
            cs.delete(id);
            out = "{\"result\":\"Movimiento realizado. Se elimino de manera correcta el registro.\"}";
            }
            else{ 
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡No se ha seleccionado ningún registro de sucursal para eliminarlo!.\"}";
            }
            
        } catch(Exception e)
        {
            //Imprimimos  el Error en la consola de servidor
            e.printStackTrace();
            
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro, @QueryParam("token") String token) throws Exception {
        ControllerSucursal cs = new ControllerSucursal();
        ControllerLogin cl = new ControllerLogin();
        List<Sucursal> sucursales = null;
        String out = null;

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                sucursales = cs.getAll(filtro);
                out = new Gson().toJson(sucursales);
                
            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"" + e.toString() + "\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
