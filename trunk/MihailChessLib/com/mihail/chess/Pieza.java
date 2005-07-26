package com.mihail.chess;

import java.util.*;

import static com.mihail.chess.Logica.Bando;

/**
 * Esta clase define un objeto Pieza y sus caracteristicas: bando, tipo de pieza
 * (peon, caballo, alfil, torre, dama, rey), posicion en la que se encuentra y
 * casillas a donde podria mover, ademas de un indicador para saber si la pieza
 * ha sido comida.
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */

public final class Pieza {

	// Atributos

	/**
	 * Este atributo indica el bando de la pieza (blanco o negro): true -> bando
	 * blanco false -> bando negro
	 */
	public Bando bando;

	/**
	 * Este atributo indica el tipo de pieza que es: <BR>
	 * 'P' -> peon <BR>
	 * 'C' -> caballo <BR>
	 * 'A' -> alfil <BR>
	 * 'T' -> torre <BR>
	 * 'D' -> dama <BR>
	 * 'R' -> rey
	 */
	public char tipo;

	/**
	 * Este atributo indica la letra de la casilla en que se encuentra la pieza.
	 */
	public char letra;

	/**
	 * Este atributo indica el numero de la casilla en que se encuentra la
	 * pieza.
	 */
	public char num;

	/**
	 * Este atributo es un array que almacena todas las casillas a las que puede
	 * mover la pieza en una determinada situacion de partida, en donde: <BR>
	 * casillasValidas[0] -> letras de las casillas <BR>
	 * casillasValidas[1] -> numeros de las casillas <BR>
	 * Son vectores de tamaño fijo.
	 * 
	 * @see java.util.ArrayList
	 */
	public ArrayList<Casilla> casillasValidas;

	public VectorDireccion[] direcciones;

	/**
	 * Inicializa una nueva instancia de esta clase.
	 * 
	 * @param ban
	 *            True para Blancas, false para Negras
	 * @param claseDePieza
	 *            Es el tipo de la pieza {P,C,A,T,D,R}
	 * @see #tipo
	 */
	public Pieza (Bando ban, char claseDePieza) {
		bando = bando;
		tipo = claseDePieza;
		letra = '\0';
		num = '\0';
		switch (tipo) {
			case 'P':
				casillasValidas = new ArrayList<Casilla> (4);
				break;
			case 'T':
				direcciones = new VectorDireccion[4];
				direcciones[0] = new VectorDireccion (1, 0);
				direcciones[1] = new VectorDireccion (-1, 0);
				direcciones[2] = new VectorDireccion (0, 1);
				direcciones[3] = new VectorDireccion (0, -1);
				casillasValidas = new ArrayList<Casilla> (13);
				break;
			case 'A':
				direcciones = new VectorDireccion[4];
				direcciones[0] = new VectorDireccion (1, 1);
				direcciones[1] = new VectorDireccion (-1, 1);
				direcciones[2] = new VectorDireccion (1, -1);
				direcciones[3] = new VectorDireccion (-1, -1);
				casillasValidas = new ArrayList<Casilla> (13);
				break;
			case 'C':
				direcciones = new VectorDireccion[8];
				direcciones[0] = new VectorDireccion (1, 2);
				direcciones[1] = new VectorDireccion (-1, 2);
				direcciones[2] = new VectorDireccion (2, 1);
				direcciones[3] = new VectorDireccion (2, -1);

				direcciones[4] = new VectorDireccion (1, -2);
				direcciones[5] = new VectorDireccion (-1, -2);
				direcciones[6] = new VectorDireccion (-2, 1);
				direcciones[7] = new VectorDireccion (-2, -1);
				casillasValidas = new ArrayList<Casilla> (8);
				break;
			case 'R':
				casillasValidas = new ArrayList<Casilla> (8);
				break;
			case 'D':
				direcciones = new VectorDireccion[8];
				direcciones[0] = new VectorDireccion (1, 0);
				direcciones[1] = new VectorDireccion (-1, 0);
				direcciones[2] = new VectorDireccion (0, 1);
				direcciones[3] = new VectorDireccion (0, -1);
				direcciones[4] = new VectorDireccion (1, 1);
				direcciones[5] = new VectorDireccion (-1, 1);
				direcciones[6] = new VectorDireccion (1, -1);
				direcciones[7] = new VectorDireccion (-1, -1);
				casillasValidas = new ArrayList<Casilla> (27);
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
	public final static boolean esBandoContrario (Pieza pieza1, Pieza pieza2) {
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
	public final static boolean esBandoContrario (Bando turno, Pieza pieza) {
		return turno != pieza.bando;
	}

	/**
	 * Inserta una casilla en la lista de casillas validas de un objeto Pieza.
	 * 
	 * @param let
	 *            Es la casilla de destino que queremos añadir
	 * @param num
	 *            Es el numero de destino que queremos añadir
	 */
	public void anadirMov (char let, char num) {
		this.casillasValidas.add (new Casilla(let, num));
	}
}