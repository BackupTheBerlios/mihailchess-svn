package com.mihail.chess;

import java.util.ArrayList;

public class Logica {

	public static enum Bando {
		BLANCO, NEGRO
	}
	
	/**
	 * Constante que representa al bando blanco.
	 */
	// public final static int BLANCO = 0;

	/**
	 * Constante que representa al bando negro.
	 */
	// public final static int NEGRO = 1;

	/**
	 * Constantes de resultado de partida
	 */
	public static final int JAQUE_MATE_BLANCO = 6;
	public static final int JAQUE_MATE_NEGRO = 1;
	public static final int TABLAS_REPETICION = 2;
	public static final int TABLAS_50_MOV = 3;
	public static final int TABLAS_INSUF_MATERIAL = 4;
	public static final int TABLAS_AHOGADO = 5;

	/**
	 * Este atributo sirve para guardar la lista de movimientos de una partida.
	 */
	private ArrayList<Movimiento> movimientos;

	/**
	 * Este atributo sirve para saber en que posicion de la lista de movimientos
	 * nos encontramos, a la hora de avanzar y retroceder por una partida.
	 * Siempre va una posición por delante de la auténtica posición
	 */
	private int indice;

	/**
	 * Este atributo se utiliza para las coronaciones.
	 */
	private char coronar = 'D';

	/**
	 * Tabla hash usada para comprobar posiciones repetidas.
	 */
	private TablaHash hash = new TablaHash (51);
	
	/**
	 * Posicion actual en juego
	 */
	private Posicion posicion;

	/**
	 * Crea una nueva instancia de la clase y crea las piezas, situandolas en su
	 * posicion de inicio de partida.
	 */
	public Logica () {
		this (Posicion.POS_INICIAL);
	}

	/**
	 * Crea una nueva instancia de la clase y crea las piezas, colocandolas en
	 * la posicion que indica el String posInicial.
	 * 
	 * @param posInicial
	 *            Es un String que indica una posicion de juego, siguiendo el
	 *            estandar FEN.
	 */
	public Logica (String posInicial) {
		movimientos = new ArrayList<Movimiento> ();
		posicion.setPosicion (posInicial);
		hash.insertar (posicion.getClavePosicion());
	}

	/**
	 * Reinicia la posicion del tablero a la posicion inicial.
	 */
	public void reiniciarTablero () {
		posicion.setPosicion (Posicion.POS_INICIAL);
	}

	/**
	 * Indica a que pieza queremos coronar por defecto.
	 * 
	 * @param c
	 *            Caracter que indica el tipo de pieza a coronar (C, A, T, D).
	 */

	public void setCoronacion (char c) {
		coronar = c;
	}

	/**
	 * Permite saber el numero total de movimientos de la partida.
	 * 
	 * @return Un entero, el numero en cuestion.
	 */

	public int getNumTotalMovimientos () {
		return movimientos.size ();
	}

	/**
	 * Obtiene el resultado de la partida.
	 * 
	 * @return Devuelve un caracter: <BR>
	 *         'B' -> Victoria Blanca <BR>
	 *         'N' -> Victoria Negra <BR>
	 *         'T' -> Tablas <BR>
	 *         '\0' -> Partida Inacabada o Resultado Desconocido
	 */
	public int getResultado () {
		return movimientos.get (indice - 1).finPartida;
	}

	/**
	 * Calcula los movimientos validos para todas las piezas del tablero.
	 */
	private void calcularMovimientos () {
		for (char i = 'a'; i <= 'h'; i++) {
			for (char j = '1'; j <= '8'; j++) {
				Pieza p = posicion.getPieza(i, j);
				if (p != null) {
					if (!Pieza.esBandoContrario (posicion.getTurno(), p)) {
						calcularMovimientos (p);
					}
				}
			}
		}
	}

	/**
	 * Calcula los movimientos validos para una pieza en concreto, que se le
	 * pasa como parametro.
	 * 
	 * @param pieza
	 *            Pieza de la que queremos calcular sus movimientos legales
	 */
	private void calcularMovimientos (Pieza pieza) {
		int posNum = pieza.num - '1', posLet = pieza.letra - 'a';
		int numI = 0, letI = 0;
		pieza.casillasValidas.clear ();
		switch (pieza.tipo) {
			case 'P':
				// Peon
				// Peon blanco
				if (pieza.bando == Bando.BLANCO) {
					// Movimiento hacia delante
					// Hacemos dos iteraciones, una para el caso de que avance
					// una casilla, otra para el caso de que avance dos
					if (posicion.esVacia (pieza.letra, (char) (pieza.num + 1))) {
						if (esLegal (pieza.letra, pieza.num, pieza.letra,
										(char) (pieza.num + 1))) {
							pieza.anadirMov (pieza.letra,
												(char) (pieza.num + 1));
						}
						if (pieza.num == '2'
								&& posicion.esVacia (pieza.letra, (char) (pieza.num + 1))
								&& posicion.esVacia (pieza.letra, (char) (pieza.num + 2))) {
							if (esLegal (pieza.letra, pieza.num, pieza.letra,
											(char) (pieza.num + 2))) {
								pieza.anadirMov (pieza.letra,
													(char) (pieza.num + 2));
							}
						}
					}
					if (posicion.getAlPaso() != '\0' && pieza.num == '5'
							&& Math.abs (pieza.letra - posicion.getAlPaso()) == 1) {
						pieza.anadirMov (posicion.getAlPaso(), (char) (pieza.num + 1));
					}
					// Movimientos para comer
					try {
						Pieza p = posicion.getPieza((char)(pieza.letra+1), (char)(pieza.num+1));
						if (p != null && p.bando == Bando.NEGRO) {
							if (esLegal (pieza.letra, pieza.num,
											(char) (pieza.letra + 1),
											(char) (pieza.num + 1))) {
								pieza.anadirMov ((char) (pieza.letra + 1),
													(char) (pieza.num + 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
					try {
						Pieza p = posicion.getPieza((char)(pieza.letra+1), (char)(pieza.num-1));
						if (p != null && p.bando == Bando.NEGRO) {
							if (esLegal (pieza.letra, pieza.num,
											(char) (pieza.letra - 1),
											(char) (pieza.num + 1))) {
								pieza.anadirMov ((char) (pieza.letra - 1),
													(char) (pieza.num + 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				// Peon negro
				else {
					if (posicion.esVacia (pieza.letra, (char) (pieza.num - 1))) {
						if (esLegal (pieza.letra, pieza.num, pieza.letra,
										(char) (pieza.num - 1))) {
							pieza.anadirMov (pieza.letra,
												(char) (pieza.num - 1));

						}
						if (pieza.num == '7'
								&& posicion.esVacia (pieza.letra, (char) (pieza.num - 1))
								&& posicion.esVacia (pieza.letra, (char) (pieza.num - 2))) {
							if (esLegal (pieza.letra, pieza.num, pieza.letra,
											(char) (pieza.num - 2))) {
								pieza.anadirMov (pieza.letra,
													(char) (pieza.num - 2));
							}
						}
					}
					if (posicion.getAlPaso() != '\0' && pieza.num == '4'
							&& Math.abs (pieza.letra - posicion.getAlPaso()) == 1) {
						pieza.anadirMov (posicion.getAlPaso(), (char) (pieza.num - 1));
					}
					try {
						Pieza p = posicion.getPieza((char)(pieza.letra-1), (char)(pieza.num+1));
						if (p != null && p.bando == Bando.NEGRO) {
							if (esLegal (pieza.letra, pieza.num,
											(char) (pieza.letra + 1),
											(char) (pieza.num - 1))) {
								pieza.anadirMov ((char) (pieza.letra + 1),
													(char) (pieza.num - 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
					try {
						Pieza p = posicion.getPieza((char)(pieza.letra-1), (char)(pieza.num-1));
						if (p != null && p.bando == Bando.NEGRO) {
							if (esLegal (pieza.letra, pieza.num,
											(char) (pieza.letra - 1),
											(char) (pieza.num - 1))) {
								pieza.anadirMov ((char) (pieza.letra - 1),
													(char) (pieza.num - 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				break;
			case 'C':
				posLet = pieza.letra - 'a';
				posNum = pieza.num - '1';
				for (int i = 0; i < pieza.direcciones.length; i++) {
					VectorDireccion v = pieza.direcciones[i];
					boolean piezaEncontrada = false;
					letI = posLet + v.getY ();
					numI = posNum + v.getX ();
					char l = (char)(letI + 'a'), n = (char)(numI + '1');
					try {
						if (posicion.getPieza(l, n) == null) {

							if (esLegal (pieza.letra, pieza.num, l, n))
								pieza.anadirMov (l, n);
						}
						else
							piezaEncontrada = true;
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
					if (piezaEncontrada
							&& Pieza.esBandoContrario (pieza, posicion.getPieza(l, n))
							&& esLegal (pieza.letra, pieza.num, l, n)) {
						pieza.anadirMov (l, n);
					}
				}

				break;
			case 'D':
			case 'A':
			case 'T':
				posLet = pieza.letra - 'a';
				posNum = pieza.num - '1';
				for (int i = 0; i < pieza.direcciones.length; i++) {
					VectorDireccion v = pieza.direcciones[i];
					boolean piezaEncontrada = false;

					letI = posLet + v.getY ();
					numI = posNum + v.getX ();
					char l = (char)(letI + 'a'), n = (char)(numI + '1');
					try {
						while (posicion.getPieza(l, n) == null) {
							
							if (esLegal (pieza.letra, pieza.num, l, n))
								pieza.anadirMov ((char) (letI + 'a'),
													(char) (numI + '1'));
							letI += v.getY ();
							numI += v.getX ();
							l = (char)(letI + 'a');
							n = (char)(numI + '1');
						}
						piezaEncontrada = true;
					}
					catch (ArrayIndexOutOfBoundsException e) {

					}

					if (piezaEncontrada
							&& Pieza.esBandoContrario (pieza, posicion.getPieza(l, n))
							&& esLegal (pieza.letra, pieza.num,
										(char) (letI + 'a'),
										(char) (numI + '1'))) {
						pieza.anadirMov ((char) (letI + 'a'),
											(char) (numI + '1'));
					}
				}
				break;
			case 'R':
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						try {
							char letDest = (char) (pieza.letra + i), numDest = (char) (pieza.num + j);
							if (j != 0 || i != 0) {
								if (esLegal (pieza.letra, pieza.num, letDest, numDest)
									&& (posicion.esVacia (letDest, numDest) 
									|| (!posicion.esVacia (letDest, numDest) 
									&& posicion.getPieza(letDest, numDest).bando != pieza.bando))) {
									pieza.anadirMov (letDest, numDest);
								}
							}
						}
						catch (ArrayIndexOutOfBoundsException e) {
						}
					}
				}
				if (posicion.getEnroqueCorto(posicion.getTurno())
						&& !esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()))
						&& posicion.esVacia ((char) (pieza.letra + 1), pieza.num)
						&& !esCasillaAtacada ((char) (pieza.letra + 1),
												(pieza.num))
						&& posicion.esVacia ((char) (pieza.letra + 2), pieza.num)
						&& !esCasillaAtacada ((char) (pieza.letra + 2),
												(pieza.num))) {
					pieza.anadirMov ((char) (pieza.letra + 2), pieza.num);
				}
				if (posicion.getEnroqueLargo(posicion.getTurno())
						&& !esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()))
						&& posicion.esVacia ((char) (pieza.letra - 1), pieza.num)
						&& !esCasillaAtacada ((char) (pieza.letra - 1),
												(pieza.num))
						&& posicion.esVacia ((char) (pieza.letra - 2), pieza.num)
						&& !esCasillaAtacada ((char) (pieza.letra - 2),
												(pieza.num))) {
					pieza.anadirMov ((char) (pieza.letra - 2), pieza.num);
				}
				break;
		}
	}

	/**
	 * Metodo de utilidad que se comporta exactamente igual que esCasillaAtacada(char, char)
	 * 
	 * @param c Casilla que queremos comprobar si esta siendo atacada
	 * @return Devuelve un booleano indicando si es una casilla atacada o no
	 */
	private boolean esCasillaAtacada(Casilla c) {
		return esCasillaAtacada(c.getLetra(), c.getNumero());
	}
	
	/**
	 * esCasillaAtacada determina si hay alguna pieza que ataque la casilla que
	 * se le pasa como parametro. <BR>
	 * NOTA sobre el codigo: se necesita comprobar el turno para: en el turno en
	 * el que mueves necesitas saber que casillas estan atacadas por el bando
	 * contrario, y solo por este bando. No interesan las casillas que ataca el
	 * bando que mueve. Ademas se puede comprobar que una casillas es atacada
	 * estando vacia lo cual implica que no hay colores contrarios y es
	 * necesario conocer el turno para saber quien ataca
	 * 
	 * @param letra
	 *            Letra de la casilla que queremos comprobar si esta siendo
	 *            atacada
	 * @param num
	 *            Número de la casilla que queremos comprobart si está siendo
	 *            atacada
	 * @return Devuelve un booleano indicando si es una casilla atacada o no
	 */
	private boolean esCasillaAtacada (char letra, char num) {
		Pieza piezaObjetivo;
		int posNum = num - '1', posLet = letra - 'a';
		int letI, numI;
		// Primero miro las casillas
		// a salto de caballo. Despues, las verticales, horizontales y
		// diagonales.

		// Casillas a salto de caballo
		// Con dos bucles for conseguimos recorrer las 8 casillas de caballo

		// Se hace un bucle que va {-2, -1, 1, 2}
		for (int i = 0; i <= 3; i++) {
			if (i == 0 || i == 1) {
				numI = posNum + (i - 2);
			}
			else { // i==2 || i==3
				numI = posNum + (i - 1);
				// Este bucle va {-1, 1}
			}
			for (int j = -1; j <= 1; j = j + 2) {
				try {
					if (i == 0 || i == 3) {
						letI = posLet + j;
					}
					else { // i==1 || i==2
						letI = posLet + 2 * j;
					}
					Pieza p = posicion.getPieza((char)(letI + 'a'), (char)(numI + '1'));
					if (p != null) {
						if (Pieza.esBandoContrario (posicion.getTurno(), p) && p.tipo == 'C') {
							return true;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		// Vertical hacia arriba
		try {
			letI = posLet;
			numI = posNum + 1;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				n++;
			}
			// Si la pieza es del bando contrario, y es una dama o una torre,
			// la casilla estara atacada.
			// Si no, ademas, si es un rey, y se encuentra en la casilla
			// adyacente, tambien lo estara.
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'T' || (numI == posNum + 1 && piezaObjetivo.tipo == 'R'))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Vertical hacia abajo
		try {
			letI = posLet;
			numI = posNum - 1;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				n--;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'T' || (numI == posNum - 1 && piezaObjetivo.tipo == 'R'))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Horizontal hacia la izquierda
		try {
			letI = posLet - 1;
			numI = posNum;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				l--;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'T' || (letI == posLet - 1 && piezaObjetivo.tipo == 'R'))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Horizontal hacia la derecha
		try {
			letI = posLet + 1;
			numI = posNum;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				l++;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'T' || (letI == posLet + 1 && piezaObjetivo.tipo == 'R'))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Diagonal abajo izquierda
		try {
			letI = posLet - 1;
			numI = posNum - 1;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				n--;
				l--;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'A' || (numI == posNum - 1
							&& letI == posLet - 1 && (piezaObjetivo.tipo == 'R' || (piezaObjetivo.tipo == 'P' && piezaObjetivo.bando == Bando.BLANCO))))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Diagonal abajo derecha
		try {
			letI = posLet + 1;
			numI = posNum - 1;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				n--;
				l++;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'A' || (numI == posNum - 1
							&& letI == posLet + 1 && (piezaObjetivo.tipo == 'R' || (piezaObjetivo.tipo == 'P' && piezaObjetivo.bando == Bando.BLANCO))))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Diagonal arriba izquierda
		try {
			letI = posLet - 1;
			numI = posNum + 1;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				n++;
				l--;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'A' || (numI == posNum + 1
							&& letI == posLet - 1 && (piezaObjetivo.tipo == 'R' || (piezaObjetivo.tipo == 'P' && piezaObjetivo.bando == Bando.NEGRO))))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		// Diagonal arriba derecha
		try {
			letI = posLet + 1;
			numI = posNum + 1;
			char l = (char)(letI + 'a'), n = (char)(numI + '1');
			while ((piezaObjetivo=posicion.getPieza(l, n)) == null) {
				n++;
				l++;
			}
			if (Pieza.esBandoContrario (posicion.getTurno(), piezaObjetivo)
					&& (piezaObjetivo.tipo == 'D' || piezaObjetivo.tipo == 'A' || (numI == posNum + 1
							&& letI == posLet + 1 && (piezaObjetivo.tipo == 'R' || (piezaObjetivo.tipo == 'P' && piezaObjetivo.bando == Bando.NEGRO))))) {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		return false;
	}

	/**
	 * Determina si un movimiento es legal, analizando la situacion en la
	 * partida.
	 * 
	 * @param letOrig
	 *            Letra de la casilla de origen.
	 * @param numOrig
	 *            Numero de la casilla de origen.
	 * @param letDest
	 *            Letra de la casilla de destino.
	 * @param numDest
	 *            Numero de la casilla de destino.
	 * @return True si el movimiento es legal, false en caso contrario.
	 */
	private boolean esLegal (char letOrig, char numOrig, char letDest,
			char numDest) {
		boolean resultado;
		Pieza movida = posicion.getPieza (letOrig, numOrig);
		Pieza temp = posicion.getPieza (letDest, numDest);
		posicion.borrarPiezaInternal(letOrig, numOrig);
		posicion.setPiezaInternal (movida, letDest, numDest);
		resultado = esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()));
		posicion.borrarPiezaInternal(letDest, numDest);
		posicion.setPiezaInternal (movida, letOrig, numOrig);
		posicion.setPiezaInternal (temp, numDest, letDest);
		return !resultado;
	}

	/**
	 * Este metodo mueve una pieza en el tablero, comprobando que este
	 * movimiento sea permitido.
	 * 
	 * @param origenLetra
	 *            Es la letra de la casilla de origen
	 * @param origenNum
	 *            Es el numero de la casilla de origen
	 * @param destinoLetra
	 *            Es la letra de la casilla de destino
	 * @param destinoNum
	 *            Es el numero de la casilla de destino
	 * @return Devuelve un objeto Movimiento o null si no esta permitido.
	 */
	public Movimiento mover (char origenLetra, char origenNum,
			char destinoLetra, char destinoNum) {
		Pieza piezaQueMueve;
		int i;
		Movimiento mov;
		piezaQueMueve = posicion.getPieza (origenLetra, origenNum);

		// Comprobamos que en la casilla de origen hay una pieza
		if (piezaQueMueve == null) {
			return null;
		}

		if (Pieza.esBandoContrario (posicion.getTurno(), piezaQueMueve)) {
			return null;
		}
		if (indice != movimientos.size ())
			return null;
		// Buscamos la casilla de destino entre las casillas validas de la
		// pieza.
		i = 0;
		do {
			// Buscamos la letra.
			while ((i < piezaQueMueve.casillasValidas.size ())
					&& (destinoLetra != piezaQueMueve.casillasValidas.get (i).getLetra())) {
				i++;
				// Comprobamos si el numero de la letra encontrada coincide.
			}
			if (i < piezaQueMueve.casillasValidas.size ()) {
				// Si se entra en el siguiente caso, es que el movimiento es
				// valido
				if (destinoNum == piezaQueMueve.casillasValidas.get (i).getLetra()) {
					mov = new Movimiento ();
					mov.origenLetra = origenLetra;
					mov.origenNum = origenNum;
					mov.destinoLetra = destinoLetra;
					mov.destinoNum = destinoNum;
					mov.numeroMovimiento = posicion.getNumeroMovimiento();
					mov.bando = posicion.getTurno();
					mov.tipoPieza = piezaQueMueve.tipo;

					// Si se come ponemos el contador a 0
					if (!posicion.esVacia (destinoLetra, destinoNum)) {
						posicion.setContadorTablas(0);
						hash.borrarTabla ();
						mov.casillaComerLetra = destinoLetra;
						mov.casillaComerNum = destinoNum;
						mov.tipoPiezaComida = posicion.getPieza (destinoLetra,
														destinoNum).tipo;
						posicion.borrarPieza(mov.casillaComerLetra, mov.casillaComerNum);
					}

					// Se hacen los calculos especiales si se trata de un peon
					if (piezaQueMueve.tipo == 'P') {
						// Se borra la pieza correspondiente si se come al paso
						if (Math.abs (destinoLetra - origenLetra) == 1
								&& posicion.esVacia (destinoLetra, destinoNum)) {
							mov.tipoPiezaComida = posicion.getPieza (destinoLetra,
															origenNum).tipo;
							mov.casillaComerLetra = destinoLetra;
							mov.casillaComerNum = origenNum;
							posicion.borrarPieza(mov.casillaComerLetra, mov.casillaComerNum);
						}
						// Se establece la variable alPaso a su valor
						// correspondiente
						if (Math.abs (destinoNum - origenNum) == 2) {
							posicion.setAlPaso(origenLetra);
							mov.alPaso = posicion.getAlPaso();
						}
						else {
							posicion.setAlPaso('\0');
						}
						// Coronacion
						if (destinoNum == '1' || destinoNum == '8') {
							// if (mostrarDialogoCoronacion) {
							// mostrarDialogoCoronacion ();
							// }
							piezaQueMueve = new Pieza (
									piezaQueMueve.bando, coronar);

							mov.coronacion = coronar;
						}
						posicion.setContadorTablas(0);
						hash.borrarTabla ();
					}
					// Se hacen los calculos especiales si se trata de un rey
					if (piezaQueMueve.tipo == 'R') {
						// Movemos las torres en caso de enroque
						if ((destinoLetra - origenLetra) == 2) {
							Pieza torre = posicion.getPieza ('h', origenNum);
							posicion.borrarPieza ('h', origenNum);
							posicion.setPieza (torre, 'f', origenNum);
						}
						else {
							if ((destinoLetra - origenLetra) == -2) {
								Pieza torre = posicion.getPieza ('a', origenNum);
								posicion.borrarPieza ('a', origenNum);
								posicion.setPieza (torre, 'd', origenNum);
							}
						}
					}

					mov.contadorTablas = posicion.getContadorTablas();
					mov.enroque[0][0] = posicion.getEnroqueCorto(Bando.BLANCO);
					mov.enroque[0][1] = posicion.getEnroqueLargo(Bando.BLANCO);
					mov.enroque[1][0] = posicion.getEnroqueLargo(Bando.NEGRO);
					mov.enroque[1][1] = posicion.getEnroqueLargo(Bando.NEGRO);
					if (posicion.getTurno() == Bando.NEGRO)
						posicion.addNumeroMovimiento();
					posicion.setTurno ();

					posicion.borrarPieza (origenLetra, origenNum);
					posicion.setPieza (piezaQueMueve, destinoLetra, destinoNum);
					posicion.addContadorTablas();
					calcularMovimientos ();

					if (esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()))) {
						mov.jaque = true;
					}
					else {
						mov.jaque = false;
					}
					mov.finPartida = esFinPartida ();

					hash.insertar (posicion.getClavePosicion());
					movimientos.add (mov);
					indice++;
					return mov;
				}
				else {
					i++;
				}
			}
			else {
				return null;
			}
		}
		while (true);
	}

	/**
	 * Este metodo comprueba si se produce alguna situacion en la que termine
	 * una partida.
	 * 
	 * @return Devuelve '\0' si la partida no se ha acabado, 'B' si las blancas
	 *         han dado jaque mate, 'N' si las negras han dado jaque mate o 'T'
	 *         si se produce una situacion de tablas.
	 */
	private char esFinPartida () {
		Pieza pieza;
		char devolver = '\0';
		boolean fin = false, fin2 = false, posibleMatInsuf = false;
		char i, j;
		// Tablas por 50 movimientos
		if (posicion.getContadorTablas() == 50) {
			fin = true;
			devolver = TABLAS_50_MOV;
		}
		// Tablas por repeticion de posiciones
		if (hash.getRepeticiones (posicion.getClavePosicion()) == 3) {
			fin = true;
			devolver = TABLAS_REPETICION;
		}
		// Tablas por material insuficiente
		i = 'a';
		j = '1';
		while (i <= 'h' && !fin2) {
			while (j <= '8' && !fin2) {
				pieza = posicion.getPieza(i, j);
				if (pieza != null) {
					if (posibleMatInsuf) {
						if (pieza.tipo != 'R') {
							fin2 = true;
						}
					}
					else {
						switch (pieza.tipo) {
							case 'P':
							case 'D':
							case 'T':
								fin2 = true;
								break;
							case 'A':
							case 'C':
								posibleMatInsuf = true;
								break;
						}
					}
				}
				j++;
			}
			j = '1';
			i++;
		}
		if (!fin2) {
			fin = true;
			devolver = TABLAS_INSUF_MATERIAL;
		}
		// Miramos si hay movimientos posibles
		i = 'a';
		j = '1';
		while (i <= 'h' && !fin) {
			while (j <= '8' && !fin) {
				pieza = posicion.getPieza(i, j);
				if ((pieza != null) && (!Pieza.esBandoContrario (posicion.getTurno(), pieza))
						&& (!pieza.casillasValidas.isEmpty ())) {
					fin = true;
				}
				j++;
			}
			j = '1';
			i++;
		}
		if (!fin) {
			// Negras dan jaque mate
			if (posicion.getTurno () == Bando.BLANCO)
				if (esCasillaAtacada (posicion.getKingPosition(Bando.BLANCO))) {
					devolver = JAQUE_MATE_NEGRO;
				}
				else {
					devolver = TABLAS_AHOGADO;
				}
			else
				// Blancas dan jaque mate
				if (esCasillaAtacada (posicion.getKingPosition(Bando.NEGRO))) {
					devolver = JAQUE_MATE_BLANCO;
				}
				// Tablas por ahogado
				else {
					devolver = TABLAS_AHOGADO;
				}
		}
		return devolver;
	}
}
