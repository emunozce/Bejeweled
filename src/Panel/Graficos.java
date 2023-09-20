/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * Clase Graficos
 * Maneja todos los graficos del juego, desde el tablero, mostrar el timer, musica del juego
 * y evidentemente las gemas del tablero.
 * Contiene la otra parte de la logica para poder eliminar Gemas o construir Gemas especiales.
 * Logica:
 * 1. Recorre toda la matriz de gemas
 * 2. Durante el recorrido manda a llamar al Scanner para que analice si hay gemas de 3 o más 
 * adjuntas para setear su id en 0, una vez detecta sus id en 0 llama a eliminar la gema y bajar
 * las que esten arriba de ella mediante una baja recursiva.
 * 3. Una vez finaliza el proceso inicial, comienza el juego
 * 4. Durante el juego detecta que casillas presiono el usuario y si son conjuntas comprueba si se puede
 * lograr el movimiento para eliminar gemas, en caso contrario retorna su gema a su posicion inicial.
 * 5. Si la eliminacion es posible, lee las gemas presionadas y las analiza para comenzar a setear id's en ceros
 * 6. Despues recorre toda la matriz en busca de otras gemas que puedan ser eliminadas debido a la baja recursiva
 * miestras siga existiendo bajas, la funcion se llamará a si misma hasta que no detecte que existan más movimientos.
 * 7. Se pinta todo el cuadro con las imagenes y moficaciones in game del juego. 
 */

package Panel;

//timer reloj
import Elementos.*;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import Hilos.*;

public class Graficos extends JComponent {
    // Atributos
    private final ImageIcon FONDO_ARBOL = new ImageIcon("Resources\\Image\\Fondo_Espiral_Verde.jpg");
    public final static int ALTO = 540, ANCHO = 640; // Alto y ancho del componente (lienzo), sera del tamano de la
                                                     // ventana (JFrame) principal
    public final static int DIMENSION_CUADRADOS_TABLERO = 44;
    public final static int LUGAR_INICIO_TABLERO_X = DIMENSION_CUADRADOS_TABLERO * 5,
            LUGAR_INICIO_TABLERO_Y = DIMENSION_CUADRADOS_TABLERO * 2;
    public final static int COLUMNAS_TABLERO = 9, FILAS_TABLERO = 9;
    public final static Font FUENTE_GENERAL = new Font("Segoe UI", Font.BOLD, 20);
    public final static float velocidadMovimiento = 40;
    private int coordenadaCursorX, coordenadaCursorY;
    private int columnaClickeada = -1, filaClickeada = -1; // Se inicializa en -1 para que no se coloree el contorno de
                                                           // blanco por defecto.
    private int columnaPorClickear, filaPorClickear, auxFilaClikeada, auxColumnaClickeada;
    protected boolean pausa;
    private boolean eliminacion;
    private boolean movimientoRetorno;
    protected Reloj timer;
    private RelojHilo tiempo;
    private Gemas[][] matrizGemas;
    private Gemas GemaAuxiliar;
    private Scanner scanner_gemas;
    private Musica sonidoNegacion, sonidoMoverGema, gemaPresionada, gemaDestruida, hiperCuboMusica;
    private Musica explosion = new Musica(new File("Resources\\Music\\bomb_explode.wav"));
    private Musica explosionCreada = new Musica(new File("Resources\\Music\\skull_appear.wav"));
    private Musica awesome = new Musica(new File("Resources\\Music\\voice_awesome.wav"));
    private Musica excellent = new Musica(new File("Resources\\Music\\voice_excellent.wav"));
    private Musica extraordinary = new Musica(new File("Resources\\Music\\voice_extraordinary.wav"));
    private static int puntos;

    private ImageIcon flechaIcon = new ImageIcon("Resources\\Image\\flecha.png");
    private ImageIcon explosionIcon = new ImageIcon("Resources\\Image\\explosion.png");

    // Metodos
    public Graficos() { // Constructor
        this.setPreferredSize(new Dimension(ANCHO, ALTO));
        this.inicializarGemas(); // Crea todas las gemas
        this.iniciaMouse(); // Inicia la lectura del mouse
        this.pausa = false; // Pausa del juego
        this.inicializarTimer(); // Inicializa el contador
        this.inicializaSonidos();
        this.setFocusable(true);
    }

    public static int getPuntos() {
        return puntos;
    }

    public String getTiempoFormato() {
        return timer.getTiempoFormato();
    }

    private void inicializaSonidos() {
        sonidoMoverGema = new Musica(new File("Resources\\Music\\gem_hit.wav"));
        sonidoNegacion = new Musica(new File("Resources\\Music\\badmove.wav"));
        gemaPresionada = new Musica(new File("Resources\\Music\\button_mouseover.wav"));
        gemaDestruida = new Musica(new File("Resources\\Music\\doubleset.wav"));
        hiperCuboMusica = new Musica(new File("Resources\\Music\\hypercube_create.wav"));
        sonidoMoverGema.setSonidoUnaVez(false);
        sonidoNegacion.setSonidoUnaVez(false);
        gemaPresionada.setSonidoUnaVez(false);
        gemaDestruida.setSonidoUnaVez(false);
        hiperCuboMusica.setSonidoUnaVez(false);
    }

    private void inicializarGemas() { // Crea todas las instancias de las gemas
        this.matrizGemas = new Gemas[FILAS_TABLERO][COLUMNAS_TABLERO];
        for (int i = 0; i < FILAS_TABLERO; i++) {
            for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                this.matrizGemas[i][j] = new Gemas();
                this.matrizGemas[i][j].setPosXNueva(j);
                this.matrizGemas[i][j].setPosYNueva(i);
                this.matrizGemas[i][j].setPosXAnterior(j);
                this.matrizGemas[i][j].setPosYAnterior(i);
            }
        }
        verificarGemasInicio(false);
        this.scanner_gemas = new Scanner(matrizGemas);
        // Llamar scanner
    }

    public void reiniciarGemas() {
        inicializarGemas();
    }

    private void verificarGemasInicio(boolean listo) {
        boolean verificar;
        Scanner inicio = new Scanner(matrizGemas);
        verificar = listo;
        if (verificar) {
            return;
        } else {
            verificar = true;
            for (int i = 0; i < FILAS_TABLERO; i++) {
                for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                    inicio.analizaGemaAntes(matrizGemas[i][j], null);
                    int abajo = inicio.getAbajo(), arriba = inicio.getArriba(), izquierda = inicio.getIzquierda(),
                            derecha = inicio.getDerecha();
                    if (matrizGemas[i][j].getId() == 0) {
                        bajaRecursiva(i, j);
                        int v = 1;
                        while (v <= abajo) {
                            bajaRecursiva(i + v, j);
                            v++;
                        }
                        v = 1;
                        while (v <= arriba) {
                            bajaRecursiva(i - v, j);
                            v++;
                        }
                        v = 1;
                        while (v <= derecha) {
                            bajaRecursiva(i, j + v);
                            v++;
                        }
                        v = 1;
                        while (v <= izquierda) {
                            bajaRecursiva(i, j - v);
                            v++;
                        }
                        verificar = false;
                    }
                    matrizGemas[i][j].quitImagenAnimada();
                }
            }
        }

        verificarGemasInicio(verificar);
        return;
    }

    private void verificarGemasJuego(boolean listo, Graphics g) {
        boolean verificar;
        Scanner inicio = new Scanner(matrizGemas);
        verificar = listo;
        if (verificar) {
            return;
        } else {
            verificar = true;
            for (int i = 0; i < FILAS_TABLERO; i++) {
                for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                    if (matrizGemas[i][j].getId() == 0) {
                        bajaRecursiva(i, j, g, true);
                        verificar = false;
                    } else {
                        puntos += ((inicio.analizaGemaAntes(matrizGemas[i][j], null, g, explosionCreada, explosion))
                                * 50);
                        int abajo = inicio.getAbajo(), arriba = inicio.getArriba(), izquierda = inicio.getIzquierda(),
                                derecha = inicio.getDerecha();
                        if (matrizGemas[i][j].getId() == 0) {
                            bajaRecursiva(i, j, g, true);
                            int v = 1;
                            while (v < abajo) {
                                bajaRecursiva(i + v, j, g, true);
                                v++;
                            }
                            v = 1;
                            while (v <= arriba) {
                                bajaRecursiva(i - v, j, g, true);
                                v++;
                            }
                            v = 1;
                            while (v <= derecha) {
                                bajaRecursiva(i, j + v, g, true);
                                v++;
                            }
                            v = 1;
                            while (v <= izquierda) {
                                bajaRecursiva(i, j - v, g, true);
                                v++;
                            }
                            verificar = false;
                        }
                    }
                    matrizGemas[i][j].quitImagenAnimada();
                }
            }
        }
        verificarGemasJuego(verificar, g);
        return;
    }

    private void verificarGemasSeleccionadas(Graphics g) {
        Scanner inicio = new Scanner(matrizGemas);
        if (matrizGemas[auxFilaClikeada][auxColumnaClickeada].getId() == 15
                || matrizGemas[filaClickeada][columnaClickeada].getId() == 15) {
            int idEliminar = 0;
            puntos += 50;
            if (matrizGemas[auxFilaClikeada][auxColumnaClickeada].getId() != 15) {
                idEliminar = matrizGemas[auxFilaClikeada][auxColumnaClickeada].getId();
                matrizGemas[filaClickeada][columnaClickeada].setId(0);
                bajaRecursiva(filaClickeada, columnaClickeada, g, true);
            } else if (matrizGemas[filaClickeada][columnaClickeada].getId() != 15) {
                idEliminar = matrizGemas[filaClickeada][columnaClickeada].getId();
                matrizGemas[auxFilaClikeada][auxColumnaClickeada].setId(0);
                bajaRecursiva(auxFilaClikeada, auxColumnaClickeada, g, true);
            } else {
                matrizGemas[auxFilaClikeada][auxColumnaClickeada].setId(0);
                matrizGemas[filaClickeada][columnaClickeada].setId(0);
                bajaRecursiva(filaClickeada, columnaClickeada, g, true);
                bajaRecursiva(auxFilaClikeada, auxColumnaClickeada, g, true);
                idEliminar = 15;
            }
            for (int i = 0; i < FILAS_TABLERO; i++) {
                for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                    if ((matrizGemas[i][j].getId() == idEliminar || matrizGemas[i][j].getId() == (idEliminar + 7)
                            || matrizGemas[i][j].getId() == (idEliminar - 7)) && idEliminar != 0) {
                        if (matrizGemas[i][j].getId() > 7) {
                            puntos += (new Scanner(matrizGemas).eliminarGemaEspecial(matrizGemas[i][j], g) * 50);
                            explosion.setSonidoUnaVez(true);
                        }
                        matrizGemas[i][j].setId(0);
                        g.drawImage(explosionIcon.getImage(), matrizGemas[i][j].getCoordenadaX_Inicio_Cuadrado(),
                                matrizGemas[i][j].getCoordenadaY_Inicio_Cuadrado(), null);
                        bajaRecursiva(i, j, g, true);
                        puntos += 50;
                    }
                }
            }

        } else {
            if (scanner_gemas.analizaGema(matrizGemas[auxFilaClikeada][auxColumnaClickeada])) {
                puntos += ((inicio.analizaGemaAntes(matrizGemas[auxFilaClikeada][auxColumnaClickeada], null, g,
                        explosionCreada, explosion)) * 50);

                int abajo = inicio.getAbajo(), arriba = inicio.getArriba(), izquierda = inicio.getIzquierda(),
                        derecha = inicio.getDerecha();
                if (matrizGemas[auxFilaClikeada][auxColumnaClickeada].getId() == 0) {
                    bajaRecursiva(auxFilaClikeada, auxColumnaClickeada, g, true);
                    int v = 1;
                    while (v < abajo) {
                        bajaRecursiva(auxFilaClikeada + v, auxColumnaClickeada, g, true);
                        v++;
                    }
                    v = 1;
                    while (v <= arriba) {
                        bajaRecursiva(auxFilaClikeada - v, auxColumnaClickeada, g, true);
                        v++;
                    }
                    v = 1;
                    while (v <= derecha) {
                        bajaRecursiva(auxFilaClikeada, auxColumnaClickeada + v, g, true);
                        v++;
                    }
                    v = 1;
                    while (v <= izquierda) {
                        bajaRecursiva(auxFilaClikeada, auxColumnaClickeada - v, g, true);
                        v++;
                    }
                }
            }
            if (scanner_gemas.analizaGema(matrizGemas[filaClickeada][columnaClickeada])) {
                puntos += ((inicio.analizaGemaAntes(matrizGemas[filaClickeada][columnaClickeada], null, g,
                        explosionCreada, explosion)) * 50);
                int abajo = inicio.getAbajo(), arriba = inicio.getArriba(), izquierda = inicio.getIzquierda(),
                        derecha = inicio.getDerecha();
                if (matrizGemas[filaClickeada][columnaClickeada].getId() == 0) {
                    bajaRecursiva(filaClickeada, columnaClickeada, g, true);
                    int v = 1;
                    while (v < abajo) {
                        bajaRecursiva(filaClickeada + v, columnaClickeada, g, true);
                        v++;
                    }
                    v = 1;
                    while (v <= arriba) {
                        bajaRecursiva(filaClickeada - v, columnaClickeada, g, true);
                        v++;
                    }
                    v = 1;
                    while (v <= derecha) {
                        bajaRecursiva(filaClickeada, columnaClickeada + v, g, true);
                        v++;
                    }
                    v = 1;
                    while (v <= izquierda) {
                        bajaRecursiva(filaClickeada, columnaClickeada - v, g, true);
                        v++;
                    }
                }
            }
        }
        matrizGemas[filaClickeada][columnaClickeada].quitImagenAnimada();
        matrizGemas[auxFilaClikeada][auxColumnaClickeada].quitImagenAnimada();
    }

    public void iniciaMouse() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseMovimiento) {
                coordenadaCursorX = mouseMovimiento.getX();
                coordenadaCursorY = mouseMovimiento.getY();
                if (((coordenadaCursorX >= LUGAR_INICIO_TABLERO_X)
                        && (coordenadaCursorX <= (LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * 9))))
                        && ((coordenadaCursorY >= LUGAR_INICIO_TABLERO_Y)
                                && (coordenadaCursorY <= (LUGAR_INICIO_TABLERO_Y
                                        + (DIMENSION_CUADRADOS_TABLERO * 9))))) {
                    if (columnaPorClickear != getColumnaGema(coordenadaCursorX)
                            || filaPorClickear != getFilaGema(coordenadaCursorY)) {
                        columnaPorClickear = getColumnaGema(coordenadaCursorX);
                        filaPorClickear = getFilaGema(coordenadaCursorY);
                    }
                } else {
                    columnaPorClickear = -1;
                    filaPorClickear = -1;
                }
            }

            @Override
            public void mouseDragged(MouseEvent mouseArrastrado) {

            }

        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mousePulsado) {
                if (!eliminacion && !pausa && !movimientoRetorno) {
                    if ((((columnaPorClickear == columnaClickeada + 1 || columnaPorClickear == columnaClickeada - 1)
                            && filaPorClickear == filaClickeada)
                            || ((filaPorClickear == filaClickeada + 1 ||
                                    filaPorClickear == filaClickeada - 1)) && columnaPorClickear == columnaClickeada)
                            &&
                            (filaPorClickear != -1 && columnaPorClickear != -1 && filaClickeada != -1
                                    && columnaClickeada != -1)) {
                        eliminacion = false;
                        movimientoRetorno = false;
                        moverGema();
                        if (!scanner_gemas.analizaGema(matrizGemas[filaPorClickear][columnaPorClickear]) &&
                                !scanner_gemas.analizaGema(matrizGemas[filaClickeada][columnaClickeada])) {
                            if (matrizGemas[filaPorClickear][columnaPorClickear].getId() == 15
                                    || matrizGemas[filaClickeada][columnaClickeada].getId() == 15) {
                                auxColumnaClickeada = columnaClickeada;
                                auxFilaClikeada = filaClickeada;
                                eliminacion = true;
                                hiperCuboMusica.setSonidoUnaVez(true);
                            } else {
                                moverGema(); // Debe quitarse para la animacion!
                                sonidoNegacion.setSonidoUnaVez(true);
                            }

                            // movimientoRetorno = true;
                            // primeroListo = false;
                        } else {
                            if (matrizGemas[filaPorClickear][columnaPorClickear].getId() == 15
                                    || matrizGemas[filaClickeada][columnaClickeada].getId() == 15) {
                                hiperCuboMusica.setSonidoUnaVez(true);
                            }
                            eliminacion = true;
                            auxColumnaClickeada = columnaClickeada;
                            auxFilaClikeada = filaClickeada;
                            sonidoMoverGema.setSonidoUnaVez(true);
                        }
                        if (!movimientoRetorno) {
                            if (columnaClickeada != -1 && filaClickeada != -1) {
                                matrizGemas[filaClickeada][columnaClickeada].quitImagenAnimada();
                            }
                            filaClickeada = filaPorClickear;
                            columnaClickeada = columnaPorClickear;
                            if (columnaClickeada != -1 && filaClickeada != -1) {
                                matrizGemas[filaClickeada][columnaClickeada].setImagenAnimada();
                            }
                        }
                    } else {
                        if (columnaClickeada != -1 && filaClickeada != -1) {
                            matrizGemas[filaClickeada][columnaClickeada].quitImagenAnimada();
                        }
                        columnaClickeada = getColumnaGema(mousePulsado.getX());
                        filaClickeada = getFilaGema(mousePulsado.getY());
                        if (columnaClickeada != -1 && filaClickeada != -1) {
                            matrizGemas[filaClickeada][columnaClickeada].setImagenAnimada();
                            gemaPresionada.setSonidoUnaVez(true);
                        }
                    }
                }
            }
        });
    }

    public int getColumnaGema(int coordenadaCursorX) {
        for (int i = 0; i < FILAS_TABLERO; i++) {
            for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                if (coordenadaCursorX >= matrizGemas[i][j].getCoordenadaX_Inicio_Cuadrado()
                        && coordenadaCursorX <= matrizGemas[i][j].getCoordenadaX_Fin_Cuadrado()) {
                    return matrizGemas[i][j].getColumna_Actual_Gema();
                }
            }
        }
        return -1;
    }

    public int getFilaGema(int coordenadaCursorY) {
        for (int i = 0; i < FILAS_TABLERO; i++) {
            for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                if (coordenadaCursorY >= matrizGemas[i][j].getCoordenadaY_Inicio_Cuadrado()
                        && coordenadaCursorY <= matrizGemas[i][j].getCoordenadaY_Fin_Cuadrado()) {
                    return matrizGemas[i][j].getFila_Actual_Gema();
                }
            }
        }
        return -1;
    }

    public void moverGema() {
        GemaAuxiliar = matrizGemas[filaClickeada][columnaClickeada];
        matrizGemas[filaClickeada][columnaClickeada] = matrizGemas[filaPorClickear][columnaPorClickear];
        matrizGemas[filaPorClickear][columnaPorClickear] = GemaAuxiliar;
        matrizGemas[filaPorClickear][columnaPorClickear].setPosXAnterior(columnaClickeada);
        matrizGemas[filaPorClickear][columnaPorClickear].setPosYAnterior(filaClickeada);
        matrizGemas[filaPorClickear][columnaPorClickear].setPosXNueva(columnaPorClickear);
        matrizGemas[filaPorClickear][columnaPorClickear].setPosYNueva(filaPorClickear);
        matrizGemas[filaClickeada][columnaClickeada].setPosXAnterior(columnaPorClickear);
        matrizGemas[filaClickeada][columnaClickeada].setPosYAnterior(filaPorClickear);
        matrizGemas[filaClickeada][columnaClickeada].setPosXNueva(columnaClickeada);
        matrizGemas[filaClickeada][columnaClickeada].setPosYNueva(filaClickeada);
        if (movimientoRetorno) {
            matrizGemas[filaPorClickear][columnaPorClickear].setPosXNueva(columnaClickeada);
            matrizGemas[filaPorClickear][columnaPorClickear].setPosYNueva(filaClickeada);
            matrizGemas[filaClickeada][columnaClickeada].setPosXNueva(columnaPorClickear);
            matrizGemas[filaClickeada][columnaClickeada].setPosYNueva(filaPorClickear);
            filaClickeada = filaPorClickear;
            columnaClickeada = columnaPorClickear;
        }
    }

    private void crearTablero(Graphics g) {
        Color NegroTransparente = new Color(0.3f, 0.3f, 0.3f, .5f);
        Color GrisTransparente = new Color(0.7f, 0.7f, 0.7f, .5f);
        g.setColor(NegroTransparente);
        for (int i = 0; i < FILAS_TABLERO; i++) {
            for (int j = 0; j < COLUMNAS_TABLERO; j++) {
                // Dibuja el recuadro
                g.fillRect(LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * j), LUGAR_INICIO_TABLERO_Y +
                        (DIMENSION_CUADRADOS_TABLERO * i), DIMENSION_CUADRADOS_TABLERO, DIMENSION_CUADRADOS_TABLERO);
                // if(matrizGemas[i][j].getPosXNueva()!=matrizGemas[i][j].getPosXAnterior() ||
                // matrizGemas[i][j].getPosYNueva()!=matrizGemas[i][j].getPosYAnterior()){
                // movimientoGema(g,i,j);
                // }else{
                matrizGemas[i][j].setFilaYColumnaActual(i, j); // Obtiene valores de la matriz logica para el objeto en
                                                               // cuestion
                // Manda las posiciones en las que se encuentra la gema, se utilizara junto al
                // mouse listener para moverlas
                matrizGemas[i][j]
                        .setCoordenadas_Tablero_En_X(LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * j)); // Obtiene
                                                                                                                  // valores
                                                                                                                  // en
                                                                                                                  // x
                                                                                                                  // de
                                                                                                                  // la
                                                                                                                  // matriz
                                                                                                                  // de
                                                                                                                  // pixeles
                matrizGemas[i][j]
                        .setCoordenadas_Tablero_En_Y(LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * i)); // Obtiene
                                                                                                                  // valores
                                                                                                                  // en
                                                                                                                  // y
                                                                                                                  // de
                                                                                                                  // la
                                                                                                                  // matriz
                                                                                                                  // de
                                                                                                                  // pixeles
                // Dibuja la gema en el tablero
                matrizGemas[i][j].dibujarGemaEnTablero(g);
                // }

                if (g.getColor() == NegroTransparente) {
                    g.setColor(GrisTransparente);
                } else if (g.getColor() == GrisTransparente) {
                    g.setColor(NegroTransparente);
                }
            }
        }
        if (eliminacion) {
            puntos = 0;
            gemaDestruida.setSonidoUnaVez(true);
            verificarGemasSeleccionadas(g);
            verificarGemasJuego(false, g);
            verificarGemasJuego(false, g);
            if (puntos >= 600 && puntos < 1000) {
                awesome.setSonidoUnaVez(true);
            } else if (puntos >= 1000 && puntos < 2000) {
                excellent.setSonidoUnaVez(true);
            } else if (puntos >= 2000) {
                extraordinary.setSonidoUnaVez(true);
            }
            MainPanelButtonsYLabels.updateMarcador(getPuntos());
            matrizGemas[filaClickeada][columnaClickeada].setImagenAnimada();
        }
    }

    /*
     * public void movimientoGema(Graphics g, int i, int j){
     * int XPosInicial1 = LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosXAnterior());
     * int YPosInicial1 = LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosYAnterior());
     * int XPosInicial2 = LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosXNueva());
     * int YPosInicial2 = LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosYNueva());
     * if(matrizGemas[i][j].getPosXAnterior()<matrizGemas[i][j].getPosXNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2-(int)
     * aumentoMovimiento,YPosInicial2);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1+
     * (int)aumentoMovimiento,YPosInicial1);
     * }else
     * if(matrizGemas[i][j].getPosXAnterior()>matrizGemas[i][j].getPosXNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2+(int)
     * aumentoMovimiento,YPosInicial2);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1-(int)
     * aumentoMovimiento,YPosInicial1);
     * }
     * if(matrizGemas[i][j].getPosYAnterior()<matrizGemas[i][j].getPosYNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2,YPosInicial2
     * -(int)aumentoMovimiento);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1,YPosInicial1+
     * (int)aumentoMovimiento);
     * }else
     * if(matrizGemas[i][j].getPosYAnterior()<matrizGemas[i][j].getPosYNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2,YPosInicial2
     * +(int)aumentoMovimiento);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1,YPosInicial1-
     * (int)aumentoMovimiento);
     * }
     * if(movimientoRetorno && primeroListo){
     * XPosInicial1 = LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosXNueva());
     * YPosInicial1 = LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosYNueva());
     * XPosInicial2 = LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosXAnterior());
     * YPosInicial2 = LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO *
     * matrizGemas[i][j].getPosYAnterior());
     * if(matrizGemas[i][j].getPosXAnterior()<matrizGemas[i][j].getPosXNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2+(int)
     * aumentoMovimiento,YPosInicial2);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1-(int)
     * aumentoMovimiento,YPosInicial1);
     * }else
     * if(matrizGemas[i][j].getPosXAnterior()>matrizGemas[i][j].getPosXNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2-(int)
     * aumentoMovimiento,YPosInicial2);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1+(int)
     * aumentoMovimiento,YPosInicial1);
     * }
     * if(matrizGemas[i][j].getPosYAnterior()<matrizGemas[i][j].getPosYNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2,YPosInicial2
     * +(int)aumentoMovimiento);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1,YPosInicial1-
     * (int)aumentoMovimiento);
     * }else
     * if(matrizGemas[i][j].getPosYAnterior()<matrizGemas[i][j].getPosYNueva()){
     * matrizGemas[matrizGemas[i][j].getPosYAnterior()][matrizGemas[i][j].
     * getPosXAnterior()].dibujarGemaEnTableroMovimiento(g,XPosInicial2,YPosInicial2
     * -(int)aumentoMovimiento);
     * matrizGemas[i][j].dibujarGemaEnTableroMovimiento(g,XPosInicial1,YPosInicial1+
     * (int)aumentoMovimiento);
     * }
     * moverGema();
     * primeroListo = false;
     * movimientoRetorno = false;
     * }
     * }
     */

    /*
     * private boolean actualiza_movimiento(float dt){
     * aumentoMovimiento += velocidadMovimiento*dt;
     * if(aumentoMovimiento >= DIMENSION_CUADRADOS_TABLERO){
     * aumentoMovimiento = 0;
     * if(movimientoRetorno){
     * primeroListo = true;
     * }
     * return true;
     * }else{
     * return false;
     * }
     * }
     */

    private void bajaRecursiva(int i, int j) {
        if (i == 0) {
            matrizGemas[i][j] = new Gemas();
            matrizGemas[i][j].setPosYNueva(i);
            matrizGemas[i][j].setPosXNueva(j);
            matrizGemas[i][j].setPosYAnterior(i);
            matrizGemas[i][j].setPosXAnterior(j);
            matrizGemas[i][j].setCoordenadas_Tablero_En_X(LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * j)); // Obtiene
                                                                                                                       // valores
                                                                                                                       // en
                                                                                                                       // x
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // matriz
                                                                                                                       // de
                                                                                                                       // pixeles
            matrizGemas[i][j]
                    .setCoordenadas_Tablero_En_Y(LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * (i - 1))); // Obtiene
                                                                                                                    // valores
                                                                                                                    // en
                                                                                                                    // y
                                                                                                                    // de
                                                                                                                    // la
                                                                                                                    // matriz
                                                                                                                    // de
                                                                                                                    // pixeles
            return;
        } else {
            matrizGemas[i][j] = null;
            matrizGemas[i][j] = matrizGemas[i - 1][j];
            matrizGemas[i][j].setPosYNueva(i);
            matrizGemas[i][j].setPosXNueva(j);
            matrizGemas[i][j].setPosYAnterior(i);
            matrizGemas[i][j].setPosXAnterior(j);
            matrizGemas[i][j].setCoordenadas_Tablero_En_X(LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * j)); // Obtiene
                                                                                                                       // valores
                                                                                                                       // en
                                                                                                                       // x
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // matriz
                                                                                                                       // de
                                                                                                                       // pixeles
            matrizGemas[i][j].setCoordenadas_Tablero_En_Y(LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * i)); // Obtiene
                                                                                                                       // valores
                                                                                                                       // en
                                                                                                                       // y
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // matriz
                                                                                                                       // de
                                                                                                                       // pixeles
            bajaRecursiva(i - 1, j);
        }
        return;
    }

    private void bajaRecursiva(int i, int j, Graphics g, boolean first) {
        if (i == 0) {
            if (!first) {
                g.drawImage(flechaIcon.getImage(), matrizGemas[i][j].getCoordenadaX_Inicio_Cuadrado(),
                        matrizGemas[i][j].getCoordenadaY_Inicio_Cuadrado(), null);
            }
            matrizGemas[i][j] = new Gemas();
            matrizGemas[i][j].setPosYNueva(i);
            matrizGemas[i][j].setPosXNueva(j);
            matrizGemas[i][j].setPosYAnterior(i);
            matrizGemas[i][j].setPosXAnterior(j);
            matrizGemas[i][j].setCoordenadas_Tablero_En_X(LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * j)); // Obtiene
                                                                                                                       // valores
                                                                                                                       // en
                                                                                                                       // x
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // matriz
                                                                                                                       // de
                                                                                                                       // pixeles
            matrizGemas[i][j]
                    .setCoordenadas_Tablero_En_Y(LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * (i - 1))); // Obtiene
                                                                                                                    // valores
                                                                                                                    // en
                                                                                                                    // y
                                                                                                                    // de
                                                                                                                    // la
                                                                                                                    // matriz
                                                                                                                    // de
                                                                                                                    // pixeles
            matrizGemas[i][j].dibujarGemaEnTablero(g);
            return;
        } else {
            if (!first) {
                g.drawImage(flechaIcon.getImage(), matrizGemas[i][j].getCoordenadaX_Inicio_Cuadrado(),
                        matrizGemas[i][j].getCoordenadaY_Inicio_Cuadrado(), null);
            }
            matrizGemas[i][j] = null;
            matrizGemas[i][j] = matrizGemas[i - 1][j];
            matrizGemas[i][j].setPosYNueva(i);
            matrizGemas[i][j].setPosXNueva(j);
            matrizGemas[i][j].setPosYAnterior(i);
            matrizGemas[i][j].setPosXAnterior(j);
            matrizGemas[i][j].setCoordenadas_Tablero_En_X(LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * j)); // Obtiene
                                                                                                                       // valores
                                                                                                                       // en
                                                                                                                       // x
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // matriz
                                                                                                                       // de
                                                                                                                       // pixeles
            matrizGemas[i][j].setCoordenadas_Tablero_En_Y(LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * i)); // Obtiene
                                                                                                                       // valores
                                                                                                                       // en
                                                                                                                       // y
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // matriz
                                                                                                                       // de
                                                                                                                       // pixeles
            bajaRecursiva(i - 1, j, g, false);
        }
        return;
    }

    private void setBackgroundImage(Graphics g) {
        g.drawImage(FONDO_ARBOL.getImage(), 0, 0, ANCHO, ALTO, this);
    }

    private void pintarCuadroPorSeleccionar(Graphics g) {
        if (((coordenadaCursorX >= LUGAR_INICIO_TABLERO_X)
                && (coordenadaCursorX <= (LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * 9))))
                && ((coordenadaCursorY >= LUGAR_INICIO_TABLERO_Y)
                        && (coordenadaCursorY <= (LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * 9))))) {
            g.setColor(new Color(168, 2, 110));
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Inicio_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Inicio_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Fin_Cuadrado()
                            - (DIMENSION_CUADRADOS_TABLERO / 2),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Inicio_Cuadrado());

            g2.drawLine(matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Inicio_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Inicio_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Inicio_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Fin_Cuadrado()
                            - (DIMENSION_CUADRADOS_TABLERO / 2));

            g2.drawLine(matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Fin_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Fin_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Fin_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Inicio_Cuadrado()
                            + (DIMENSION_CUADRADOS_TABLERO / 2));

            g2.drawLine(matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Fin_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Fin_Cuadrado(),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaX_Inicio_Cuadrado()
                            + (DIMENSION_CUADRADOS_TABLERO / 2),
                    matrizGemas[filaPorClickear][columnaPorClickear].getCoordenadaY_Fin_Cuadrado());

        }
    }

    public void pintarCuadroSeleccionado(Graphics g) {
        if (((coordenadaCursorX >= LUGAR_INICIO_TABLERO_X)
                && (coordenadaCursorX <= (LUGAR_INICIO_TABLERO_X + (DIMENSION_CUADRADOS_TABLERO * 9))))
                && ((coordenadaCursorY >= LUGAR_INICIO_TABLERO_Y)
                        && (coordenadaCursorY <= (LUGAR_INICIO_TABLERO_Y + (DIMENSION_CUADRADOS_TABLERO * 9))))) {
            g.setColor(Color.WHITE);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            if (columnaClickeada != -1 && filaClickeada != -1) {
                g2.drawLine(matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Inicio_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Inicio_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Fin_Cuadrado()
                                - (DIMENSION_CUADRADOS_TABLERO / 2),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Inicio_Cuadrado());
                g2.drawLine(matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Inicio_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Inicio_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Inicio_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Fin_Cuadrado()
                                - (DIMENSION_CUADRADOS_TABLERO / 2));
                g2.drawLine(matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Fin_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Fin_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Fin_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Inicio_Cuadrado()
                                + (DIMENSION_CUADRADOS_TABLERO / 2));
                g2.drawLine(matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Fin_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Fin_Cuadrado(),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaX_Inicio_Cuadrado()
                                + (DIMENSION_CUADRADOS_TABLERO / 2),
                        matrizGemas[filaClickeada][columnaClickeada].getCoordenadaY_Fin_Cuadrado());
            }
        }
    }

    public void dibuja() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                paintImmediately(0, 0, ANCHO, ALTO);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) { // Lo que se incluya dentro, es dibujado.
        g.setFont(FUENTE_GENERAL);
        g.setColor(Color.BLACK);
        setBackgroundImage(g);
        if (pausa) {
            timer.pausarContador();
            timer.dibujar(g); // Pinta el reloj en pantalla
            crearTablero(g);
        } else {
            timer.dibujar(g); // Pinta el reloj en pantalla
            crearTablero(g);
            pintarCuadroPorSeleccionar(g);
            pintarCuadroSeleccionado(g);
        }
    }

    public void cicloPrincipal() throws Exception {
        while (true) {
            dibuja();
            if (eliminacion) {
                Thread.sleep(1000);
                eliminacion = false;
            }
            Thread.sleep(17);
        }
    }

    private void inicializarTimer() {
        timer = new Reloj(DIMENSION_CUADRADOS_TABLERO * 8, DIMENSION_CUADRADOS_TABLERO); // Crea el reloj y mostrará el
                                                                                         // tiempo en la posicion dada
        tiempo = new RelojHilo(timer); // Crea un hilo para el reloj
        tiempo.iniciar();
        timer.renaudarContador(); // Inicia el contador
    }
}