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
	public char letra;
	public char numero;
	
	public Casilla() {
		this.letra = 0;
		this.numero = 0;
	}
	
	public Casilla(char letra, char numero) {
		this.letra = letra;
		this.numero = numero;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Casilla) {
			Casilla c = (Casilla)o;
			return c.letra == letra && c.numero == numero;
		}
		return false;
	}
}
