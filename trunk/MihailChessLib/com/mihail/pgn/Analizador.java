package com.mihail.pgn;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que se encarga de cargar y guardar partidas en diferentes formatos. Por
 * ahora soportamos formato PGN, pero en un futuro se soportara formato MCN
 * (Markup Chess Notation) basado en XML, y quien sabe si alguno mas.
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */
public abstract class Analizador {

	/**
	 * Sirve para almacenar las partidas que se cargarán.
	 */
	protected ArrayList<Partida> listaPartidas = new ArrayList<Partida>();

	/**
	 * Sirve para obtener el numero de partidas que tenemos.
	 * 
	 * @return El tamaño de la lista de partidas.
	 */
	public int getTamLista() {
		return listaPartidas.size();
	}

	/**
	 * Sirve para obtener una partida en particular.
	 * 
	 * @param pos
	 *            La posicion dentro de la lista de partidas.
	 * @return La partida en cuestion.
	 */
	public Partida getPartida(int pos) {
		return listaPartidas.get(pos);
	}

	/**
	 * Sirve para insertar una partida en la lista de partidas.
	 * 
	 * @param p
	 *            La partida en cuestion.
	 */
	public void insertarPartida(Partida p) {
		listaPartidas.add(p);
	}

	/**
	 * Guarda a un archivo las partidas que tengamos en listaPartidas.
	 * 
	 * @param archivo
	 *            El string que representa al archivo donde vamos a guardarlas.
	 */
	public abstract void guardarArchivo(String archivo) throws IOException;

	/**
	 * Analiza e interpreta un fichero con partidas con el objetivo de cargarlas
	 * a memoria.
	 */
	public abstract void analizar() throws ParseException;
}