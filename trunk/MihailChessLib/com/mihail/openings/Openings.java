package com.mihail.openings;

import java.io.*;
import java.util.Locale;

import com.mihail.chess.*;

public class Openings {
	/**
	 * Tabla hash con las aperturas.
	 */
	private OpeningsDictionary aperturas;

	/**
	 * Recibe el tablero donde se va a mirar la apertura y el idioma en que se
	 * quieren las aperturas.
	 * 
	 * @param loc
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Openings(Locale loc) throws FileNotFoundException, IOException {
		// Se construye la Tabla Hash con las aperturas.
		aperturas = new OpeningsDictionary(loc);
		aperturas.parse();
	}

	/**
	 * Obtiene la apertura que se esta jugando en una Posicion pos
	 * 
	 * @param pos
	 * @return Nombre de la apertura que se esta jugando en la Posicion pos.
	 */
	public String getOpening(Position pos) {
		int clave = pos.getPositionKey();
		if (aperturas.containsKey(clave))
			return aperturas.get(clave);
		else
			return null;
	}
}
