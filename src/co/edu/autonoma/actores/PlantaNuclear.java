/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.actores;

import co.edu.autonoma.elementos.ControladorReactor;
import co.edu.autonoma.elementos.Notificaciones;
import co.edu.autonoma.redes.ConexionOperario;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author nikof
 */
public class PlantaNuclear {
    
    public static void main(String[] args){
    
        int serverPort = 9090;
        Notificaciones notificaciones = new Notificaciones();
        ControladorReactor control = new ControladorReactor();

        try
        {
            System.out.println("SERVIDOR: binding port");

            ServerSocket listenSocket = new ServerSocket(serverPort);

            while(true) 
            {
                System.out.println("SERVIDOR: esperando a un cliente");

                Socket clientSocket = listenSocket.accept();

                System.out.println("SERVIDOR: cliente recibido " + clientSocket.getInetAddress().getHostName());

                ConexionOperario con = new ConexionOperario(clientSocket, control, notificaciones);

                con.start();
            }
        } catch(IOException e){
                System.out.println("Error connecting a client: " + e.getMessage());
        }
    
    }
}
