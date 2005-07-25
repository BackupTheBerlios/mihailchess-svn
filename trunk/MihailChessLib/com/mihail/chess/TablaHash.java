package com.mihail.chess;

import java.util.ArrayList;

/**
 * Esta clase define un objeto TablaHash, que representa a la tabla hash que se
 * usa para almacenar las claves que identifican las posiciones a lo largo de la
 * partida de ajedrez. Asocia a cada clave de posición el número de repeticiones.
 * 
 * @author Pedro Suárez Casal
 * @author Iago Porto Díaz
 */
public final class TablaHash {
	private ArrayList[] tabla;

	private int tam;

	/**
	  * Crea una nueva tabla hash del tamaño indicado. Normalmente se creara una tabla 
	  * con un tamaño proximo a 40-50, para situarse en el peor caso de repeticiones, y 
	  * aumentar el rendimiento de la tabla.
	  */
	public TablaHash (int TAM) {
		tabla = new ArrayList[TAM];
		for (int i = 0; i < tabla.length; i++)
			tabla[i] = new ArrayList<Item> ();
		tam = TAM;
	}

	/**
	  * Inserta una clave de posicion dentro de la tabla. Cuando inserta una de las claves
	  * tambien comprueba si esa posicion esta guardada. Si lo esta, vuelve a guardar
	  * la clave con el numero de repeticiones aumentado, si no, añade un nuevo elemento
	  * con las repeticiones a 1.
	  * @param clave Clave que queremos insertar dentro de la tabla
	  */
	public void insertar (int clave) {
		int pos = clave % tam;
		int i = 0;

		while (i < tabla[pos].size ()
				&& ((Item) tabla[pos].get (i)).getClave () != clave)
			i++;

		if (i < tabla[pos].size ()) {
			if (((Item) tabla[pos].get (i)).getClave () == clave)
				((Item) tabla[pos].get (i)).aumentarRep ();
		}
		else
			tabla[pos].add (new Item (clave));
	}
	/**
	  * Borra todos los elementos de la tabla.
	  */
	public void borrarTabla () {
		for (int i = 0; i < tabla.length; i++)
			tabla[i].clear ();
	}

	/**
	  * Obtiene el numero de repeticiones de una posicion dada.
	  * @param clave La clave asociada con una posicion.
	  * @return El numero de repeticiones de la clave recibida.
	  */
	public int getRepeticiones (int clave) {
		int pos = clave % tam;
		int i = 0;

		while (i < tabla[pos].size ()
				&& ((Item) (tabla[pos].get (i))).getClave () != clave)
			i++;

		if (i < tabla[pos].size ())
			return ((Item) tabla[pos].get (i)).getRepeticiones ();
		return -1;
	}

	public String toString () {
		String cad = "[";
		for (int i = 0; i < tabla.length; i++) {
			cad += "[";
			for (int j = 0; j < tabla[i].size (); j++) {
				cad += tabla[i].get (j);
			}
			cad += "]";
		}

		cad += "]";
		return cad;
	}
}