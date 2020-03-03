/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.redes;

import co.edu.autonoma.elementos.ControladorReactor;
import co.edu.autonoma.elementos.Notificaciones;
import co.edu.autonoma.elementos.RespuestaAPeticion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author nikof
 */
public class ConexionOperario extends Thread{
    private Socket clientSocket;
    private ControladorReactor control;
    private Notificaciones notificaciones;
    private DataInputStream in;
    
    private SalidaNotificaciones salidaDatos;
    
    public ConexionOperario(Socket clientSocket, ControladorReactor control, Notificaciones notificaciones){
        this.clientSocket = clientSocket;
        this.control = control;
        this.notificaciones = notificaciones;
        
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            
        } catch (IOException ex) {
            System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
        }
    }    
    
    @Override
    public void run(){
        
        String mensajeRecibido;
        DataOutputStream out=null;
        
        try{
            out = new DataOutputStream(clientSocket.getOutputStream());

            salidaDatos = new SalidaNotificaciones(out, notificaciones);
            salidaDatos.start();
        }catch(IOException e){
            System.out.println("Error al crear el stream de salida: " + e.getMessage());
        }
        
        while(true){
            
            try {
                System.out.println("Esperando informacion del operario: " + this.clientSocket.getInetAddress().getHostName());
                
                mensajeRecibido = in.readUTF();
                
                if(mensajeRecibido.equalsIgnoreCase("cerrar"))
                    break;
                
                System.out.println("Mensaje: " +mensajeRecibido +" del operario: " + this.clientSocket.getInetAddress().getHostName());
                
                RespuestaAPeticion respuesta = control.procesarMensaje(mensajeRecibido);
                
                if(respuesta!=null){
                    if(respuesta.isNotificacion()){
                        System.out.println("Enviando Notificacion");
                        notificaciones.setNotificacion(respuesta.getRespuesta());
                    }else{
                        //enviar al cliente no mas
                        System.out.println("Enviando mensaje al operario");
                        if(out!=null){
                            out.writeUTF(respuesta.getRespuesta());
                            out.flush();
                        }
                    }   
                }
                
            } catch (IOException ex) {
                System.out.println("Error de IO, en lectura " + ex.getMessage());
                break;
            }
        
        }
        
        try 
        {
            if(clientSocket != null)
                clientSocket.close();
        }
        catch (IOException e) 
        {
            System.out.println("Error cerrando la sesion con el operario " + clientSocket.getInetAddress().getHostName());
        }
            
    }
}
