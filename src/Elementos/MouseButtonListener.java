/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * Clase MouseButtonListener
 * utilizado para crear listener de JButtons, sirve para que lo botones se hagan grandes o más pequeños
 * También include una función para salir del programa que reproduce una voz de despedida antes de salir.
 */

package Elementos;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.event.MouseInputListener;
import java.io.File;
import java.awt.*;


public class MouseButtonListener {
    public static MouseInputListener crearEfectos(JButton aConfigurar){
        ImageIcon savedIcon = (ImageIcon)aConfigurar.getIcon();
        Musica mouseEnteredMusic = new Musica(new File("Resources\\Music\\button_mouseover.wav"));
        Musica mouseExitMusic = new Musica(new File("Resources\\Music\\button_mouseleave.wav"));
        return new MouseInputListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                mouseEnteredMusic.setSonidoUnaVez(true);
                aConfigurar.setBounds(aConfigurar.getX()-15, aConfigurar.getY()-5, aConfigurar.getWidth()+30, aConfigurar.getHeight()+10);
                aConfigurar.setIcon(new ImageIcon(savedIcon.getImage().getScaledInstance(aConfigurar.getWidth(), aConfigurar.getHeight(), Image.SCALE_SMOOTH)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                mouseExitMusic.setSonidoUnaVez(true);
                aConfigurar.setBounds(aConfigurar.getX()+15, aConfigurar.getY()+5, aConfigurar.getWidth()-30, aConfigurar.getHeight()-10);
                aConfigurar.setIcon(savedIcon);
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                
            }
            
        };
    }

    public static void exitProgram(){
        Musica salida = new Musica(new File("Resources\\Music\\voice_goodbye.wav"));
        salida.setSonidoUnaVez(true);
        try{
            Thread.sleep(1200);
        }catch(Exception a){
            a.printStackTrace();
        }
        System.exit(0);
    }
}
