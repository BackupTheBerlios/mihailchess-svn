package com.mihail.chess;

import static com.mihail.chess.Logica.Bando;
import static com.mihail.chess.Pieza.Tipo;

public class Posicion {

	/**
	 * Constante que representa al bando blanco.
	 */
	// public final static int BLANCO = 0;
	/**
	 * Constante que representa al bando negro.
	 */
	// public final static int NEGRO = 1;
	/**
	 * Constante que representa la cadena FEN con la posición inicial en el
	 * tablero.
	 */
	public final static String CAD_INICIAL = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

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
	private Bando turno;

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
		turno = Bando.BLANCO;
		kingPosition[0] = new Casilla();
		kingPosition[1] = new Casilla();
		enroque[0][0] = false;
		enroque[0][1] = false;
		enroque[1][0] = false;
		enroque[1][1] = false;
		alPaso = 0;
		clavePosicion = 0;
		contadorTablas = 0;
		numeroMovimiento = 1;
	}

	public Posicion(String posicion) {
		this();
		setPosicion(posicion);
	}

	/**
	 * Convierte el tipo de pieza expresado en castellano (P, C, A, etc.) al
	 * ingles.
	 * 
	 * @param t
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
	private final static char tipoToEnglish(Tipo t) {
		switch (t) {
		case ALFIL:
			return 'B';
		case CABALLO:
			return 'N';
		case DAMA:
			return 'Q';
		case PEON:
			return 'P';
		case REY:
			return 'K';
		case TORRE:
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
	private final static int bandoToInt(Bando c) {
		if (c == Bando.BLANCO) {
			return 0;
		}
		return 1;
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
	private final static int tipoToInt(Tipo c) {
		switch (c) {
		case PEON:
			return 0;
		case CABALLO:
			return 1;
		case ALFIL:
			return 2;
		case TORRE:
			return 3;
		case DAMA:
			return 4;
		case REY:
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
					if (p.getBando() == Bando.BLANCO)
						cad += tipoToEnglish(p.getTipo());
					else
						cad += (Character
								.toLowerCase(tipoToEnglish(p.getTipo())));
					cont = 0;
				}
			}
			if (cont > 0)
				cad += cont;
			if (i > 0)
				cad += '/';
			cont = 0;
		}

		if (turno == Bando.BLANCO)
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
		cad += (" " + numeroMovimiento);
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
				setPieza(new Pieza(Bando.BLANCO, Tipo.PEON), let, num);
				let++;
				break;
			case 'p':
				setPieza(new Pieza(Bando.NEGRO, Tipo.PEON), let, num);
				let++;
				break;
			case 'N':
				setPieza(new Pieza(Bando.BLANCO, Tipo.CABALLO), let, num);
				let++;
				break;
			case 'n':
				setPieza(new Pieza(Bando.NEGRO, Tipo.CABALLO), let, num);
				let++;
				break;
			case 'B':
				setPieza(new Pieza(Bando.BLANCO, Tipo.ALFIL), let, num);
				let++;
				break;
			case 'b':
				setPieza(new Pieza(Bando.NEGRO, Tipo.ALFIL), let, num);
				let++;
				break;
			case 'R':
				setPieza(new Pieza(Bando.BLANCO, Tipo.TORRE), let, num);
				let++;
				break;
			case 'r':
				setPieza(new Pieza(Bando.NEGRO, Tipo.TORRE), let, num);
				let++;
				break;
			case 'Q':
				setPieza(new Pieza(Bando.BLANCO, Tipo.DAMA), let, num);
				let++;
				break;
			case 'q':
				setPieza(new Pieza(Bando.NEGRO, Tipo.DAMA), let, num);
				let++;
				break;
			case 'K':
				setPieza(new Pieza(Bando.BLANCO, Tipo.REY), let, num);
				let++;
				break;
			case 'k':
				setPieza(new Pieza(Bando.NEGRO, Tipo.REY), let, num);
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
			setTurno(Bando.BLANCO);
		} else {
			setTurno(Bando.NEGRO);
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
					setEnroqueCorto(Bando.BLANCO, true);
					break;
				case 'Q':
					setEnroqueLargo(Bando.BLANCO, true);
					break;
				case 'k':
					setEnroqueCorto(Bando.NEGRO, true);
					break;
				case 'q':
					setEnroqueLargo(Bando.NEGRO, true);
					break;
				}
			}

		}
		if (FEN[3].charAt(0) == '-') {
			setAlPaso('\0');
		} else {
			setAlPaso(FEN[3].charAt(0));
		}
		setContadorTablas((new Integer(FEN[4])).intValue());
		setNumeroMovimiento((new Integer(FEN[5])).intValue());
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
	public boolean getEnroqueCorto(Bando c) {
		switch (c) {
		case BLANCO:
			return enroque[0][0];
		case NEGRO:
			return enroque[1][0];
		}
		throw new AssertionError("El Bando solo puede ser BLANCO o NEGRO: "
				+ this);
	}

	/**
	 * Permite saber si el enroque largo esta disponible para un bando.
	 * 
	 * @param c
	 *            Bando del que se quiere obtener la informacion.
	 * @return True si el enroque largo puede realizarse, false en caso
	 *         contrario.
	 */
	public boolean getEnroqueLargo(Bando c) {
		switch (c) {
		case BLANCO:
			return enroque[0][1];
		case NEGRO:
			return enroque[1][1];
		}
		throw new AssertionError("El Bando solo puede ser BLANCO o NEGRO: "
				+ this);
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
	public Bando getTurno() {
		return turno;
	}

	/**
	 * Este metodo alterna el turno. Si le tocaba a blancas le toca a negras y
	 * viceversa.
	 */
	public void setTurno() {
		if (turno == Bando.BLANCO) {
			turno = Bando.NEGRO;
		} else {
			turno = Bando.BLANCO;
		}
	}

	/**
	 * Este metodo permite dar el turno a cualquiera de los dos bandos.
	 * 
	 * @param t
	 *            BLANCO -> blancas <BR>
	 *            NEGRO -> negras
	 */
	public void setTurno(Bando t) {
		turno = t;
	}

	public boolean setEnroqueCorto(Bando b, boolean c) {
		if (!c) {
			int x;
			switch (b) {
			case BLANCO:
				x = 0;
				break;
			case NEGRO:
				x = 1;
				break;
			default:
				throw new AssertionError(
						"El Bando solo puede ser BLANCO o NEGRO: " + this);
			}
			enroque[x][0] = c;
			return true;
		}
		if (b == Bando.BLANCO) {
			Pieza p = getPieza('h', '1');
			if (p != null
					&& kingPosition[bandoToInt(Bando.BLANCO)].getLetra() == 'e'
					&& kingPosition[bandoToInt(Bando.BLANCO)].getNumero() == '1'
					&& p.getBando() == Bando.BLANCO
					&& p.getTipo() == Tipo.TORRE) {
				enroque[0][0] = c;
				return true;
			} else
				return false;
		} else if (b == Bando.NEGRO) {
			Pieza p = getPieza('h', '8');
			if (p != null
					&& kingPosition[bandoToInt(Bando.NEGRO)].getLetra() == 'e'
					&& kingPosition[bandoToInt(Bando.NEGRO)].getNumero() == '8'
					&& p.getBando() == Bando.NEGRO && p.getTipo() == Tipo.TORRE) {
				enroque[1][0] = c;
				return true;
			} else
				return false;
		}
		return false;
	}

	public boolean setEnroqueLargo(Bando b, boolean c) {
		if (!c) {
			int x;
			switch (b) {
			case BLANCO:
				x = 0;
				break;
			case NEGRO:
				x = 1;
				break;
			default:
				throw new AssertionError(
						"El Bando solo puede ser BLANCO o NEGRO: " + this);
			}
			enroque[x][1] = c;
			return true;
		}
		if (b == Bando.BLANCO) {
			Pieza p = getPieza('a', '1');
			if (p != null
					&& kingPosition[bandoToInt(Bando.BLANCO)].getLetra() == 'e'
					&& kingPosition[0].getNumero() == '1'
					&& p.getBando() == Bando.BLANCO
					&& p.getTipo() == Tipo.TORRE) {
				enroque[0][1] = c;
				return true;
			} else
				return false;
		} else if (b == Bando.NEGRO) {
			Pieza p = getPieza('a', '8');
			if (p != null
					&& kingPosition[bandoToInt(Bando.NEGRO)].getLetra() == 'e'
					&& kingPosition[bandoToInt(Bando.NEGRO)].getNumero() == '8'
					&& p.getBando() == Bando.NEGRO && p.getTipo() == Tipo.TORRE) {
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
		pieza.setLetra(letra);
		pieza.setNum(num);
		tabla[num - '1'][letra - 'a'] = pieza;
		clavePosicion = clavePosicion
				^ indices[bandoToInt(pieza.getBando())][tipoToInt(pieza
						.getTipo())][num - '1'][letra - 'a'];

		if (pieza.getTipo() == Tipo.REY) {
			kingPosition[bandoToInt(pieza.getBando())] = pieza.getCasilla();
		}
	}

	public void borrarPieza(Casilla casilla) {
		borrarPieza(casilla.getLetra(), casilla.getNumero());
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
			if (p.getTipo() == Tipo.REY) {
				if (p.getBando() == Bando.BLANCO) {
					enroque[0][0] = false;
					enroque[0][1] = false;
					kingPosition[bandoToInt(Bando.BLANCO)].setLetra('\0');
					kingPosition[bandoToInt(Bando.BLANCO)].setNumero('\0');
				} else {
					enroque[1][0] = false;
					enroque[1][1] = false;
					kingPosition[bandoToInt(Bando.NEGRO)].setLetra('\0');
					kingPosition[bandoToInt(Bando.NEGRO)].setNumero('\0');
				}
			} else if (p.getTipo() == Tipo.TORRE) {
				if (p.getLetra() == 'a' && p.getNum() == '1'
						&& p.getBando() == Bando.BLANCO)
					enroque[0][1] = false;
				else if (p.getLetra() == 'h' && p.getNum() == '1'
						&& p.getBando() == Bando.BLANCO)
					enroque[0][0] = false;
				else if (p.getLetra() == 'a' && p.getNum() == '8'
						&& p.getBando() == Bando.NEGRO)
					enroque[1][1] = false;
				else if (p.getLetra() == 'h' && p.getNum() == '8'
						&& p.getBando() == Bando.NEGRO)
					enroque[1][0] = false;
			} else if (p.getTipo() == Tipo.PEON) {
				if (letra == alPaso
						&& (p.getBando() == Bando.BLANCO && num == '4')
						|| (p.getBando() == Bando.NEGRO && num == '5'))
					alPaso = 0;
			}
			clavePosicion = clavePosicion
					^ indices[bandoToInt(p.getBando())][tipoToInt(p.getTipo())][iNum][iLetra];
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
		if (alPaso == '\0') {
			this.alPaso = alPaso;
			return;
		}
		boolean encontrado = false;
		Pieza p;
		if (this.turno == Bando.NEGRO) {
			p = getPieza(alPaso, '4');
			encontrado = p != null && p.getBando() == Bando.BLANCO
					&& p.getTipo() == Tipo.PEON;
		} else {
			p = getPieza(alPaso, '5');
			encontrado = p != null && p.getBando() == Bando.NEGRO
					&& p.getTipo() == Tipo.PEON;
		}
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
	public Casilla getKingPosition(Bando color) {
		return kingPosition[bandoToInt(color)];
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
		if (p.getTipo() == Tipo.REY) {
			kingPosition[bandoToInt(p.getBando())].setLetra(letra);
			kingPosition[bandoToInt(p.getBando())].setNumero(num);
		}
	}
}
