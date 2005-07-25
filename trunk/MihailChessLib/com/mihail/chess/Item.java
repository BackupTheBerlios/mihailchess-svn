package com.mihail.chess;

public final class Item {
	private int clave;
	private int repeticiones = 1;

	public Item (int num) {
		clave = num;
	}

	public int getClave () {
		return clave;
	}

	public int getRepeticiones () {
		return repeticiones;
	}

	public void aumentarRep () {
		repeticiones++;
	}

	public String toString () {
		String cad = "(" + clave + ", " + repeticiones + ")";

		return cad;
	}

}
