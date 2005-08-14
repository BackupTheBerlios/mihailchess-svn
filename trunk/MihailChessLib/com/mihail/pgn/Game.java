package com.mihail.pgn;

import java.util.ArrayList;

import com.mihail.chess.Movement;

/**
 * Esta clase define un objeto partida y sus caracteristicas: evento, sitio,
 * fecha, jugadores, elo, lista de movimientos y lista de piezas comidas.
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */

public final class Game {
	public String evento;

	public String sitio;

	public String fecha;

	public String ronda;

	public String jugadorB;

	public String jugadorN;

	public String resultado;

	public int[] elos;

	public String[] titulos;

	public String[] emails;

	public String[] tipos; // Humano o COM

	public String apertura;

	public String variacion;

	public String subVariacion;

	public String ECO;

	public String controlDeTiempo;

	public StringBuffer listaMovimientos = new StringBuffer();

	public ArrayList listaPiezasComidas;

	public String FEN;

	/**
	 * Este metodo devuelve la lista de movimientos de la partida.
	 * 
	 * @return La lista de movimientos de la partida
	 */
	public Movement[] getListaMovimientos() {
		return null;
	}

	/**
	 * 
	 */
	public void setListaMovimientos(Movement[] listaMovs) {
	}
}
