/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.systemComunity.myspa.rest;

import com.google.gson.Gson;
import com.scd.myspa.model.Cliente;
import com.scd.myspa.model.Horario;
import com.scd.myspa.model.Reservacion;
import com.scd.myspa.model.Sala;
import com.systemComunity.myspa.controller.ControllerLogin;
import com.systemComunity.myspa.controller.ControllerReservacion;
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
 * @author eveli
 */
@Path("reservacion")
public class RESTReservacion {

    @Path("insert")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@QueryParam("estatus") int estatus,
            @QueryParam("idCliente") int idCliente,
            @QueryParam("idSala") int idSala,
            @QueryParam("fecha") String fecha,
            @QueryParam("idHorario") int idHorario,
            @QueryParam("token") String token) {

        ControllerReservacion cr = new ControllerReservacion();
        ControllerLogin cl = new ControllerLogin();
        Cliente c = new Cliente();
        Sala s = new Sala();
        Horario h = new Horario();
        Reservacion r = new Reservacion();
        String out = null;

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                c.setId(idCliente);
                s.setId(idSala);
                h.setId(idHorario);

                // Llenamos las propiedades de la reservacion:
                r.setEstatus(estatus);
                r.setCliente(c);
                r.setSala(s);
                r.setFecha(fecha);
                r.setHorario(h);

                cr.insert(r);
                out = "{\"result\":\"¡Reservación realizada con éxito!\"}";

            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"¡Se produjo un error al realizar la reservación. Intente más tarde!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("getAllHorarios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHorarios(@QueryParam("fecha") String fecha,
            @QueryParam("idSala") int idSala,
            @QueryParam("token") String token) {

        ControllerReservacion cr = new ControllerReservacion();
        ControllerLogin cl = new ControllerLogin();
        String out = null;
        List<Horario> horarios = null;

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                horarios = cr.getAllHorarios(fecha, idSala);
                out = new Gson().toJson(horarios);

            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Se produjo un error al cargar los horarios disponibles. Intente más tarde!\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro, @QueryParam("token") String token) {
        String out = null;
        ControllerReservacion cr = new ControllerReservacion();
        ControllerLogin cl = new ControllerLogin();
        List<Reservacion> reservaciones = null;
        Gson gson = new Gson();

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                reservaciones = cr.getAll(filtro);
                out = gson.toJson(reservaciones);

            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";

        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    @Path("getAllAtendida")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAtendida(@QueryParam("filtro") @DefaultValue("") String filtro, @QueryParam("token") String token) {
        String out = null;
        ControllerReservacion cr = new ControllerReservacion();
        ControllerLogin cl = new ControllerLogin();
        List<Reservacion> reservaciones = null;
        Gson gson = new Gson();

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                reservaciones = cr.getAllAtendidas(filtro);
                out = gson.toJson(reservaciones);

            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";

        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    @Path("getAllCancelada")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCancelada(@QueryParam("filtro") @DefaultValue("") String filtro, @QueryParam("token") String token) {
        String out = null;
        ControllerReservacion cr = new ControllerReservacion();
        ControllerLogin cl = new ControllerLogin();
        List<Reservacion> reservaciones = null;
        Gson gson = new Gson();

        try {
            if (!token.equals("") && cl.validateToken(token)) {
                reservaciones = cr.getAllCancelada(filtro);
                out = gson.toJson(reservaciones);

            } else {
                out = "{\"error\":\"¡Acceso no autorizado!\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"¡Ocurrió un error inesperado. Intenta nuevamente o llama al Administrador!\"}";

        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("delete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("idReservacion") @DefaultValue("0") int id) throws Exception {

        // Creamos un objeto de tipo ControllerProducto:
        ControllerReservacion cr = new ControllerReservacion();
        String out = null;

        try {

            if (id >= 1) {
                // Mandamos el parámetro de ID al método de DELETE:
                cr.delete(id);

                // Devolvemos como respuesta un result de que el registro se ha eliminado correctamente:
                out = "{\"result\":\"Movimiento realizado. Se cancelo de manera correcta el registro.\"}";
            } else {
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
