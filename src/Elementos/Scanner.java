/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * Clase del scanner, se le envia un objeto gema y analiza si a su al rededor
 * al momento de ser cambiada de posicion tiene otras gemas iguales para
 * eliminarse o debe ser regresada a su posicion anterior.
 * 
 * También tiene la capacidad de analizar todas las posibilidades de las gemas especiales
 * contiene una gran cantidad de condiciones que deben ser analizadas detenidamente para
 * encontrar la logica, pero el objetivo basico es el siguiente:
 * 1. Obtiene la gema que va a analizar
 * 2. Lee arriba, abajo, derecha e izquierda de la gema para ver si hay gemas del mismo tipo.
 * 3. Si arriba y abajo o derecha e izquierda tienen más de dos coincidencias quiere decir que
 * hay suficientes gemas para hacer una eliminación.
 * 4. Si hay eliminación hay que detectar si alguna gema es especial, en ese caso se guardan en un arreglo
 * 5. Se hace id=0 a las gemas que se van a eliminar
 * 6. Si existieron gemas especiales, entonces recorre el array y hace que las gemas al rededor de esta exploten.
 * 7. Si alguna gema que explota también es especial, de forma recursiva explotará
 * 8. Una vez seteados los id, se regresará al apartados de graficos donde se terminará de hacer la logica.
 */

package Elementos;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.awt.*;

public class Scanner {
    Gemas gemaAnalizar;
    Gemas auxiliar;
    Gemas [][] matrizGemas;
    int posXGemaAnalizar, posYGemaAnalizar, id;
    int posXGemaEspecial, posYGemaEspecial;
    int arriba, abajo, derecha, izquierda;

    ArrayList<Gemas>gemasEspeciales;

    boolean existeGemaEspecial;

    boolean inicialEsEspecial;

    private int eliminadas;
    private ImageIcon explosionIcon = new ImageIcon("Resources\\Image\\explosion.png");

    public Scanner(Gemas [][] matrizGemas){
        this.matrizGemas = matrizGemas;
    }

    public int getAbajo(){
        return abajo;
    }
    public int getArriba(){
        return arriba;
    }
    public int getIzquierda(){
        return izquierda;
    }
    public int getDerecha(){
        return derecha;
    }


    public int analizaGemaAntes(Gemas gemaAnalizar, ArrayList<Gemas> gemasEspeciales){
        this.gemaAnalizar = gemaAnalizar;
        id = gemaAnalizar.getId();
        this.posXGemaAnalizar = gemaAnalizar.getPosXNueva();
        this.posYGemaAnalizar = gemaAnalizar.getPosYNueva();
        existeGemaEspecial = false;
        
        
        if(gemasEspeciales==null){
            this.gemasEspeciales = new ArrayList<Gemas>();
        }else{
            this.gemasEspeciales = gemasEspeciales;
        }
        if(id>7&&id<15){
            existeGemaEspecial = true;
            for(int i=0; i<this.gemasEspeciales.size(); i++){
                if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar]){
                    existeGemaEspecial = false;
                }
            }
            if(existeGemaEspecial){
                this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar][posXGemaAnalizar]);
            }
            id=id-7;
            inicialEsEspecial = true; //Si la inicial es especial hacer un analisis distinto de las gemas
            //gemaAnalizar.setId(id);
        }
        arriba = analizarArriba();
        abajo = analizarAbajo();
        izquierda = analizarIzquierda();
        derecha = analizarDerecha();
        if(calcularX(derecha, izquierda) || calcularY(arriba, abajo)){
            matrizGemas[posYGemaAnalizar][posXGemaAnalizar].setId(0);
            int contador = 1;
            eliminadas = 1;
            if(calcularX(derecha, izquierda)) {
                contador = 1;
                while (contador <= izquierda) {
                    if((id+7) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar-contador].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar-contador]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar][posXGemaAnalizar-contador]);
                        }
                    }
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar - contador].setId(0);
                    contador++;
                }
                contador = 1;
                while (contador <= derecha) {
                    if((id+7) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar+contador].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar+contador]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar][posXGemaAnalizar+contador]);
                        }
                    }
                    eliminadas+=(new Scanner(matrizGemas).analizaGemaAntes(matrizGemas[posYGemaAnalizar][posXGemaAnalizar + contador],this.gemasEspeciales));
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar + contador].setId(0);
                    contador++;
                }
                eliminadas += derecha+izquierda;
            }
            if(calcularY(arriba, abajo)) {
                contador = 1;
                while (contador <= arriba) {
                    if((id+7) == matrizGemas[posYGemaAnalizar-contador][posXGemaAnalizar].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar-contador][posXGemaAnalizar]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar-contador][posXGemaAnalizar]);
                        }
                    }
                    matrizGemas[posYGemaAnalizar - contador][posXGemaAnalizar].setId(0);
                    contador++;
                }
                contador = 1;
                while (contador <= abajo) {
                    if((id+7) == matrizGemas[posYGemaAnalizar+contador][posXGemaAnalizar].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar+contador][posXGemaAnalizar]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar+contador][posXGemaAnalizar]);
                        }
                    }
                    eliminadas+=(new Scanner(matrizGemas).analizaGemaAntes(matrizGemas[posYGemaAnalizar + contador][posXGemaAnalizar],this.gemasEspeciales));
                    matrizGemas[posYGemaAnalizar + contador][posXGemaAnalizar].setId(0);
                    contador++;
                }
                eliminadas += arriba+abajo;
            }
            
            inicialEsEspecial = false;
            if(eliminadas>=4){
                if((derecha+izquierda+1==5) || (arriba+abajo+1==5)  ){
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar].crearGemaHiperCubo();
                }else if((arriba+abajo+1==4 || (derecha+izquierda+1==4) || eliminadas>4)){
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar].setId(id);
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar].crearGemaEspecial();
                }
            }
            if(gemasEspeciales==null){
                for(int i=0; i<this.gemasEspeciales.size(); i++){
                    eliminadas+=eliminarGemaEspecial(this.gemasEspeciales.get(i));
                }
            }
            //System.out.print(eliminadas);
            
            return eliminadas;
            //Si hay elementos suficientes elementos contiguos a la gema entonces debería eliminarlos y crear nue
        }
        inicialEsEspecial = false;
        return 0;
    }

    private int eliminarGemaEspecial(Gemas gemaEspecialEliminar){
        int eliminados=0;
        if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()].getId()!=0){
            matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()].setId(0);
        }
        if(((gemaEspecialEliminar.getPosXNueva()+1) > -1) && ((gemaEspecialEliminar.getPosXNueva()+1) < 9) && (gemaEspecialEliminar.getPosYNueva()>-1) && (gemaEspecialEliminar.getPosYNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosXNueva()-1)>-1) && ((gemaEspecialEliminar.getPosXNueva()-1) < 9)&& (gemaEspecialEliminar.getPosYNueva()>-1) && (gemaEspecialEliminar.getPosYNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()+1)>-1) && ((gemaEspecialEliminar.getPosYNueva()+1) < 9) && (gemaEspecialEliminar.getPosXNueva()>-1) && (gemaEspecialEliminar.getPosXNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()-1)>-1) && ((gemaEspecialEliminar.getPosYNueva()-1) < 9) && (gemaEspecialEliminar.getPosXNueva()>-1) && (gemaEspecialEliminar.getPosXNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getId()>7){
                        eliminados+= eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()+1)>-1) && ((gemaEspecialEliminar.getPosYNueva()+1) < 9) && ((gemaEspecialEliminar.getPosXNueva()+1)>-1) && ((gemaEspecialEliminar.getPosXNueva()+1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()+1)>-1) && ((gemaEspecialEliminar.getPosYNueva()+1) < 9) && ((gemaEspecialEliminar.getPosXNueva()-1)>-1) && ((gemaEspecialEliminar.getPosXNueva()-1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()-1)>-1) && ((gemaEspecialEliminar.getPosYNueva()-1) < 9) && ((gemaEspecialEliminar.getPosXNueva()-1)>-1) && ((gemaEspecialEliminar.getPosXNueva()-1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].setId(0);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()-1)>-1) && ((gemaEspecialEliminar.getPosYNueva()-1) < 9) && ((gemaEspecialEliminar.getPosXNueva()+1)>-1) && ((gemaEspecialEliminar.getPosXNueva()+1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1]);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].setId(0);
                    eliminados++;
                }
            }
        }
        return eliminados;
    }

    public int analizaGemaAntes(Gemas gemaAnalizar, ArrayList<Gemas> gemasEspeciales, Graphics g, Musica explosionCreada, Musica explosion){
        this.gemaAnalizar = gemaAnalizar;
        id = gemaAnalizar.getId();
        this.posXGemaAnalizar = gemaAnalizar.getPosXNueva();
        this.posYGemaAnalizar = gemaAnalizar.getPosYNueva();
        existeGemaEspecial = false;
        
        if(gemasEspeciales==null){
            this.gemasEspeciales = new ArrayList<Gemas>();
        }else{
            this.gemasEspeciales = gemasEspeciales;
        }
        if(id>7&&id<15){
            existeGemaEspecial = true;
            for(int i=0; i<this.gemasEspeciales.size(); i++){
                if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar]){
                    existeGemaEspecial = false;
                }
            }
            if(existeGemaEspecial){
                this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar][posXGemaAnalizar]);
            }
            id=id-7;
            inicialEsEspecial = true; //Si la inicial es especial hacer un analisis distinto de las gemas
            //gemaAnalizar.setId(id);
        }
        arriba = analizarArriba();
        abajo = analizarAbajo();
        izquierda = analizarIzquierda();
        derecha = analizarDerecha();
        if(calcularX(derecha, izquierda) || calcularY(arriba, abajo)){
            matrizGemas[posYGemaAnalizar][posXGemaAnalizar].setId(0);
            g.drawImage(explosionIcon.getImage(),matrizGemas[posYGemaAnalizar][posXGemaAnalizar].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[posYGemaAnalizar][posXGemaAnalizar].getCoordenadaY_Inicio_Cuadrado(),null);
            int contador = 1;
            eliminadas = 1;
            if(calcularX(derecha, izquierda)) {
                contador = 1;
                while (contador <= izquierda) {
                    if((id+7) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar-contador].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar-contador]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar][posXGemaAnalizar-contador]);
                        }
                    }
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar - contador].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[posYGemaAnalizar][posXGemaAnalizar - contador].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[posYGemaAnalizar][posXGemaAnalizar - contador].getCoordenadaY_Inicio_Cuadrado(),null);
                    contador++;
                }
                contador = 1;
                while (contador <= derecha) {
                    if((id+7) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar+contador].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar+contador]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar][posXGemaAnalizar+contador]);
                        }
                    }
                    eliminadas+=(new Scanner(matrizGemas).analizaGemaAntes(matrizGemas[posYGemaAnalizar][posXGemaAnalizar + contador],this.gemasEspeciales));
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar + contador].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[posYGemaAnalizar][posXGemaAnalizar + contador].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[posYGemaAnalizar][posXGemaAnalizar + contador].getCoordenadaY_Inicio_Cuadrado(),null);
                    contador++;
                }
                eliminadas += derecha+izquierda;
            }
            if(calcularY(arriba, abajo)) {
                contador = 1;
                while (contador <= arriba) {
                    if((id+7) == matrizGemas[posYGemaAnalizar-contador][posXGemaAnalizar].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar-contador][posXGemaAnalizar]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar-contador][posXGemaAnalizar]);
                        }
                    }
                    matrizGemas[posYGemaAnalizar - contador][posXGemaAnalizar].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[posYGemaAnalizar - contador][posXGemaAnalizar].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[posYGemaAnalizar - contador][posXGemaAnalizar].getCoordenadaY_Inicio_Cuadrado(),null);
                    contador++;
                }
                contador = 1;
                while (contador <= abajo) {
                    if((id+7) == matrizGemas[posYGemaAnalizar+contador][posXGemaAnalizar].getId()){
                        existeGemaEspecial = true;
                        for(int i=0; i<this.gemasEspeciales.size(); i++){
                            if(this.gemasEspeciales.get(i) == matrizGemas[posYGemaAnalizar+contador][posXGemaAnalizar]){
                                existeGemaEspecial = false;
                            }
                        }
                        if(existeGemaEspecial){
                            this.gemasEspeciales.add(matrizGemas[posYGemaAnalizar+contador][posXGemaAnalizar]);
                        }
                    }
                    eliminadas+=(new Scanner(matrizGemas).analizaGemaAntes(matrizGemas[posYGemaAnalizar + contador][posXGemaAnalizar],this.gemasEspeciales));
                    matrizGemas[posYGemaAnalizar + contador][posXGemaAnalizar].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[posYGemaAnalizar + contador][posXGemaAnalizar].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[posYGemaAnalizar + contador][posXGemaAnalizar].getCoordenadaY_Inicio_Cuadrado(),null);
                    contador++;
                }
                eliminadas += arriba+abajo;
            }
            int auxEliminadas = eliminadas;
            inicialEsEspecial = false;
            if(gemasEspeciales==null){
                for(int i=0; i<this.gemasEspeciales.size(); i++){
                    eliminadas+=eliminarGemaEspecial(this.gemasEspeciales.get(i),g);
                }
                if(this.gemasEspeciales.size()>0){
                    explosion.setSonidoUnaVez(true);
                }
            }
            if(auxEliminadas>=4){
                if((derecha+izquierda+1==5) || (arriba+abajo+1==5)  ){
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar].crearGemaHiperCubo();
                }else if((arriba+abajo+1==4 || (derecha+izquierda+1==4) || eliminadas>4)){
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar].setId(id);
                    matrizGemas[posYGemaAnalizar][posXGemaAnalizar].crearGemaEspecial();
                    explosionCreada.setSonidoUnaVez(true);
                }
            }
            
            //System.out.print(eliminadas);
            
            return eliminadas;
            //Si hay elementos suficientes elementos contiguos a la gema entonces debería eliminarlos y crear nue
        }
        inicialEsEspecial = false;
        return 0;
    }

    public int eliminarGemaEspecial(Gemas gemaEspecialEliminar, Graphics g){
        int eliminados=0;
        if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()].getId()!=0){
            matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()].setId(0);
        }
        if(((gemaEspecialEliminar.getPosXNueva()+1) > -1) && ((gemaEspecialEliminar.getPosXNueva()+1) < 9) && (gemaEspecialEliminar.getPosYNueva()>-1) && (gemaEspecialEliminar.getPosYNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()+1].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosXNueva()-1)>-1) && ((gemaEspecialEliminar.getPosXNueva()-1) < 9)&& (gemaEspecialEliminar.getPosYNueva()>-1) && (gemaEspecialEliminar.getPosYNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()][gemaEspecialEliminar.getPosXNueva()-1].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()+1)>-1) && ((gemaEspecialEliminar.getPosYNueva()+1) < 9) && (gemaEspecialEliminar.getPosXNueva()>-1) && (gemaEspecialEliminar.getPosXNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()-1)>-1) && ((gemaEspecialEliminar.getPosYNueva()-1) < 9) && (gemaEspecialEliminar.getPosXNueva()>-1) && (gemaEspecialEliminar.getPosXNueva()<9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getId()>7){
                        eliminados+= eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()+1)>-1) && ((gemaEspecialEliminar.getPosYNueva()+1) < 9) && ((gemaEspecialEliminar.getPosXNueva()+1)>-1) && ((gemaEspecialEliminar.getPosXNueva()+1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()+1].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()+1)>-1) && ((gemaEspecialEliminar.getPosYNueva()+1) < 9) && ((gemaEspecialEliminar.getPosXNueva()-1)>-1) && ((gemaEspecialEliminar.getPosXNueva()-1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()+1][gemaEspecialEliminar.getPosXNueva()-1].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()-1)>-1) && ((gemaEspecialEliminar.getPosYNueva()-1) < 9) && ((gemaEspecialEliminar.getPosXNueva()-1)>-1) && ((gemaEspecialEliminar.getPosXNueva()-1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()-1].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        if(((gemaEspecialEliminar.getPosYNueva()-1)>-1) && ((gemaEspecialEliminar.getPosYNueva()-1) < 9) && ((gemaEspecialEliminar.getPosXNueva()+1)>-1) && ((gemaEspecialEliminar.getPosXNueva()+1) < 9)){
            if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=0){
                if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getId()!=15){
                    if(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getId()>7){
                        eliminados+=eliminarGemaEspecial(matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1],g);
                    }
                    matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].setId(0);
                    g.drawImage(explosionIcon.getImage(),matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getCoordenadaX_Inicio_Cuadrado(),matrizGemas[gemaEspecialEliminar.getPosYNueva()-1][gemaEspecialEliminar.getPosXNueva()+1].getCoordenadaY_Inicio_Cuadrado(),null);
                    eliminados++;
                }
            }
        }
        return eliminados;
    }

    public boolean analizaGema(Gemas gemaAnalizar){
        this.gemaAnalizar = gemaAnalizar;
        this.id = gemaAnalizar.getId();
        if(id>7&&id<15){
            id=id-7;
            inicialEsEspecial = true; //Si la inicial es especial hacer un analisis distinto de las gemas
            //gemaAnalizar.setId(id);
        }
        this.posXGemaAnalizar = gemaAnalizar.getPosXNueva();
        this.posYGemaAnalizar = gemaAnalizar.getPosYNueva();
        gemasEspeciales = new ArrayList<Gemas>();
        if(calcularX(analizarDerecha(), analizarIzquierda()) || calcularY(analizarArriba(), analizarAbajo())){
            return true;
            //Si hay elementos suficientes elementos contiguos a la gema entonces debería eliminarlos y crear nue
        }else{
            //En caso contrario debería retornar la gema a su posicion anterior.
            return false;
        }
    }


    public boolean calcularX(int derecha, int izquierda){
        if(derecha + izquierda >= 2)
            return true;
        return false;
    }

    public boolean calcularY(int arriba, int abajo){
        if(arriba+abajo >= 2) return true;
        return false;
    }

    private int analizarDerecha(){
        int iguales = 0;
        int var = 1;
        while(posXGemaAnalizar+var <= 8){
            if(id == matrizGemas[posYGemaAnalizar][posXGemaAnalizar+var].getId() || 
            (((id+7) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar+var].getId())&&
            (matrizGemas[posYGemaAnalizar][posXGemaAnalizar+var].getId()!=0) && id!=0)){
                iguales++;
                var++;
            }else{
                break;
            }
        }
        return iguales;
    }

    private int analizarIzquierda(){
        int iguales = 0;
        int var = 1;
        while(posXGemaAnalizar-var >=0){
            if(id == matrizGemas[posYGemaAnalizar][posXGemaAnalizar-var].getId() || 
            ((id+7) == matrizGemas[posYGemaAnalizar][posXGemaAnalizar-var].getId()&&
            matrizGemas[posYGemaAnalizar][posXGemaAnalizar-var].getId()!=0 && id!=0)){
                iguales++;
                var++;
            }else{
                break;
            }
        }
        return iguales;
    }

    private int analizarArriba(){
        int iguales = 0;
        int var = 1;
        while(posYGemaAnalizar-var >=0){
            if(id == matrizGemas[posYGemaAnalizar-var][posXGemaAnalizar].getId() || 
                ((id+7) == matrizGemas[posYGemaAnalizar-var][posXGemaAnalizar].getId()&&
                matrizGemas[posYGemaAnalizar-var][posXGemaAnalizar].getId()!=0 && id!=0)){
                iguales++;
                var++;
            }else{
                break;
            }
        }
        return iguales;
    }

    private int analizarAbajo(){
        int iguales = 0;
        int var = 1;
        while(posYGemaAnalizar+var <= 8){
            if(id == matrizGemas[posYGemaAnalizar+var][posXGemaAnalizar].getId() || 
            ((id+7) == matrizGemas[posYGemaAnalizar+var][posXGemaAnalizar].getId()&&
            matrizGemas[posYGemaAnalizar+var][posXGemaAnalizar].getId()!=0 && id!=0)){
                iguales++;
                var++;
            }else{
                break;
            }
        }
        return iguales;
    }
}
