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

import com.mihail.chess.Pieza;
import com.mihail.chess.Posicion;

import junit.framework.TestCase;

import static com.mihail.chess.Logica.Bando;

public class PositionTest extends TestCase {

//	/*
//	 * Test method for 'com.mihail.chess.Position.Position()'
//	 */
//	public void testPosition() {
//		Posicion p = new Posicion();
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.getFEN()'
//	 */
//	public void testGetFEN() {
//		Posicion p = new Posicion();
//		p.setPosicion(Posicion.POS_INICIAL);
//		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", p.getFEN());
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setPosicion(String)'
//	 */
//	public void testSetPosicion() {
//
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setTurno()'
//	 */
//	public void testSetTurno() {
//		Posicion p = new Posicion();
//		Bando turno = p.getTurno();
//		p.setTurno();
//		p.setTurno();
//		assertEquals(turno, p.getTurno());
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setTurno(int)'
//	 */
//	public void testSetTurnoInt() {
//		Posicion p = new Posicion();
//		p.setTurno(Bando.BLANCO);
//		assertEquals(Bando.BLANCO, p.getTurno());
//		p.setTurno(Bando.NEGRO);
//		assertEquals(Bando.NEGRO, p.getTurno());
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setEnroqueCorto(int, boolean)'
//	 */
//	public void testSetEnroqueCorto() {
//		Posicion p = new Posicion();
//		p.setPieza(new Pieza(Bando.BLANCO, 'R'), 'e', '1');
//		p.setEnroqueCorto(Bando.BLANCO, true);
//		assertEquals(false, p.getEnroqueCorto(Bando.BLANCO));
//		p.setPieza(new Pieza(Bando.BLANCO, 'T'), 'h', '1');
//		p.setEnroqueCorto(Bando.BLANCO, true);
//		assertEquals(true, p.getEnroqueCorto(Bando.BLANCO));
//		
//		p.setPieza(new Pieza(Bando.NEGRO, 'R'), 'e', '8');
//		p.setEnroqueCorto(Bando.NEGRO, true);
//		assertEquals(false, p.getEnroqueCorto(Bando.NEGRO));
//		p.setPieza(new Pieza(Bando.NEGRO, 'T'), 'h', '8');
//		p.setEnroqueCorto(Bando.NEGRO, true);
//		assertEquals(true, p.getEnroqueCorto(Bando.NEGRO));
//		System.out.println(p.getFEN());
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setEnroqueLargo(int, boolean)'
//	 */
//	public void testSetEnroqueLargo() {
//		Posicion p = new Posicion();
//		p.setPieza(new Pieza(Bando.BLANCO, 'R'), 'e', '1');
//		p.setEnroqueLargo(Bando.BLANCO, true);
//		assertEquals(false, p.getEnroqueLargo(Bando.BLANCO));
//		p.setPieza(new Pieza(Bando.BLANCO, 'T'), 'a', '1');
//		p.setEnroqueLargo(Bando.BLANCO, true);
//		assertEquals(true, p.getEnroqueLargo(Bando.BLANCO));
//		
//		p.setPieza(new Pieza(Bando.NEGRO, 'R'), 'e', '8');
//		p.setEnroqueLargo(Bando.NEGRO, true);
//		assertEquals(false, p.getEnroqueLargo(Bando.NEGRO));
//		p.setPieza(new Pieza(Bando.NEGRO, 'T'), 'a', '8');
//		p.setEnroqueLargo(Bando.NEGRO, true);
//		assertEquals(true, p.getEnroqueLargo(Bando.NEGRO));
//		System.out.println(p.getFEN());
//	}
//	
//	/*
//	 * Test method for 'com.mihail.chess.Position.setPieza(Piece, char, char)'
//	 */
//	public void testSetPieza() {
//
//	}

	/*
	 * Test method for 'com.mihail.chess.Position.borrarPieza(char, char)'
	 */
	public void testBorrarPieza() {
		Posicion p = new Posicion("8/8/8/7p/8/8/8/8 w - h 0 1");
		System.out.println(p.getFEN());
		// Al borrar la pieza, alPaso deberia resetearse, ya que no hay peon en esa columna
		p.borrarPieza('h', '5');
		assertEquals(p.getAlPaso(), 0);
		assertNull(p.getPieza('h', '5'));
		
		p.setPosicion("8/8/8/8/8/8/8/4K2R w K h 0 1");
	}

//	/*
//	 * Test method for 'com.mihail.chess.Position.esVacia(char, char)'
//	 */
//	public void testEsVacia() {
//
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.getClavePosicion()'
//	 */
//	public void testGetClavePosicion() {
//		Posicion p = new Posicion();
//		p.setPieza(new Pieza(Bando.BLANCO, 'C'), 'd', '4');
//		int clave = p.getClavePosicion();
//		p.borrarPieza('d', '4');
//		p.setPieza(new Pieza(Bando.BLANCO, 'C'), 'd', '4');
//		assertEquals(clave, p.getClavePosicion());
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setAlPaso(char)'
//	 */
//	public void testSetAlPaso() {
//		Posicion p = new Posicion();
//		// Comprobamos que no se pone siempre alPaso
//		p.setPieza(new Pieza(Bando.BLANCO, 'P'), 'a', '1');
//		p.setAlPaso('a');
//		assertEquals(p.getAlPaso(), 0);
//		// Comprobamos que al poner un peon negro en una casilla de alPaso, la funcion deja
//		p.setPieza(new Pieza(Bando.NEGRO, 'P'), 'h', '5');
//		p.setAlPaso('h');
//		assertEquals(p.getAlPaso(), 'h');
//		// Probamos lo mismo pero con un peon blanco
//		p.setPieza(new Pieza(Bando.BLANCO, 'P'), 'h', '4');
//		p.setTurno(Bando.NEGRO);
//		p.setAlPaso('h');
//		assertEquals(p.getAlPaso(), 'h');
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setContadorTablas(int)'
//	 */
//	public void testSetContadorTablas() {
//
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.addContadorTablas()'
//	 */
//	public void testAddContadorTablas() {
//
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.setNumeroMovimiento(int)'
//	 */
//	public void testSetNumeroMovimiento() {
//
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.addNumeroMovimiento()'
//	 */
//	public void testAddNumeroMovimiento() {
//
//	}
//
//	/*
//	 * Test method for 'com.mihail.chess.Position.getKingPosition(int)'
//	 */
//	public void testGetKingPosition() {
//
//	}
//	
//	public static void main(String [] args) {
//		junit.textui.TestRunner.run(PositionTest.class);
//	}
}
