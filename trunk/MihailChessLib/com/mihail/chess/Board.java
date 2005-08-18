package com.mihail.chess;

import java.util.ArrayList;

import com.mihail.chess.Piece.Type;

public class Board {

	public static enum Side {
		WHITE, BLACK;

		public boolean equals(Side b) {
			return this == b;
		}
	}

	public static enum Result {
		WHITE_CHECKMATE, BLACK_CHECKMATE, REPETITION_DRAW, FIFTY_MOV_DRAW, INSUF_MATERIAL_DRAW, STALEMATE
	}

	// public static final int JAQUE_MATE_BLANCO = 6;
	// public static final int JAQUE_MATE_NEGRO = 1;
	// public static final int TABLAS_REPETICION = 2;
	// public static final int TABLAS_50_MOV = 3;
	// public static final int TABLAS_INSUF_MATERIAL = 4;
	// public static final int TABLAS_AHOGADO = 5;

	/**
	 * Este atributo sirve para guardar la lista de movimientos de una partida.
	 */
	private VariationsTree movimientos;

	/**
	 * Este atributo sirve para saber en que posicion de la lista de movimientos
	 * nos encontramos, a la hora de avanzar y retroceder por una partida.
	 * Siempre va una posición por delante de la auténtica posición
	 */
	private int indice;

	/**
	 * Este atributo se utiliza para las coronaciones.
	 */
	private Type coronar = Type.QUEEN;

	/**
	 * Tabla hash usada para comprobar posiciones repetidas.
	 */
	private PositionsDictionary hash = new PositionsDictionary(51);

	/**
	 * Posicion actual en juego
	 */
	private Position posicion;

	/**
	 * Crea una nueva instancia de la clase y crea las piezas, colocandolas en
	 * la posicion que indica el String posInicial.
	 * 
	 * @param posInicial
	 *            Es un String que indica una posicion de juego, siguiendo el
	 *            estandar FEN.
	 */
	public Board(Position posInicial) {
		movimientos = new VariationsTree();
		posicion = posInicial;
		hash.insert(posicion.getPositionKey());
		this.calculateMoves();
	}

	/**
	 * Reinicia la posicion del tablero a la posicion inicial.
	 */
	public void restartBoard() {
		posicion.setFEN(Position.INITIAL_POSITION_FEN);
	}

	/**
	 * Indica a que pieza queremos coronar por defecto.
	 * 
	 * @param c
	 *            Caracter que indica el tipo de pieza a coronar (C, A, T, D).
	 */

	public void setPromotionPiece(Type c) {
		coronar = c;
	}

	/**
	 * Permite saber el numero total de movimientos de la partida.
	 * 
	 * @return Un entero, el numero en cuestion.
	 */

	public int getTotalFullmoveNumber() {
		return movimientos.getFullmoveNumber();
	}

	public Position getPosition() {
		return this.posicion;
	}

	/**
	 * Obtiene el resultado de la partida.
	 * 
	 * @return Devuelve un caracter: <BR>
	 *         'B' -> Victoria Blanca <BR>
	 *         'N' -> Victoria Negra <BR>
	 *         'T' -> Tablas <BR>
	 *         '\0' -> Partida Inacabada o Resultado Desconocido
	 */
	public Result getResult() {
		return movimientos.getLastMove().getFinPartida();
	}

	/**
	 * Calcula los movimientos validos para todas las piezas del tablero.
	 */
	public void calculateMoves() {
		for (char i = 'a'; i <= 'h'; i++) {
			for (char j = '1'; j <= '8'; j++) {
				Piece p = posicion.getPieza(i, j);
				if (p != null) {
					if (!Piece.isOppositeSide(posicion.getTurn(), p)) {
						calcularMovimientos(p);
					}
				}
			}
		}
	}

	/**
	 * Calcula los movimientos validos para una pieza en concreto, que se le
	 * pasa como parametro.
	 * 
	 * @param pieza
	 *            Pieza de la que queremos calcular sus movimientos legales
	 */
	private void calcularMovimientos(Piece pieza) {
		pieza.getCasillasValidas().clear();
		switch (pieza.getType()) {
		case PAWN:
			// Peon
			// Peon blanco
			if (pieza.getSide() == Side.WHITE) {
				// Movimiento hacia delante
				// Hacemos dos iteraciones, una para el caso de que avance
				// una casilla, otra para el caso de que avance dos
				if (posicion.isEmpty(pieza.getFile(),
						(char) (pieza.getRank() + 1))) {
					if (esLegal(pieza.getFile(), pieza.getRank(), pieza
							.getFile(), (char) (pieza.getRank() + 1))) {
						pieza.addMove(pieza.getFile(),
								(char) (pieza.getRank() + 1));
						if (pieza.getRank() == '2'
								&& posicion.isEmpty(pieza.getFile(),
										(char) (pieza.getRank() + 2))) {
							if (esLegal(pieza.getFile(), pieza.getRank(), pieza
									.getFile(), (char) (pieza.getRank() + 2))) {
								pieza.addMove(pieza.getFile(), (char) (pieza
										.getRank() + 2));
							}
						}
					}
				}
				if (posicion.getEnPassant() != '\0'
						&& pieza.getRank() == '5'
						&& Math.abs(pieza.getFile() - posicion.getEnPassant()) == 1) {
					pieza.addMove(posicion.getEnPassant(), (char) (pieza
							.getRank() + 1));
				}
				// Movimientos para comer
				try {
					Piece p = posicion.getPieza((char) (pieza.getFile() + 1),
							(char) (pieza.getRank() + 1));
					if (p != null && p.getSide() == Side.BLACK) {
						if (esLegal(pieza.getFile(), pieza.getRank(),
								(char) (pieza.getFile() + 1), (char) (pieza
										.getRank() + 1))) {
							pieza.addMove((char) (pieza.getFile() + 1),
									(char) (pieza.getRank() + 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				try {
					Piece p = posicion.getPieza((char) (pieza.getFile() - 1),
							(char) (pieza.getRank() + 1));
					if (p != null && p.getSide() == Side.BLACK) {
						if (esLegal(pieza.getFile(), pieza.getRank(),
								(char) (pieza.getFile() - 1), (char) (pieza
										.getRank() + 1))) {
							pieza.addMove((char) (pieza.getFile() - 1),
									(char) (pieza.getRank() + 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			// Peon negro
			else {
				if (posicion.isEmpty(pieza.getFile(),
						(char) (pieza.getRank() - 1))) {
					if (esLegal(pieza.getFile(), pieza.getRank(), pieza
							.getFile(), (char) (pieza.getRank() - 1))) {
						pieza.addMove(pieza.getFile(),
								(char) (pieza.getRank() - 1));
						if (pieza.getRank() == '7'
								&& posicion.isEmpty(pieza.getFile(),
										(char) (pieza.getRank() - 2))) {
							if (esLegal(pieza.getFile(), pieza.getRank(), pieza
									.getFile(), (char) (pieza.getRank() - 2))) {
								pieza.addMove(pieza.getFile(), (char) (pieza
										.getRank() - 2));
							}
						}
					}
				}
				if (posicion.getEnPassant() != '\0'
						&& pieza.getRank() == '4'
						&& Math.abs(pieza.getFile() - posicion.getEnPassant()) == 1) {
					pieza.addMove(posicion.getEnPassant(), (char) (pieza
							.getRank() - 1));
				}
				try {
					Piece p = posicion.getPieza((char) (pieza.getFile() + 1),
							(char) (pieza.getRank() - 1));
					if (p != null && p.getSide() == Side.WHITE) {
						if (esLegal(pieza.getFile(), pieza.getRank(),
								(char) (pieza.getFile() + 1), (char) (pieza
										.getRank() - 1))) {
							pieza.addMove((char) (pieza.getFile() + 1),
									(char) (pieza.getRank() - 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				try {
					Piece p = posicion.getPieza((char) (pieza.getFile() - 1),
							(char) (pieza.getRank() - 1));
					if (p != null && p.getSide() == Side.WHITE) {
						if (esLegal(pieza.getFile(), pieza.getRank(),
								(char) (pieza.getFile() - 1), (char) (pieza
										.getRank() - 1))) {
							pieza.addMove((char) (pieza.getFile() - 1),
									(char) (pieza.getRank() - 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			break;
		case KNIGHT:
			for (DirectionVector v : pieza.getDirections()) {
				Square destino = pieza.getSquare().add(v);
				try {
					if (esLegal(pieza.getFile(), pieza.getRank(), destino
							.getFile(), destino.getRank())) {
						Piece p = posicion.getPieza(destino.getFile(), destino
								.getRank());
						if (p == null || (p != null && p.isOppositeSide(pieza)))
							pieza.addMove(destino);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

			break;
		case QUEEN:
		case BISHOP:
		case ROOK:
			for (DirectionVector v : pieza.getDirections()) {
				try {
					Square destino = pieza.getSquare().add(v);
					Piece p = posicion.getPieza(destino.getFile(), destino
							.getRank());
					while (p == null) {
						if (esLegal(pieza.getFile(), pieza.getRank(), destino
								.getFile(), destino.getRank())) {
								pieza.addMove(destino);

						}
						destino = destino.add(v);
						p = posicion.getPieza(destino.getFile(), destino
								.getRank());
					}
					if (p != null && p.isOppositeSide(pieza)) {
						pieza.addMove(destino);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

			break;
		case KING:
			for (DirectionVector v : pieza.getDirections()) {
				Square destino = pieza.getSquare().add(v);
				try {
					if (esLegal(pieza.getFile(), pieza.getRank(), destino
							.getFile(), destino.getRank())) {
						Piece p = posicion.getPieza(destino.getFile(), destino
								.getRank());
						if (p == null || (p != null && p.isOppositeSide(pieza)))
							pieza.addMove(destino);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

			if (posicion.getKingsideCastling(posicion.getTurn())
					&& !isAttackedSquare(posicion.getKingPosition(posicion
							.getTurn()))
					&& posicion.isEmpty((char) (pieza.getFile() + 1), pieza
							.getRank())
					&& !isAttackedSquare((char) (pieza.getFile() + 1), (pieza
							.getRank()))
					&& posicion.isEmpty((char) (pieza.getFile() + 2), pieza
							.getRank())
					&& !isAttackedSquare((char) (pieza.getFile() + 2), (pieza
							.getRank()))) {
				pieza.addMove((char) (pieza.getFile() + 2), pieza.getRank());
			}
			if (posicion.getQueensideCastling(posicion.getTurn())
					&& !isAttackedSquare(posicion.getKingPosition(posicion
							.getTurn()))
					&& posicion.isEmpty((char) (pieza.getFile() - 1), pieza
							.getRank())
					&& !isAttackedSquare((char) (pieza.getFile() - 1), (pieza
							.getRank()))
					&& posicion.isEmpty((char) (pieza.getFile() - 2), pieza
							.getRank())
					&& !isAttackedSquare((char) (pieza.getFile() - 2), (pieza
							.getRank()))) {
				pieza.addMove((char) (pieza.getFile() - 2), pieza.getRank());
			}
			break;
		}
	}

	/**
	 * Metodo de utilidad que se comporta exactamente igual que
	 * esCasillaAtacada(char, char)
	 * 
	 * @param c
	 *            Casilla que queremos comprobar si esta siendo atacada
	 * @return Devuelve un booleano indicando si es una casilla atacada o no
	 */
	public boolean isAttackedSquare(Square c) {
		return isAttackedSquare(c.getFile(), c.getRank());
	}

	/**
	 * esCasillaAtacada determina si hay alguna pieza que ataque la casilla que
	 * se le pasa como parametro. <BR>
	 * NOTA sobre el codigo: se necesita comprobar el turno para: en el turno en
	 * el que mueves necesitas saber que casillas estan atacadas por el bando
	 * contrario, y solo por este bando. No interesan las casillas que ataca el
	 * bando que mueve. Ademas se puede comprobar que una casillas es atacada
	 * estando vacia lo cual implica que no hay colores contrarios y es
	 * necesario conocer el turno para saber quien ataca
	 * 
	 * @param letra
	 *            Letra de la casilla que queremos comprobar si esta siendo
	 *            atacada
	 * @param num
	 *            Número de la casilla que queremos comprobart si está siendo
	 *            atacada
	 * @return Devuelve un booleano indicando si es una casilla atacada o no
	 */
	public boolean isAttackedSquare(char letra, char num) {
		// Primero miro las casillas
		// a salto de caballo. Despues, las verticales, horizontales y
		// diagonales.

		// Casillas a salto de caballo
		ArrayList<DirectionVector> dir = new ArrayList<DirectionVector>();
		dir.add(new DirectionVector(1, 2));
		dir.add(new DirectionVector(-1, 2));
		dir.add(new DirectionVector(2, 1));
		dir.add(new DirectionVector(2, -1));
		dir.add(new DirectionVector(1, -2));
		dir.add(new DirectionVector(-1, -2));
		dir.add(new DirectionVector(-2, 1));
		dir.add(new DirectionVector(-2, -1));
		for (DirectionVector v : dir) {
			try {
				Piece p = posicion.getPieza((char) (letra + v.getX()),
						(char) (num + v.getY()));
				if (p != null) {
					if (Piece.isOppositeSide(posicion.getTurn(), p)
							&& p.getType() == Type.KNIGHT) {
						return true;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		dir.clear();
		dir.add(new DirectionVector(1, 0));
		dir.add(new DirectionVector(-1, 0));
		dir.add(new DirectionVector(0, 1));
		dir.add(new DirectionVector(0, -1));

		for (DirectionVector v : dir) {
			try {
				char letDest = (char) (letra + v.getX()), numDest = (char) (num + v
						.getY());
				Piece p = posicion.getPieza(letDest, numDest);
				while (p == null) {
					letDest = (char) (letDest + v.getX());
					numDest = (char) (numDest + v.getY());
					p = posicion.getPieza(letDest, numDest);
				}
				if (Piece.isOppositeSide(posicion.getTurn(), p)
						&& (p.getType() == Type.QUEEN
								|| p.getType() == Type.ROOK || (num + v.getY() == numDest
								&& letra + v.getX() == letDest && p.getType() == Type.KING))) {
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		dir.clear();
		dir.add(new DirectionVector(1, 1));
		dir.add(new DirectionVector(-1, 1));

		for (DirectionVector v : dir) {
			try {
				char letDest = (char) (letra + v.getX()), numDest = (char) (num + v
						.getY());
				Piece p = posicion.getPieza(letDest, numDest);
				while (p == null) {
					letDest = (char) (letDest + v.getX());
					numDest = (char) (numDest + v.getY());
					p = posicion.getPieza(letDest, numDest);
				}
				if (Piece.isOppositeSide(posicion.getTurn(), p)
						&& (p.getType() == Type.QUEEN
								|| p.getType() == Type.BISHOP || (num
								+ v.getY() == numDest
								&& letra + v.getX() == letDest && (p.getType() == Type.KING || (p
								.getType() == Type.PAWN && p.getSide() == Side.BLACK))))) {
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		dir.clear();
		dir.add(new DirectionVector(1, -1));
		dir.add(new DirectionVector(-1, -1));

		for (DirectionVector v : dir) {
			try {
				char letDest = (char) (letra + v.getX()), numDest = (char) (num + v
						.getY());
				Piece p = posicion.getPieza(letDest, numDest);
				while (p == null) {
					letDest = (char) (letDest + v.getX());
					numDest = (char) (numDest + v.getY());
					p = posicion.getPieza(letDest, numDest);
				}
				if (Piece.isOppositeSide(posicion.getTurn(), p)
						&& (p.getType() == Type.QUEEN
								|| p.getType() == Type.BISHOP || (num
								+ v.getY() == numDest
								&& letra + v.getX() == letDest && (p.getType() == Type.KING || (p
								.getType() == Type.PAWN && p.getSide() == Side.WHITE))))) {
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}

		return false;
	}

	/**
	 * Determina si un movimiento es legal, analizando la situacion en la
	 * partida.
	 * 
	 * @param letOrig
	 *            Letra de la casilla de origen.
	 * @param numOrig
	 *            Numero de la casilla de origen.
	 * @param letDest
	 *            Letra de la casilla de destino.
	 * @param numDest
	 *            Numero de la casilla de destino.
	 * @return True si el movimiento es legal, false en caso contrario.
	 */
	private boolean esLegal(char letOrig, char numOrig, char letDest,
			char numDest) {
		boolean resultado;
		Piece movida = posicion.getPieza(letOrig, numOrig);
		Piece temp = posicion.getPieza(letDest, numDest);
		posicion.borrarPiezaInternal(letOrig, numOrig);
		posicion.setPiezaInternal(movida, letDest, numDest);
		resultado = isAttackedSquare(posicion.getKingPosition(posicion
				.getTurn()));
		posicion.borrarPiezaInternal(letDest, numDest);
		posicion.setPiezaInternal(movida, letOrig, numOrig);
		if (temp != null)
			posicion.setPiezaInternal(temp, letDest, numDest);
		return !resultado;
	}

	/**
	 * Este metodo mueve una pieza en el tablero, comprobando que este
	 * movimiento sea permitido.
	 * 
	 * @param origenLetra
	 *            Es la letra de la casilla de origen
	 * @param origenNum
	 *            Es el numero de la casilla de origen
	 * @param destinoLetra
	 *            Es la letra de la casilla de destino
	 * @param destinoNum
	 *            Es el numero de la casilla de destino
	 * @return Devuelve un objeto Movimiento o null si no esta permitido.
	 */
	public Move move(char origenLetra, char origenNum, char destinoLetra,
			char destinoNum) {
		Piece piezaQueMueve;
		int i;
		Move mov;
		piezaQueMueve = posicion.getPieza(origenLetra, origenNum);

		// Comprobamos que en la casilla de origen hay una pieza
		if (piezaQueMueve == null) {
			return null;
		}

		if (Piece.isOppositeSide(posicion.getTurn(), piezaQueMueve)) {
			return null;
		}
		if (indice != movimientos.getHalfmoveNumber())
			return null;
		// Buscamos la casilla de destino entre las casillas validas de la
		// pieza.
		i = 0;
		do {
			// Buscamos la letra.
			while ((i < piezaQueMueve.getCasillasValidas().size())
					&& (destinoLetra != piezaQueMueve.getCasillasValidas().get(
							i).getFile())) {
				i++;
				// Comprobamos si el numero de la letra encontrada coincide.
			}
			if (i < piezaQueMueve.getCasillasValidas().size()) {
				// Si se entra en el siguiente caso, es que el movimiento es
				// valido
				if (destinoNum == piezaQueMueve.getCasillasValidas().get(i)
						.getRank()) {
					mov = new Move();
					mov.setCasillaOrigen(new Square(origenLetra, origenNum));
					mov.setCasillaDestino(new Square(destinoLetra, destinoNum));
					mov.setTipoPieza(piezaQueMueve.getType());

//					 Se hacen los calculos especiales si se trata de un peon
					if (piezaQueMueve.getType() == Type.PAWN) {
						// Se borra la pieza correspondiente si se come al paso
						if (Math.abs(destinoLetra - origenLetra) == 1
								&& posicion.isEmpty(destinoLetra, destinoNum)) {
							mov.setTipoPiezaComida(posicion.getPieza(
									destinoLetra, origenNum).getType());
							mov.setCasillaComer(new Square(destinoLetra,
									origenNum));
							posicion.removePiece(mov.getCasillaComer());
						}
						// Se establece la variable alPaso a su valor
						// correspondiente
						if (Math.abs(destinoNum - origenNum) == 2) {
							posicion.setEnPassant(origenLetra);
							mov.getFinalBoardStatus().setEnPassant(
									posicion.getEnPassant());
						} else {
							posicion.setEnPassant('\0');
						}
						// Coronacion
						if (destinoNum == '1' || destinoNum == '8') {
							// if (mostrarDialogoCoronacion) {
							// mostrarDialogoCoronacion ();
							// }
							piezaQueMueve = new Piece(piezaQueMueve.getSide(),
									coronar);

							mov.setCoronacion(coronar);
						}
						posicion.setHalfmoveClock(0);
						hash.clearDictionary();
					}
					// Se hacen los calculos especiales si se trata de un rey
					if (piezaQueMueve.getType() == Type.KING) {
						// Movemos las torres en caso de enroque
						if ((destinoLetra - origenLetra) == 2) {
							Piece torre = posicion.getPieza('h', origenNum);
							posicion.removePiece('h', origenNum);
							posicion.setPiece(torre, 'f', origenNum);
						} else {
							if ((destinoLetra - origenLetra) == -2) {
								Piece torre = posicion.getPieza('a', origenNum);
								posicion.removePiece('a', origenNum);
								posicion.setPiece(torre, 'd', origenNum);
							}
						}
					}
					
					// Si se come ponemos el contador a 0
					if (!posicion.isEmpty(destinoLetra, destinoNum)) {
						posicion.setHalfmoveClock(0);
						hash.clearDictionary();
						mov
								.setCasillaComer(new Square(destinoLetra,
										destinoNum));
						mov.setTipoPiezaComida(posicion.getPieza(destinoLetra,
								destinoNum).getType());
						posicion.removePiece(mov.getCasillaComer());
					}

					mov.getFinalBoardStatus().setStatus(posicion.status);
					if (posicion.getTurn() == Side.BLACK)
						posicion.addFullmoveNumber();
					posicion.setTurn();

					posicion.removePiece(origenLetra, origenNum);
					posicion.setPiece(piezaQueMueve, destinoLetra, destinoNum);
					posicion.addHalfmoveClock();
					calculateMoves();

					if (isAttackedSquare(posicion.getKingPosition(posicion
							.getTurn()))) {
						mov.setJaque(true);
					} else {
						mov.setJaque(false);
					}
					mov.setFinPartida(esFinPartida());

					hash.insert(posicion.getPositionKey());
					movimientos.appendMovimiento(mov);
					indice++;
					return mov;
				} else {
					i++;
				}
			} else {
				return null;
			}
		} while (true);
	}

	/**
	 * Avanza por la lista de movimientos de la partida.
	 * 
	 * @return El movimiento al que lleguemos en la lista de movimientos.
	 */
	public Move goForward() {
		Move mov;
		Piece piezaQueMueve;
		if (indice < movimientos.getHalfmoveNumber()) {
			mov = movimientos.getMove(indice);
			piezaQueMueve = posicion.getPieza(mov.getCasillaOrigen());
			// Si se come al paso
			if (piezaQueMueve.getType() == Type.PAWN
					&& Math.abs(mov.getCasillaDestino().getFile()
							- mov.getCasillaOrigen().getFile()) == 1
					&& posicion.isEmpty(mov.getCasillaDestino())) {
				posicion.removePiece(mov.getCasillaComer());
			}
			// Si se corona
			if (mov.getCoronacion() != null) {
				piezaQueMueve = new Piece(piezaQueMueve.getSide(), mov
						.getCoronacion());
			}
			// Se hacen los calculos especiales si se trata de un rey
			if (piezaQueMueve.getType() == Type.KING) {
				// Movemos las torres en caso de enroque
				Square origen = mov.getCasillaOrigen();
				Square destino = mov.getCasillaDestino();
				if ((destino.getFile() - origen.getFile()) == 2) {
					Piece torre = posicion.getPieza('h', origen.getRank());
					posicion.removePiece('h', origen.getRank());
					posicion.setPiece(torre, 'f', origen.getRank());
				} else {
					if ((destino.getFile() - origen.getFile()) == -2) {
						Piece torre = posicion.getPieza('a', origen.getRank());
						posicion.removePiece('a', origen.getRank());
						posicion.setPiece(torre, 'd', origen.getRank());
					}
				}
			}
			posicion.removePiece(mov.getCasillaOrigen());
			posicion.setPiece(piezaQueMueve, mov.getCasillaDestino());

			posicion.status.setStatus(mov.getFinalBoardStatus());

			posicion.setTurn();
			indice++;
			return mov;
		} else {
			return null;
		}
	}

	/**
	 * Retrocede por la lista de movimientos de la partida.
	 * 
	 * @return El movimiento al que lleguemos en la lista de movimientos.
	 */
	public Move goBack() {
		Move mov;
		Piece piezaQueMueve;
		if (indice > 0) {
			indice--;
			mov = movimientos.getMove(indice);
			piezaQueMueve = posicion.getPieza(mov.getCasillaDestino());
			if (mov.getCoronacion() != null) {
				piezaQueMueve = new Piece(piezaQueMueve.getSide(), mov
						.getCoronacion());
			}
			if (piezaQueMueve.getType() == Type.KING) {
				// Movemos las torres en caso de enroque
				Square origen = mov.getCasillaOrigen();
				Square destino = mov.getCasillaDestino();
				if ((destino.getFile() - origen.getFile()) == 2) {
					Piece torre = posicion.getPieza('f', origen.getRank());
					posicion.removePiece('f', origen.getRank());
					posicion.setPiece(torre, 'h', origen.getRank());
				} else {
					if ((destino.getFile() - origen.getFile()) == -2) {
						Piece torre = posicion.getPieza('d', origen.getRank());
						posicion.removePiece('d', origen.getRank());
						posicion.setPiece(torre, 'a', origen.getRank());
					}
				}
			}
			posicion.removePiece(mov.getCasillaDestino());
			if (mov.getTipoPiezaComida() != null) {
				posicion.setPiece(new Piece(
						piezaQueMueve.getSide() == Side.WHITE ? Side.BLACK
								: Side.WHITE, mov.getTipoPiezaComida()), mov
						.getCasillaComer());
			}
			posicion.setPiece(piezaQueMueve, mov.getCasillaOrigen());

			posicion.status.setStatus(mov.getFinalBoardStatus());

			posicion.setTurn();
			return mov;
		} else {
			return null;
		}
	}

	/**
	 * Este metodo comprueba si se produce alguna situacion en la que termine
	 * una partida.
	 * 
	 * @return Devuelve '\0' si la partida no se ha acabado, 'B' si las blancas
	 *         han dado jaque mate, 'N' si las negras han dado jaque mate o 'T'
	 *         si se produce una situacion de tablas.
	 */
	private Result esFinPartida() {
		Piece pieza;
		Result devolver = null;
		boolean fin = false, fin2 = false, posibleMatInsuf = false;
		char i, j;
		// Tablas por 50 movimientos
		if (posicion.getHalfmoveClock() == 50) {
			fin = true;
			devolver = Result.FIFTY_MOV_DRAW;
		}
		// Tablas por repeticion de posiciones
		if (hash.getRepetitions(posicion.getPositionKey()) == 3) {
			fin = true;
			devolver = Result.REPETITION_DRAW;
		}
		// Tablas por material insuficiente
		i = 'a';
		j = '1';
		while (i <= 'h' && !fin2) {
			while (j <= '8' && !fin2) {
				pieza = posicion.getPieza(i, j);
				if (pieza != null) {
					if (posibleMatInsuf) {
						if (pieza.getType() != Type.KING) {
							fin2 = true;
						}
					} else {
						switch (pieza.getType()) {
						case PAWN:
						case QUEEN:
						case ROOK:
							fin2 = true;
							break;
						case BISHOP:
						case KNIGHT:
							posibleMatInsuf = true;
							break;
						case KING:
							break;
						}
					}
				}
				j++;
			}
			j = '1';
			i++;
		}
		if (!fin2) {
			fin = true;
			devolver = Result.INSUF_MATERIAL_DRAW;
		}
		// Miramos si hay movimientos posibles
		i = 'a';
		j = '1';
		while (i <= 'h' && !fin) {
			while (j <= '8' && !fin) {
				pieza = posicion.getPieza(i, j);
				if ((pieza != null)
						&& (!Piece.isOppositeSide(posicion.getTurn(), pieza))
						&& (!pieza.getCasillasValidas().isEmpty())) {
					fin = true;
				}
				j++;
			}
			j = '1';
			i++;
		}
		if (!fin) {
			// Negras dan jaque mate
			if (posicion.getTurn() == Side.WHITE)
				if (isAttackedSquare(posicion.getKingPosition(Side.WHITE))) {
					devolver = Result.BLACK_CHECKMATE;
				} else {
					devolver = Result.STALEMATE;
				}
			else
			// Blancas dan jaque mate
			if (isAttackedSquare(posicion.getKingPosition(Side.BLACK))) {
				devolver = Result.WHITE_CHECKMATE;
			}
			// Tablas por ahogado
			else {
				devolver = Result.STALEMATE;
			}
		}
		return devolver;
	}

	/**
	 * Interpreta un movimiento en notacion algebraica y lo realiza en la
	 * Logica. NOTA: Partimos de la base de que el turno corresponde con el
	 * movimiento que se recibe: no se puede dar el caso de que se reciba un
	 * movimiento de negras y que el turno pertenezca a blancas o viceversa.
	 * 
	 * @param mov
	 *            String que contiene el movimiento en notacion algebraica.
	 * @todo Poner las ambigüedades de la dama
	 * @todo Puede ser que tengamos un problema: cuando una pieza esta clavada
	 *       con el rey, no hay que marcar la posible ambiguedad. Hay que
	 *       tenerlo en cuenta.
	 */
	public Move moveALG(String mov) {
		char origenLetra = '\0', origenNum = '\0', destinoLetra = '\0', destinoNum = '\0';
		Type tipoPieza = Type.PAWN;
		Type piezaCoronacion = null;
		int contadorOesEnroque = 0, i;
		boolean finDestino = false;

		for (i = mov.length() - 1; i >= 0; i--) {
			switch (mov.charAt(i)) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
				if (!finDestino) {
					destinoNum = mov.charAt(i);
				} else {
					origenNum = mov.charAt(i);
				}
				break;
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
				if (!finDestino) {
					destinoLetra = mov.charAt(i);
				} else {
					origenLetra = mov.charAt(i);
				}
				finDestino = true;
				break;
			case 'B':
				if (i != 0) {
					piezaCoronacion = Type.BISHOP;
				} else {
					tipoPieza = Type.BISHOP;
				}
				break;
			case 'K':
				tipoPieza = Type.KING;
				break;
			case 'N':
				if (i != 0) {
					piezaCoronacion = Type.KNIGHT;
				} else {
					tipoPieza = Type.KNIGHT;
				}
				break;
			case 'Q':
				if (i != 0) {
					piezaCoronacion = Type.QUEEN;
				} else {
					tipoPieza = Type.QUEEN;
				}
				break;
			case 'R':
				if (i != 0) {
					piezaCoronacion = Type.ROOK;
				} else {
					tipoPieza = Type.ROOK;
				}
				break;
			case '0':
			case 'o':
			case 'O':
				contadorOesEnroque++;
				break;
			case 'x':
			case '+':
			case '!':
			case '?':
			case '-':
			case '=':
				break;
			default:
				System.out.print("Caracter desconocido: " + mov.charAt(i));
				break;
			}
		}
		if (piezaCoronacion != null) {
			// TODO hacer el mostrarDialogoCoronacion;
			coronar = piezaCoronacion;
		}
		// Enroque Corto
		if (contadorOesEnroque == 2) {
			origenLetra = 'e';
			destinoLetra = 'g';
			if (posicion.getTurn() == Side.WHITE) {
				origenNum = '1';
				destinoNum = '1';
			} else { // turno == NEGRO
				origenNum = '8';
				destinoNum = '8';
			}
		} else if (contadorOesEnroque == 3) {
			origenLetra = 'e';
			destinoLetra = 'c';
			if (posicion.getTurn() == Side.WHITE) {
				origenNum = '1';
				destinoNum = '1';
			} else { // turno == NEGRO
				origenNum = '8';
				destinoNum = '8';
			}
		} else {
			Square c = new Square(destinoLetra, destinoNum);
			if (origenLetra == '\0' || origenNum == '\0') {
				if (origenLetra == '\0' && origenNum != '\0') {
					for (char ii = 'a'; ii <= 'h'; ii++) {
						Piece p = posicion.getPieza(ii, origenNum);
						if (p!=null && p.getType() == tipoPieza  && !Piece.isOppositeSide(posicion.getTurn(), p) && p.canMove(c)) {
							origenLetra = ii;
							break;
						}
					}
				} else if (origenLetra != '\0' && origenNum == '\0') {
					for (char ii = '1'; ii <= '8'; ii++) {
						Piece p = posicion.getPieza(origenLetra, ii);
						if (p!=null && p.getType() == tipoPieza  && !Piece.isOppositeSide(posicion.getTurn(), p) && p.canMove(c)) {
							origenNum = ii;
							break;
						}
					}
				} else {
					boolean finished = false;
					for (char ii = '1'; ii <= '8' && !finished; ii++) {
						for (char jj = 'a'; jj <= 'h'; jj++) {
							Piece p = posicion.getPieza(jj, ii);
							if (p!=null && p.getType() == tipoPieza  && !Piece.isOppositeSide(posicion.getTurn(), p) && p.canMove(c)) {
								origenNum = ii;
								origenLetra  = jj;
								finished = true;
								break;
							}
						}
					}
				}
			}
		}
		if (origenLetra == '\0' || origenNum == '\0' || destinoLetra == '\0'
				|| destinoNum == '\0') {
			System.out.println(tipoPieza + " " + origenLetra + " " + origenNum
					+ " " + destinoLetra + " " + destinoNum);
			return null;
		} else {
			return move(origenLetra, origenNum, destinoLetra, destinoNum);
		}
	}
}
