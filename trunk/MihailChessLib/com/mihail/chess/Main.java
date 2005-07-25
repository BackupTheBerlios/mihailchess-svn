package com.mihail.chess;

/*
 * Created on 25-jul-2005
 *
 * MihailChess - Main.java
 * 
 * Autores:  Iago Porto Diaz
 * 			 Pedro Suarez Casal
 * 
 */

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Posicion p = new Posicion();
		
		System.out.println(p.getFEN());
		
		p.setPieza(new Pieza(true, 'R'), 'a', '7');
		p.setPieza(new Pieza(false, 'R'), 'g', '3');
		
		System.out.println(p.getFEN());
		
		p.setPosicion(Posicion.POS_INICIAL);
		
		System.out.println(p.getFEN());
		
		p.borrarPieza('e', '1');
		
		System.out.println(p.getFEN());
	}

}
