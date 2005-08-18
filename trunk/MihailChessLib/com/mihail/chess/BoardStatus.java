package com.mihail.chess;

import com.mihail.chess.Board.Side;

public class BoardStatus {
	/**
	 * Este atributo indica el valor de contadorTablas en el momento que se
	 * produce el movimiento.
	 */
	private int contadorTablas;

	/**
	 * Este atributo indica que enroques estan disponibles para que bandos en el
	 * momento en que se produce el movimiento. Es un array 2x2, en donde: <BR>
	 * enroque[0][0] -> Blancas, enroque corto <BR>
	 * enroque[0][1] -> Blancas, enroque largo <BR>
	 * enroque[1][0] -> Negras, enroque corto <BR>
	 * enroque[1][1] -> Negras, enroque largo <BR>
	 */
	private boolean[][] enroque;

	/**
	 * Este atributo contiene 0 en todos los casos salvo si se ha movido un peon
	 * dos casillas. En ese caso contendra la letra de la columna del peon. Es
	 * necesario para la captura al paso.
	 */
	private char alPaso;
	
	/**
	 * Numero de movimiento en la partida. Es el mismo numero para blancas que
	 * para negras. Por ejemplo: en '1. e4 c5', tanto e4 como c5 compartirian el
	 * 1 como numero de movimiento.
	 */
	private int numeroMovimiento;
	

	/**
	 * Bando que mueve.
	 */
	private Side bando;
	
	public BoardStatus() {
		enroque = new boolean[2][2];
		enroque[0][0] = false;
		enroque[0][1] = false;
		enroque[1][0] = false;
		enroque[1][1] = false;
	}
	
	public BoardStatus(BoardStatus original) {
		this.setStatus(original);
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
	
	public void setKingsideCastling(Side c, boolean b) {
		switch (c) {
		case WHITE:
			enroque[0][0] = b;
			return;
		case BLACK:
			enroque[1][0] = b;
			return;
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
	
	public void setQueensideCastling(Side c, boolean b) {
		switch (c) {
		case WHITE:
			enroque[0][1] = b;
			return;
		case BLACK:
			enroque[1][1] = b;
			return;
		}
		throw new AssertionError("El Bando solo puede ser BLANCO o NEGRO: "
				+ this);
	}
	
	/**
	 * Este metodo nos permite consultar el valor del turno.
	 * 
	 * @return Devuelve el valor del turno (0 -> blancas, 1 -> negras)
	 */
	public Side getTurn() {
		return bando;
	}
	

	/**
	 * Este metodo permite dar el turno a cualquiera de los dos bandos.
	 * 
	 * @param t
	 *            BLANCO -> blancas <BR>
	 *            NEGRO -> negras
	 */
	public void setTurn(Side t) {
		bando = t;
	}
	
	/**
	 * @return Devuelve numeroMovimiento.
	 */
	public int getFullmoveNumber() {
		return numeroMovimiento;
	}
	
	public void setFullmoveNumber(int num) {
		this.numeroMovimiento = num;
	}
	
	/**
	 * @return Returns the alPaso.
	 */
	public char getEnPassant() {
		return alPaso;
	}
	
	public void setEnPassant(char columna) {
		this.alPaso = columna;
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
	
	public void setStatus(BoardStatus original) {
		this.alPaso = original.alPaso;
		this.bando = original.bando;
		this.contadorTablas = original.contadorTablas;
		this.enroque  = original.enroque;
		this.numeroMovimiento = original.numeroMovimiento;
	}
}
