package aperturas;

import java.io.*;
import java.util.Locale;

import com.mihail.chess.*;


public class Aperturas {
	/**
	 * Tabla hash con las aperturas.
	 */
	private DiccionarioAperturas aperturas;

	/**
	 * Recibe el tablero donde se va a mirar la apertura y el idioma en que se
	 * quieren las aperturas.
	 * 
	 * @param tab
	 * @param loc
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Aperturas (Locale loc) throws FileNotFoundException, IOException {
		// Se construye la Tabla Hash con las aperturas.
		aperturas = new DiccionarioAperturas (loc);
		aperturas.parse ();
	}

	/**
	 * Obtiene la apertura que se esta jugando en una Posicion pos
	 * 
	 * @param pos
	 * @return
	 */
	public String getApertura (Posicion pos) {
		int clave = pos.getClavePosicion ();
		if (aperturas.containsKey (clave))
			return aperturas.get (clave);
		else
			return null;
	}
}
