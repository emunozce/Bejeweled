/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * Clase Musica
 * Ampliamente utilizada durante todo el programa su utilidad es crear una forma sencilla
 * de crear archivos musicales con dos funciones, 
 * 1. Reproducir el archivo de forma indefinida hasta que se le quiera detener
 * 2. Reproducir un sonido una sola vez
 * Se le envia un File que almacene la ruta del sonido.
 */

package Elementos;
import javax.sound.sampled.*;
import java.io.*;

public class Musica{
    private Clip clip;
    private AudioInputStream audioIn;

    public Musica(File archivo){
        try{
            audioIn = AudioSystem.getAudioInputStream(archivo);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void setSonidoActivo(boolean sonidoActivo) {
        if(sonidoActivo == false){
            clip.stop();
        }else{
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } 
    }

    public void setSonidoUnaVez(boolean sonidoActivo){
        if(sonidoActivo == false){
            clip.stop();
        }else{
            clip.setMicrosecondPosition(0);
            clip.start();
        } 
    }
}
