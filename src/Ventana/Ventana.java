/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * 
 * Clase Ventana
 * Es la clase donde se corre el juego, se llaman a las clases de Graficos, MainPanelButtonsYLabels, PausaFrame.
 * Constituye la union de todos los elementos anteriores para formar una ventana
 * que se le muestre al jugador, acomoda cada uno de los elementos y los muestra en la pantalla.
 */

package Ventana;

import Elementos.MyFileManager;
import Panel.*;

import java.awt.event.*;

import javax.swing.*;

public class Ventana extends JFrame {
    private Graficos principal;
    private PausaFrame pausaFrame;
    private MainPanelButtonsYLabels panelButtonsYLabels;

    public Ventana(VentanaMenu menu, String usuario) throws Exception {
        this.setTitle("Bejeweled"); //Establecemos el titulo de la ventana
        this.setIconImage(new ImageIcon("Resources\\Image\\Icono_Diamante.png").getImage()); //Establecemos el icono de la ventana
        this.setResizable(false);

        principal = new Graficos();
        pausaFrame = new PausaFrame(this, principal, usuario, menu);
        panelButtonsYLabels = new MainPanelButtonsYLabels(pausaFrame, principal);

        this.add(pausaFrame);
        this.add(panelButtonsYLabels);
        this.add(principal);

        this.pack();
        this.setLocationRelativeTo(null); //!Si el parametro es null, hace que la ventana se ponga por defecto en el centro del monitor
        //?Esto permite que siempre se muestre en el centro de cualquier pantalla (ya que no todas las pantallas fisicas son del mismo tamano)

        this.addWindowListener(new WindowAdapter(){ //El programa principal termina cuando se cierre la ventana principal
            public void windowClosing(WindowEvent e){
                MyFileManager.escribirPuntos(Integer.parseInt(MainPanelButtonsYLabels.marcador.getText()),principal.getTiempoFormato(),usuario); //Obtiene los puntos y los escribe
                System.exit(0);
            }
        }); //Es lo mismo que this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setVisible(true);
        principal.cicloPrincipal();
    }
}