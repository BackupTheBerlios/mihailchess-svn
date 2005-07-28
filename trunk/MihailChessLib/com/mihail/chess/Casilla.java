/*
 * Created on 25-jul-2005
 *
 * MihailChess - Casilla.java
 * 
 * Autores:  Iago Porto Diaz
 * 			 Pedro Suarez Casal
 * 
 */
package com.mihail.chess;

/**
 * Representa una casilla en un tablero de ajedrez. Simplemente almacena informacion sobre
 * la letra y el numero.
 * @author wotan
 *
 */

public class Casilla {

	private char letra;

	private char numero;

	/**
	 * Crea una casilla con la letra y numero iguales a cero.
	 *
	 */
	
	public Casilla() {
		this.letra = 0;
		this.numero = 0;
	}
	
	/**
	 * Crea una casilla con la letra y numero indicados.
	 * 
	 * @param letra Letra de la casilla
	 * @param numero Numera de la casilla
	 */

	public Casilla(char letra, char numero) {
		this.letra = letra;
		this.numero = numero;
	}

	public boolean equals(Object o) {
		if (o instanceof Casilla) {
			Casilla c = (Casilla) o;
			return c.letra == letra && c.numero == numero;
		}
		return false;
	}
	
	/**
	 * Suma a la casilla actual un vector de direccion, dando como resultado otra casilla.
	 * A la letra se le suma la direccion x y al numero la direccion y.
	 * 
	 * @param v El vector que queremos sumar a la casilla actual.
	 * @return Otra casilla resultado de sumar a la casilla actual el vector recibido.
	 */

	public Casilla add(VectorDireccion v) {
		return new Casilla((char) (this.letra + v.getX()),
				(char) (this.numero + v.getY()));
	}
	
	/**
	 * Obtiene la letra de la casilla.
	 * 
	 * @return La letra de la casilla
	 */

	public char getLetra() {
		return letra;
	}
	
	/**
	 * Establece la letra de la casilla.
	 * 
	 * @param letra La letra de la casilla
	 */

	public void setLetra(char letra) {
		this.letra = letra;
	}
	
	/**
	 * Obtiene el numero de la casilla.
	 * 
	 * @return El numero de la casilla
	 */

	public char getNumero() {
		return numero;
	}
	
	/**
	 * Establece el numero de la casilla.
	 * 
	 * @param numero El numero de la casilla
	 */

	public void setNumero(char numero) {
		this.numero = numero;
	}

	public String toString() {
		return letra + "" + numero;
	}
}
