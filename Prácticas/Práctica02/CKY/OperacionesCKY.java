/**
 * Interfaz que define acciones
 * sobre matrices.
 * @author Bonilla Ruiz Roberto Adrián
 * @author Gómez Elizalde Alexys
 * @version 1.0 Diciembre 2020
 */

public interface OperacionesCKY{

	/**
     * Método que obtiene la cadena a verificar que fue ingresada por terminal
     * @param arreglo el ultimo argumento ingresado por entrada estandar
     * el cual corresponde a la cadena a verificar con la gramática
     * @return el contenido de la tabla con elementos sin corchetes
     */
	public String obtenPalabra(String[] arreglo);

	/**
	 * Método que crea nuestra tabla y realiza el
	 * algortimo CKY
	 * @param arreglo los argumentos ingresados por entrada estandar
	 */
	public void realiza(String[] arreglo);


    /**
     * Método que toma el nombre del archivo txt con la gramatica
     * y que toma la cadena a verificar, asi como el simbolo inicial
     * para determinar que simbolos son terminales y cuales no
     * @param arreglo los argumentos ingresados por entrada estandar
     */
	public void convierteG(String[] arreglo);

    /**
     * Método que muestra en terminal la cadena ingresada por entrada estandar
     * las gramatica del archivo txt y realiza el algoritmo CKY
     * Asi como la impresion de la tabla con sus elementos
     * @param arreglo un arreglo bidimensional
     */
	public void imprimeResultado (String[][] arreglo);


    /**
     * Método que muestra en terminal la cadena ingresada por entrada estandar
     * las gramatica del archivo txt y realiza el algoritmo CKY
     * Asi como la impresion de la tabla con sus elementos
     * @param arreglo un arreglo bidimensional
     */
	public String imprimeTabla(String[][] tabla);


    /**
     * Metodo que toma un arreglo bidimensional, compara las
     * longitudes de las cadenas en el arreglo y define cual es la mayor
     * @param arregloDoble nuestro arreglo bidimensional
     * @return la longitud de la cadena más larga
     */
	public int cadenaMasLarga(String[][] arregloDoble);


	/**
	 * Método que se encarga de realizar todo el algortimo CKY como tal
	 * @param contenido los elementos que conforman nuestra tabla (los datos de manera concisa)
	 */
	 public String[][] realizaCKY(String[][] contenido);

    /**
     * Método que crea nuestra tabla para darle formato al del algoritmo
     * @return el arreglo bidimensional que corresponde a la tabla
     */
	 public String[][] creaTablaCKY ();

	 /**
		* Metodo que verifica si las producciones son correctas
		* @param verifica el arreglo con las producciones
		* @return el arreglo ya verificado
		*/
 	 public String[] verificaProducciones(String[] verifica);


	  /**
	   * Metodo que obtiene las posibles conbinaciones a partir de dos arreglos
	   * @param primer el primer arreglo a combinar
	   * @param segundo el segundo arreglo a combinar
	   * @return un arreglo resultado de combinar los arreglos pasados como parámetro
	   */
	 	public String[] obtenerCombinaciones(String[] primer, String[] segundo);

    /**
     * Metodo auxiliar el cual nos ayuda a llenar nuestra tabla
     * @param cadena la cadena asociada con la cual vamos a trabajar
     * @param posicion la posicion con la que vamos a trabajar la cadena
     * @return la cadena asociada una posicion
     */
	public String administraPalabra(String cadena, int posicion);


}
