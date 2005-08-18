package com.mihail.chess;

import static com.mihail.chess.Board.Side;

import com.mihail.chess.Board.Result;
import com.mihail.chess.Piece.Type;

/**
 * Esta clase define un objeto Movimiento, que guarda informacion sobre el
 * movimiento que se realiza y sobre el estado de la partida en ese momento (qué
 * enroques son posibles, es jaque, etc).
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */

public final class Move {

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
	 * Este atributo indica el tipo de pieza que es: {P,C,A,T,D,R}
	 * 
	 */
	private Type tipoPieza;

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
	 * Este atributo indica si el movimiento termina la partida y quien es el
	 * ganador o bien tablas. <BR>
	 * 'B' -> Ganan blancas <BR>
	 * 'N' -> Ganan negras <BR>
	 * 'T' -> Tablas <BR>
	 * 0 -> No ha terminado la partida
	 */
	private Result finPartida;

	/**
	 * Este atributo indica si se ha producido una coronacion y a que pieza se
	 * promociona. <BR>
	 * 'C' -> Caballo <BR>
	 * 'A' -> Alfil <BR>
	 * 'T' -> Torre <BR>
	 * 'D' -> Dama <BR>
	 * 0 -> No hay coronacion
	 */
	private Type coronacion;

	/**
	 * Este atributo indica el tipo de la pieza que se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	private Type tipoPiezaComida;

	/**
	 * Este atributo guarda la representacion del movimiento en notacion
	 * algebraica.
	 */
	private String notacion;
	
	/**
	 * Este atributo indica si el movimiento produce un jaque.
	 */
	private boolean jaque;
	
	private BoardStatus status = new BoardStatus();

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
	
	public Type getTipoPieza() {
		return tipoPieza;
	}

	public void setTipoPieza(Type tipoPieza) {
		this.tipoPieza = tipoPieza;
	}

	public Square getCasillaComer() {
		return casillaComer;
	}

	public void setCasillaComer(Square casillaComer) {
		this.casillaComer = casillaComer;
	}

	public Type getCoronacion() {
		return coronacion;
	}

	public void setCoronacion(Type coronacion) {
		this.coronacion = coronacion;
	}

	public Result getFinPartida() {
		return finPartida;
	}

	public void setFinPartida(Result finPartida) {
		this.finPartida = finPartida;
	}

	public boolean isJaque() {
		return jaque;
	}

	public void setJaque(boolean jaque) {
		this.jaque = jaque;
	}
	
	public String getNotacion() {
		return notacion;
	}

	public void setNotacion(String notacion) {
		this.notacion = notacion;
	}

	public Type getTipoPiezaComida() {
		return tipoPiezaComida;
	}

	public void setTipoPiezaComida(Type tipoPiezaComida) {
		this.tipoPiezaComida = tipoPiezaComida;
	}
	
	public BoardStatus getFinalBoardStatus() {
		return status;
	}
}