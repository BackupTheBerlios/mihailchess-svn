package com.mihail.chess;

import static com.mihail.chess.Logica.Bando;
/**
 * Esta clase define un objeto Movimiento, que guarda informacion sobre el
 * movimiento que se realiza y sobre el estado de la partida en ese momento (qué
 * enroques son posibles, es jaque, etc).
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */

public final class Movimiento {

	/**
	 * Este atributo indica la letra de la casilla origen.
	 */
	public char origenLetra;

	/**
	 * Este atributo indica el numero de la casilla origen.
	 */
	public char origenNum;

	/**
	 * Este atributo indica la letra de la casilla destino.
	 */
	public char destinoLetra;

	/**
	 * Este atributo indica el numero de la casilla destino.
	 */
	public char destinoNum;

	/**
	 * Numero de movimiento en la partida. Es el mismo numero para blancas que
	 * para negras. Por ejemplo: en '1. e4 c5', tanto e4 como c5 compartirian el
	 * 1 como numero de movimiento.
	 */
	public int numeroMovimiento;

	/**
	 * Bando que mueve.
	 */
	public Bando bando;

	/**
	 * Este atributo indica el tipo de pieza que es: {P,C,A,T,D,R}
	 * 
	 * @see Pieza#tipo
	 */
	public char tipoPieza;

	/**
	 * Este atributo indica la letra de la casilla donde se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	public char casillaComerLetra;

	/**
	 * Este atributo indica el numero de la casilla donde se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	public char casillaComerNum;

	/**
	 * Este atributo indica si el movimiento produce un jaque.
	 */
	public boolean jaque;

	/**
	 * Este atributo indica si el movimiento termina la partida y quien es el
	 * ganador o bien tablas. <BR>
	 * 'B' -> Ganan blancas <BR>
	 * 'N' -> Ganan negras <BR>
	 * 'T' -> Tablas <BR>
	 * 0 -> No ha terminado la partida
	 */
	public int finPartida;

	/**
	 * Este atributo indica si se ha producido una coronacion y a que pieza se
	 * promociona. <BR>
	 * 'C' -> Caballo <BR>
	 * 'A' -> Alfil <BR>
	 * 'T' -> Torre <BR>
	 * 'D' -> Dama <BR>
	 * 0 -> No hay coronacion
	 */
	public char coronacion;

	/**
	 * Este atributo indica el valor de contadorTablas en el momento que se
	 * produce el movimiento.
	 */
	public int contadorTablas;

	/**
	 * Este atributo indica que enroques estan disponibles para que bandos en el
	 * momento en que se produce el movimiento. Es un array 2x2, en donde: <BR>
	 * enroque[0][0] -> Blancas, enroque corto <BR>
	 * enroque[0][1] -> Blancas, enroque largo <BR>
	 * enroque[1][0] -> Negras, enroque corto <BR>
	 * enroque[1][1] -> Negras, enroque largo <BR>
	 */
	public boolean[][] enroque;

	/**
	 * Este atributo contiene 0 en todos los casos salvo si se ha movido un peon
	 * dos casillas. En ese caso contendra la letra de la columna del peon. Es
	 * necesario para la captura al paso.
	 */
	public char alPaso;

	/**
	 * Este atributo indica el tipo de la pieza que se come. Si no se ha
	 * producido una captura contiene un 0.
	 */
	public char tipoPiezaComida;

	/**
	 * Este atributo guarda la representacion del movimiento en notacion
	 * algebraica.
	 */
	public String notacion;
}