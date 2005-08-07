package com.mihail.chess;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.mihail.chess.Logica.Bando;

/**
 * @author Wotan
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ArbolVariantes {
	private class NodoArbol {
		public Movimiento movimiento;

		public ArrayList<ArbolVariantes> variantes;

		public NodoArbol(Movimiento mov) {
			movimiento = mov;
		}
	}

	/**
	 * Desplazamiento con respecto al principio de la partida. En el caso de la
	 * variante principal, el desplazamiento es 0, pero en otro caso el
	 * desplazamiento es igual el numero de movimiento en el que se encuentra la
	 * variante.
	 */

	private int desplazamiento = 0;

	private AbstractList<NodoArbol> arbol = new ArrayList<NodoArbol>();

	/**
	 * 
	 */
	public ArbolVariantes() {
	}

	private ArbolVariantes(int desp) {
		desplazamiento = desp;
	}

	/**
	 * Añade el movimiento al final de la variante principal.
	 * 
	 * @param mov
	 *            Movimiento que se añade.
	 */
	public void appendMovimiento(Movimiento mov) {
		arbol.add(new NodoArbol(mov));
	}

	/**
	 * Obtiene un movimiento a partir del numero de movimiento y el turno de
	 * juego.
	 * 
	 * @param color
	 *            Turno del que es el movimiento
	 * @param numero
	 *            Numero del movimiento
	 * @return Devuelve el movimiento correspondiente a color y numero
	 */
	public Movimiento getMovimiento(Bando color, int numero) {
		NodoArbol n = getNodo(color, numero);
		if (n != null)
			return n.movimiento;
		return null;
	}
	
	public Movimiento getMovimiento(int halfPly) {
		return arbol.get(halfPly).movimiento;
	}

	/**
	 * Comprueba si dado un movimiento, existen alternativas.
	 * 
	 * @param color
	 *            Turno del que es el movimiento.
	 * @param numero
	 *            Numero del movimiento.
	 * @return Un booleano que indica si para ese movimiento existen
	 *         alternativas.
	 */
	public boolean existeVariante(Bando color, int numero) {
		NodoArbol n = getNodo(color, numero);

		if (n != null && n.variantes != null)
			return (!n.variantes.isEmpty());
		return false;
	}

	/**
	 * Devuelve el numero de variantes a un movimiento determinado.
	 * 
	 * @param color
	 *            Turno del que es el movimiento.
	 * @param numero
	 *            Numero del movimiento.
	 * @return El numero de alternativas a un movimiento.
	 */
	public int getNumVariantes(Bando color, int numero) {
		NodoArbol n = getNodo(color, numero);

		if (n != null) {
			if (n.variantes != null)
				return n.variantes.size();
		}
		return 0;
	}

	private int bandoToInt(Bando b) {
		if (b == Bando.BLANCO)
			return 0;
		return 1;
	}

	// Funcion de utilidad.
	private NodoArbol getNodo(Bando color, int numero) {
		int indice = (numero - 1) * 2 + bandoToInt(color);
		if (indice < arbol.size())
			return arbol.get(indice);
		return null;
	}

	/**
	 * Añade un movimiento como variante a otro movimiento.
	 * 
	 * @param mov
	 *            Movimiento que se añade como alternativa
	 * @param color
	 *            Turno del que es el movimiento.
	 * @param numero
	 *            Numero del movimiento.
	 */
	public void addVariante(Movimiento mov, Bando color, int numero) {
		NodoArbol n = getNodo(color, numero);

		if (n.variantes == null)
			n.variantes = new ArrayList<ArbolVariantes>();

		ArbolVariantes a = new ArbolVariantes(numero);
		if (color == Bando.NEGRO) // Cada arbol de variantes debe empezar con un movimiento de blancas
			a.appendMovimiento(new Movimiento());
		a.appendMovimiento(mov);

		n.variantes.add(a);
	}

	/**
	 * Devuelve la variante numero num a un movimiento dado.
	 * 
	 * @param color
	 * @param numero
	 * @param num
	 *            Numero de variante que se quiere obtener.
	 * @return Devuelve un ArbolVariantes, que es la variante del movimiento.
	 */
	public ArbolVariantes getVariante(Bando color, int numero, int num) {
		NodoArbol n = getNodo(color, numero);
		if (n != null)
			return n.variantes.get(num);
		return null;
	}

	/**
	 * @return Devuelve el numero total de movimientos desde el comienzo de la
	 *         partida. Solo cuenta los turnos jugados, no cuenta un movimiento
	 *         por negras y otro por blancas.
	 */
	public int getNumMovimientos() {
		return desplazamiento + ((arbol.size() + 1) / 2);
	}

	/**
	 * Cuenta los medios movimientos, es decir, contando uno por cada movimiento
	 * de negras y blancas.
	 * 
	 * @return Los medios movimientos desde el comienzo de la partida.
	 */

	public int getNumHalfPly() {
		return arbol.size();
	}

	/**
	 * Devuelve el último movimiento de la variante actual.
	 * 
	 * @return
	 */

	public Movimiento getLastMovimiento() {
		return arbol.get(arbol.size() - 1).movimiento;
	}
	
	/**
	 * Hace que una variante pase a ser la variante principal.
	 * 
	 * @param color
	 * @param numero
	 * @param num
	 */
	
	public void promoteVariant(Bando color, int numero, int num) {
		ArbolVariantes a = getVariante(color, numero, num);
		int indice = (numero - 1) * 2 + bandoToInt(color);
		NodoArbol n = getNodo(color, numero);
		
		ArbolVariantes newTree = new ArbolVariantes();
		newTree.arbol  = (AbstractList<ArbolVariantes.NodoArbol>)arbol.subList(indice, arbol.size());
		n.variantes.add(newTree);
		
		arbol.addAll(a.arbol);
		n.variantes.remove(num);
	}
}