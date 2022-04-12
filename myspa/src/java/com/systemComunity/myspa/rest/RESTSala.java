/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.systemComunity.myspa.controller.ControllerLogin;
import com.systemComunity.myspa.controller.ControllerSala;
import com.scd.myspa.model.Sala;
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
 */
@Path("sala")
public class RESTSala {

    @Path("save")
    @POST 
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("idSala") @DefaultValue("0") int id,
            @FormParam("nombre") @DefaultValue("") String nombre,
            @FormParam("descripcion") @DefaultValue("") String descripcion,
            @FormParam("foto") @DefaultValue("") String foto,
            @FormParam("rutaFoto") @DefaultValue("") String rutaFoto,
            @FormParam("idSucursal") @DefaultValue("0") int idSucursal,
            @FormParam("token") String token
            ) { //Insertar o actualizar un registro de sala
 
        String out = null;
        ControllerSala cs = new ControllerSala();
        ControllerLogin cl = new ControllerLogin();
        
        // Creamos un objeto de tipo Sala
        Sala salas = new Sala();
        
        try {
            if (!token.equals("") && cl.validateToken(token)) {
                // Llenamos las propiedades del objeto sucursal:
                salas.setId(id);
                salas.setNombre(nombre);
                salas.setDescripcion(descripcion);
                salas.setFoto(foto);
                salas.setRutaFoto(rutaFoto);
                salas.setIdSucursal(idSucursal);

                // Evaluamos si hacemos un INSERT o un UPDATE con base en
                // con base en el ID de la sucursal
                if (salas.getId() == 0) {
                    cs.insert(salas);
                } else {
                    cs.update(salas);
                }
                
                //Devolvemos como respuesta TODOS los datos de la sucursal
                out = new Gson().toJson(salas);
                
            } else {
                out = "{\"error\":\"¡Acceso no autorizado para esta acción!\"}";
            }
            
        } catch (Exception e) {
            //Imprimir el error en la consola del servidor:
            e.printStackTrace();

            //Devolvemos una descripcion del Error
            out = "{\"error\":\"¡No se recibieron datos de la sala para guardar!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idSala") @DefaultValue("0") int id, @FormParam("token") String token) {
        String out = null;
        ControllerSala cs = new ControllerSala();
        ControllerLogin cl = new ControllerLogin();

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                cs.delete(id);
                out = "{\"result\":\"Movimiento realizado. Se eliminó de manera correcta el registro.\"}";
            
            } else {
                out = "{\"error\":\"¡Acceso no autorizado para esta acción!\"}";
            }
                
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("getAllBySucursal") 
    @GET 
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getAllBySucursal(@QueryParam("filtro") @DefaultValue("") String filtro,
            @QueryParam("idSucursal") @DefaultValue("0") int idSucursal, @QueryParam("token") String token) {
        
        ControllerSala cs = new ControllerSala();
        ControllerLogin cl = new ControllerLogin();
        List<Sala> salas = null;
        String out = null;
        
        try {
            if (!token.equals("") && cl.validateToken(token)) {
                salas = cs.getAllBySucursal(filtro, idSucursal);
                out = new Gson().toJson(salas);
                
            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("getAll")
    @GET           
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro, @QueryParam("token") String token) {
        ControllerSala cs = new ControllerSala();
        ControllerLogin cl = new ControllerLogin();
        List<Sala> salas = null;
        String out = null;
        try {
            if (!token.equals("") && cl.validateToken(token)) {
                salas = cs.getAll(filtro);
                out = new Gson().toJson(salas);
                
            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}