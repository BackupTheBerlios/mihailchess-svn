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

import com.mihail.chess.Piece;
import com.mihail.chess.Position;
import junit.framework.TestCase;

import static com.mihail.chess.Board.Side;
import static com.mihail.chess.Piece.Type;

public class PositionTest extends TestCase {

	/*
	 * Test method for 'com.mihail.chess.Position.Position()'
	 */
	public void testPosition() {
		Position p = new Position();
		assertEquals("8/8/8/8/8/8/8/8 w - - 0 1", p.getFEN());
	}

	/*
	 * Test method for 'com.mihail.chess.Position.getFEN()'
	 */
	public void testGetFEN() {
		Position p = new Position();
		p.setFEN(Position.INITIAL_POSITION_FEN);
		assertEquals(
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", p
						.getFEN());
	}

	public void testSetPosicion() {
		Position p = new Position();
	}

	/*
	 * Test method for 'com.mihail.chess.Position.setTurno()'
	 */
	public void testSetTurno() {
		Position p = new Position();
		Side turno = p.getTurn();
		p.setTurn();
		p.setTurn();
		assertEquals(turno, p.getTurn());
	}

	/*
	 * Test method for 'com.mihail.chess.Position.setTurno(int)'
	 */
	public void testSetTurnoInt() {
		Position p = new Position();
		p.setTurn(Side.WHITE);
		assertEquals(Side.WHITE, p.getTurn());
		p.setTurn(Side.BLACK);
		assertEquals(Side.BLACK, p.getTurn());
	}

	/*
	 * Test method for 'com.mihail.chess.Position.setEnroqueCorto(int, boolean)'
	 */
	public void testSetEnroqueCorto() {
		Position p = new Position();
		p.setPiece(new Piece(Side.WHITE, Type.KING), 'e', '1');
		p.setKingsideCastling(Side.WHITE, true);
		assertFalse(p.getKingsideCastling(Side.WHITE));
		p.setPiece(new Piece(Side.WHITE, Type.ROOK), 'h', '1');
		p.setKingsideCastling(Side.WHITE, true);
		assertTrue(p.getKingsideCastling(Side.WHITE));

		p.setPiece(new Piece(Side.BLACK, Type.KING), 'e', '8');
		p.setKingsideCastling(Side.BLACK, true);
		assertFalse(p.getKingsideCastling(Side.BLACK));
		p.setPiece(new Piece(Side.BLACK, Type.ROOK), 'h', '8');
		p.setKingsideCastling(Side.BLACK, true);
		assertTrue(p.getKingsideCastling(Side.BLACK));
	}

	/*
	 * Test method for 'com.mihail.chess.Position.setEnroqueLargo(int, boolean)'
	 */
	public void testSetEnroqueLargo() {
		Position p = new Position();
		p.setPiece(new Piece(Side.WHITE, Type.KING), 'e', '1');
		p.setQueensideCastling(Side.WHITE, true);
		assertEquals(false, p.getQueensideCastling(Side.WHITE));
		p.setPiece(new Piece(Side.WHITE, Type.ROOK), 'a', '1');
		p.setQueensideCastling(Side.WHITE, true);
		assertTrue(p.getQueensideCastling(Side.WHITE));

		p.setPiece(new Piece(Side.BLACK, Type.KING), 'e', '8');
		p.setQueensideCastling(Side.BLACK, true);
		assertFalse(p.getQueensideCastling(Side.BLACK));
		p.setPiece(new Piece(Side.BLACK, Type.ROOK), 'a', '8');
		p.setQueensideCastling(Side.BLACK, true);
		assertTrue(p.getQueensideCastling(Side.BLACK));
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
		Position p = new Position("8/8/8/7p/8/8/8/8 w - h 0 1");
		// Al borrar la pieza, alPaso deberia resetearse, ya que no hay peon en
		// esa columna
		p.removePiece('h', '5');
		assertEquals(p.getEnPassant(), 0);
		assertNull(p.getPieza('h', '5'));

		p.setFEN("8/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		p.removePiece('e', '1');
		assertFalse(p.getQueensideCastling(Side.WHITE));
		assertFalse(p.getKingsideCastling(Side.WHITE));

		p.setFEN("8/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		p.removePiece('h', '1');
		assertTrue(p.getQueensideCastling(Side.WHITE));
		assertFalse(p.getKingsideCastling(Side.WHITE));

		p.setFEN("r3k2r/8/8/8/8/8/8/8 w kq - 0 1");
		p.removePiece('e', '8');
		assertFalse(p.getQueensideCastling(Side.BLACK));
		assertFalse(p.getKingsideCastling(Side.BLACK));

		p.setFEN("r3k2r/8/8/8/8/8/8/8 w kq - 0 1");
		p.removePiece('a', '8');
		assertFalse(p.getQueensideCastling(Side.BLACK));
		assertTrue(p.getKingsideCastling(Side.BLACK));
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
		Position p = new Position();
		p.setPiece(new Piece(Side.WHITE, Type.KNIGHT), 'd', '4');
		int clave = p.getPositionKey();
		p.removePiece('d', '4');
		p.setPiece(new Piece(Side.WHITE, Type.KNIGHT), 'd', '4');
		assertEquals(clave, p.getPositionKey());
	}

	/*
	 * Test method for 'com.mihail.chess.Position.setAlPaso(char)'
	 */
	public void testSetAlPaso() {
		Position p = new Position();
		// Comprobamos que no se pone siempre alPaso
		p.setPiece(new Piece(Side.WHITE, Type.PAWN), 'a', '1');
		p.setEnPassant('a');
		assertEquals(p.getEnPassant(), 0);
		// Comprobamos que al poner un peon negro en una casilla de alPaso, la
		// funcion deja
		p.setPiece(new Piece(Side.BLACK, Type.PAWN), 'h', '5');
		p.setEnPassant('h');
		assertEquals(p.getEnPassant(), 'h');
		// Probamos lo mismo pero con un peon blanco
		p.setPiece(new Piece(Side.WHITE, Type.PAWN), 'h', '4');
		p.setTurn(Side.BLACK);
		p.setEnPassant('h');
		assertEquals(p.getEnPassant(), 'h');
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

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PositionTest.class);
	}
}
