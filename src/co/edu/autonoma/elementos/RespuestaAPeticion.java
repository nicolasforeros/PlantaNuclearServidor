/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.elementos;

/**
 *
 * @author nikof
 */
public class RespuestaAPeticion {
    boolean notificacion;
    String respuesta;

    public RespuestaAPeticion() {
        this.notificacion = false;
        this.respuesta = null;
    }

    public boolean isNotificacion() {
        return notificacion;
    }

    public void setNotificacion(boolean notificacion) {
        this.notificacion = notificacion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    
    
    
}
