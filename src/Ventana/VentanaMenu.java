/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * 
 * Clase VentanaMenu
 * Esta clase une todos los elementos anteriores y crea la ventana que se le muestra al inicio al usuario
 * Contiene los botones:
 * 1. inicia juego (Start) que manda a llamar a un hilo de juego y oculta esta pantalla
 * 2. LogIn, manda un mensaje para poder ingresar el nombre de usuario, hace uso de MyFileManager para comprobar
 * si el usuario ya existe o si es necesario crear uno nuevo.
 * 3. High Scores, muestra un nuevo panel donde se ven los datos de los puntos más altos, contiene un boton para regresar al panel anterior.
 * Cada vez que se llama, actualiza el modelo de tabla para obtener los resultados actualizados.
 * 4. Salir llama a una funcion de MouseButtonListener para dar la despedida y cerrar todo el programa.
 */

package Ventana;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import Hilos.VentanaHilo;
import Elementos.MyFileManager;
import Elementos.MouseButtonListener;
import Elementos.Musica;

import Panel.*;

public class VentanaMenu extends JFrame {
    //private JTextField cuadroTexto;
    ImageIcon fondoMenu = new ImageIcon("Resources\\Image\\fondo1.jpg");
    ImageIcon usuario = new ImageIcon("Resources\\Image\\userLogIn.png");
    ImageIcon fondoHighScores = new ImageIcon("Resources\\Image\\fondo2.jpg");
    ImageIcon startButton = new ImageIcon("Resources\\Image\\button_start.png");
    ImageIcon logInButton = new ImageIcon("Resources\\Image\\button_log-in.png");
    ImageIcon highScoresButton = new ImageIcon("Resources\\Image\\button_high-scores.png");
    ImageIcon salirButton = new ImageIcon("Resources\\Image\\button_quit_morado.png");
    ImageIcon regresoButton = new ImageIcon("Resources\\Image\\button_return.png");
    Musica welcom = new Musica(new File("Resources\\Music\\voice_welcometobejeweled.wav"));
    Musica welcomeBack = new Musica(new File("Resources\\Music\\voice_welcomeback.wav"));
    Musica salida = new Musica(new File("Resources\\Music\\voice_goodbye.wav"));
    Musica fondo = new Musica(new File("Resources\\Music\\mainMenuMusic.wav"));
    Musica getReady = new Musica(new File("Resources\\Music\\voice_getready.wav"));
    Musica scores = new Musica(new File("Resources\\Music\\voice_spectacular.wav"));
    private JButton startGame;
    private JButton logIn;
    private JButton highScores;
    private JButton salir;
    private VentanaHilo juegoHilo;
    private Ventana juegoVentana;
    private JPanel panel;
    private JPanel panelHighScores;
    private JTable tabla;
    private JScrollPane muestraTabla;
    public static String [][] datos;
    public static final String [] nombreColumnas = {"Username", "Scores"};
    public String nombre;

    public VentanaMenu() throws Exception{
        this.setTitle("Bejeweled"); //Establecemos el titulo de la ventana
        this.setIconImage(new ImageIcon("Resources\\Image\\Icono_Diamante.png").getImage()); //Establecemos el icono de la ventana
        this.setSize(Graficos.ANCHO, Graficos.ALTO);
        this.setLayout(null);
        this.setResizable(false);
        nombre = "Anonimo";
        MyFileManager.escribirUsuario(nombre); //si no existe el usuario anonimo, lo crea en el archivo
        
        inicializarPanel();
        inicializarPanelHighScores();

        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter(){ //El programa principal termina cuando se cierre la ventana principal
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        }); //Es lo mismo que this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, "Iniciado como: "+ nombre, "Usuario", JOptionPane.PLAIN_MESSAGE, usuario);
        iniciaSonidoFondo();
    }

    private void inicializarPanel() {
        this.panel = new JPanel();
        this.panel.setSize(Graficos.ANCHO, Graficos.ALTO);
        this.panel.setLayout(null);
        this.panel.setOpaque(true);
        this.panel.setBackground(Color.BLACK);
        inicializarBotones();
        inicializarFondoLabel();
        this.panel.setVisible(true);
        this.add(panel);
    }

    private void inicializarPanelHighScores() {
        this.panelHighScores = new JPanel();
        this.panelHighScores.setSize(Graficos.ANCHO, Graficos.ALTO);
        this.panelHighScores.setLayout(null);
        this.panelHighScores.setOpaque(true);
        this.panelHighScores.setBackground(Color.BLACK);

        tabla = new JTable(){
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Usuario","Score","Tiempo"
            }
        ));
        tabla.setOpaque(false);
        muestraTabla = new JScrollPane(tabla);
        muestraTabla.setBounds(100, 180, 440, 183);
        muestraTabla.setVisible(true);
        muestraTabla.setOpaque(false);

        JButton regreso = new JButton();
        regreso.setEnabled(true);
        regreso.setOpaque(false);
        regreso.setContentAreaFilled(false);
        regreso.setFocusPainted(false);
        regreso.setBorderPainted(false);
        regreso.setBounds(350, 400, 226, 75);
        regreso.setIcon(regresoButton);
        regreso.addMouseListener(MouseButtonListener.crearEfectos(regreso));
        regreso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPanelHS().setVisible(false);
                getPanel().setVisible(true);
            }
        });
        this.panelHighScores.add(regreso);
        this.panelHighScores.add(muestraTabla);
        this.panelHighScores.setVisible(false);

        inicializarFondoHighScores();

        this.add(panelHighScores);
    }

    public void iniciaSonidoFondo(){
        fondo.setSonidoActivo(true);
    }

	private void inicializarBotones() {
        this.startGame = new JButton();
        this.startGame.setEnabled(true);
        this.startGame.setOpaque(false);
        this.startGame.setBorderPainted(false);
        this.startGame.setContentAreaFilled(false);
        this.startGame.setFocusPainted(false);
        this.startGame.setBounds(200, 140, 226, 75);
        this.startGame.setVisible(true);
        this.startGame.setIcon(startButton);
        this.startGame.addMouseListener(MouseButtonListener.crearEfectos(startGame));
        this.startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fondo.setSonidoActivo(false);
                getReady.setSonidoUnaVez(true);
                try {
                    getFrame().setVisible(false);
                    if(juegoVentana == null){
                        juegoHilo = new VentanaHilo(getFrame(), juegoVentana, nombre);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.panel.add(startGame);


        this.logIn = new JButton();
        this.logIn.setEnabled(true);
        this.logIn.setOpaque(false);
        this.logIn.setBorderPainted(false);
        this.logIn.setFocusPainted(false);
        this.logIn.setContentAreaFilled(false);
        this.logIn.setBounds(200, 225, 226, 75);
        this.logIn.setVisible(true);
        this.logIn.setIcon(logInButton);
        this.logIn.addMouseListener(MouseButtonListener.crearEfectos(logIn));
        this.logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombre = (String) JOptionPane.showInputDialog(null, "Ingrese el nombre del jugador", "Usuario", JOptionPane.PLAIN_MESSAGE , usuario, null, null);
                if(nombre!=null){
                    if(nombre.isEmpty()) nombre = "Anonimo";
                    if(MyFileManager.escribirUsuario(nombre)==-1){
                        welcom.setSonidoUnaVez(true);
                        JOptionPane.showMessageDialog(null, "Nuevo usuario: " + nombre + ", Bienvenido", "Usuario", JOptionPane.PLAIN_MESSAGE, usuario);
                    }else{
                        welcomeBack.setSonidoUnaVez(true);
                        JOptionPane.showMessageDialog(null, "Bienvenido de vuelta "+ nombre + "!", "Usuario", JOptionPane.PLAIN_MESSAGE, usuario);
                    }
                }else{
                    nombre = "Anonimo";
                    JOptionPane.showMessageDialog(null, "Iniciado como: "+ nombre + "!", "Usuario", JOptionPane.PLAIN_MESSAGE, usuario);
                }
            }
        });
        this.panel.add(logIn);

        this.highScores = new JButton();
        this.highScores.setEnabled(true);
        this.highScores.setOpaque(false);
        this.highScores.setFocusPainted(false);
        this.highScores.setBorderPainted(false);
        this.highScores.setContentAreaFilled(false);
        this.highScores.setBounds(200, 310, 226, 75);
        this.highScores.setVisible(true);
        this.highScores.setIcon(highScoresButton);
        this.highScores.addMouseListener(MouseButtonListener.crearEfectos(highScores));
        this.highScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scores.setSonidoUnaVez(true);
                editDatosTablaHighScores();
                /*tabla = null;
                tabla = new JTable(datos, nombreColumnas);
                tabla.setEnabled(false);
                tabla.setBounds(100, 100, 440, 160);
                tabla.setVisible(true);*/
                String [] columnas = new String[]{"Usuario","Score","Tiempo"};
                tabla.setModel(new DefaultTableModel(datos,columnas));
                panel.setVisible(false);
                panelHighScores.setVisible(true);
            }
        });
        this.panel.add(highScores);

        this.salir = new JButton();
        this.salir.setEnabled(true);
        this.salir.setBorderPainted(false);
        this.salir.setOpaque(false);
        this.salir.setFocusPainted(false);
        this.salir.setContentAreaFilled(false);
        this.salir.setBounds(200, 395, 226, 75);
        this.salir.setVisible(true);
        this.salir.setIcon(salirButton);
        this.salir.addMouseListener(MouseButtonListener.crearEfectos(salir));
        this.salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseButtonListener.exitProgram();
            }
        });
        this.panel.add(salir);
    }

    public void cerrarJuego(){
        juegoHilo = null;
        juegoVentana = null;
        this.setVisible(true);
    }

    private VentanaMenu getFrame() {
        return this;
    }

    private JPanel getPanel() {
        return this.panel;
    }

    public String getNombre() {
        return nombre;
    }

    public static void editDatosTablaHighScores() {
        datos = null;
        datos = new String [10][3];

        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[i].length; j++) {
                if(j == 0) {
                    try {
                        datos[i][j] =  MyFileManager.obtenerNombre(i);
                    }catch(Exception e) {
                        datos[i][j] = "---";
                    }
                }else if(j==1){
                    try {
                        datos[i][j] =  MyFileManager.obtenerPuntos(i);
                    }catch(Exception e) {
                        datos[i][j] = "---";
                    }
                }else if(j==2){
                    try{
                        datos[i][j] = MyFileManager.obtenerTiempo(i);
                    }catch(Exception e){
                        datos[i][j] = "---";
                    }
                }
            }
        }
        comprobarNull();
    }

    private static void comprobarNull() {
        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[i].length; j++) {
                if(datos[i][j] == null) {
                    datos[i][j] = "---";
                }
            }
        }
    }

    private JPanel getPanelHS() {
        return this.panelHighScores;
    }

    private void inicializarFondoLabel() {
        JLabel fondo = new JLabel();
        fondo.setBounds(0, 0, Graficos.ANCHO, Graficos.ALTO);
        fondo.setIcon(new ImageIcon(fondoMenu.getImage().getScaledInstance(fondo.getWidth(), fondo.getHeight(), Image.SCALE_SMOOTH))); //Se reescala la imagen al tamano del label);
        this.panel.add(fondo);
    }

    private void inicializarFondoHighScores() {
        JLabel fondo = new JLabel();
        fondo.setBounds(0, 0, Graficos.ANCHO, Graficos.ALTO);
        fondo.setIcon(new ImageIcon(fondoHighScores.getImage().getScaledInstance(fondo.getWidth(), fondo.getHeight(), Image.SCALE_SMOOTH))); //Se reescala la imagen al tamano del label);
        this.panelHighScores.add(fondo);
    }
}