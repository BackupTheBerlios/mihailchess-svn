/*
 * Created on 25-jul-2005
 *
 * MihailChess - PositionTest.java
 * 
 * Autores:  Iago Porto Diaz
 * 			 Pedro Suarez Casal
 * 
 */
package com.mihail.test;

import junit.framework.TestCase;

import com.mihail.chess.Pieza;
import com.mihail.chess.Posicion;

public class PositionTest extends TestCase {

	/*
	 * Test method for 'com.mihail.chess.Position.Position()'
	 */
	public void testPosition() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.getFEN()'
	 */
	public void testGetFEN() {
		Posicion p = new Posicion();
		p.setPosicion(Posicion.POS_INICIAL);
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", p.getFEN());
	}

	/*
	 * Test method for 'com.mihail.chess.Position.setPosicion(String)'
	 */
	public void testSetPosicion() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setTurno()'
	 */
	public void testSetTurno() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setTurno(int)'
	 */
	public void testSetTurnoInt() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setEnroqueCorto(int, boolean)'
	 */
	public void testSetEnroqueCorto() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setEnroqueLargo(int, boolean)'
	 */
	public void testSetEnroqueLargo() {

	}
	
	/*
	 * Test method for 'com.mihail.chess.Position.setPieza(Piece, char, char)'
	 */
	public void testSetPieza() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.borrarPieza(char, char)'
	 */
	public void testBorrarPieza() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.esVacia(char, char)'
	 */
	public void testEsVacia() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.getClavePosicion()'
	 */
	public void testGetClavePosicion() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setAlPaso(char)'
	 */
	public void testSetAlPaso() {
		Posicion p = new Posicion();
		// Comprobamos que no se pone siempre alPaso
		p.setPieza(new Pieza(true, 'P'), 'a', '1');
		p.setAlPaso('a');
		assertEquals(p.getAlPaso(), 0);
		// Comprobamos que al poner un peon negro en una casilla de alPaso, la funcion deja
		p.setPieza(new Pieza(false, 'P'), 'h', '5');
		p.setAlPaso('h');
		assertEquals(p.getAlPaso(), 'h');
		// Al borrar la pieza, alPaso deberia resetearse, ya que no hay peon en esa columna
		p.borrarPieza('h', '6');
		assertEquals(p.getAlPaso(), 0);
		// Probamos lo mismo pero con un peon blanco
		p.setPieza(new Pieza(true, 'P'), 'h', '4');
		p.setAlPaso('h');
		assertEquals(p.getAlPaso(), 'h');
	}

	/*
	 * Test method for 'com.mihail.chess.Position.getContadorTablas()'
	 */
	public void testGetContadorTablas() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setContadorTablas(int)'
	 */
	public void testSetContadorTablas() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.addContadorTablas()'
	 */
	public void testAddContadorTablas() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.setNumeroMovimiento(int)'
	 */
	public void testSetNumeroMovimiento() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.addNumeroMovimiento()'
	 */
	public void testAddNumeroMovimiento() {

	}

	/*
	 * Test method for 'com.mihail.chess.Position.getKingPosition(int)'
	 */
	public void testGetKingPosition() {

	}
	
	public static void main(String [] args) {
		junit.textui.TestRunner.run(PositionTest.class);
	}
}
