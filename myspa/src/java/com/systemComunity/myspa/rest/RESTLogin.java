package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.systemComunity.myspa.controller.ControllerLogin;
import com.scd.myspa.model.Empleado;
import com.scd.myspa.model.Usuario;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("login")
public class RESTLogin {
    
    @Path("validate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("nombreUsuario") String nombreUsuario, 
                          @FormParam("contrasenia") String contrasenia){
        String out = null;
        ControllerLogin cl = new ControllerLogin();
        Empleado em = null;
        try {
            em = cl.login(nombreUsuario, contrasenia);
            
            // Revisamos que tengamos una instancia de tipo empleado:
            if (em != null) {
                out= new Gson().toJson(em);
            }
            else{
                 out = "{\"error\":\"Datos de acceso incorectos. Intenta nuevamente o llama al Administrador\"}";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("out")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response out(@FormParam("id") @DefaultValue("0") String idUsuario) {
        String out = null;
        ControllerLogin cl = new ControllerLogin();
        Usuario u = new Usuario();
        
        try {
            u.setId(Integer.parseInt(idUsuario));
            cl.deleteToken(u);
            out = "{\"result\":\"OK\"}";
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Se generó un error inesperado en el cierre de sesión!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}