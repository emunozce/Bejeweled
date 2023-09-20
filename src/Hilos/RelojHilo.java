/**Gerardo Femat Delgado ID: 244371
 * al 10/04/2022
 * Clase RelojHilo
 * Es el hilo que contantemente se ejecuta esperando
 * que deba contar tiempo el reloj.
 */


package Hilos;
import Elementos.Reloj;

public class RelojHilo extends Thread{
    private Reloj timer;

    public RelojHilo(Reloj timer){
        this.timer = timer;
    }

    public void iniciar(){
        this.start();
    }

    @Override
    public void run(){
        timer.contar();
    }
}
