/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.elementos;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikof
 */
public class Notificaciones {
    
    private String notificacion;
    
    public Notificaciones(){
        
    }
    
    public synchronized String getNotificacion() {
        try {
            wait();
        } catch (InterruptedException ex) {
            System.out.println("Interrupcion en la espera de notificaciones");
        }
        
        return notificacion;
    }

    public synchronized void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
        notifyAll();
    }    
}
