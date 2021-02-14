import java.io.*;
import java.util.*;

/**
 * Práctica 2 Algoritmo CKY
 * Autmatas y Lenguajes Formales, 2021-1
 * @author Bonilla Ruiz Roberto Adrián
 * @author Gómez Elizalde Alexys
 * @version 1.0 Diciembre 2020
 */

public class AlgoritmoCKY implements OperacionesCKY{

    /** A tributos que nos permitirán dar color al texto en consola. */
    static final String GREEN = "\033[32m";
    static final String YELLOW= "\033[33m";
    static final String PURPLE= "\033[35m";
    static final String CYAN  = "\033[36m";
    static final String RESET = "\u001B[0m";
    static final String RED   = "\033[31m";
    /** Atributos auxiliares para métodos */
    static String cadena;
    static ArrayList<String> terminales = new ArrayList<String>();
    static String simboloInicial;
    static ArrayList<String> noTerminales = new ArrayList<String>();
    static boolean inicio = false;
    static TreeMap<String,ArrayList<String>> gramatica = new TreeMap<>();

    /**
     * Método que obtiene la cadena a verificar que fue ingresada por terminal
     * @param arreglo el ultimo argumento ingresado por entrada estandar
     * el cual corresponde a la cadena a verificar con la gramática
     * @return el contenido de la tabla con elementos sin corchetes
     */
    public String obtenPalabra(String[] arreglo){
        if(!inicio){
             return arreglo[1];
        }
        String[] nuevo = new String[arreglo.length - 1];

            for(int i = 1; i < arreglo.length; i++){
                nuevo[i-1] = arreglo[i];
            }

        return toString(nuevo);
    }

    /**
     * Método que crea nuestra tabla y llama a el metodo respectivo al
     * algortimo CKY
     * @param arreglo los argumentos ingresados por entrada estandar
     */
      public void realiza(String[] arreglo){
            convierteG(arreglo);
            String[][] tablaCKY = creaTablaCKY();
            imprimeResultado(realizaCKY(tablaCKY));
        }


    /**
     * Método que toma el nombre del archivo txt con la gramatica
     * y que toma la cadena a verificar, asi como el simbolo inicial
     * para determinar que simbolos son terminales y cuales no
     * @param arreglo los argumentos ingresados por entrada estandar
     */
    public void convierteG(String[] arreglo){
        ArrayList<String> aux = new ArrayList<>();
        int linea = 2;
        Scanner entrada = ExtraerGram(arreglo[0]);
        cadena = obtenPalabra(arreglo);
        simboloInicial = entrada.next();
        entrada.nextLine();

        while(linea < 4 && entrada.hasNextLine()){
            aux.addAll(Arrays.<String>asList(toArray(entrada.nextLine())));

            if(linea == 3){
                noTerminales.addAll(aux);
            }

            if(linea == 2){
                terminales.addAll(aux);
            }

            aux.clear();
            linea++;
        }

        while(entrada.hasNextLine()){
            aux.addAll(Arrays.<String>asList(toArray(entrada.nextLine())));
            String izq = aux.get(0);
            aux.remove(0);
            gramatica.put(izq, new ArrayList<String>());
            gramatica.get(izq).addAll(aux);
            aux.clear();
        }
        entrada.close();
    }

    /**
     * Método que permite entrar a un archivo para obtener la gramática
     * @param nombreArchivo el nombre del archivo .txt
     * @return un objeto de tipo Scanner con el nombre del archivo
     * null si hay un problema al abrirlo
     */
    public Scanner ExtraerGram(String nombreArchivo){
        try{
            return new Scanner(new File(nombreArchivo));
        }catch(FileNotFoundException fnfe){
            System.out.println(RED+"Lo sentimos, el archivo no fue encontrado " + nombreArchivo + "."+RESET);
            System.exit(1);
            return null;
        }
    }


    /**
     * Método que muestra en terminal la cadena ingresada por entrada estandar
     * las gramatica del archivo txt y realiza el algoritmo CKY
     * Asi como la impresion de la tabla con sus elementos
     * @param arreglo un arreglo bidimensional
     */
    public void imprimeResultado (String[][] arreglo){
        System.out.println(YELLOW+"\n\t\tRealizando el algoritmo CKY con la cadena \""+cadena+"\", nuestra tabla queda:\n"+RESET);
        String resultado = imprimeTabla(arreglo);
        System.out.println(CYAN+"\nLas producciones del archivo \"grammar.txt\" son:\n"+RESET);
        for(String s: gramatica.keySet()){
            System.out.print(s + " => " + gramatica.get(s).toString().replaceAll("[\\[\\]\\,]", "").replaceAll("\\s", " | ")+"\t\t");
        }
        System.out.println("\n\n\t\t\t(Con S nuestro símbolo inicial)");
        System.out.println(resultado+"\n");
        System.out.println("NOTA: Si la tabla queda distorcionada, se recomienda reducir el zoom a la terminal");
    }

    /**
     * Método que muestra en terminal la cadena ingresada por entrada estandar
     * las gramatica del archivo txt y realiza el algoritmo CKY
     * Asi como la impresion de la tabla con sus elementos
     * @param arreglo un arreglo bidimensional
     */
    public String imprimeTabla(String[][] tabla){
        StringBuilder sb = new StringBuilder();
        String s = "";
        String mensaje = "";
        int l = cadenaMasLarga(tabla) + 2;
        String formato = "\t\t| %-" + l + "s";
        String bajo = sb.toString();
        sb.delete(0, 1);
        String bajoDerecho = sb.toString();
        for(int i = 0; i < tabla.length; i++){
            for(int j = 0; j <= tabla[i].length; j++){
                System.out.print((j == 0) ? bajo : (i <= 1 && j == tabla[i].length - 1) ? "" : bajoDerecho);
            }
            System.out.println();
            for(int j = 0; j < tabla[i].length; j++){
                s = (tabla[i][j].isEmpty()) ? PURPLE+"vacío"+RESET : tabla[i][j];
                System.out.format(formato, s.replaceAll("\\s", ","));
                if(j == tabla[i].length - 1) {
                 System.out.print("|");
                }
            }
            System.out.println();
        }
        System.out.println(bajo+"\n");
        if(tabla[tabla.length-1][tabla[tabla.length-1].length-1].contains(simboloInicial)){
            mensaje = GREEN+ "\nLa cadena \"" + cadena + "\" es generada por la gramática."+RESET;
        }else{
            mensaje=RED+"\nLa cadena \"" + cadena + "\" no es generada por la gramática."+RESET;
        }
        return mensaje;
    }

    /**length
     * Metodo que toma un arreglo bidimensional, compara las
     * longitudes de las cadenas en el arreglo y define cual es la mayor
     * @param arregloDoble nuestro arreglo bidimensional
     * @return la longitud de la cadena más larga
     */
    public int cadenaMasLarga(String[][] arregloDoble){
        int x = 0;
        for(String[] s : arregloDoble){
            for(String d : s){
                if(d.length() > x)
                    x = d.length();
            }
        }
        return x;
    }


    /**
     * Método que se encarga de realizar todo el algortimo CKY como tal
     * @param contenido los elementos que conforman nuestra tabla (los datos de manera concisa)
     */
    public String[][] realizaCKY(String[][] contenido){
        for(int i = 0; i < contenido[0].length; i++){
            contenido[0][i] = administraPalabra(cadena, i);
        }
        for(int i = 0; i < contenido[1].length; i++){
            String[] verificaCombinaciones = verificaProducciones(new String[] {contenido[0][i]});
            contenido[1][i] = toString(verificaCombinaciones);
        }

        if(cadena.length() <= 1){
            return contenido;
        }
        for(int i = 0; i < contenido[2].length; i++){
            String[] abajo = toArray(contenido[1][i]);
            String[] diagonal = toArray(contenido[1][i+1]);
            String[] verificaCombinaciones = verificaProducciones(obtenerCombinaciones(abajo, diagonal));
            contenido[2][i] = toString(verificaCombinaciones);
        }

        if(cadena.length()<= 2){
            return contenido;
        }
        TreeSet<String> valorActual = new TreeSet<String>();

        for(int i = 3; i < contenido.length; i++){
            for(int j = 0; j < contenido[i].length; j++){
                for(int compara = 1; compara < i; compara++){
                    String[] abajo = contenido[compara][j].split("\\s");
                    String[] diagonal = contenido[i-compara][j+compara].split("\\s");
                    String[] combinaciones = obtenerCombinaciones(abajo, diagonal);
                    String[] verificaCombinaciones = verificaProducciones(combinaciones);
                    if(contenido[i][j].isEmpty()){
                        contenido[i][j] = toString(verificaCombinaciones);
                    }else{
                        String[] oldValues = toArray(contenido[i][j]);
                        ArrayList<String> nuevo = new ArrayList<String>(Arrays.asList(oldValues));
                        nuevo.addAll(Arrays.asList(verificaCombinaciones));
                        valorActual.addAll(nuevo);
                        contenido[i][j] = toString(valorActual.toArray(new String[valorActual.size()]));
                    }
                }
                valorActual.clear();
            }
        }
        return contenido;
    }

        /**
         * Método que crea nuestra tabla para darle formato al del algoritmo
         * @return el arreglo bidimensional que corresponde a la tabla
         */
        public String[][] creaTablaCKY (){
            int length = inicio ? toArray(cadena).length : cadena.length();
            String[][] array = new String[length + 1][];
            array[0] = new String[length];
            for(int i = 1; i < array.length; i++){
                array[i] = new String[length - (i - 1)];
            }
            for(int i = 1; i < array.length; i++){
                for(int j = 0; j < array[i].length; j++){
                    array[i][j] = "";
                }
            }
            return array;
        }

    /**
     * Metodo que verifica si las producciones son correctas
     * @param verifica el arreglo con las producciones
     * @return el arreglo ya verificado
     */
    public String[] verificaProducciones(String[] verifica){
        ArrayList<String> almacena = new ArrayList<>();
        for(String s : gramatica.keySet()){
            for(String actual : verifica){
                if(gramatica.get(s).contains(actual)){
                    almacena.add(s);
                }
            }
        }
        if(almacena.size() == 0) {
            return new String[] {};
        }
        return almacena.toArray(new String[almacena.size()]);
    }

    /**
     * Metodo que obtiene las posibles conbinaciones a partir de dos arreglos
     * @param primer el primer arreglo a combinar
     * @param segundo el segundo arreglo a combinar
     * @return un arreglo resultado de combinar los arreglos pasados como parámetro
     */
    public String[] obtenerCombinaciones(String[] primer, String[] segundo){
        int length = primer.length * segundo.length;
        int contador = 0;
        String[] combinaciones = new String[length];
        if(length == 0){
            return combinaciones;
            }

        for(int i = 0; i < primer.length; i++){
            for(int j = 0; j < segundo.length; j++){
                combinaciones[contador] = primer[i] + segundo[j];
                contador++;
            }
        }
        return combinaciones;
    }

    /**
     * Metodo auxiliar el cual nos ayuda a llenar nuestra tabla
     * @param cadena la cadena asociada con la cual vamos a trabajar
     * @param posicion la posicion con la que vamos a trabajar la cadena
     * @return la cadena asociada una posicion
     */
    public String administraPalabra(String cadena, int posicion){
        if(!inicio){
            return Character.toString(cadena.charAt(posicion));
        }
        return toArray(cadena)[posicion];
    }

    /**
     * Método que imprime el contenido de la tabla en consola
     * elimina los corchetes donde no hay variables y da formato
     * @param arreglo la tabla con corchetes, comas y espacios sin elementos
     * @return el contenido de la tabla con elementos sin corchetes
     */
    public String toString(String[] entrada){
        return Arrays.toString(entrada).replaceAll("[\\[\\]\\,]", "");
    }

    /**
     * Metodo auxiliar que recibe una cadena y la fragmenta a partir
     * de un cierto caracter
     * @param entrada la cadena a fragmentar
     * @return el arreglo con la cadena fragmentada
     */
    public  String[] toArray(String entrada){
        return entrada.split("\\s");
    }

    public static void main(String[] args){
        AlgoritmoCKY nuevo  = new AlgoritmoCKY();
        if(args.length < 2){
            System.out.println(YELLOW+"Ejemplo de como probar la práctica: java CKY <Archivo.txt> <cadena>."+RESET);
            System.exit(1);
        }else if (args.length > 2){
            inicio = true;
        }
        nuevo.realiza(args);
    }
}
