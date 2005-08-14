package com.mihail.chess;

import java.util.ArrayList;

import com.mihail.chess.Piece.Tipo;

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
	private Tipo coronar = Tipo.DAMA;

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
		hash.insertar(posicion.getPositionKey());
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

	public void setPromotionPiece(Tipo c) {
		coronar = c;
	}

	/**
	 * Permite saber el numero total de movimientos de la partida.
	 * 
	 * @return Un entero, el numero en cuestion.
	 */

	public int getTotalFullmoveNumber() {
		return movimientos.getNumMovimientos();
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
		return movimientos.getLastMovimiento().getFinPartida();
	}

	/**
	 * Calcula los movimientos validos para todas las piezas del tablero.
	 */
	public void calculateMoves() {
		for (char i = 'a'; i <= 'h'; i++) {
			for (char j = '1'; j <= '8'; j++) {
				Piece p = posicion.getPieza(i, j);
				if (p != null) {
					if (!Piece.esBandoContrario(posicion.getTurn(), p)) {
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
		switch (pieza.getTipo()) {
		case PEON:
			// Peon
			// Peon blanco
			if (pieza.getBando() == Side.WHITE) {
				// Movimiento hacia delante
				// Hacemos dos iteraciones, una para el caso de que avance
				// una casilla, otra para el caso de que avance dos
				if (posicion.isEmpty(pieza.getLetra(),
						(char) (pieza.getNum() + 1))) {
					if (esLegal(pieza.getLetra(), pieza.getNum(), pieza
							.getLetra(), (char) (pieza.getNum() + 1))) {
						pieza.anadirMov(pieza.getLetra(), (char) (pieza
								.getNum() + 1));
						if (pieza.getNum() == '2'
								&& posicion.isEmpty(pieza.getLetra(),
										(char) (pieza.getNum() + 2))) {
							if (esLegal(pieza.getLetra(), pieza.getNum(), pieza
									.getLetra(), (char) (pieza.getNum() + 2))) {
								pieza.anadirMov(pieza.getLetra(), (char) (pieza
										.getNum() + 2));
							}
						}
					}
				}
				if (posicion.getEnPassant() != '\0'
						&& pieza.getNum() == '5'
						&& Math.abs(pieza.getLetra() - posicion.getEnPassant()) == 1) {
					pieza.anadirMov(posicion.getEnPassant(), (char) (pieza
							.getNum() + 1));
				}
				// Movimientos para comer
				try {
					Piece p = posicion.getPieza((char) (pieza.getLetra() + 1),
							(char) (pieza.getNum() + 1));
					if (p != null && p.getBando() == Side.BLACK) {
						if (esLegal(pieza.getLetra(), pieza.getNum(),
								(char) (pieza.getLetra() + 1), (char) (pieza
										.getNum() + 1))) {
							pieza.anadirMov((char) (pieza.getLetra() + 1),
									(char) (pieza.getNum() + 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				try {
					Piece p = posicion.getPieza((char) (pieza.getLetra() + 1),
							(char) (pieza.getNum() - 1));
					if (p != null && p.getBando() == Side.BLACK) {
						if (esLegal(pieza.getLetra(), pieza.getNum(),
								(char) (pieza.getLetra() - 1), (char) (pieza
										.getNum() + 1))) {
							pieza.anadirMov((char) (pieza.getLetra() - 1),
									(char) (pieza.getNum() + 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			// Peon negro
			else {
				if (posicion.isEmpty(pieza.getLetra(),
						(char) (pieza.getNum() - 1))) {
					if (esLegal(pieza.getLetra(), pieza.getNum(), pieza
							.getLetra(), (char) (pieza.getNum() - 1))) {
						pieza.anadirMov(pieza.getLetra(), (char) (pieza
								.getNum() - 1));
						if (pieza.getNum() == '7'
								&& posicion.isEmpty(pieza.getLetra(),
										(char) (pieza.getNum() - 2))) {
							if (esLegal(pieza.getLetra(), pieza.getNum(), pieza
									.getLetra(), (char) (pieza.getNum() - 2))) {
								pieza.anadirMov(pieza.getLetra(), (char) (pieza
										.getNum() - 2));
							}
						}
					}
				}
				if (posicion.getEnPassant() != '\0'
						&& pieza.getNum() == '4'
						&& Math.abs(pieza.getLetra() - posicion.getEnPassant()) == 1) {
					pieza.anadirMov(posicion.getEnPassant(), (char) (pieza
							.getNum() - 1));
				}
				try {
					Piece p = posicion.getPieza((char) (pieza.getLetra() - 1),
							(char) (pieza.getNum() + 1));
					if (p != null && p.getBando() == Side.BLACK) {
						if (esLegal(pieza.getLetra(), pieza.getNum(),
								(char) (pieza.getLetra() + 1), (char) (pieza
										.getNum() - 1))) {
							pieza.anadirMov((char) (pieza.getLetra() + 1),
									(char) (pieza.getNum() - 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				try {
					Piece p = posicion.getPieza((char) (pieza.getLetra() - 1),
							(char) (pieza.getNum() - 1));
					if (p != null && p.getBando() == Side.BLACK) {
						if (esLegal(pieza.getLetra(), pieza.getNum(),
								(char) (pieza.getLetra() - 1), (char) (pieza
										.getNum() - 1))) {
							pieza.anadirMov((char) (pieza.getLetra() - 1),
									(char) (pieza.getNum() - 1));
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			break;
		case CABALLO:
			for (DirectionVector v : pieza.getDirecciones()) {
				Square destino = pieza.getCasilla().add(v);
				try {
					if (esLegal(pieza.getLetra(), pieza.getNum(), destino
							.getLetra(), destino.getNumero())) {
						Piece p = posicion.getPieza(destino.getLetra(), destino
								.getNumero());
						if (p == null
								|| (p != null && Piece.esBandoContrario(pieza,
										p)))
							pieza.anadirMov(destino);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

			break;
		case DAMA:
		case ALFIL:
		case TORRE:
			for (DirectionVector v : pieza.getDirecciones()) {
				try {
					Square destino = pieza.getCasilla().add(v);
					Piece p = posicion.getPieza(destino.getLetra(), destino
							.getNumero());
					while (p == null) {
						p = posicion.getPieza(destino.getLetra(), destino
								.getNumero());
						if (esLegal(pieza.getLetra(), pieza.getNum(), destino
								.getLetra(), destino.getNumero())) {
							if (p == null
									|| (p != null && Piece.esBandoContrario(
											pieza, p))) {
								pieza.anadirMov(destino);
							}
						}
						destino = destino.add(v);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

			break;
		case REY:
			for (DirectionVector v : pieza.getDirecciones()) {
				Square destino = pieza.getCasilla().add(v);
				try {
					if (esLegal(pieza.getLetra(), pieza.getNum(), destino
							.getLetra(), destino.getNumero())) {
						Piece p = posicion.getPieza(destino.getLetra(), destino
								.getNumero());
						if (p == null
								|| (p != null && Piece.esBandoContrario(pieza,
										p)))
							pieza.anadirMov(destino);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

			if (posicion.getKingsideCastling(posicion.getTurn())
					&& !isAttackedSquare(posicion.getKingPosition(posicion
							.getTurn()))
					&& posicion.isEmpty((char) (pieza.getLetra() + 1), pieza
							.getNum())
					&& !isAttackedSquare((char) (pieza.getLetra() + 1), (pieza
							.getNum()))
					&& posicion.isEmpty((char) (pieza.getLetra() + 2), pieza
							.getNum())
					&& !isAttackedSquare((char) (pieza.getLetra() + 2), (pieza
							.getNum()))) {
				pieza.anadirMov((char) (pieza.getLetra() + 2), pieza.getNum());
			}
			if (posicion.getQueensideCastling(posicion.getTurn())
					&& !isAttackedSquare(posicion.getKingPosition(posicion
							.getTurn()))
					&& posicion.isEmpty((char) (pieza.getLetra() - 1), pieza
							.getNum())
					&& !isAttackedSquare((char) (pieza.getLetra() - 1), (pieza
							.getNum()))
					&& posicion.isEmpty((char) (pieza.getLetra() - 2), pieza
							.getNum())
					&& !isAttackedSquare((char) (pieza.getLetra() - 2), (pieza
							.getNum()))) {
				pieza.anadirMov((char) (pieza.getLetra() - 2), pieza.getNum());
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
		return isAttackedSquare(c.getLetra(), c.getNumero());
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
					if (Piece.esBandoContrario(posicion.getTurn(), p)
							&& p.getTipo() == Tipo.CABALLO) {
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
				if (Piece.esBandoContrario(posicion.getTurn(), p)
						&& (p.getTipo() == Tipo.DAMA
								|| p.getTipo() == Tipo.TORRE || (num + v.getY() == numDest
								&& letra + v.getX() == letDest && p.getTipo() == Tipo.REY))) {
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
				if (Piece.esBandoContrario(posicion.getTurn(), p)
						&& (p.getTipo() == Tipo.DAMA
								|| p.getTipo() == Tipo.ALFIL || (num + v.getY() == numDest
								&& letra + v.getX() == letDest && (p.getTipo() == Tipo.REY || (p
								.getTipo() == Tipo.PEON && p.getBando() == Side.BLACK))))) {
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
				if (Piece.esBandoContrario(posicion.getTurn(), p)
						&& (p.getTipo() == Tipo.DAMA
								|| p.getTipo() == Tipo.ALFIL || (num + v.getY() == numDest
								&& letra + v.getX() == letDest && (p.getTipo() == Tipo.REY || (p
								.getTipo() == Tipo.PEON && p.getBando() == Side.WHITE))))) {
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
	public Movement move(char origenLetra, char origenNum,
			char destinoLetra, char destinoNum) {
		Piece piezaQueMueve;
		int i;
		Movement mov;
		piezaQueMueve = posicion.getPieza(origenLetra, origenNum);

		// Comprobamos que en la casilla de origen hay una pieza
		if (piezaQueMueve == null) {
			return null;
		}

		if (Piece.esBandoContrario(posicion.getTurn(), piezaQueMueve)) {
			return null;
		}
		if (indice != movimientos.getNumHalfPly())
			return null;
		// Buscamos la casilla de destino entre las casillas validas de la
		// pieza.
		i = 0;
		do {
			// Buscamos la letra.
			while ((i < piezaQueMueve.getCasillasValidas().size())
					&& (destinoLetra != piezaQueMueve.getCasillasValidas().get(
							i).getLetra())) {
				i++;
				// Comprobamos si el numero de la letra encontrada coincide.
			}
			if (i < piezaQueMueve.getCasillasValidas().size()) {
				// Si se entra en el siguiente caso, es que el movimiento es
				// valido
				if (destinoNum == piezaQueMueve.getCasillasValidas().get(i)
						.getLetra()) {
					mov = new Movement();
					mov.setCasillaOrigen(new Square(origenLetra, origenNum));
					mov
							.setCasillaDestino(new Square(destinoLetra,
									destinoNum));
					mov.setNumeroMovimiento(posicion.getFullmoveNumber());
					mov.setBando(posicion.getTurn());
					mov.setTipoPieza(piezaQueMueve.getTipo());

					// Si se come ponemos el contador a 0
					if (!posicion.isEmpty(destinoLetra, destinoNum)) {
						posicion.setHalfmoveClock(0);
						hash.borrarTabla();
						mov.setCasillaComer(new Square(destinoLetra,
								destinoNum));
						mov.setTipoPiezaComida(posicion.getPieza(destinoLetra,
								destinoNum).getTipo());
						posicion.removePiece(mov.getCasillaComer());
					}

					// Se hacen los calculos especiales si se trata de un peon
					if (piezaQueMueve.getTipo() == Tipo.PEON) {
						// Se borra la pieza correspondiente si se come al paso
						if (Math.abs(destinoLetra - origenLetra) == 1
								&& posicion.isEmpty(destinoLetra, destinoNum)) {
							mov.setTipoPiezaComida(posicion.getPieza(
									destinoLetra, origenNum).getTipo());
							mov.setCasillaComer(new Square(destinoLetra,
									origenNum));
							posicion.removePiece(mov.getCasillaComer());
						}
						// Se establece la variable alPaso a su valor
						// correspondiente
						if (Math.abs(destinoNum - origenNum) == 2) {
							posicion.setEnPassant(origenLetra);
							mov.setAlPaso(posicion.getEnPassant());
						} else {
							posicion.setEnPassant('\0');
						}
						// Coronacion
						if (destinoNum == '1' || destinoNum == '8') {
							// if (mostrarDialogoCoronacion) {
							// mostrarDialogoCoronacion ();
							// }
							piezaQueMueve = new Piece(piezaQueMueve.getBando(),
									coronar);

							mov.setCoronacion(coronar);
						}
						posicion.setHalfmoveClock(0);
						hash.borrarTabla();
					}
					// Se hacen los calculos especiales si se trata de un rey
					if (piezaQueMueve.getTipo() == Tipo.REY) {
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

					mov.setContadorTablas(posicion.getHalfmoveClock());
					boolean[][] enroque = new boolean[2][2];
					enroque[0][0] = posicion.getKingsideCastling(Side.WHITE);
					enroque[0][1] = posicion.getQueensideCastling(Side.WHITE);
					enroque[1][0] = posicion.getKingsideCastling(Side.BLACK);
					enroque[1][1] = posicion.getQueensideCastling(Side.BLACK);
					mov.setEnroque(enroque);
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

					hash.insertar(posicion.getPositionKey());
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
	public Movement goBack() {
		Movement mov;
		Piece piezaQueMueve;
		if (indice < movimientos.getNumHalfPly()) {
			mov = movimientos.getMovimiento(indice);
			piezaQueMueve = posicion.getPieza(mov.getCasillaOrigen());
			// Si se come al paso
			if (piezaQueMueve.getTipo() == Tipo.PEON
					&& Math.abs(mov.getCasillaDestino().getLetra()
							- mov.getCasillaOrigen().getLetra()) == 1
					&& posicion.isEmpty(mov.getCasillaDestino())) {
				posicion.removePiece(mov.getCasillaComer());
			}
			// Si se corona
			if (mov.getCoronacion() != null) {
				piezaQueMueve = new Piece(piezaQueMueve.getBando(), mov
						.getCoronacion());
			}
			// Se hacen los calculos especiales si se trata de un rey
			if (piezaQueMueve.getTipo() == Tipo.REY) {
				// Movemos las torres en caso de enroque
				Square origen = mov.getCasillaOrigen();
				Square destino = mov.getCasillaDestino();
				if ((destino.getLetra() - origen.getLetra()) == 2) {
					Piece torre = posicion.getPieza('h', origen.getNumero());
					posicion.removePiece('h', origen.getNumero());
					posicion.setPiece(torre, 'f', origen.getNumero());
				} else {
					if ((destino.getLetra() - origen.getLetra()) == -2) {
						Piece torre = posicion
								.getPieza('a', origen.getNumero());
						posicion.removePiece('a', origen.getNumero());
						posicion.setPiece(torre, 'd', origen.getNumero());
					}
				}
			}
			posicion.removePiece(mov.getCasillaOrigen());
			posicion.setPiece(piezaQueMueve, mov.getCasillaDestino());
			posicion.setHalfmoveClock(mov.getContadorTablas());
			boolean[][] enroques = mov.getEnroque();
			posicion.setKingsideCastling(Side.WHITE, enroques[0][0]);
			posicion.setQueensideCastling(Side.WHITE, enroques[0][1]);
			posicion.setKingsideCastling(Side.BLACK, enroques[1][0]);
			posicion.setQueensideCastling(Side.BLACK, enroques[1][1]);
			posicion.setEnPassant(mov.getAlPaso());
			posicion.setTurn();
			indice++;
			posicion.setFullmoveNumber(mov.getNumeroMovimiento());
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
	public Movement goForward() {
		Movement mov;
		Piece piezaQueMueve;
		if (indice > 0) {
			indice--;
			mov = movimientos.getMovimiento(indice);
			posicion.setFullmoveNumber(mov.getNumeroMovimiento());
			piezaQueMueve = posicion.getPieza(mov.getCasillaDestino());
			if (mov.getCoronacion() != null) {
				piezaQueMueve = new Piece(piezaQueMueve.getBando(), mov
						.getCoronacion());
			}
			if (piezaQueMueve.getTipo() == Tipo.REY) {
				// Movemos las torres en caso de enroque
				Square origen = mov.getCasillaOrigen();
				Square destino = mov.getCasillaDestino();
				if ((destino.getLetra() - origen.getLetra()) == 2) {
					Piece torre = posicion.getPieza('h', origen.getNumero());
					posicion.removePiece('h', origen.getNumero());
					posicion.setPiece(torre, 'f', origen.getNumero());
				} else {
					if ((destino.getLetra() - origen.getLetra()) == -2) {
						Piece torre = posicion
								.getPieza('a', origen.getNumero());
						posicion.removePiece('a', origen.getNumero());
						posicion.setPiece(torre, 'd', origen.getNumero());
					}
				}
			}
			posicion.removePiece(mov.getCasillaDestino());
			if (mov.getTipoPiezaComida() != null) {
				posicion.setPiece(new Piece(
						piezaQueMueve.getBando() == Side.WHITE ? Side.WHITE
								: Side.BLACK, mov.getTipoPieza()), mov
						.getCasillaComer());
			}
			posicion.setPiece(piezaQueMueve, mov.getCasillaOrigen());
			posicion.setHalfmoveClock(mov.getContadorTablas());
			boolean[][] enroques = mov.getEnroque();
			posicion.setKingsideCastling(Side.WHITE, enroques[0][0]);
			posicion.setQueensideCastling(Side.WHITE, enroques[0][1]);
			posicion.setKingsideCastling(Side.BLACK, enroques[1][0]);
			posicion.setQueensideCastling(Side.BLACK, enroques[1][1]);
			posicion.setEnPassant(mov.getAlPaso());
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
		if (hash.getRepeticiones(posicion.getPositionKey()) == 3) {
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
						if (pieza.getTipo() != Tipo.REY) {
							fin2 = true;
						}
					} else {
						switch (pieza.getTipo()) {
						case PEON:
						case DAMA:
						case TORRE:
							fin2 = true;
							break;
						case ALFIL:
						case CABALLO:
							posibleMatInsuf = true;
							break;
						case REY:
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
						&& (!Piece.esBandoContrario(posicion.getTurn(), pieza))
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
	public Movement moveALG(String mov) {
		char origenLetra = '\0', origenNum = '\0', destinoLetra = '\0', destinoNum = '\0';
		char tipoPieza = 'P';
		Tipo piezaCoronacion = null;
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
					piezaCoronacion = Tipo.ALFIL;
				} else {
					tipoPieza = 'A';
				}
				break;
			case 'K':
				tipoPieza = 'R';
				break;
			case 'N':
				if (i != 0) {
					piezaCoronacion = Tipo.CABALLO;
				} else {
					tipoPieza = 'C';
				}
				break;
			case 'Q':
				if (i != 0) {
					piezaCoronacion = Tipo.DAMA;
				} else {
					tipoPieza = 'D';
				}
				break;
			case 'R':
				if (i != 0) {
					piezaCoronacion = Tipo.TORRE;
				} else {
					tipoPieza = 'T';
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
		if(piezaCoronacion!=null) {
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
			if (origenLetra == '\0' && origenNum != '\0') {
				for (char ii = 'a'; ii <= 'h'; ii++) {
					Piece p = posicion.getPieza(ii, origenNum);
					if (p.canMove(c)) {
						origenLetra = ii;
						break;
					}
				}
			} else if (origenLetra != '\0' && origenNum == '\0') {
				for (char ii = '1'; ii <= '8'; ii++) {
					Piece p = posicion.getPieza(origenLetra, ii);
					if (p.canMove(c)) {
						origenNum = ii;
						break;
					}
				}
			} else {
				for (char ii = '1'; ii <= '8'; ii++) {
					for (char jj = 'a'; jj <= 'h'; jj++) {
						Piece p = posicion.getPieza(jj, ii);
						if (p.canMove(c)) {
							origenNum = ii;
							break;
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
