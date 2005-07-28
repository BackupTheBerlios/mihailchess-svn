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

public class Casilla {

	private char letra;

	private char numero;

	public Casilla() {
		this.letra = 0;
		this.numero = 0;
	}

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

	public Casilla add(VectorDireccion v) {
		return new Casilla((char) (this.letra + v.getX()),
				(char) (this.numero + v.getY()));
	}

	public char getLetra() {
		return letra;
	}

	public void setLetra(char letra) {
		this.letra = letra;
	}

	public char getNumero() {
		return numero;
	}

	public void setNumero(char numero) {
		this.numero = numero;
	}

	public String toString() {
		return letra + "" + numero;
	}
}
