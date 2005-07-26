package com.mihail.openings;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Esta clase define un objeto DiccionarioAperturas y se encarga de almacenar en
 * una tabla hash los nombres de las aperturas junto con una clave numerica que
 * los identifica. Ofrece soporte para varios idiomas.
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 */
public final class DiccionarioAperturas {

	/**
	 * Tamaño de la Tabla Hash. Se elige 2017 porque es el numero primo mas
	 * cercano a 2014, que es el numero de entradas del fichero eco.dat
	 */
	private final static int CAPACIDAD = 2017;

	/**
	 * Constante que representa el nombre del fichero donde estan las aperturas.
	 */
	private final static String NOMBRE_FICHERO_APERTURAS = "eco";

	/**
	 * Representa la extension que indica en que idioma estan las aperturas
	 * (_es, _en, etc).
	 */
	private static String SUFIJO_FICHERO_APERTURAS;

	/**
	 * Constante que representa la extension del fichero donde estan las
	 * aperturas.
	 */
	private final static String EXTENSION_FICHERO_APERTURAS = ".dat";

	/**
	 * Tabla Hash donde se almacenan los nombres de aperturas. Las claves se
	 * generan a partir del numero de la posicion.
	 */
	private HashMap<Integer, String> tabla;

	/**
	 * Idioma de las aperturas.
	 */
	private Locale idioma;

	/**
	 * Crea una nueva instancia de la clase y crea la Tabla Hash con capacidad
	 * CAPACIDAD.
	 */
	public DiccionarioAperturas (Locale id) {
		tabla = new HashMap<Integer, String> (CAPACIDAD);
		// Segun el idioma que se use en el programa, se cargan las aperturas en
		// ingles o en castellano.
		idioma = id;
		String lang = idioma.getLanguage ();
		if (lang.equals ("en") || lang.equals ("de"))
			SUFIJO_FICHERO_APERTURAS = "_en";
		else
			SUFIJO_FICHERO_APERTURAS = "_es";
	}

	/**
	 * Lee el fichero de aperturas FICHERO_APERTURAS e introduce sus datos en la
	 * Tabla Hash.
	 */
	public void parse () throws FileNotFoundException, IOException {
		byte[] text;
		String c;
		String[] lineas, partes;
		String eco, apertura, variante;
		StringBuffer aInsertar;
		Integer clave;
		FileInputStream ent;
		ent = new FileInputStream (NOMBRE_FICHERO_APERTURAS
				+ SUFIJO_FICHERO_APERTURAS + EXTENSION_FICHERO_APERTURAS);
		text = new byte[ent.available ()];
		ent.read (text);
		ent.close ();
		c = new String (text);
		lineas = c.split ("\n");
		for (int i = 0; i < lineas.length; i++) {
			partes = lineas[i].split ("\t");
			clave = new Integer (partes[0]);
			eco = partes[1];
			apertura = partes[2];
			variante = partes[3];
			aInsertar = new StringBuffer (eco + " " + apertura + ", "
					+ variante);
			if (aInsertar.charAt (aInsertar.length () - 2) == ',') {
				aInsertar.deleteCharAt (aInsertar.length () - 1);
				aInsertar.deleteCharAt (aInsertar.length () - 1);
			}
			tabla.put (clave, aInsertar.toString ());
		}
	}

	/**
	 * Este método sirve para consultar si la tabla hash contiene la clave n.
	 * 
	 * @param n
	 *            La clave hash.
	 * @return True si la contiene, false en caso contrario.
	 */
	public boolean containsKey (Integer n) {
		return tabla.containsKey (n);
	}

	/**
	 * Este método sirve para obtener un nombre de apertura de la tabla hash.
	 * 
	 * @param n
	 *            La clave hash.
	 * @return El string que corresponde con la clave n.
	 */
	public String get (Integer n) {
		return tabla.get (n);
	}
}