package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
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
import com.systemComunity.myspa.controller.ControllerProducto;
import com.scd.myspa.model.Producto;

@Path("producto")
public class RESTProducto {
    
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("idProducto") @DefaultValue("0") int id,
                         @FormParam("nombre") @DefaultValue("") String nombre,
                         @FormParam("marca") @DefaultValue("") String marca,
                         @FormParam("precioUso") @DefaultValue("0") double precioUso) {
        
        // Creamos un objeto de tipo ControllerProducto:
        ControllerProducto cp = new ControllerProducto();
        String out = null;
        
        // Creamos un objeto de tipo Producto:
        Producto producto = new Producto();
        
        try {
            // Llenamos las propiedades del método Producto:
            producto.setId(id);
            producto.setNombre(nombre);
            producto.setMarca(marca);
            producto.setPrecioUso(precioUso);
            
            // Evaluamos si hacemos un INSERT o un UPDATE con base en el ID del producto:
            if (producto.getId() == 0) {
                cp.insert(producto);

            } 
            else{ 
                cp.update(producto);
            // Devolvemos como respuesta todos los datos del Producto:
            out = new Gson().toJson(producto);
            }
        } 
        catch (Exception e) {
            // Imprimimos el Error en la consola del servidor:
            e.printStackTrace();
            
           //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡No se ha seleccionado ningún registro de producto!\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idProducto") @DefaultValue("0") int id) throws Exception {
        
        // Creamos un objeto de tipo ControllerTratamiento:
        ControllerProducto cp = new ControllerProducto();
        String out = null;
        
        try {      
            if (id >= 1) {
                // Mandamos el parámetro de ID al método de DELETE:
                cp.delete(id);

                // Devolvemos como respuesta un result de que el registro se ha eliminado correctamente:
              out = "{\"result\":\"Movimiento realizado. Se eliminó de manera correcta el registro.\"}";
            }
            else{ 
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
            }
            
        } catch (Exception e) {
            // Imprimimos el Error en la consola del servidor:
            e.printStackTrace();
            
            //Devolvemos una descripcion del Error:
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) throws Exception{
        
        ControllerProducto cp = new ControllerProducto();
        List<Producto> productos = null;
        String out = null;
        
        try {
            productos = cp.getAll(filtro);
            out = new Gson().toJson(productos);
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
}