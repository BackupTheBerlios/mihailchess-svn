package com.mihail.chess;

import static com.mihail.chess.Board.Bando;

import com.mihail.chess.Board.Resultado;
import com.mihail.chess.Piece.Tipo;

/**
 * Esta clase define un objeto Movimiento, que guarda informacion sobre el
 * movimiento que se realiza y sobre el estado de la partida en ese momento (qué
 * enroques son posibles, es jaque, etc).
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */

public final class Movement {

	/**
	 * Este atributo indica la letra de la casilla origen.
	 */
	// public char origenLetra;
	/**
	 * Este atributo indica el numero de la casilla origen.
	 */
	// public char origenNum;
	private Square casillaOrigen;

	/**
	 * Este atributo indica la letra de la casilla destino.
	 */
	// public char destinoLetra;
	/**
	 * Este atributo indica el numero de la casilla destino.
	 */
	// public char destinoNum;
	private Square casillaDestino;

	/**
	 * Numero de movimiento en la partida. Es el mismo numero para blancas que
	 * para negras. Por ejemplo: en '1. e4 c5', tanto e4 como c5 compartirian el
	 * 1 como numero de movimiento.
	 */
	private int numeroMovimiento;

	/**
	 * Bando que mueve.
	 */
	private Bando bando;

	/**
	 * Este atributo indica el tipo de pieza que es: {P,C,A,T,D,R}
	 * 
	 */
	private Tipo tipoPieza;

	/**
	 * Este atributo indica la letra de la casilla donde se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	// public char casillaComerLetra;
	/**
	 * Este atributo indica el numero de la casilla donde se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	// public char casillaComerNum;
	private Square casillaComer;

	/**
	 * Este atributo indica si el movimiento produce un jaque.
	 */
	private boolean jaque;

	/**
	 * Este atributo indica si el movimiento termina la partida y quien es el
	 * ganador o bien tablas. <BR>
	 * 'B' -> Ganan blancas <BR>
	 * 'N' -> Ganan negras <BR>
	 * 'T' -> Tablas <BR>
	 * 0 -> No ha terminado la partida
	 */
	private Resultado finPartida;

	/**
	 * Este atributo indica si se ha producido una coronacion y a que pieza se
	 * promociona. <BR>
	 * 'C' -> Caballo <BR>
	 * 'A' -> Alfil <BR>
	 * 'T' -> Torre <BR>
	 * 'D' -> Dama <BR>
	 * 0 -> No hay coronacion
	 */
	private Tipo coronacion;

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
	 * Este atributo indica el tipo de la pieza que se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	private Tipo tipoPiezaComida;

	/**
	 * Este atributo guarda la representacion del movimiento en notacion
	 * algebraica.
	 */
	private String notacion;

	public Square getCasillaDestino() {
		return casillaDestino;
	}

	public void setCasillaDestino(Square casillaDestino) {
		this.casillaDestino = casillaDestino;
	}

	public Square getCasillaOrigen() {
		return casillaOrigen;
	}

	public void setCasillaOrigen(Square casillaOrigen) {
		this.casillaOrigen = casillaOrigen;
	}

	public Bando getBando() {
		return bando;
	}

	public void setBando(Bando bando) {
		this.bando = bando;
	}

	public int getNumeroMovimiento() {
		return numeroMovimiento;
	}

	public void setNumeroMovimiento(int numeroMovimiento) {
		this.numeroMovimiento = numeroMovimiento;
	}

	public Tipo getTipoPieza() {
		return tipoPieza;
	}

	public void setTipoPieza(Tipo tipoPieza) {
		this.tipoPieza = tipoPieza;
	}

	public Square getCasillaComer() {
		return casillaComer;
	}

	public void setCasillaComer(Square casillaComer) {
		this.casillaComer = casillaComer;
	}

	public int getContadorTablas() {
		return contadorTablas;
	}

	public void setContadorTablas(int contadorTablas) {
		this.contadorTablas = contadorTablas;
	}

	public Tipo getCoronacion() {
		return coronacion;
	}

	public void setCoronacion(Tipo coronacion) {
		this.coronacion = coronacion;
	}

	public boolean[][] getEnroque() {
		return enroque;
	}

	public void setEnroque(boolean[][] enroque) {
		this.enroque = enroque;
	}

	public Resultado getFinPartida() {
		return finPartida;
	}

	public void setFinPartida(Resultado finPartida) {
		this.finPartida = finPartida;
	}

	public boolean isJaque() {
		return jaque;
	}

	public void setJaque(boolean jaque) {
		this.jaque = jaque;
	}

	public char getAlPaso() {
		return alPaso;
	}

	public void setAlPaso(char alPaso) {
		this.alPaso = alPaso;
	}

	public String getNotacion() {
		return notacion;
	}

	public void setNotacion(String notacion) {
		this.notacion = notacion;
	}

	public Tipo getTipoPiezaComida() {
		return tipoPiezaComida;
	}

	public void setTipoPiezaComida(Tipo tipoPiezaComida) {
		this.tipoPiezaComida = tipoPiezaComida;
	}
}