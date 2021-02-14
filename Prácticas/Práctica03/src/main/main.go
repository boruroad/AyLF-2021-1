package main
import ("fmt";"maquinaDeTuring";"os")

func main() {
    //CONSTANTES AUXILIARES PARA DETALLES DE FORMATO Y ESTÉTICA
    const rojo = "\u001b[31m";const verde = "\u001b[32m"

    //ENTRADA: EL PROGRAMA RECIBIRÁ DOS CADENAS POR LA ENTRADA ESTÁNDAR:
    //OBTIENE EL NOMBRE DEL ARCHIVO Y LA CADENA POR ENTRADA ESTANDAR
    archivo:= os.Args[1];cadena:= os.Args[2]

    // OBTIEMOS LOS DATOS DEL ARCHIVO JSON PARA DESPUES CREAR UNA MÁQUINA DE TURING Y EJECUTARLA
    datosMTuring:= maquinaDeTuring.LeerArchivo(archivo);estados:= datosMTuring.Estados;blanco:= []rune(datosMTuring.Blanco)[0]
    estadoInicial:= datosMTuring.Inicial;transiciones:= datosMTuring.Transiciones;estadosFinales:= datosMTuring.Finales
    maquinaTuring:= maquinaDeTuring.MaquinaTuring(estados, blanco, estadoInicial, estadosFinales,transiciones, cadena)

    //VARIABLES DE VERIFICACION Y TEXTO A MOSTRAR EN PANTALLA
    var esValida bool;var mensaje string
    fmt.Println(maquinaTuring.ObtenerConfig())
    for !maquinaTuring.FinalizaMT() {esValida = maquinaTuring.SimulaMT();fmt.Println(maquinaTuring.ObtenerConfig())}
    if esValida {mensaje= verde+"\n\t\t\t\tCADENA ACEPTADA"
    } else {mensaje=rojo+"\n\t\t\t\tCADENA NO ACEPTADA"}
    fmt.Println(mensaje);
}
