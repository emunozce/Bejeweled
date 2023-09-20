/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * 
 * Clase MainPanelButtonsYLabels
 * Esta clase crea el panel derecho del juego, es donde se encuentra un label de 
 * puntos, así como sus botones de pausa y shuffle
 * El boton de pausa hace lo necesario para pausar el juego de fondo y mostrar un nuevo panel
 * donde se observaran otras opciones. Estas se encuentran en PausaFrame.
 * El shuffle crea una nueva matriz para el ciclo principal que es el juego.
 * Los puntos muestran los puntos que lleva actualmente el jugardor.
 */

package Panel;

import javax.swing.*;

import Elementos.Musica;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Elementos.MouseButtonListener;

import java.io.*;

public class MainPanelButtonsYLabels extends JPanel {
    Musica musica;
    private ImageIcon pausaButtonImage = new ImageIcon("Resources\\Image\\button_pause.png");
    private ImageIcon shuffleButtonImage = new ImageIcon("Resources\\Image\\button_shuffle.png");

    public static JLabel marcador;
    
    public MainPanelButtonsYLabels(PausaFrame pausaFrame, Graficos principal) {
        this.setBounds(0, 0, (Graficos.ANCHO / 3),(Graficos.ALTO));
        this.setLayout(null);
        this.setBackground(new Color(0f, 0f, 0f, 0f));
        inicializaMusica();
        inicializarBotones(pausaFrame, principal);

        inicializarLabels();

        this.setVisible(true);
    }

    private void inicializaMusica(){
        musica = new Musica(new File("Resources\\Music\\OST.wav"));
        musica.setSonidoActivo(true);
    }

    protected void detenerMusica(){
        musica.setSonidoActivo(false);
    }

    protected void reanudarMusica(){
        musica.setSonidoActivo(true);
    }


    public void inicializarBotones(PausaFrame pausaFrame, Graficos principal) {
        JButton pausaButton = new JButton();
        pausaButton.setEnabled(true); //Habilita o deshabilita el boton para la interaccion
        pausaButton.setFocusPainted(false);
        pausaButton.setOpaque(false);
        pausaButton.setBorderPainted(false);
        pausaButton.setContentAreaFilled(false);
        pausaButton.setBounds(50, 250, 121, 60);
        pausaButton.setIcon(pausaButtonImage);
        pausaButton.addMouseListener(MouseButtonListener.crearEfectos(pausaButton));
        pausaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principal.pausa = true;
                getPanel().setVisible(false);
                getPanel().detenerMusica();
                pausaFrame.setVisible(true);
                pausaFrame.obtenerPanel(getPanel());
            }
        });
        this.add(pausaButton);
        JButton reiniciarGemas = new JButton();
        reiniciarGemas.setEnabled(true); //Habilita o deshabilita el boton para la interaccion
        reiniciarGemas.setFocusPainted(false);
        reiniciarGemas.setBounds(50, 390, 121, 60);
        reiniciarGemas.setOpaque(false);
        reiniciarGemas.setBorderPainted(false);
        reiniciarGemas.setContentAreaFilled(false);
        reiniciarGemas.setIcon(shuffleButtonImage);
        reiniciarGemas.addMouseListener(MouseButtonListener.crearEfectos(reiniciarGemas));
        reiniciarGemas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principal.reiniciarGemas();
            }
        });
        this.add(reiniciarGemas);
    }

    public void inicializarLabels() {
        JLabel puntaje = new JLabel("Puntaje", SwingConstants.CENTER);
        puntaje.setOpaque(true);
        puntaje.setBounds(50, 110, 120, 45);
        puntaje.setForeground(Color.BLACK);
        puntaje.setBackground(Color.GRAY);
        puntaje.setFont(Graficos.FUENTE_GENERAL);
        this.add(puntaje);

        marcador = new JLabel("0", SwingConstants.CENTER);
        marcador.setOpaque(true);
        marcador.setBounds(50, 150, 120, 30);
        marcador.setForeground(Color.BLACK); //Color de la letra
        marcador.setBackground(Color.ORANGE); //Color del background
        marcador.setFont(Graficos.FUENTE_GENERAL);  //Establecemos la fuente del texto, asi como estilo y tamano de letra
        this.add(marcador);
    }

    protected MainPanelButtonsYLabels getPanel() {
        return this;
    }

    public static void updateMarcador(int puntos) {
        marcador.setText(String.valueOf(Integer.parseInt(marcador.getText()) + Graficos.getPuntos()));
    }
}
