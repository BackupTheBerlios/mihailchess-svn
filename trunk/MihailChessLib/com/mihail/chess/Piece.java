package com.mihail.chess;

import java.util.*;

import static com.mihail.chess.Board.Side;

/**
 * Esta clase define un objeto Pieza y sus caracteristicas: bando, tipo de pieza
 * (peon, caballo, alfil, torre, dama, rey), posicion en la que se encuentra y
 * casillas a donde podria mover, ademas de un indicador para saber si la pieza
 * ha sido comida.
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */

public final class Piece {

	public static enum Tipo {
		PEON, CABALLO, ALFIL, TORRE, DAMA, REY
	}

	// Atributos

	/**
	 * Este atributo indica el bando de la pieza (blanco o negro): true -> bando
	 * blanco false -> bando negro
	 */
	private Side bando;

	/**
	 * Este atributo indica el tipo de pieza que es: <BR>
	 * 'P' -> peon <BR>
	 * 'C' -> caballo <BR>
	 * 'A' -> alfil <BR>
	 * 'T' -> torre <BR>
	 * 'D' -> dama <BR>
	 * 'R' -> rey
	 */
	private Tipo tipo;

	private Square casilla;

	/**
	 * Este atributo es un array que almacena todas las casillas a las que puede
	 * mover la pieza en una determinada situacion de partida, en donde: <BR>
	 * casillasValidas[0] -> letras de las casillas <BR>
	 * casillasValidas[1] -> numeros de las casillas <BR>
	 * Son vectores de tamaño fijo.
	 * 
	 * @see java.util.ArrayList
	 */
	private ArrayList<Square> casillasValidas;

	private DirectionVector[] direcciones;

	/**
	 * Inicializa una nueva instancia de esta clase.
	 * 
	 * @param ban
	 *            True para Blancas, false para Negras
	 * @param claseDePieza
	 *            Es el tipo de la pieza {P,C,A,T,D,R}
	 * @see #tipo
	 */
	public Piece(Side ban, Tipo claseDePieza) {
		bando = ban;
		tipo = claseDePieza;
		casilla = new Square();
		switch (tipo) {
		case PEON:
			direcciones = new DirectionVector[1];
			if (bando == Side.WHITE)
				direcciones[0] = new DirectionVector(0, 1);
			else
				direcciones[0] = new DirectionVector(0, -1);
			casillasValidas = new ArrayList<Square>(4);
			break;
		case TORRE:
			direcciones = new DirectionVector[4];
			direcciones[0] = new DirectionVector(1, 0);
			direcciones[1] = new DirectionVector(-1, 0);
			direcciones[2] = new DirectionVector(0, 1);
			direcciones[3] = new DirectionVector(0, -1);
			casillasValidas = new ArrayList<Square>(13);
			break;
		case ALFIL:
			direcciones = new DirectionVector[4];
			direcciones[0] = new DirectionVector(1, 1);
			direcciones[1] = new DirectionVector(-1, 1);
			direcciones[2] = new DirectionVector(1, -1);
			direcciones[3] = new DirectionVector(-1, -1);
			casillasValidas = new ArrayList<Square>(13);
			break;
		case CABALLO:
			direcciones = new DirectionVector[8];
			direcciones[0] = new DirectionVector(1, 2);
			direcciones[1] = new DirectionVector(-1, 2);
			direcciones[2] = new DirectionVector(2, 1);
			direcciones[3] = new DirectionVector(2, -1);

			direcciones[4] = new DirectionVector(1, -2);
			direcciones[5] = new DirectionVector(-1, -2);
			direcciones[6] = new DirectionVector(-2, 1);
			direcciones[7] = new DirectionVector(-2, -1);
			casillasValidas = new ArrayList<Square>(8);
			break;
		case REY:
			direcciones = new DirectionVector[8];
			direcciones[0] = new DirectionVector(1, 0);
			direcciones[1] = new DirectionVector(-1, 0);
			direcciones[2] = new DirectionVector(0, 1);
			direcciones[3] = new DirectionVector(0, -1);
			direcciones[4] = new DirectionVector(1, 1);
			direcciones[5] = new DirectionVector(-1, 1);
			direcciones[6] = new DirectionVector(1, -1);
			direcciones[7] = new DirectionVector(-1, -1);
			casillasValidas = new ArrayList<Square>(8);
			break;
		case DAMA:
			direcciones = new DirectionVector[8];
			direcciones[0] = new DirectionVector(1, 0);
			direcciones[1] = new DirectionVector(-1, 0);
			direcciones[2] = new DirectionVector(0, 1);
			direcciones[3] = new DirectionVector(0, -1);
			direcciones[4] = new DirectionVector(1, 1);
			direcciones[5] = new DirectionVector(-1, 1);
			direcciones[6] = new DirectionVector(1, -1);
			direcciones[7] = new DirectionVector(-1, -1);
			casillasValidas = new ArrayList<Square>(27);
			break;
		}
	}

	/**
	 * Permite saber si dos piezas son de bandos contrarios.
	 * 
	 * @param pieza1
	 *            Pieza para saber si es de bando contrario a pieza2.
	 * @param pieza2
	 *            La otra pieza.
	 * @return Devuelve true cuando pieza1 y pieza2 sean de bandos contrarios,
	 *         false cuando sean del mismo bando.
	 */
	public final static boolean esBandoContrario(Piece pieza1, Piece pieza2) {
		return pieza1.bando != pieza2.bando;
	}

	/**
	 * Permite saber si una pieza pertenece al bando contrario al que tiene el
	 * turno.
	 * 
	 * @param turno
	 *            Indica de que bando es el turno.
	 * @param pieza
	 *            La pieza en cuestion.
	 * @return Devuelve true cuando pieza sea del bando contrario al que le toca
	 *         mover, false si es del mismo bando
	 */
	public final static boolean esBandoContrario(Side turno, Piece pieza) {
		return turno != pieza.bando;
	}

	/**
	 * Inserta una casilla en la lista de casillas validas de un objeto Pieza.
	 * 
	 * @param let
	 *            Es la casilla de destino que queremos añadir
	 * @param n
	 *            Es el numero de destino que queremos añadir
	 */
	public void anadirMov(char let, char n) {
		this.casillasValidas.add(new Square(let, n));
	}

	public void anadirMov(Square c) {
		this.casillasValidas.add(c);
	}

	public Side getBando() {
		return bando;
	}

	public ArrayList<Square> getCasillasValidas() {
		return casillasValidas;
	}

	public DirectionVector[] getDirecciones() {
		return direcciones;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public char getLetra() {
		return casilla.getLetra();
	}

	public void setLetra(char let) {
		casilla.setLetra(let);
	}

	public char getNum() {
		return casilla.getNumero();
	}

	public void setNum(char num) {
		casilla.setNumero(num);
	}

	public Square getCasilla() {
		return casilla;
	}

	public void setCasilla(Square casilla) {
		this.casilla = casilla;
	}
	
	public boolean canMove(Square casilla) {
		return casillasValidas.contains(casilla);
	}
}