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
public class Reactor {
    public static final int ESTADO_APAGADO = 0;
    public static final int ESTADO_ENCENDIDO = 1;
    public static final int ESTADO_DANADO = 2;
    
    private int estado;
    private double carga;

    public Reactor() {
        this.estado = Reactor.ESTADO_APAGADO;
        this.carga = 0;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getCarga() {
        return carga;
    }

    public void setCarga(double carga) {
        this.carga = carga;
    }
    
    public void cargar(double carga){
        this.carga += carga;
    }
}
