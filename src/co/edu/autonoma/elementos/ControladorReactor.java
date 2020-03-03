/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.elementos;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author nikof
 */
public class ControladorReactor {
    
    private static final String EXITO = "200";
    private static final String ERROR = "500";
    private static final String AVERIA = "300";
    
    private ArrayList<Reactor> reactores;
    
    private JSONParser parser;
    
    public ControladorReactor(){
        reactores = new ArrayList<>();
        parser = new JSONParser();
        
        reactores.add(new Reactor());
        reactores.add(new Reactor());
        reactores.add(new Reactor());
    }
    
    public RespuestaAPeticion procesarMensaje(String mensaje){
        
        JSONObject obj;
        String mensajeRespuesta=null;
        boolean esNotificacion = false;
        RespuestaAPeticion respuesta = new RespuestaAPeticion();
        String operacion;
        String resultado;
        
        try {
            obj = (JSONObject)parser.parse(mensaje);
        } catch (ParseException ex) {
            System.out.println("Error procesando el mensaje");
            return null;
        }
        
        operacion = (String)obj.get("operacion");
        
        if(operacion.trim().equalsIgnoreCase("chat")){
            mensajeRespuesta =  this.realizarMensajeChat(obj);
        }else{
            if(operacion.trim().equalsIgnoreCase("encender")){
                mensajeRespuesta = this.realizarMensajeEncender(obj);
            }else{
                if(operacion.trim().equalsIgnoreCase("apagar")){
                    mensajeRespuesta = this.realizarMensajeApagar(obj);
                }else{
                    if(operacion.trim().equalsIgnoreCase("apagar")){
                        mensajeRespuesta = this.realizarMensajeCargar(obj);
                    }else{
                        if(operacion.trim().equalsIgnoreCase("reparar")){
                            mensajeRespuesta = this.realizarMensajeReparar(obj);
                        }
                    }
                }
            }
        }
        
        if(mensajeRespuesta==null)
            return null;
    
        resultado = (String)obj.get("resultado");
        
        if(resultado!=null){
            esNotificacion = !resultado.equals(ControladorReactor.ERROR);
            respuesta.setRespuesta(mensajeRespuesta);
            respuesta.setNotificacion(esNotificacion);
            return respuesta;
        }
        
        return null;
    }
    
    private String encenderReactor(Reactor reactor){
        if(reactor.getEstado()==Reactor.ESTADO_APAGADO){
            reactor.setEstado(Reactor.ESTADO_ENCENDIDO);
            return EXITO;
        }else{
            return ERROR;
        }
    }
    
    private String apagarReactor(Reactor reactor){
        if(reactor.getEstado()==Reactor.ESTADO_ENCENDIDO){
            reactor.setEstado(Reactor.ESTADO_APAGADO);
            return EXITO;
        }else{
            return ERROR;
        }
    }
    
    private String cargarReactor(Reactor reactor, double valorACargar){
        if(valorACargar>0){
            if(reactor.getEstado()==Reactor.ESTADO_ENCENDIDO){
                reactor.cargar(valorACargar);
                double cargaReactor = reactor.getCarga();
                if(cargaReactor>100){
                    this.danarReactor(reactor);
                    return AVERIA;
                }else{
                    return EXITO;
                }
            }
        }
        
        return ERROR;
    }

    private void danarReactor(Reactor reactor) {
        reactor.setEstado(Reactor.ESTADO_DANADO);
    }
    
    private String repararReactor(Reactor reactor){
        if(reactor.getEstado()==Reactor.ESTADO_DANADO){
            reactor.setCarga(0);
            reactor.setEstado(Reactor.ESTADO_APAGADO);
            return EXITO;
        }else{
            return ERROR;
        }
    }
    
    public void addReactor(Reactor reactor){
        this.reactores.add(reactor);
    }

    private String realizarMensajeChat(JSONObject obj) {
        
        obj.put("resultado", ControladorReactor.EXITO);
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("Error realizando el mensaje de chat: " + ex.getMessage());
        }
        return null;
    }

    private String realizarMensajeEncender(JSONObject obj) {
        
        String reactorId = (String)obj.get("reactor");
        
        Reactor reactor = this.reactores.get(Integer.parseInt(reactorId));
        
        String resultado = this.encenderReactor(reactor);
        
        obj.put("resultado", resultado);
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("Error realizando el mensaje de encender: " + ex.getMessage());
        }
        return null;
    }

    private String realizarMensajeApagar(JSONObject obj) {

        String reactorId = (String)obj.get("reactor");
        
        Reactor reactor = this.reactores.get(Integer.parseInt(reactorId));
        
        String resultado = this.apagarReactor(reactor);
        
        obj.put("resultado", resultado);
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("Error realizando el mensaje de apagar: " + ex.getMessage());
        }
        return null;
    }

    private String realizarMensajeCargar(JSONObject obj) {
        String reactorId = (String)obj.get("reactor");
        
        Reactor reactor = this.reactores.get(Integer.parseInt(reactorId));
        
        double valorACargar = Double.parseDouble((String)obj.get("carga"));
        
        String resultado = this.cargarReactor(reactor,valorACargar);
        
        obj.put("resultado", resultado);
        obj.put("carga", reactor.getCarga());
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("Error realizando el mensaje de cargar: " + ex.getMessage());
        }
        return null;
    }

    private String realizarMensajeReparar(JSONObject obj) {
        
        String reactorId = (String)obj.get("reactor");
        
        Reactor reactor = this.reactores.get(Integer.parseInt(reactorId));
        
        String resultado = this.repararReactor(reactor);
        
        obj.put("resultado", resultado);
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("Error realizando el mensaje de reparar: " + ex.getMessage());
        }
        return null;
    }
}
