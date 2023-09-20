/**Gerardo Femat Delgado ID: 244371
 * al 10/04/2022
 * Clase Reloj
 * Esta clase esta adaptada para ser utilizada 
 * por un hilo en su funcion principal de contar
 * tiene metodos para ser controlada desde cualquier
 * apartado donde este su variable, ya sea detener
 * el contador o volver a iniciarlo.
 * De forma predeterminada el reloj comienza detenido
 * y debe ser iniciado.
 */

package Elementos;
import java.awt.*;

public class Reloj {
    private long tiempoViejo, tiempoNuevo;
    private boolean estado;
    private long tiempo;
    private long minutos;
    private long segundos;
    private int x,y;


    public Reloj(int x, int y){
        estado = false;
        segundos = 0;
        this.x = x;
        this.y = y;
    }

    public void contar(){
        try{
            while(true){
                tiempoViejo = System.currentTimeMillis();
                while(estado){
                    tiempoNuevo = System.currentTimeMillis();
                    if((tiempoNuevo/1000) - (tiempoViejo/1000) >= 1){
                        tiempo++;
                        break;
                    }
                }
                synchronized (this){
                    while(!estado){
                        wait();
                    }
                    if(!estado) {
                        break;
                    }
                }
            }
        }catch(InterruptedException e){

        }
    }

    public synchronized void pausarContador(){
        estado = false;
        notify();
    }

    public synchronized void renaudarContador(){
        estado = true;
        notify();
    }

    public boolean isRunning(){
        return estado;
    }

    public void dibujar(Graphics g){
        g.setColor(Color.BLACK);
        if(tiempo>59){
            minutos = (long)(tiempo/60);
            segundos = (long)(tiempo - minutos*60);
        }else{
            segundos = tiempo;
        }
        g.drawString("Tiempo: " + String.format("%02d", minutos) + ":" + String.format("%02d", segundos), x, y);
    }

    public long getTiempo(){
        return tiempo;
    }

    public String getTiempoFormato(){
        String tiempoFormato="";
        if(tiempo>59){
            long min = (long)(tiempo/60);
            long seg = (long)(tiempo - (minutos*60));
            if(min<10){
                tiempoFormato += "0";
            }
            tiempoFormato += String.valueOf(min) + ":";
            if(seg<10){
                tiempoFormato += "0";
            }
            tiempoFormato += String.valueOf(seg);
        }else{
            tiempoFormato = "00:";
            if(tiempo<10){
                tiempoFormato+="0";
            }
            tiempoFormato += String.valueOf(tiempo);
        }
        return tiempoFormato;
    }

    public void setTiempo(long tiempo){
        this.tiempo = tiempo;
    }
}
