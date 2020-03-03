/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.redes;

import co.edu.autonoma.elementos.Notificaciones;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author nikof
 */
class SalidaNotificaciones extends Thread{
    
    private DataOutputStream out;
    private Notificaciones notificaciones;

    public SalidaNotificaciones(DataOutputStream out, Notificaciones notificaciones) {
        this.notificaciones = notificaciones;
        this.out = out;
    }
    
    @Override
    public void run(){
        while(true){
            
            String mensaje = this.notificaciones.getNotificacion();
            
            System.out.println("Notificacion al operario: " + mensaje);

            try{
                this.out.writeUTF(mensaje);

                this.out.flush();
            }catch(IOException ex){
                System.out.println("Error notificando " +ex.getMessage());
                break;
            }
        }
    }
    
}
