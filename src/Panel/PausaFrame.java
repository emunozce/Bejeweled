/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * 
 * Clase PausaFrame
 * 
 * Este Frame contiene un boton para continuar jugando que hace lo necesario
 * para que el juego continue.
 * Un boton para regresar la pantalla, esto cierra el juego, escribe los puntos en el archivo
 * y regresa a la ventana de MainMenu, mostrando todas las opciones iniciales
 * Finalmente el boton de Quitar guarda los puntos en el archivo, da la despedida y cierra toda la instancia de la aplicacion.
 * 
 */

package Panel;

import Elementos.MyFileManager;
import Ventana.VentanaMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Elementos.MouseButtonListener;

public class PausaFrame extends JInternalFrame {
    JButton despausar;
    JButton quitar;
    JButton regresarMenu;
    MainPanelButtonsYLabels panel;
    private ImageIcon depausarButtonImage = new ImageIcon("Resources\\Image\\button_continue.png");
    private ImageIcon regresarButtonImage = new ImageIcon("Resources\\Image\\button_return-menu.png");
    private ImageIcon quitarButtonImage = new ImageIcon("Resources\\Image\\button_quit_azul.png");

    public PausaFrame(JFrame ventana, Graficos principal, String usuario, VentanaMenu menu) {
        this.setBounds(100, 100, 440, 340);
        this.setLayout(null);
        this.setBackground(new Color(0f, 0f, 0f, 0.5f));
        this.setOpaque(false);
        inicializarButtons(ventana, principal, usuario, menu);
        this.setBorder(null);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setVisible(false);
    }

    private void inicializarButtons(JFrame ventana, Graficos principal, String usuario, VentanaMenu menu) {
        this.despausar = new JButton();
        this.despausar.setEnabled(true);
        this.despausar.setOpaque(false);
        this.despausar.setFocusPainted(false);
        this.despausar.setContentAreaFilled(false);
        this.despausar.setBorderPainted(false);
        this.despausar.setBounds(102, 30, 226, 76);
        this.despausar.setIcon(depausarButtonImage);
        this.despausar.addMouseListener(MouseButtonListener.crearEfectos(despausar));
        this.despausar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principal.pausa = false;
                principal.timer.renaudarContador();
                dejarDeMostar(true);
                getPanel().setVisible(true);
                getPanel().reanudarMusica();
            }
        });
        this.add(despausar);

        this.regresarMenu = new JButton();
        this.regresarMenu.setEnabled(true);
        this.regresarMenu.setOpaque(false);
        this.regresarMenu.setFocusPainted(false);
        this.regresarMenu.setBorderPainted(false);
        this.regresarMenu.setContentAreaFilled(false);
        this.regresarMenu.setBounds(102, 120, 226, 75);
        this.regresarMenu.setIcon(regresarButtonImage);
        this.regresarMenu.addMouseListener(MouseButtonListener.crearEfectos(regresarMenu));
        this.regresarMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principal.pausa = true;
                MyFileManager.escribirPuntos(Integer.parseInt(MainPanelButtonsYLabels.marcador.getText()),principal.getTiempoFormato(),usuario); //Obtiene los puntos y los escribe
                ventana.setVisible(false);
                ventana.setDefaultCloseOperation(EXIT_ON_CLOSE);
                menu.iniciaSonidoFondo();
                menu.cerrarJuego();
            }
        });
        this.add(regresarMenu);

        this.quitar = new JButton();
        this.quitar.setEnabled(true);
        this.quitar.setOpaque(false);
        this.quitar.setContentAreaFilled(false);
        this.quitar.setBorderPainted(false);
        this.quitar.setFocusPainted(false);
        this.quitar.setBounds(102, 210, 226, 75);
        this.quitar.setIcon(quitarButtonImage);
        this.quitar.addMouseListener(MouseButtonListener.crearEfectos(quitar));
        this.quitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFileManager.escribirPuntos(Integer.parseInt(MainPanelButtonsYLabels.marcador.getText()),principal.getTiempoFormato(),usuario); //Obtiene los puntos y los escribe
                MouseButtonListener.exitProgram();
            }
        });
        this.add(quitar);
    }

    public void obtenerPanel(MainPanelButtonsYLabels panel){
        this.panel = panel;
    }
    private void dejarDeMostar(boolean decision) {
        if(decision){
            setVisible(false);
        }
    }

    private MainPanelButtonsYLabels getPanel() {
        return panel;
    }
}