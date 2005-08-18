package com.mihail.chess;

import static com.mihail.chess.Board.Side;
import static com.mihail.chess.Piece.Type;

import com.mihail.chess.Board.Result;
import com.mihail.chess.Board.Side;
import com.mihail.chess.Piece.Type;

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
	
	BoardStatus status;

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
		status = new BoardStatus();
		status.setTurn(Side.WHITE);
		kingPosition[0] = new Square();
		kingPosition[1] = new Square();
		status.setEnPassant('\0');
		clavePosicion = 0;
		status.setHalfmoveClock(0);
		status.setFullmoveNumber(1);
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
	private final static char tipoToEnglish(Type t) {
		switch (t) {
		case BISHOP:
			return 'B';
		case KNIGHT:
			return 'N';
		case QUEEN:
			return 'Q';
		case PAWN:
			return 'P';
		case KING:
			return 'K';
		case ROOK:
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
	private final static int tipoToInt(Type c) {
		switch (c) {
		case PAWN:
			return 0;
		case KNIGHT:
			return 1;
		case BISHOP:
			return 2;
		case ROOK:
			return 3;
		case QUEEN:
			return 4;
		case KING:
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
					if (p.getSide() == Side.WHITE)
						cad += tipoToEnglish(p.getType());
					else
						cad += (Character
								.toLowerCase(tipoToEnglish(p.getType())));
					cont = 0;
				}
			}
			if (cont > 0)
				cad += cont;
			if (i > 0)
				cad += '/';
			cont = 0;
		}

		if (status.getTurn() == Side.WHITE)
			cad += " w";
		else
			cad += " b";

		cad += " ";
		if (!status.getKingsideCastling(Side.WHITE) &&
			!status.getQueensideCastling(Side.WHITE) &&
			!status.getKingsideCastling(Side.BLACK) &&
			!status.getQueensideCastling(Side.BLACK))
			cad += "-";
		else {
			if (status.getKingsideCastling(Side.WHITE))
				cad += "K";
			if (status.getQueensideCastling(Side.WHITE))
				cad += "Q";
			if (status.getKingsideCastling(Side.BLACK))
				cad += "k";
			if (status.getQueensideCastling(Side.BLACK))
				cad += "q";
		}
		if (status.getEnPassant() != '\0')
			cad += (" " + status.getEnPassant());
		else
			cad += (" -");
		cad += (" " + status.getFullmoveNumber());
		cad += (" " + status.getHalfmoveClock());
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
		status = new BoardStatus();
		/*
		 * hash.borrarTabla (); movimientos.clear (); indice = 0;
		 */
		clavePosicion = 0;
		String[] FEN = pos.split(" ");
		for (int i = 0; i < FEN[0].length(); i++) {
			switch (FEN[0].charAt(i)) {
			case 'P':
				setPiece(new Piece(Side.WHITE, Type.PAWN), let, num);
				let++;
				break;
			case 'p':
				setPiece(new Piece(Side.BLACK, Type.PAWN), let, num);
				let++;
				break;
			case 'N':
				setPiece(new Piece(Side.WHITE, Type.KNIGHT), let, num);
				let++;
				break;
			case 'n':
				setPiece(new Piece(Side.BLACK, Type.KNIGHT), let, num);
				let++;
				break;
			case 'B':
				setPiece(new Piece(Side.WHITE, Type.BISHOP), let, num);
				let++;
				break;
			case 'b':
				setPiece(new Piece(Side.BLACK, Type.BISHOP), let, num);
				let++;
				break;
			case 'R':
				setPiece(new Piece(Side.WHITE, Type.ROOK), let, num);
				let++;
				break;
			case 'r':
				setPiece(new Piece(Side.BLACK, Type.ROOK), let, num);
				let++;
				break;
			case 'Q':
				setPiece(new Piece(Side.WHITE, Type.QUEEN), let, num);
				let++;
				break;
			case 'q':
				setPiece(new Piece(Side.BLACK, Type.QUEEN), let, num);
				let++;
				break;
			case 'K':
				setPiece(new Piece(Side.WHITE, Type.KING), let, num);
				let++;
				break;
			case 'k':
				setPiece(new Piece(Side.BLACK, Type.KING), let, num);
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
			status.setTurn(Side.WHITE);
		} else {
			status.setTurn(Side.BLACK);
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
		status.setHalfmoveClock((new Integer(FEN[4])).intValue());
		status.setFullmoveNumber((new Integer(FEN[5])).intValue());
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
		return getPieza(c.getFile(), c.getRank());
	}

	/**
	 * Este metodo alterna el turno. Si le tocaba a blancas le toca a negras y
	 * viceversa.
	 */
	public void setTurn() {
		if (status.getTurn() == Side.WHITE) {
			status.setTurn(Side.BLACK);
		} else {
			status.setTurn(Side.WHITE);
		}
	}
	
	public boolean setKingsideCastling(Side b, boolean c) {
		if (!c) {
			status.setKingsideCastling(b, false);
			return true;
		}
		if (b == Side.WHITE) {
			Piece p = getPieza('h', '1');
			if (p != null
					&& kingPosition[bandoToInt(Side.WHITE)].getFile() == 'e'
					&& kingPosition[bandoToInt(Side.WHITE)].getRank() == '1'
					&& p.getSide() == Side.WHITE
					&& p.getType() == Type.ROOK) {
				status.setKingsideCastling(Side.WHITE, c);
				return true;
			} else
				return false;
		} else if (b == Side.BLACK) {
			Piece p = getPieza('h', '8');
			if (p != null
					&& kingPosition[bandoToInt(Side.BLACK)].getFile() == 'e'
					&& kingPosition[bandoToInt(Side.BLACK)].getRank() == '8'
					&& p.getSide() == Side.BLACK && p.getType() == Type.ROOK) {
				status.setKingsideCastling(Side.BLACK, c);
				return true;
			} else
				return false;
		}
		return false;
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
		return status.getKingsideCastling(c);
	}

	public boolean setQueensideCastling(Side b, boolean c) {
		if (!c) {
			status.setQueensideCastling(b, false);
			return true;
		}
		if (b == Side.WHITE) {
			Piece p = getPieza('a', '1');
			if (p != null
					&& kingPosition[bandoToInt(Side.WHITE)].getFile() == 'e'
					&& kingPosition[0].getRank() == '1'
					&& p.getSide() == Side.WHITE
					&& p.getType() == Type.ROOK) {
				status.setQueensideCastling(Side.WHITE, c);
				return true;
			} else
				return false;
		} else if (b == Side.BLACK) {
			Piece p = getPieza('a', '8');
			if (p != null
					&& kingPosition[bandoToInt(Side.BLACK)].getFile() == 'e'
					&& kingPosition[bandoToInt(Side.BLACK)].getRank() == '8'
					&& p.getSide() == Side.BLACK && p.getType() == Type.ROOK) {
				status.setQueensideCastling(Side.BLACK, c);
				return true;
			} else
				return false;
		}
		return false;
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
		return status.getQueensideCastling(c);
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
		pieza.setFile(letra);
		pieza.setRank(num);
		tabla[num - '1'][letra - 'a'] = pieza;
		clavePosicion = clavePosicion
				^ indices[bandoToInt(pieza.getSide())][tipoToInt(pieza
						.getType())][num - '1'][letra - 'a'];

		if (pieza.getType() == Type.KING) {
			kingPosition[bandoToInt(pieza.getSide())] = pieza.getSquare();
		}
	}
	
	public void setPiece(Piece pieza, Square casilla) {
		setPiece(pieza, casilla.getFile(), casilla.getRank());
	}

	public void removePiece(Square casilla) {
		removePiece(casilla.getFile(), casilla.getRank());
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
			if (p.getType() == Type.KING) {
				if (p.getSide() == Side.WHITE) {
					status.setKingsideCastling(Side.WHITE, false);
					status.setQueensideCastling(Side.WHITE, false);
					kingPosition[bandoToInt(Side.WHITE)].setFile('\0');
					kingPosition[bandoToInt(Side.WHITE)].setRank('\0');
				} else {
					status.setKingsideCastling(Side.BLACK, false);
					status.setQueensideCastling(Side.BLACK, false);
					kingPosition[bandoToInt(Side.BLACK)].setFile('\0');
					kingPosition[bandoToInt(Side.BLACK)].setRank('\0');
				}
			} else if (p.getType() == Type.ROOK) {
				if (p.getFile() == 'a' && p.getRank() == '1'
						&& p.getSide() == Side.WHITE)
					status.setQueensideCastling(Side.WHITE, false);
				else if (p.getFile() == 'h' && p.getRank() == '1'
						&& p.getSide() == Side.WHITE)
					status.setKingsideCastling(Side.WHITE, false);
				else if (p.getFile() == 'a' && p.getRank() == '8'
						&& p.getSide() == Side.BLACK)
					status.setQueensideCastling(Side.BLACK, false);
				else if (p.getFile() == 'h' && p.getRank() == '8'
						&& p.getSide() == Side.BLACK)
					status.setKingsideCastling(Side.BLACK, false);
			} else if (p.getType() == Type.PAWN) {
				if (letra == status.getEnPassant()
						&& (p.getSide() == Side.WHITE && num == '4')
						|| (p.getSide() == Side.BLACK && num == '5'))
					status.setEnPassant('\0');
			}
			clavePosicion = clavePosicion
					^ indices[bandoToInt(p.getSide())][tipoToInt(p.getType())][iNum][iLetra];
			tabla[num - '1'][letra - 'a'] = null;
		}
	}
	
	public boolean isEmpty(Square c) {
		return isEmpty(c.getFile(), c.getRank());
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
			this.status.setEnPassant(alPaso);
			return;
		}
		boolean encontrado = false;
		Piece p;
		if (this.status.getTurn() == Side.BLACK) {
			p = getPieza(alPaso, '4');
			encontrado = p != null && p.getSide() == Side.WHITE
					&& p.getType() == Type.PAWN;
		} else {
			p = getPieza(alPaso, '5');
			encontrado = p != null && p.getSide() == Side.BLACK
					&& p.getType() == Type.PAWN;
		}
		if (encontrado)
			this.status.setEnPassant(alPaso);
	}

	public void addFullmoveNumber() {
		this.status.setFullmoveNumber(this.status.getFullmoveNumber()+1);
	}
	
	public void addHalfmoveClock() {
		this.status.setHalfmoveClock(this.status.getHalfmoveClock()+1);
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
	private String generarNotacionALG (Move mov) {
		StringBuffer temp = new StringBuffer ();

		if (mov.getTipoPieza() == Type.KING) {
			if (Math.abs (mov.getCasillaOrigen().getFile() - mov.getCasillaDestino().getFile() ) == 2) {
				if (mov.getCasillaDestino().getFile() == 'g') {
					temp.append ("O-O");
				}
				else { // mov.destinoLetra == 'c'
					temp.append ("O-O-O");
				}
				return temp.toString ();
			}
		}
		if (mov.getTipoPieza() != Type.PAWN) {
			temp.append (tipoToEnglish (mov.getTipoPieza()));
		}
		Piece pieza = getPieza (mov.getCasillaDestino());
		switch (mov.getTipoPieza()) {
			case PAWN:
				if (mov.getCasillaComer() != null)
					temp.append (mov.getCasillaOrigen().getFile());
				break;
			case KNIGHT:
				for(DirectionVector v: pieza.getDirections()) {
					try {
						Piece p = getPieza(mov.getCasillaDestino().add(v));
						if(p!=null && p.getType() == Type.KNIGHT && p.getSide() == pieza.getSide()) {
							if(mov.getCasillaOrigen().getFile() != p.getSquare().getFile()) {
								temp.append(mov.getCasillaOrigen().getFile());
								break;
							} else {
								temp.append (mov.getCasillaOrigen().getRank());
								break;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}
				break;

			case BISHOP:
			case ROOK:
			case QUEEN:
				for(DirectionVector v: pieza.getDirections()) {
					try {
						Square destino = mov.getCasillaDestino().add(v);
						Piece p;
						while((p=getPieza(destino))==null) {
							destino.add(v);
						}
						if(p!=null && p.getType() == pieza.getType() && p.getSide() == pieza.getSide()) {
							if(mov.getCasillaOrigen().getFile() != p.getSquare().getFile()) {
								temp.append(mov.getCasillaOrigen().getFile());
								break;
							} else {
								temp.append (mov.getCasillaOrigen().getRank());
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
	 * Este metodo nos permite consultar el valor del turno.
	 * 
	 * @return Devuelve el valor del turno (0 -> blancas, 1 -> negras)
	 */
	public Side getTurn() {
		return status.getTurn();
	}
	

	/**
	 * Este metodo permite dar el turno a cualquiera de los dos bandos.
	 * 
	 * @param t
	 *            BLANCO -> blancas <BR>
	 *            NEGRO -> negras
	 */
	public void setTurn(Side t) {
		status.setTurn(t);
	}
	
	/**
	 * @return Devuelve numeroMovimiento.
	 */
	public int getFullmoveNumber() {
		return status.getFullmoveNumber();
	}
	
	public void setFullmoveNumber(int num) {
		status.setFullmoveNumber(num);
	}
	
	/**
	 * @return Returns the alPaso.
	 */
	public char getEnPassant() {
		return status.getEnPassant();
	}
	
	/**
	 * @return Returns the contadorTablas.
	 */
	public int getHalfmoveClock() {
		return status.getHalfmoveClock();
	}

	/**
	 * @param contadorTablas
	 *            The contadorTablas to set.
	 */
	public void setHalfmoveClock(int contadorTablas) {
		status.setHalfmoveClock(contadorTablas);
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
		if (p.getType() == Type.KING) {
			kingPosition[bandoToInt(p.getSide())].setFile(letra);
			kingPosition[bandoToInt(p.getSide())].setRank(num);
		}
	}
}
