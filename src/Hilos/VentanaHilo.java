/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * 
 * Clase VentanaHilo
 * 
 * Hace el manejo de la ventana del juego para que corra en un 
 * hilo distinto al de la ventana menu, esto con el objetivo
 * de que la funcion paint pueda ser invocada en otra ventana.
 * Crea una instancia de nuevo Juego.
 */

package Hilos;

import Ventana.*;

public class VentanaHilo extends Thread {
    private Ventana ventanaGraficos;
    private String usuario;
    private VentanaMenu menu;

    public VentanaHilo(VentanaMenu menu, Ventana ventanaGraficos, String nombre){
        this.ventanaGraficos = ventanaGraficos;
        this.usuario = nombre;
        this.menu = menu;
        start();
    }

    @Override
    public void run(){
        if(ventanaGraficos == null){
            try{
                ventanaGraficos = new Ventana(menu, usuario);
            }catch (Exception e){}
        }
    }
}