package com.mihail.chess;

public class Posicion {

	public static enum Bando {
		Blanco, Negro
	}

	/**
	 * Constante que representa al bando blanco.
	 */
	public final static int BLANCO = 0;

	/**
	 * Constante que representa al bando negro.
	 */
	public final static int NEGRO = 1;

	/**
	 * Constante que representa la posición inicial en el tablero.
	 */
	public final static String POS_INICIAL = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	/**
	 * Este atributo es la representacion del tablero en la logica del programa.
	 * Es una matriz cuadrada de 64 casillas. El concepto de casilla no se
	 * representa con un objeto: directamente se almacena un objeto Pieza o null
	 * si la casilla esta vacia.
	 */
	private Pieza[][] tabla = new Pieza[8][8];

	/**
	 * Este atributo indica a quien le toca mover. Vale BLANCO cuando mueven
	 * blancas y NEGRO cuando mueven negras.
	 */
	private int turno;

	/**
	 * Este atributo indica el numero de movimiento por el que va la partida.
	 * Por ejemplo: en '1. e4 c5', tanto e4 como c5 compartirian el 1 como
	 * numero de movimiento.
	 */
	private int numeroMovimiento;

	/**
	 * Este atributo indica que enroques estan disponibles para que bandos. Es
	 * un array 2x2, en donde: <BR>
	 * enroque[0][0] -> Blancas, enroque corto <BR>
	 * enroque[0][1] -> Blancas, enroque largo <BR>
	 * enroque[1][0] -> Negras, enroque corto <BR>
	 * enroque[1][1] -> Negras, enroque largo
	 */
	private boolean[][] enroque = new boolean[2][2];

	/**
	 * Este atributo contiene 0 en todos los casos salvo si se ha movido un peon
	 * dos casillas. En ese caso contendra la letra de la columna del peon. Es
	 * necesario para la captura al paso.
	 */
	private char alPaso;

	/**
	 * Este atributo sirve para contar movimientos a la hora de declarar las
	 * tablas por la regla de los 50 movimientos.
	 */
	private int contadorTablas;

	/**
	 * Vamos almacenando la clave resultante para calcular la siguiente posicion
	 * a partir de ella.
	 */
	private int clavePosicion;

	/**
	 * Tabla usada para guardar los indices para generar claves en la tabla
	 * hash.
	 */
	private int[][][][] indices = new int[2][6][8][8];

	/**
	 * Este atributo contiene el numero de las casillas en las que se encuentran
	 * los reyes. <BR>
	 * letraPosRey[BLANCO] -> Rey Blanco <BR>
	 * letraPosRey[NEGRO] -> Rey Negro <BR>
	 */

	private Casilla[] kingPosition = new Casilla[2];

	public Posicion() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++) {
				for (int k = 0; k < 8; k++) {
					for (int l = 0; l < 8; l++) {
						indices[i][j][k][l] = i
								* (new Double(Math.pow(10, 7))).intValue() + j
								* (new Double(Math.pow(10, 6))).intValue() + k
								* (new Double(Math.pow(10, 5))).intValue() + l
								* (new Double(Math.pow(10, 4))).intValue() + i
								* (new Double(Math.pow(10, 3))).intValue() + j
								* (new Double(Math.pow(10, 2))).intValue() + k
								* 10 + l;
					}
				}
			}
		}
		kingPosition[0] = new Casilla();
		kingPosition[1] = new Casilla();
	}

	/**
	 * Convierte el tipo de pieza expresado en castellano (P, C, A, etc.) al
	 * ingles.
	 * 
	 * @param c
	 *            Caracter que indica el tipo de pieza en castellano.
	 * @return Tipo de pieza en ingles, es decir: <BR>
	 *         P -> P <BR>
	 *         C -> N <BR>
	 *         A -> B <BR>
	 *         T -> R <BR>
	 *         D -> Q <BR>
	 *         R -> K <BR>
	 *         Otro caso -> \0
	 */
	private final static char tipoToEnglish(char t) {
		switch (t) {
		case 'A':
			return 'B';
		case 'C':
			return 'N';
		case 'D':
			return 'Q';
		case 'P':
			return 'P';
		case 'R':
			return 'K';
		case 'T':
			return 'R';
		default:
			return '\0';
		}
	}

	/**
	 * Convierte el booleano que indica el bando de una pieza a un entero.
	 * 
	 * @param c
	 *            Es el booleano en cuestion.
	 * @return 0 si el turno es blanco y 1 si el turno es negro
	 */
	private final static int bandoToInt(boolean c) {
		if (c) {
			return BLANCO;
		}
		return NEGRO;
	}

	/**
	 * Convierte el tipo de pieza expresado con un caracter (P, C, A, etc.) a un
	 * entero.
	 * 
	 * @param c
	 *            Caracter que indica el tipo de pieza.
	 * @return El entero que corresponde al tipo de pieza, es decir: <BR>
	 *         P -> 0 <BR>
	 *         C -> 1 <BR>
	 *         A -> 2 <BR>
	 *         T -> 3 <BR>
	 *         D -> 4 <BR>
	 *         R -> 5 <BR>
	 *         Otro caso -> -1
	 */
	private final static int tipoToInt(char c) {
		switch (c) {
		case 'P':
			return 0;
		case 'C':
			return 1;
		case 'A':
			return 2;
		case 'T':
			return 3;
		case 'D':
			return 4;
		case 'R':
			return 5;
		default:
			return -1;
		}
	}

	/**
	 * Construye una cadena FEN a partir de la posicion que hay en el tablero.
	 * 
	 * @return Un string, la cadena FEN.
	 */
	public String getFEN() {
		String cad = "";
		int cont = 0;
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j < 8; j++) {
				Pieza p = getPieza((char) ('a' + j), (char) ('1' + i));
				if (p == null)
					cont++;
				else {
					if (cont > 0)
						cad += cont;
					if (p.bandoBlanco)
						cad += tipoToEnglish(p.tipo);
					else
						cad += (Character.toLowerCase(tipoToEnglish(p.tipo)));
					cont = 0;
				}
			}
			if (cont > 0)
				cad += cont;
			if (i > 0)
				cad += '/';
			cont = 0;
		}

		if (turno == BLANCO)
			cad += " w";
		else
			cad += " b";

		cad += " ";
		if (!enroque[0][0] && !enroque[0][1] && !enroque[1][0]
				&& !enroque[1][1])
			cad += "-";
		else {
			if (enroque[0][0])
				cad += "K";
			if (enroque[0][1])
				cad += "Q";
			if (enroque[1][0])
				cad += "k";
			if (enroque[1][1])
				cad += "q";
		}
		if (alPaso != '\0')
			cad += (" " + alPaso);
		else
			cad += (" -");
		cad += (" " + contadorTablas);
		cad += " 1";
		return cad;
	}

	/**
	 * Coloca las piezas en el tablero en la posicion que indica posInicial.
	 * 
	 * @param pos
	 *            Es un String que indica una posicion de juego, siguiendo el
	 *            estandar FEN.
	 */
	public void setPosicion(String pos) {
		char let = 'a';
		char num = '8';
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tabla[i][j] = null;
			}
		}
		/*
		 * hash.borrarTabla (); movimientos.clear (); indice = 0;
		 */
		clavePosicion = 0;
		String[] FEN = pos.split(" ");
		for (int i = 0; i < FEN[0].length(); i++) {
			switch (FEN[0].charAt(i)) {
			case 'P':
				setPieza(new Pieza(true, 'P'), let, num);
				clavePosicion = clavePosicion
						^ indices[0][0][num - '1'][let - 'a'];
				let++;
				break;
			case 'p':
				setPieza(new Pieza(false, 'P'), let, num);
				clavePosicion = clavePosicion
						^ indices[1][0][num - '1'][let - 'a'];
				let++;
				break;
			case 'N':
				setPieza(new Pieza(true, 'C'), let, num);
				clavePosicion = clavePosicion
						^ indices[0][1][num - '1'][let - 'a'];
				let++;
				break;
			case 'n':
				setPieza(new Pieza(false, 'C'), let, num);
				clavePosicion = clavePosicion
						^ indices[1][1][num - '1'][let - 'a'];
				let++;
				break;
			case 'B':
				setPieza(new Pieza(true, 'A'), let, num);
				clavePosicion = clavePosicion
						^ indices[0][2][num - '1'][let - 'a'];
				let++;
				break;
			case 'b':
				setPieza(new Pieza(false, 'A'), let, num);
				clavePosicion = clavePosicion
						^ indices[1][2][num - '1'][let - 'a'];
				let++;
				break;
			case 'R':
				setPieza(new Pieza(true, 'T'), let, num);
				clavePosicion = clavePosicion
						^ indices[0][3][num - '1'][let - 'a'];
				let++;
				break;
			case 'r':
				setPieza(new Pieza(false, 'T'), let, num);
				clavePosicion = clavePosicion
						^ indices[1][3][num - '1'][let - 'a'];
				let++;
				break;
			case 'Q':
				setPieza(new Pieza(true, 'D'), let, num);
				clavePosicion = clavePosicion
						^ indices[0][4][num - '1'][let - 'a'];
				let++;
				break;
			case 'q':
				setPieza(new Pieza(false, 'D'), let, num);
				clavePosicion = clavePosicion
						^ indices[1][4][num - '1'][let - 'a'];
				let++;
				break;
			case 'K':
				setPieza(new Pieza(true, 'R'), let, num);
				clavePosicion = clavePosicion
						^ indices[0][5][num - '1'][let - 'a'];
				let++;
				break;
			case 'k':
				setPieza(new Pieza(false, 'R'), let, num);
				clavePosicion = clavePosicion
						^ indices[1][5][num - '1'][let - 'a'];
				let++;
				break;
			case '/':
				let = 'a';
				num--;
				break;
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
				let += (char) (FEN[0].charAt(i) - '0');
				break;
			default:
			}
		}
		if (FEN[1].charAt(0) == 'w') {
			turno = 0;
		} else {
			turno = 1;
		}
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= 1; j++) {
				enroque[i][j] = false;
			}
		}
		if (FEN[2].charAt(0) != '-') {
			for (int i = 0; i < FEN[2].length(); i++) {
				switch (FEN[2].charAt(i)) {
				case 'K':
					enroque[0][0] = true;
					break;
				case 'Q':
					enroque[0][1] = true;
					break;
				case 'k':
					enroque[1][0] = true;
					break;
				case 'q':
					enroque[1][1] = true;
					break;
				}
			}

		}
		if (FEN[3].charAt(0) == '-') {
			alPaso = '\0';
		} else {
			alPaso = FEN[3].charAt(0);

		}
		contadorTablas = (new Integer(FEN[4])).intValue();
		numeroMovimiento = (new Integer(FEN[5])).intValue();
	}

	/**
	 * Permite saber el numero de movimiento actual.
	 * 
	 * @return Un entero, el numero en cuestion.
	 */

	public int getNumMovimiento() {
		return numeroMovimiento;
	}

	/**
	 * Permite saber si el enroque corto esta disponible para un bando.
	 * 
	 * @param c
	 *            Bando del que se quiere obtener la informacion.
	 * @return True si el enroque corto puede realizarse, false en caso
	 *         contrario.
	 */
	public boolean getEnroqueCorto(int c) {
		return enroque[c][0];
	}

	/**
	 * Permite saber si el enroque largo esta disponible para un bando.
	 * 
	 * @param c
	 *            Bando del que se quiere obtener la informacion.
	 * @return True si el enroque largo puede realizarse, false en caso
	 *         contrario.
	 */
	public boolean getEnroqueLargo(int c) {
		return enroque[c][1];
	}

	/**
	 * Este metodo devuelve la pieza que se encuentra en una determinada
	 * casilla. Si no hay ninguna pieza, devuelve null.
	 * 
	 * @param letra
	 *            Es la letra de la casilla
	 * @param num
	 *            Es el numero de la casilla
	 * @return Devuelve la pieza que se encuentra en la casilla (letra, num),
	 *         null si la casilla esta vacia.
	 */
	public Pieza getPieza(char letra, char num) {
		return tabla[num - '1'][letra - 'a'];
	}

	/**
	 * Este metodo nos permite consultar el valor del turno.
	 * 
	 * @return Devuelve el valor del turno (0 -> blancas, 1 -> negras)
	 */
	public int getTurno() {
		return turno;
	}

	/**
	 * Este metodo alterna el turno. Si le tocaba a blancas le toca a negras y
	 * viceversa.
	 */
	public void setTurno() {
		if (turno == BLANCO) {
			turno = NEGRO;
		} else {
			turno = BLANCO;
		}
	}

	/**
	 * Este metodo permite dar el turno a cualquiera de los dos bandos.
	 * 
	 * @param t
	 *            BLANCO -> blancas <BR>
	 *            NEGRO -> negras
	 */
	public void setTurno(int t) {
		turno = t;
	}

	public boolean setEnroqueCorto(int b, boolean c) {
		if (b == BLANCO) {
			Pieza p = getPieza('h', '1');
			if (p != null && kingPosition[BLANCO].letra == 'e'
					&& kingPosition[BLANCO].numero == '1' && p.bandoBlanco
					&& p.tipo == 'T') {
				enroque[0][0] = c;
				return true;
			} else
				return false;
		} else if (b == NEGRO) {
			Pieza p = getPieza('h', '8');
			if (p != null && kingPosition[NEGRO].letra == 'e'
					&& kingPosition[NEGRO].numero == '8' && !p.bandoBlanco
					&& p.tipo == 'T') {
				enroque[1][0] = c;
				return true;
			} else
				return false;
		}
		return false;
	}

	public boolean setEnroqueLargo(int b, boolean c) {
		if (b == BLANCO) {
			Pieza p = getPieza('a', '1');
			if (p != null && kingPosition[BLANCO].letra == 'e'
					&& kingPosition[BLANCO].numero == '1' && p.bandoBlanco
					&& p.tipo == 'T') {
				enroque[0][1] = c;
				return true;
			} else
				return false;
		} else if (b == NEGRO) {
			Pieza p = getPieza('a', '8');
			if (p != null && kingPosition[BLANCO].letra == 'e'
					&& kingPosition[BLANCO].numero == '8' && !p.bandoBlanco
					&& p.tipo == 'T') {
				enroque[1][1] = c;
				return true;
			} else
				return false;
		}
		return false;
	}

	/**
	 * @return Devuelve numeroMovimiento.
	 */
	public int getNumeroMovimiento() {
		return numeroMovimiento;
	}

	/**
	 * Este metodo pone una pieza en el tablero en la casilla indicada. Ademas,
	 * cambia los atributos del objeto pieza que se le pasa como argumento,
	 * actualizando su situacion en el tablero.
	 * 
	 * @param pieza
	 *            Es la pieza que queremos poner en el tablero
	 * @param letra
	 *            Es la letra de la casilla
	 * @param num
	 *            Es el numero de la casilla
	 */
	public void setPieza(Pieza pieza, char letra, char num) {
		pieza.letra = letra;
		pieza.num = num;
		tabla[num - '1'][letra - 'a'] = pieza;
		clavePosicion = clavePosicion
				^ indices[bandoToInt(pieza.bandoBlanco)][tipoToInt(pieza.tipo)][num - '1'][letra - 'a'];

		if (pieza.tipo == 'R') {
			kingPosition[bandoToInt(pieza.bandoBlanco)].letra = pieza.letra;
			kingPosition[bandoToInt(pieza.bandoBlanco)].numero = pieza.num;
		}

	}

	/**
	 * Este metodo borra la pieza que se encuentra en una determinada casilla.
	 * Si la casilla esta vacia, no produce error y la deja vacia. Ademas,
	 * modifica el atributo enJuego de la pieza.
	 * 
	 * @param letra
	 *            Es la letra de la casilla
	 * @param num
	 *            Es el numero de la casilla
	 */
	public void borrarPieza(char letra, char num) {
		int iNum = num - '1', iLetra = letra - 'a';
		Pieza p = tabla[iNum][iLetra];
		// Se actualiza el estado de los enroques en caso de que se borre un rey
		// o una torre
		if (p != null) {
			if (p.tipo == 'R')
				if (p.bandoBlanco) {
					enroque[0][0] = false;
					enroque[0][1] = false;
					kingPosition[BLANCO].letra = '\0';
					kingPosition[BLANCO].numero = '\0';
				} else {
					enroque[1][0] = false;
					enroque[1][1] = false;
					kingPosition[NEGRO].letra = '\0';
					kingPosition[NEGRO].numero = '\0';
				}
			else if (p.tipo == 'T')
				if (p.letra == 'a' && p.num == '1' && p.bandoBlanco)
					enroque[0][1] = false;
				else if (p.letra == 'h' && p.num == '1' && p.bandoBlanco)
					enroque[0][0] = false;
				else if (p.letra == 'a' && p.num == '8' && !p.bandoBlanco)
					enroque[1][1] = false;
				else if (p.letra == 'h' && p.num == '8' && !p.bandoBlanco)
					enroque[1][0] = false;

			clavePosicion = clavePosicion
					^ indices[bandoToInt(p.bandoBlanco)][tipoToInt(p.tipo)][iNum][iLetra];
			tabla[num - '1'][letra - 'a'] = null;
		}
	}

	/**
	 * Consulta si una casilla esta vacia o no.
	 * 
	 * @return Devuelve true si la casilla esta vacia (contiene null), false en
	 *         caso contrario.
	 */
	public boolean esVacia(char let, char num) {
		return (getPieza(let, num) == null);
	}

	/**
	 * Permite obtener la clave hash numerica de la posicion actual.
	 * 
	 * @return Un entero, la clave en cuestion.
	 */
	public int getClavePosicion() {
		return clavePosicion;
	}

	/**
	 * @return Returns the alPaso.
	 */
	public char getAlPaso() {
		return alPaso;
	}

	/**
	 * Establece la columna en la que un peon puede ser comido al paso. Es
	 * decir, si un peon avanza dos casillas, entonces esa es la columna alPaso.
	 * Es condicicion necesaria que en esa columna haya un peon que haya
	 * avanzado dos casillas.
	 * 
	 * @param alPaso
	 *            La columna en la qeu se puede comer al paso. Se indica con la
	 *            letra de la columna.
	 */
	public void setAlPaso(char alPaso) {
		boolean encontrado = false;
		Pieza p = getPieza(alPaso, '4');
		encontrado = p != null && p.bandoBlanco && p.tipo == 'P';
		p = getPieza(alPaso, '5');
		encontrado = p != null && !p.bandoBlanco && p.tipo == 'P';
		if (encontrado)
			this.alPaso = alPaso;
	}

	/**
	 * @return Returns the contadorTablas.
	 */
	public int getContadorTablas() {
		return contadorTablas;
	}

	/**
	 * @param contadorTablas
	 *            The contadorTablas to set.
	 */
	public void setContadorTablas(int contadorTablas) {
		this.contadorTablas = contadorTablas;
	}

	public void addContadorTablas() {
		this.contadorTablas++;
	}

	/**
	 * @param numeroMovimiento
	 *            The numeroMovimiento to set.
	 */
	public void setNumeroMovimiento(int numeroMovimiento) {
		this.numeroMovimiento = numeroMovimiento;
	}

	public void addNumeroMovimiento() {
		this.numeroMovimiento++;
	}

	/**
	 * @return Returns the kingPosition.
	 */
	public Casilla getKingPosition(int color) {
		return kingPosition[color];
	}

	/**
	 * Funciones de uso interno. No usar directamente
	 * 
	 * @param letra
	 * @param num
	 */
	protected void borrarPiezaInternal(char letra, char num) {
		tabla[num - '1'][letra - 'a'] = null;
	}

	protected void setPiezaInternal(Pieza p, char letra, char num) {
		tabla[num - '1'][letra - 'a'] = p;
		if (p.tipo == 'R') {
			kingPosition[bandoToInt(p.bandoBlanco)].letra = p.letra;
			kingPosition[bandoToInt(p.bandoBlanco)].numero = p.num;
		}
	}
}
