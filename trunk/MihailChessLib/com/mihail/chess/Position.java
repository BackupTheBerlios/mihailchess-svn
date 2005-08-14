package com.mihail.chess;

import static com.mihail.chess.Board.Side;
import static com.mihail.chess.Piece.Tipo;

import com.mihail.chess.Board.Result;
import com.mihail.chess.Piece.Tipo;

public class Position {

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
	public final static String INITIAL_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	/**
	 * Este atributo es la representacion del tablero en la logica del programa.
	 * Es una matriz cuadrada de 64 casillas. El concepto de casilla no se
	 * representa con un objeto: directamente se almacena un objeto Pieza o null
	 * si la casilla esta vacia.
	 */
	private Piece[][] tabla = new Piece[8][8];

	/**
	 * Este atributo indica a quien le toca mover. Vale BLANCO cuando mueven
	 * blancas y NEGRO cuando mueven negras.
	 */
	private Side turno;

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

	private Square[] kingPosition = new Square[2];

	public Position() {
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
		turno = Side.WHITE;
		kingPosition[0] = new Square();
		kingPosition[1] = new Square();
		enroque[0][0] = false;
		enroque[0][1] = false;
		enroque[1][0] = false;
		enroque[1][1] = false;
		alPaso = 0;
		clavePosicion = 0;
		contadorTablas = 0;
		numeroMovimiento = 1;
	}

	public Position(String posicion) {
		this();
		setFEN(posicion);
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
	private final static int bandoToInt(Side c) {
		if (c == Side.WHITE) {
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
				Piece p = getPieza((char) ('a' + j), (char) ('1' + i));
				if (p == null)
					cont++;
				else {
					if (cont > 0)
						cad += cont;
					if (p.getBando() == Side.WHITE)
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

		if (turno == Side.WHITE)
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
	 * @see getFEN()
	 */
	public void setFEN(String pos) {
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
				setPiece(new Piece(Side.WHITE, Tipo.PEON), let, num);
				let++;
				break;
			case 'p':
				setPiece(new Piece(Side.BLACK, Tipo.PEON), let, num);
				let++;
				break;
			case 'N':
				setPiece(new Piece(Side.WHITE, Tipo.CABALLO), let, num);
				let++;
				break;
			case 'n':
				setPiece(new Piece(Side.BLACK, Tipo.CABALLO), let, num);
				let++;
				break;
			case 'B':
				setPiece(new Piece(Side.WHITE, Tipo.ALFIL), let, num);
				let++;
				break;
			case 'b':
				setPiece(new Piece(Side.BLACK, Tipo.ALFIL), let, num);
				let++;
				break;
			case 'R':
				setPiece(new Piece(Side.WHITE, Tipo.TORRE), let, num);
				let++;
				break;
			case 'r':
				setPiece(new Piece(Side.BLACK, Tipo.TORRE), let, num);
				let++;
				break;
			case 'Q':
				setPiece(new Piece(Side.WHITE, Tipo.DAMA), let, num);
				let++;
				break;
			case 'q':
				setPiece(new Piece(Side.BLACK, Tipo.DAMA), let, num);
				let++;
				break;
			case 'K':
				setPiece(new Piece(Side.WHITE, Tipo.REY), let, num);
				let++;
				break;
			case 'k':
				setPiece(new Piece(Side.BLACK, Tipo.REY), let, num);
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
			setTurn(Side.WHITE);
		} else {
			setTurn(Side.BLACK);
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
					setKingsideCastling(Side.WHITE, true);
					break;
				case 'Q':
					setQueensideCastling(Side.WHITE, true);
					break;
				case 'k':
					setKingsideCastling(Side.BLACK, true);
					break;
				case 'q':
					setQueensideCastling(Side.BLACK, true);
					break;
				}
			}

		}
		if (FEN[3].charAt(0) == '-') {
			setEnPassant('\0');
		} else {
			setEnPassant(FEN[3].charAt(0));
		}
		setHalfmoveClock((new Integer(FEN[4])).intValue());
		setFullmoveNumber((new Integer(FEN[5])).intValue());
	}

	/**
	 * Permite saber si el enroque corto esta disponible para un bando.
	 * 
	 * @param c
	 *            Bando del que se quiere obtener la informacion.
	 * @return True si el enroque corto puede realizarse, false en caso
	 *         contrario.
	 */
	public boolean getKingsideCastling(Side c) {
		switch (c) {
		case WHITE:
			return enroque[0][0];
		case BLACK:
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
	public boolean getQueensideCastling(Side c) {
		switch (c) {
		case WHITE:
			return enroque[0][1];
		case BLACK:
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
	public Piece getPieza(char letra, char num) {
		return tabla[num - '1'][letra - 'a'];
	}
	
	public Piece getPieza(Square c) {
		return getPieza(c.getLetra(), c.getNumero());
	}

	/**
	 * Este metodo nos permite consultar el valor del turno.
	 * 
	 * @return Devuelve el valor del turno (0 -> blancas, 1 -> negras)
	 */
	public Side getTurn() {
		return turno;
	}

	/**
	 * Este metodo alterna el turno. Si le tocaba a blancas le toca a negras y
	 * viceversa.
	 */
	public void setTurn() {
		if (turno == Side.WHITE) {
			turno = Side.BLACK;
		} else {
			turno = Side.WHITE;
		}
	}

	/**
	 * Este metodo permite dar el turno a cualquiera de los dos bandos.
	 * 
	 * @param t
	 *            BLANCO -> blancas <BR>
	 *            NEGRO -> negras
	 */
	public void setTurn(Side t) {
		turno = t;
	}

	public boolean setKingsideCastling(Side b, boolean c) {
		if (!c) {
			int x;
			switch (b) {
			case WHITE:
				x = 0;
				break;
			case BLACK:
				x = 1;
				break;
			default:
				throw new AssertionError(
						"El Bando solo puede ser BLANCO o NEGRO: " + this);
			}
			enroque[x][0] = c;
			return true;
		}
		if (b == Side.WHITE) {
			Piece p = getPieza('h', '1');
			if (p != null
					&& kingPosition[bandoToInt(Side.WHITE)].getLetra() == 'e'
					&& kingPosition[bandoToInt(Side.WHITE)].getNumero() == '1'
					&& p.getBando() == Side.WHITE
					&& p.getTipo() == Tipo.TORRE) {
				enroque[0][0] = c;
				return true;
			} else
				return false;
		} else if (b == Side.BLACK) {
			Piece p = getPieza('h', '8');
			if (p != null
					&& kingPosition[bandoToInt(Side.BLACK)].getLetra() == 'e'
					&& kingPosition[bandoToInt(Side.BLACK)].getNumero() == '8'
					&& p.getBando() == Side.BLACK && p.getTipo() == Tipo.TORRE) {
				enroque[1][0] = c;
				return true;
			} else
				return false;
		}
		return false;
	}

	public boolean setQueensideCastling(Side b, boolean c) {
		if (!c) {
			int x;
			switch (b) {
			case WHITE:
				x = 0;
				break;
			case BLACK:
				x = 1;
				break;
			default:
				throw new AssertionError(
						"El Bando solo puede ser BLANCO o NEGRO: " + this);
			}
			enroque[x][1] = c;
			return true;
		}
		if (b == Side.WHITE) {
			Piece p = getPieza('a', '1');
			if (p != null
					&& kingPosition[bandoToInt(Side.WHITE)].getLetra() == 'e'
					&& kingPosition[0].getNumero() == '1'
					&& p.getBando() == Side.WHITE
					&& p.getTipo() == Tipo.TORRE) {
				enroque[0][1] = c;
				return true;
			} else
				return false;
		} else if (b == Side.BLACK) {
			Piece p = getPieza('a', '8');
			if (p != null
					&& kingPosition[bandoToInt(Side.BLACK)].getLetra() == 'e'
					&& kingPosition[bandoToInt(Side.BLACK)].getNumero() == '8'
					&& p.getBando() == Side.BLACK && p.getTipo() == Tipo.TORRE) {
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
	public int getFullmoveNumber() {
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
	public void setPiece(Piece pieza, char letra, char num) {
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
	
	public void setPiece(Piece pieza, Square casilla) {
		setPiece(pieza, casilla.getLetra(), casilla.getNumero());
	}

	public void removePiece(Square casilla) {
		removePiece(casilla.getLetra(), casilla.getNumero());
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
	public void removePiece(char letra, char num) {
		int iNum = num - '1', iLetra = letra - 'a';
		Piece p = tabla[iNum][iLetra];
		// Se actualiza el estado de los enroques en caso de que se borre un rey
		// o una torre
		if (p != null) {
			if (p.getTipo() == Tipo.REY) {
				if (p.getBando() == Side.WHITE) {
					enroque[0][0] = false;
					enroque[0][1] = false;
					kingPosition[bandoToInt(Side.WHITE)].setLetra('\0');
					kingPosition[bandoToInt(Side.WHITE)].setNumero('\0');
				} else {
					enroque[1][0] = false;
					enroque[1][1] = false;
					kingPosition[bandoToInt(Side.BLACK)].setLetra('\0');
					kingPosition[bandoToInt(Side.BLACK)].setNumero('\0');
				}
			} else if (p.getTipo() == Tipo.TORRE) {
				if (p.getLetra() == 'a' && p.getNum() == '1'
						&& p.getBando() == Side.WHITE)
					enroque[0][1] = false;
				else if (p.getLetra() == 'h' && p.getNum() == '1'
						&& p.getBando() == Side.WHITE)
					enroque[0][0] = false;
				else if (p.getLetra() == 'a' && p.getNum() == '8'
						&& p.getBando() == Side.BLACK)
					enroque[1][1] = false;
				else if (p.getLetra() == 'h' && p.getNum() == '8'
						&& p.getBando() == Side.BLACK)
					enroque[1][0] = false;
			} else if (p.getTipo() == Tipo.PEON) {
				if (letra == alPaso
						&& (p.getBando() == Side.WHITE && num == '4')
						|| (p.getBando() == Side.BLACK && num == '5'))
					alPaso = 0;
			}
			clavePosicion = clavePosicion
					^ indices[bandoToInt(p.getBando())][tipoToInt(p.getTipo())][iNum][iLetra];
			tabla[num - '1'][letra - 'a'] = null;
		}
	}
	
	public boolean isEmpty(Square c) {
		return isEmpty(c.getLetra(), c.getNumero());
	}

	/**
	 * Consulta si una casilla esta vacia o no.
	 * 
	 * @return Devuelve true si la casilla esta vacia (contiene null), false en
	 *         caso contrario.
	 */
	public boolean isEmpty(char let, char num) {
		return (getPieza(let, num) == null);
	}

	/**
	 * Permite obtener la clave hash numerica de la posicion actual.
	 * 
	 * @return Un entero, la clave en cuestion.
	 */
	public int getPositionKey() {
		return clavePosicion;
	}

	/**
	 * @return Returns the alPaso.
	 */
	public char getEnPassant() {
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
	public void setEnPassant(char alPaso) {
		if (alPaso == '\0') {
			this.alPaso = alPaso;
			return;
		}
		boolean encontrado = false;
		Piece p;
		if (this.turno == Side.BLACK) {
			p = getPieza(alPaso, '4');
			encontrado = p != null && p.getBando() == Side.WHITE
					&& p.getTipo() == Tipo.PEON;
		} else {
			p = getPieza(alPaso, '5');
			encontrado = p != null && p.getBando() == Side.BLACK
					&& p.getTipo() == Tipo.PEON;
		}
		if (encontrado)
			this.alPaso = alPaso;
	}

	/**
	 * @return Returns the contadorTablas.
	 */
	public int getHalfmoveClock() {
		return contadorTablas;
	}

	/**
	 * @param contadorTablas
	 *            The contadorTablas to set.
	 */
	public void setHalfmoveClock(int contadorTablas) {
		this.contadorTablas = contadorTablas;
	}

	public void addHalfmoveClock() {
		this.contadorTablas++;
	}

	/**
	 * @param numeroMovimiento
	 *            The numeroMovimiento to set.
	 */
	public void setFullmoveNumber(int numeroMovimiento) {
		this.numeroMovimiento = numeroMovimiento;
	}

	public void addFullmoveNumber() {
		this.numeroMovimiento++;
	}

	/**
	 * @return Returns the kingPosition.
	 */
	public Square getKingPosition(Side color) {
		return kingPosition[bandoToInt(color)];
	}
	
	/**
	 * Genera la notación algebraica de un movimiento.
	 * 
	 * @param mov
	 *            Objeto movimiento del que se generara la notacion algebraica.
	 * @return Una cadena con el movimiento expresado en notacion algebraica,
	 *         (por ejemplo: Nf3, e6, Bd5...)
	 * @todo Resolver ambiguedades, coronaciones, jaques, mates...
	 * @todo Esto no esta bien aqui, es mejor dejarlo en Logica....
	 */
	private String generarNotacionALG (Movement mov) {
		StringBuffer temp = new StringBuffer ();

		if (mov.getTipoPieza() == Tipo.REY) {
			if (Math.abs (mov.getCasillaOrigen().getLetra() - mov.getCasillaDestino().getLetra() ) == 2) {
				if (mov.getCasillaDestino().getLetra() == 'g') {
					temp.append ("O-O");
				}
				else { // mov.destinoLetra == 'c'
					temp.append ("O-O-O");
				}
				return temp.toString ();
			}
		}
		if (mov.getTipoPieza() != Tipo.PEON) {
			temp.append (tipoToEnglish (mov.getTipoPieza()));
		}
		Piece pieza = getPieza (mov.getCasillaDestino());
		switch (mov.getTipoPieza()) {
			case PEON:
				if (mov.getCasillaComer() != null)
					temp.append (mov.getCasillaOrigen().getLetra());
				break;
			case CABALLO:
				for(DirectionVector v: pieza.getDirecciones()) {
					try {
						Piece p = getPieza(mov.getCasillaDestino().add(v));
						if(p!=null && p.getTipo() == Tipo.CABALLO && p.getBando() == pieza.getBando()) {
							if(mov.getCasillaOrigen().getLetra() != p.getCasilla().getLetra()) {
								temp.append(mov.getCasillaOrigen().getLetra());
								break;
							} else {
								temp.append (mov.getCasillaOrigen().getNumero());
								break;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}
				break;

			case ALFIL:
			case TORRE:
			case DAMA:
				for(DirectionVector v: pieza.getDirecciones()) {
					try {
						Square destino = mov.getCasillaDestino().add(v);
						Piece p;
						while((p=getPieza(destino))==null) {
							destino.add(v);
						}
						if(p!=null && p.getTipo() == pieza.getTipo() && p.getBando() == pieza.getBando()) {
							if(mov.getCasillaOrigen().getLetra() != p.getCasilla().getLetra()) {
								temp.append(mov.getCasillaOrigen().getLetra());
								break;
							} else {
								temp.append (mov.getCasillaOrigen().getNumero());
								break;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}
				break;
		}

		if (mov.getCasillaComer() != null)
			temp.append ("x");

		temp.append (mov.getCasillaDestino().toString());

		if (mov.getCoronacion() != null)
			temp.append ("=" + tipoToEnglish (mov.getCoronacion()));

		if (mov.isJaque())
			if (mov.getFinPartida() == Result.WHITE_CHECKMATE
					|| mov.getFinPartida() == Result.BLACK_CHECKMATE)
				temp.append ("#");
			else
				temp.append ("+");
		return temp.toString ();
	}

	/**
	 * Funciones de uso interno. No usar directamente
	 * 
	 * @param letra
	 * @param num
	 */
	void borrarPiezaInternal(char letra, char num) {
		tabla[num - '1'][letra - 'a'] = null;
	}

	void setPiezaInternal(Piece p, char letra, char num) {
		tabla[num - '1'][letra - 'a'] = p;
		if (p.getTipo() == Tipo.REY) {
			kingPosition[bandoToInt(p.getBando())].setLetra(letra);
			kingPosition[bandoToInt(p.getBando())].setNumero(num);
		}
	}
}
