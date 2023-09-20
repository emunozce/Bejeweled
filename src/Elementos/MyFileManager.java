/* Gerardo Femat Delgado
 * Emmanuel Muñoz Cerda
 * Clase MyFileManager
 * Mediante esta clase con unicamente metodos estaticos manejamos el archivo de puntos
 * Tiene 4 metodos principales que se utilizan durante el programa
 * 1. escribirUsuario, cada vez que se hace un login esta funcion comprueba si ya existe en el archivo
 * si esto es cierto, entonces le da la bienvenida, si no existe creara el usuario con valores cero.
 * 2. escribirPuntos, escribe los puntos que obtuvo el jugador siempre y cuando sean mayores a los que tenía antes,
 * ademas agrega el tiempo que duro en el juego.
 * 3. Obtener nombre, puntos, tiempo: Estas tres funciones son fundamentales para mostrar en la tabla de scores,
 * obtienen los datos del numero de usuario que se les indica.
 * 4. Acomodar puntos: cada que se escriben nuevos puntos, es necesario revisar quien quedo en el top, por lo que
 * actualiza el archivo para que el primer lugar siempre sea el de mayor puntos.
 * 
 * Las demás funciones sirven para leer el archivo y hacer comprobaciones y comparaciones. 
 */

package Elementos;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class MyFileManager {

    private final static File archivo = new File("Scores.txt");


    public static int escribirUsuario(String usuario){
        try {
            if(comprobarUsuario(usuario)==-1){
                BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(archivo, true));
                writer.write(usuario + "\t" + 0 + "\t" + "00:00" + "\n");
                writer.close();
                return -1;
            }else{
                return comprobarUsuario(usuario);
            }
        } catch (IOException a) {
            a.printStackTrace();
        }
        return 0;
    }

    public static void escribirPuntos(int puntos, String timepo, String nombre){
        try {
            if(archivo.exists()){
                int i=0;
                String textoArchivo = "";
                String linea;
                while((linea = obtenerLinea(i)) != null){
                    if(i == comprobarUsuario(nombre)){
                        textoArchivo = getString(puntos, timepo, textoArchivo, linea);
                    }else{
                        textoArchivo += linea + '\n';
                    }
                    i++;
                }
                BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(archivo));
                writer.write(textoArchivo);
                writer.close();
                acomodarPuntos();
            }
        } catch (
                IOException a) {
            a.printStackTrace();
        }
    }

    public static String obtenerNombre(int posUsuarioArchivo){
        String linea = obtenerLinea(posUsuarioArchivo);
        int j=0;
        while(linea.charAt(j)!='\t'){
            j++;
        }
        return linea.substring(0,j);
    }

    public static String obtenerTiempo(int posUsuarioArchivo){
        String linea = obtenerLinea(posUsuarioArchivo);
        int i=0;
        while(linea.charAt(i)!='\t'){
            i++;
        }
        i++;
        int j=i;
        while(linea.charAt(j)!='\t'){
            j++;
        }
        j++;
        return linea.substring(j);
    }

    public static String obtenerPuntos(int posUsuarioArchivo){
        String linea = obtenerLinea(posUsuarioArchivo);
        int j=0;
        while(linea.charAt(j)!='\t'){
            j++;
        }
        j++;
        int i=j;
        while(linea.charAt(i)!='\t'){
            i++;
        }
        return linea.substring(j,i);
    }

    private static String getString(int puntos, String tiempo, String textoArchivo, String linea) {
        int j=0;
        while(linea.charAt(j)!='\t'){
            j++;
        }
        j++;
        int i=j;
        while(linea.charAt(i)!='\t'){
            i++;
        }
        String modificado = linea.substring(0,j);
        String puntosAnteriores = linea.substring(j,i);
        int puntosAnterioresInt = Integer.parseInt(puntosAnteriores);
        if(puntosAnterioresInt>puntos){
            return (linea+"\n");
        }else{
            modificado += Integer.toString(puntos) + '\t';
            modificado += tiempo + '\n';
            textoArchivo += modificado;
            return textoArchivo;
        }
    }

    private static int comprobarUsuario(String nombreUsuario){
        int i=0;
        String linea;
        while(true){
            linea = obtenerLinea(i);
            if(linea == null){
                break;
            }else{
                int j=0;
                while(linea.charAt(j)!='\t'){
                    j++;
                }
                String nom = linea.substring(0,j);
                if(nom.compareTo(nombreUsuario) == 0) return i;
            }
            i++;
        }
        return -1;
    }

    private static void acomodarPuntos(){
        String linea;
        ArrayList<String> lineas = new ArrayList<String>();
        int i=0;
        while((linea = obtenerLinea(i))!=null){
            lineas.add(i,linea);
            i++;
        }
        for(int j=0; j< lineas.size(); j++){
            for(int k=j; k< lineas.size(); k++){
                if(obtenerPuntos(lineas.get(j))<obtenerPuntos(lineas.get(k))){
                    //linea = lineas.get(j);
                    Collections.swap(lineas, j, k);
                }
            }
        }
        try{
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(archivo));
            for(int j=0;j<lineas.size();j++){ 
                writer.write(lineas.get(j) + '\n');
            }
            writer.close();
        }catch(IOException a){
            a.printStackTrace();
        }
    }

    private static int obtenerPuntos(String linea){
        int i=0;
        int j=0;
        while(linea.charAt(i)!='\t'){
            i++;
        }
        i++;
        j=i;
        while(linea.charAt(j)!='\t'){
            j++;
        }
        return Integer.parseInt(linea.substring(i,j));
    }


    private static String obtenerLinea(int numeroLinea){
        try{
            int i = 0;
            if(archivo.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    while (i < numeroLinea) {
                        if (reader.readLine() == null) return null;
                        i++;
                    }
                    String linea = reader.readLine();
                    reader.close();
                    return linea;
                }
            }
        } catch(IOException a) {
            a.printStackTrace();
        }
        return null;
    }
}
