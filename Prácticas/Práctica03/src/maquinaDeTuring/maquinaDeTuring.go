package maquinaDeTuring

/**
 * Práctica 3
 * Autómatas y Lenguajes Formales, 2021-1
 * Bonilla Ruiz Roberto Adrián
 * Gómez Elizalde Alexys
 * Enero 2021
 */


import ("encoding/json";"io/ioutil";"fmt")

//DEFINE UNA MAQUINA DE TURING CON SUS ESTADOS, LA CINTA, SUS FUNCIONES DE TRANSICIÓN
//EL ESTADO ACTUAL DE LA MAQUINA DE TURING,EL SIMBOLO QUE INDICA VACIO, Y EL BOOLEAN
//QUE INDICA SI LA MAQUINA DE TURING SE HA DETENIDO.
type MaquinaDeTuring struct {estados map[Estado]bool;cinta Cinta;funcionTransicion FuncionDeTransicion; actual Estado; sVacio rune; seDetuvo bool}

//CINTA PARA LA MAQUINA DE TURING.
type Cinta struct {blanco rune;inicio*Casilla;casillaActual*Casilla;fin*Casilla}

//ESTRUCTURA DE UN ESTADO
type Estado struct {id string}

// CASILLA PARA LA CINTA DE LA MAQUINA DE TURING.
type Casilla struct {anterior*Casilla;simboloContenido rune;siguiente*Casilla}

//LOS MOVIMIENTOS POSIBLES PARA LA CINTA DE LA MAQUINA DE TURING.
type Direccion string
const Derecha Direccion="Derecha"; const Izquierda Direccion="Izquierda"; const Neutral Direccion="Neutral"

//CONTIENE EL NUEVO ESTADO, EL SIMBOLO QUE SE PONDRÁ EN LA CINTA Y EL MOVIMIENTO
//QUE HARÁ LA MAQUINA DE TURING.
type ResultadoFunTransicion struct{estado Estado;simbolo rune;movimiento Direccion}

//TIPO DE DATOS PARA LAS TRANSICIONES LA CABEZA DE LA MAQUINA DE TURING Y
//CONTIENE EL ESTADO ACTUAL Y EL SIMBOLO ACTUAL QUE LEE.
type Funci struct {estado Estado;simbolo rune}

//DEFINE LA FUNCIÓN DE TRANSICIÓN DE LA MAQUINA DE TURING.
type FuncionDeTransicion struct {transicion map[Funci]ResultadoFunTransicion}

//FORMA EN QUE LOS JSON DEBE ESTAR ESCRITOS PARA PODER HACER LA MAQUINA DE TURING
type ArchivoJSON struct {Estados[]string;Entrada[]string;Cinta[]string;Inicial string;Blanco string;Finales []string;Transiciones [][]string}

// Constructor de un estado.
func NuevoEstado(id string) *Estado {nuevo := new(Estado);nuevo.id = id
    return nuevo}

//VERICA SI DOS ESTADOS SON IGUALES.
func (this *Estado) sonIguales(nuevo *Estado) bool {return this.id == nuevo.id}

//RECIBE UN TROZO DE CADENA QUE CONTIENE LAS TRANSICIONES NECESARIAS PARA PODER
//hacer la función de transición.
func NuevaFunTransic(transiciones [][]string)*FuncionDeTransicion{funTrans := new(FuncionDeTransicion)
    funTrans.transicion = make(map[Funci]ResultadoFunTransicion);funTrans.auxNuevaFunTransic(transiciones)
    return funTrans
}

//CREA LA CINTA CON EL SIMBOLO CONTENIDO.
func creaCinta(blanco rune) *Cinta {cintaNueva:=new(Cinta);casillaAux:=new(Casilla)
    casillaAux.simboloContenido=blanco;cintaNueva.blanco=blanco
    cintaNueva.inicio = casillaAux;cintaNueva.casillaActual=casillaAux;cintaNueva.fin=casillaAux
    return cintaNueva}

//AGREGA UNA NUEVA TRANSICIÓN.
func (funTrans *FuncionDeTransicion) agregaTransicion(datos Funci, resultado ResultadoFunTransicion){funTrans.transicion[datos] = resultado}

//DA LA FUNCIÓN DE TRANSICIÓN, DADO UN TROZO DE LA CADENA (FUNCION AUXILIAR PARA "NUEVAFUNTRANSIC")
func (funTrans *FuncionDeTransicion) auxNuevaFunTransic(transiciones [][]string ) {
    for _, transicion := range transiciones {i:=5;j:=0;k:=4
        if (len(transicion)%i==j){var derecha string = "R";dEstado := *NuevoEstado(transicion[0]);t1 := []rune(transicion[1])
            var izquierda string = "L"; dSimbolo := t1[0]; datos   := Funci{dEstado, dSimbolo}
			var neutral   string = "N";llegaE    := *NuevoEstado(transicion[2]);rT := []rune(transicion[3]);simb := rT[0]
            var resultado ResultadoFunTransicion
            if(transicion[k] == derecha){resultado   = ResultadoFunTransicion{llegaE, simb, Derecha}}
            if(transicion[k] == izquierda){resultado = ResultadoFunTransicion{llegaE, simb, Izquierda}}
            if(transicion[k] == neutral){resultado   = ResultadoFunTransicion{llegaE, simb, Neutral}}
            if(transicion[k] != derecha && transicion[4]!= izquierda && transicion[4]!= neutral){resultado = ResultadoFunTransicion{llegaE, simb, Neutral}}
            funTrans.agregaTransicion(datos, resultado)}
    }
}

//AGREGA UNA NUEVA CASILLA A LA CINTA
func (cintaNueva *Cinta) agregaCasilla(simboloContenido rune) {nuevaC := new(Casilla)
    nuevaC.simboloContenido=simboloContenido;nuevaC.anterior=cintaNueva.fin;cintaNueva.fin.siguiente=nuevaC;cintaNueva.fin=nuevaC}
    
//AGREGA UNA NUEVA CASILLA AL INICIO DE LA CINTA
func (cintaNueva *Cinta) agregaCasillaIn(simboloContenido rune) {nuevaC := new(Casilla)
    nuevaC.simboloContenido = simboloContenido;nuevaC.siguiente=cintaNueva.inicio;cintaNueva.inicio.anterior=nuevaC;cintaNueva.inicio=nuevaC}
    

//AGREGA UNA NUEVA CASILLA AL FINAL DE LA CINTA
func (cintaNueva *Cinta) agregaCasillaFin(simboloContenido rune) {
    nuevaC := new(Casilla);nuevaC.simboloContenido = simboloContenido;nuevaC.anterior = cintaNueva.fin;cintaNueva.fin.siguiente = nuevaC;cintaNueva.fin = nuevaC}

//OBTIENE LA SALIDA DE LA FUNCIÓN DE TRANSICIÓN.
func (funTrans *FuncionDeTransicion) ObtenResultado(datos Funci) (ResultadoFunTransicion, bool) {
    resultado, def := funTrans.transicion[datos]
    if def { return resultado, true
    } else { return resultado, false}
}
//CONSTRUCTOR DE UNA MAQUINA DE TURING.
func MaquinaTuring(eEstados []string,sVacio rune,eInicial string,eFinal []string,transiciones [][]string,entrada string) *MaquinaDeTuring {
    maquinaDeTuring:= new(MaquinaDeTuring);maquinaDeTuring.sVacio=sVacio
   	maquinaDeTuring.cinta=*creaCinta(sVacio);maquinaDeTuring.estados = make(map[Estado]bool);edoInicial:= *NuevoEstado(eInicial);maquinaDeTuring.actual = edoInicial
    for _, edo := range eEstados {estado := *NuevoEstado(edo)
        maquinaDeTuring.estados[estado] = false}
    for _, eF := range eFinal {estado := *NuevoEstado(eF)
		maquinaDeTuring.estados[estado] = true}
    maquinaDeTuring.seDetuvo = false; entr := []rune(entrada)
    for _, simbolo := range entr{maquinaDeTuring.cinta.agregaCasilla(simbolo)}
    		maquinaDeTuring.cinta.Derecha();maquinaDeTuring.funcionTransicion = *NuevaFunTransic(transiciones)
    return maquinaDeTuring
}
// FUNCIÓN QUE OBTIENE LA CONFIGURACIÓN ACTUAL DE LA CINTA DE LA MÁQUINA DE TURING
func (maquinaDeTuring *MaquinaDeTuring) ObtenerConfig() string {return maquinaDeTuring.cinta.ObtenerConfig(maquinaDeTuring.actual.id)}

//OBTIENE LA CONFIGURACIÓN DE LA CASILLA ACTUAL Y LA PONE EN UNA CADENA.
func (cintaNueva *Cinta) ObtenerConfig(currentState string) string {
    cadena := "\t";recorrido := cintaNueva.inicio;amarillo := "\u001b[33m";reset:= "\u001b[0m"
    for recorrido != nil {
        if recorrido == cintaNueva.casillaActual {cadena += " ~ " + currentState + " ~ "}
        if !((recorrido.simboloContenido == cintaNueva.blanco) && (recorrido == cintaNueva.inicio)){cadena += string(recorrido.simboloContenido)}
        recorrido = recorrido.siguiente}
    final := "\n\t\t\t"+cadena; formato :=amarillo+"\n\t\t\t\t-----------------"+reset;    
    return formato+final}

//LEE EL SIMBOLO CONTENIDO EN LA CASILLA DE LA CINTA.
func (cintaNueva *Cinta) getSimbolo() rune {
    return cintaNueva.casillaActual.simboloContenido}

//FUNCIÓN QUE ESCRIBE EL SIMBOLO EN LA CASILLA ACTUAL EN LA CINTA.
func (cintaNueva *Cinta) setSimbolo(simboloContenido rune) {cintaNueva.casillaActual.simboloContenido = simboloContenido}

//Función de la maquina de Turing.
func(maquinaDeTuring *MaquinaDeTuring) SimulaMT() bool {simbolo:= maquinaDeTuring.cinta.getSimbolo();estado:= maquinaDeTuring.actual; datos:= Funci{estado, simbolo};mov, definido:= maquinaDeTuring.funcionTransicion.ObtenResultado(datos)
    if !definido {maquinaDeTuring.seDetuvo = true;return false}
    maquinaDeTuring.actual = mov.estado; maquinaDeTuring.cinta.setSimbolo(mov.simbolo)
    if(mov.movimiento == Derecha){maquinaDeTuring.cinta.Derecha()}
    if(mov.movimiento == Izquierda){maquinaDeTuring.cinta.Izquierda()}
    if(mov.movimiento == Neutral){maquinaDeTuring.cinta.Neutral()}	
    termina, _:= maquinaDeTuring.estados[maquinaDeTuring.actual]
    if termina {maquinaDeTuring.seDetuvo = true;return true}
    return false}

//MOVIMIENTOS
//CAMBIA LA CASILLA ACTUAL A LA CASILLA DE LA DERECHA.
func (cintaNueva *Cinta) Derecha() {
    if cintaNueva.casillaActual.siguiente != nil {cintaNueva.casillaActual = cintaNueva.casillaActual.siguiente
    } else {cintaNueva.agregaCasillaFin(cintaNueva.blanco)
        cintaNueva.casillaActual = cintaNueva.casillaActual.siguiente}}
        
//CAMBIA LA CASILLA ACTUAL A LA CASILLA DE LA IZQUIERDA.
func (cintaNueva *Cinta) Izquierda() {
    if cintaNueva.casillaActual.anterior != nil {cintaNueva.casillaActual = cintaNueva.casillaActual.anterior
    } else { cintaNueva.agregaCasillaIn(cintaNueva.blanco)
    cintaNueva.casillaActual = cintaNueva.casillaActual.anterior}}

//EL MOVIMIENTO DE CASILLA ES NEUTRO Y SE QUEDA EN LA MISMA CASILLA.
func (cintaNueva *Cinta) Neutral() {cintaNueva.casillaActual = cintaNueva.casillaActual}

//DA UN BOOLEAN QUE INDICA SI LA MAQUINA DE TURING SE DETUVO.
func (maquinaDeTuring *MaquinaDeTuring) FinalizaMT() bool {return maquinaDeTuring.seDetuvo}

//LEE UN JSON PARA LA MAQUINA DE TURING.
func LeerArchivo(nombreArchivo string) ArchivoJSON{
    var rojo string  = "\u001b[31m"
    archivoJson, verificaError := ioutil.ReadFile(nombreArchivo)
    if verificaError != nil {fmt.Println("")}
    maquinaT := ArchivoJSON{};verificaError2 := json.Unmarshal([]byte(archivoJson), &maquinaT)
    if verificaError2 != nil {fmt.Println(rojo+"\t\t\tARCHIVO JSON MAL ESTRUCTURADO O  INEXISTENTE\n")}
    return maquinaT
}    






