/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.scd.myspa.model.Reservacion;
import com.scd.myspa.model.Servicio;
import com.systemComunity.myspa.controller.ControllerLogin;
import com.systemComunity.myspa.controller.ControllerReservacion;
import com.systemComunity.myspa.controller.ControllerServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author zende
 */
@Path("servicio")
public class RESTServicio extends Application {

    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("s") @DefaultValue("") String s) {
        String out = "";
        try {
            System.out.println(s);
            Gson gs = new Gson();
            Servicio sv = gs.fromJson(s, Servicio.class);
            ControllerServicio cs = new ControllerServicio();
            int r = cs.insert(sv);

            out = "{\"result\":\"Servicio " + r + " generado con éxito\"}";
        } catch (Exception ex) {
            ex.printStackTrace();

            out = "{\"result\":\"Se produjo un error al insertar el servicio, vuelva a intentarlo.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro, @QueryParam("token") String token) {
        String out = null;
        ControllerServicio cs = new ControllerServicio();
        ControllerLogin cl = new ControllerLogin();
        List<Servicio> servicios = null;
        Gson gson = new Gson();

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                servicios = cs.getAll(filtro);
                out = gson.toJson(servicios);

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
