package com.mihail.chess;

/**
 * Almacena la dirección en la que una pieza puede mover. Cuando se crea un
 * nuevo vector dirección, se entiende que la x y la y indican las componentes
 * del vector dentro del tablero. Además el vector debe ser 'unitario' para que
 * la lógica lo interprete correctamente. Por ejemplo, si queremos indicar los
 * movimientos posibles de una dama, basta con indicar 8 vectores de dirección:
 * (1, 0) (1, 1) (1, -1) (0, 1) (0, -1) (-1, 0) (-1, 1) (-1, -1). Esto
 * proporciona gran flexibilidad a la hora de definir nuevas piezas en sistemas
 * de ajedrez alternativos. Podriamos definir una pieza que se mueva unicamente
 * como (1, 2) y (0, 1). TODO indicar los vectores que deben ser estrictamente
 * unitarios, para diferenciar piezas que se mueven una casilla de las que se
 * mueven toda la fila. Quiza sea conveniente indicarlo en Pieza, junto con si
 * la pieza puede saltar a otras.
 */

public class VectorDireccion {
	private int x, y;

	/**
	 * Crea un nuevo vector de dirección.
	 * 
	 * @param x_
	 *            Desplazamiento permitido en sentido horizontal, es decir, a
	 *            traves de las filas
	 * @param y_
	 *            Desplazamiento permitido en sentido vertical, es decir, a
	 *            traves de las columnas
	 */
	public VectorDireccion(int x_, int y_) {
		x = x_;
		y = y_;
	}

	/**
	 * Comprueba si un vector es multiplo de otro. Por ejemplo, devolveria true
	 * en caso que al vector (1, 1) se le pase el vector (2, 2).
	 * 
	 * @param v
	 *            Vector con el que queremos comprobar si es la misma direccion.
	 * @return Un booleano si este vector y el recibido siguen la misma
	 *         direccion.
	 */
	public boolean esMismaDireccion(VectorDireccion v) {
		int difX, difY;

		if ((x == 0 && v.getX() != 0) || (y == 0 && v.getY() != 0))
			return false;

		if (v.getX() == 0 && x == 0)
			difX = 1;
		else
			difX = v.getX() / x;

		if (v.getY() == 0 && y == 0)
			difY = 1;
		else
			difY = v.getY() / y;

		return (difY == difX);
	}

	/**
	 * Devuelve el desplazamiento horizontal de este vector.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Devuelve el desplazamiento vertical de este vector.
	 */
	public int getY() {
		return y;
	}
}
