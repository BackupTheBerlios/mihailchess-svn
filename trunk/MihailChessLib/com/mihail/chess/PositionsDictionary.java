package com.mihail.chess;

import java.util.ArrayList;

/**
 * Esta clase define un objeto TablaHash, que representa a la tabla hash que se
 * usa para almacenar las claves que identifican las posiciones a lo largo de la
 * partida de ajedrez. Asocia a cada clave de posición el número de
 * repeticiones.
 * 
 * @author Pedro Suárez Casal
 * @author Iago Porto Díaz
 */
public final class PositionsDictionary {
	private ArrayList[] tabla;

	private int tam;

	/**
	 * Crea una nueva tabla hash del tamaño indicado. Normalmente se creara una
	 * tabla con un tamaño proximo a 40-50, para situarse en el peor caso de
	 * repeticiones, y aumentar el rendimiento de la tabla.
	 */
	public PositionsDictionary(int t) {
		tabla = new ArrayList[t];
		for (int i = 0; i < tabla.length; i++)
			tabla[i] = new ArrayList();
		tam = t;
	}

	/**
	 * Inserta una clave de posicion dentro de la tabla. Cuando inserta una de
	 * las claves tambien comprueba si esa posicion esta guardada. Si lo esta,
	 * vuelve a guardar la clave con el numero de repeticiones aumentado, si no,
	 * añade un nuevo elemento con las repeticiones a 1.
	 * 
	 * @param clave
	 *            Clave que queremos insertar dentro de la tabla
	 */
	public void insert(int clave) {
		int pos = clave % tam;
		int i = 0;

		while (i < tabla[pos].size()
				&& ((Item) tabla[pos].get(i)).getClave() != clave)
			i++;

		if (i < tabla[pos].size()) {
			if (((Item) tabla[pos].get(i)).getClave() == clave)
				((Item) tabla[pos].get(i)).aumentarRep();
		} else
			tabla[pos].add(new Item(clave));
	}

	/**
	 * Borra todos los elementos de la tabla.
	 */
	public void clearDictionary() {
		for (int i = 0; i < tabla.length; i++)
			tabla[i].clear();
	}

	/**
	 * Obtiene el numero de repeticiones de una posicion dada.
	 * 
	 * @param clave
	 *            La clave asociada con una posicion.
	 * @return El numero de repeticiones de la clave recibida.
	 */
	public int getRepetitions(int clave) {
		int pos = clave % tam;
		int i = 0;

		while (i < tabla[pos].size()
				&& ((Item) (tabla[pos].get(i))).getClave() != clave)
			i++;

		if (i < tabla[pos].size())
			return ((Item) tabla[pos].get(i)).getRepeticiones();
		return -1;
	}

	public String toString() {
		String cad = "[";
		for (int i = 0; i < tabla.length; i++) {
			cad += "[";
			for (int j = 0; j < tabla[i].size(); j++) {
				cad += tabla[i].get(j);
			}
			cad += "]";
		}

		cad += "]";
		return cad;
	}
	
	private final class Item {
		private int clave;

		private int repeticiones = 1;

		public Item(int num) {
			clave = num;
		}

		public int getClave() {
			return clave;
		}

		public int getRepeticiones() {
			return repeticiones;
		}

		public void aumentarRep() {
			repeticiones++;
		}

		public String toString() {
			String cad = "(" + clave + ", " + repeticiones + ")";

			return cad;
		}

	}
}