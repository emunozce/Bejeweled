/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda 
 * Clase de gemas,
 * Almacena todos los datos necesarios de una gema, desde su posicion en la matriz
 * hasta su posicion inicial en pixeles y final.
 * Por igual tiene la imagen y una funcion para ser pintada mediante graficos.
 * Tiene multiples seters and getters.
 */

package Elementos;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import Panel.Graficos;

public class Gemas {
    private ImageIcon formaGema; //Corresponde a la imageIcon que se le asigna el objeto. (Se encuentra en la carpeta "Resources\\Images")
    private int id; //El id (del 1 al 7) sirve para elegir la imageIcon que se le asignara al objeto en la variable formaGema. Ademas de usarse en el scanner.
    private int coordenadaX_Inicio_Cuadrado, coordenadaX_Fin_Cuadrado; //Coordenadas en X del cuadrado en donde se encuentra la gema dentro de la matriz de pixeles
    private int coordenadaY_Inicio_Cuadrado, coordenadaY_Fin_Cuadrado; //Coordenadas en Y del cuadrado en el que se encuentra la gema dentro de la matriz de pixeles
    private int fila_Actual_Gema, columna_Actual_Gema; //Valores de la posicion de la gema en la matriz logica [fila][columna]
    private int posXAnterior, posYAnterior; //Usadas en scanner, indica las coordenadas (x, y) de la gema seleccionada antes de efectuar el swap de gemas
    private int posXNueva, posYNueva; //Usadas en scanner, indica las coordenadas (x, y) de la gema seleccionada despues de efectuar el swap de gemas

    public Gemas(){
        crearGema();
    }

    private void crearGema(){
        Random rand = new Random();
        id = rand.nextInt(1, 8);//Obtiene valores entre el 1 y 7
        switch (id) {
            case 1 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Roja.png");
            case 2 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Azul.png");
            case 3 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Naranja.png");
            case 4 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Morada.png");
            case 5 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Verde.png");
            case 6 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Blanca.png");
            case 7 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Amarilla.png");
        }
    }

    public void setId(int newId){
        this.id = newId;
    }

    public int getId(){
        return id;
    }

    public ImageIcon getImagenActual(){
        return formaGema;
    }

    public void setImagenAnimada(){
        switch(id){
            case 1 -> formaGema = new ImageIcon("Resources\\Image\\Roja_Animada.gif");
            case 2 -> formaGema = new ImageIcon("Resources\\Image\\Azul_Animada.gif");
            case 3 -> formaGema = new ImageIcon("Resources\\Image\\Naranja_Animada.gif");
            case 4 -> formaGema = new ImageIcon("Resources\\Image\\Morada_Animada.gif");
            case 5 -> formaGema = new ImageIcon("Resources\\Image\\Verde_Animada.gif");
            case 6 -> formaGema = new ImageIcon("Resources\\Image\\Blanca_Animada.gif");
            case 7 -> formaGema = new ImageIcon("Resources\\Image\\Amarilla_Animada.gif");
        }
    }

    public void quitImagenAnimada(){
        switch (id) {
            case 1 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Roja.png");
            case 2 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Azul.png");
            case 3 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Naranja.png");
            case 4 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Morada.png");
            case 5 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Verde.png");
            case 6 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Blanca.png");
            case 7 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Amarilla.png");
            case 8 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Roja_Especial.png");
            case 9 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Azul_Especial.png");
            case 10 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Naranja_Especial.png");
            case 11 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Morada_Especial.png");
            case 12 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Verde_Especial.png");
            case 13 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Blanca_Especial.png");
            case 14 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Amarilla_Especial.png");
        }
    }

    public void crearGemaEspecial(){
        this.setId(this.getId()+7);
        switch(id){
            case 8 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Roja_Especial.png");
            case 9 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Azul_Especial.png");
            case 10 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Naranja_Especial.png");
            case 11 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Morada_Especial.png");
            case 12 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Verde_Especial.png");
            case 13 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Blanca_Especial.png");
            case 14 -> formaGema = new ImageIcon("Resources\\Image\\Gema_Amarilla_Especial.png");
        }
    }

    public void crearGemaHiperCubo(){
        this.setId(15);
        formaGema = new ImageIcon("Resources\\Image\\HiperCubo_Animado.gif");
    }

    public void dibujarGemaEnTablero(Graphics g){
        g.drawImage(formaGema.getImage(), this.coordenadaX_Inicio_Cuadrado, this.coordenadaY_Inicio_Cuadrado,null);
    }

    public void dibujarGemaEnTableroMovimiento(Graphics g, int x, int y){
        g.drawImage(formaGema.getImage(), x, y,null);
    }

    public void setCoordenadas_Tablero_En_X(int coordenadaX_Inicio_Cuadrado){
        this.coordenadaX_Inicio_Cuadrado = coordenadaX_Inicio_Cuadrado;
        this.coordenadaX_Fin_Cuadrado = coordenadaX_Inicio_Cuadrado + Graficos.DIMENSION_CUADRADOS_TABLERO;
    }

    public int getCoordenadaX_Inicio_Cuadrado() {
        return coordenadaX_Inicio_Cuadrado;
    }

    public int getCoordenadaX_Fin_Cuadrado() {
        return coordenadaX_Fin_Cuadrado;
    }

    public void setCoordenadas_Tablero_En_Y(int coordenadaY_Inicio_Cuadrado) {
        this.coordenadaY_Inicio_Cuadrado = coordenadaY_Inicio_Cuadrado;
        this.coordenadaY_Fin_Cuadrado = coordenadaY_Inicio_Cuadrado + Graficos.DIMENSION_CUADRADOS_TABLERO;
    }

    public int getCoordenadaY_Inicio_Cuadrado() {
        return coordenadaY_Inicio_Cuadrado;
    }

    public int getCoordenadaY_Fin_Cuadrado() {
        return coordenadaY_Fin_Cuadrado;
    }

    public void setPosXAnterior(int posXAnterior) {
        this.posXAnterior = posXAnterior;
    }

    public int getPosXAnterior() {
        return posXAnterior;
    }

    public void setPosYAnterior(int posYAnterior) {
        this.posYAnterior = posYAnterior;
    }

    public int getPosYAnterior() {
        return posYAnterior;
    }

    public int getPosXNueva() {
        return posXNueva;
    }

    public void setPosXNueva(int posXNueva) {
        this.posXNueva = posXNueva;
    }

    public int getPosYNueva() {
        return posYNueva;
    }

    public void setPosYNueva(int posYNueva) {
        this.posYNueva = posYNueva;
    }

    public void setFilaYColumnaActual(int fila_En_Matriz, int columna_En_Matriz){
        this.fila_Actual_Gema = fila_En_Matriz;
        this.columna_Actual_Gema = columna_En_Matriz;
    }

    public int getFila_Actual_Gema() {
        return fila_Actual_Gema;
    }

    public int getColumna_Actual_Gema() {
        return columna_Actual_Gema;
    }
}