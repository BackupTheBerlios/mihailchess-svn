package com.mihail.chess;

import java.util.ArrayList;

import com.mihail.chess.Pieza.Tipo;

public class Logica {

	public static enum Bando {
		BLANCO, NEGRO;
		
		public boolean equals (Bando b) {
			return this == b;			
		}
	}
	
	public static enum Resultado {
		JAQUE_MATE_BLANCO, JAQUE_MATE_NEGRO, TABLAS_REPETICION, TABLAS_50_MOV, TABLAS_INSUF_MATERIAL, TABLAS_AHOGADO
	}
//	public static final int JAQUE_MATE_BLANCO = 6;
//	public static final int JAQUE_MATE_NEGRO = 1;
//	public static final int TABLAS_REPETICION = 2;
//	public static final int TABLAS_50_MOV = 3;
//	public static final int TABLAS_INSUF_MATERIAL = 4;
//	public static final int TABLAS_AHOGADO = 5;

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
	private Tipo coronar = Tipo.DAMA;

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
	public Logica (Posicion posInicial) {
		movimientos = new ArrayList<Movimiento> ();
		posicion = posInicial;
		hash.insertar (posicion.getClavePosicion());
	}

	/**
	 * Reinicia la posicion del tablero a la posicion inicial.
	 */
	public void reiniciarTablero () {
		posicion = Posicion.POS_INICIAL;
	}

	/**
	 * Indica a que pieza queremos coronar por defecto.
	 * 
	 * @param c
	 *            Caracter que indica el tipo de pieza a coronar (C, A, T, D).
	 */

	public void setCoronacion (Tipo c) {
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
	public Resultado getResultado () {
		return movimientos.get (indice - 1).getFinPartida();
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
		pieza.getCasillasValidas().clear ();
		switch (pieza.getTipo()) {
			case PEON:
				// Peon
				// Peon blanco
				if (pieza.getBando() == Bando.BLANCO) {
					// Movimiento hacia delante
					// Hacemos dos iteraciones, una para el caso de que avance
					// una casilla, otra para el caso de que avance dos
					if (posicion.esVacia (pieza.getLetra(), (char) (pieza.getNum() + 1))) {
						if (esLegal (pieza.getLetra(), pieza.getNum(), pieza.getLetra(),
										(char) (pieza.getNum() + 1))) {
							pieza.anadirMov (pieza.getLetra(),
												(char) (pieza.getNum() + 1));
							if (pieza.getNum() == '2'
								&& posicion.esVacia (pieza.getLetra(), (char) (pieza.getNum() + 2))) {
								if (esLegal (pieza.getLetra(), pieza.getNum(), pieza.getLetra(),
											(char) (pieza.getNum() + 2))) {
									pieza.anadirMov (pieza.getLetra(),
											(char) (pieza.getNum() + 2));
								}
							}
						}
					}
					if (posicion.getAlPaso() != '\0' && pieza.getNum() == '5'
							&& Math.abs (pieza.getLetra() - posicion.getAlPaso()) == 1) {
						pieza.anadirMov (posicion.getAlPaso(), (char) (pieza.getNum() + 1));
					}
					// Movimientos para comer
					try {
						Pieza p = posicion.getPieza((char)(pieza.getLetra()+1), (char)(pieza.getNum()+1));
						if (p != null && p.getBando() == Bando.NEGRO) {
							if (esLegal (pieza.getLetra(), pieza.getNum(),
											(char) (pieza.getLetra() + 1),
											(char) (pieza.getNum() + 1))) {
								pieza.anadirMov ((char) (pieza.getLetra() + 1),
													(char) (pieza.getNum() + 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
					try {
						Pieza p = posicion.getPieza((char)(pieza.getLetra()+1), (char)(pieza.getNum()-1));
						if (p != null && p.getBando() == Bando.NEGRO) {
							if (esLegal (pieza.getLetra(), pieza.getNum(),
											(char) (pieza.getLetra() - 1),
											(char) (pieza.getNum() + 1))) {
								pieza.anadirMov ((char) (pieza.getLetra() - 1),
													(char) (pieza.getNum() + 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				// Peon negro
				else {
					if (posicion.esVacia (pieza.getLetra(), (char) (pieza.getNum() - 1))) {
						if (esLegal (pieza.getLetra(), pieza.getNum(), pieza.getLetra(),
										(char) (pieza.getNum() - 1))) {
							pieza.anadirMov (pieza.getLetra(),
												(char) (pieza.getNum() - 1));
							if (pieza.getNum() == '7'
								&& posicion.esVacia (pieza.getLetra(), (char) (pieza.getNum() - 2))) {
								if (esLegal (pieza.getLetra(), pieza.getNum(), pieza.getLetra(),
											(char) (pieza.getNum() - 2))) {
								pieza.anadirMov (pieza.getLetra(),
													(char) (pieza.getNum() - 2));
								}
							}
						}
					}
					if (posicion.getAlPaso() != '\0' && pieza.getNum() == '4'
							&& Math.abs (pieza.getLetra() - posicion.getAlPaso()) == 1) {
						pieza.anadirMov (posicion.getAlPaso(), (char) (pieza.getNum() - 1));
					}
					try {
						Pieza p = posicion.getPieza((char)(pieza.getLetra()-1), (char)(pieza.getNum()+1));
						if (p != null && p.getBando() == Bando.NEGRO) {
							if (esLegal (pieza.getLetra(), pieza.getNum(),
											(char) (pieza.getLetra() + 1),
											(char) (pieza.getNum() - 1))) {
								pieza.anadirMov ((char) (pieza.getLetra() + 1),
													(char) (pieza.getNum() - 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
					try {
						Pieza p = posicion.getPieza((char)(pieza.getLetra()-1), (char)(pieza.getNum()-1));
						if (p != null && p.getBando() == Bando.NEGRO) {
							if (esLegal (pieza.getLetra(), pieza.getNum(),
											(char) (pieza.getLetra() - 1),
											(char) (pieza.getNum() - 1))) {
								pieza.anadirMov ((char) (pieza.getLetra() - 1),
													(char) (pieza.getNum() - 1));
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				break;
			case CABALLO:
				for(VectorDireccion v: pieza.getDirecciones()) {
					Casilla destino = pieza.getCasilla().add(v);
					try {
						if(esLegal(pieza.getLetra(), pieza.getNum(), destino.getLetra(), destino.getNumero())) {
							Pieza p = posicion.getPieza(destino.getLetra(), destino.getNumero());
							if(p == null || (p!=null && Pieza.esBandoContrario(pieza, p)))
								pieza.anadirMov(destino);
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}

				break;
			case DAMA:
			case ALFIL:
			case TORRE:
				for(VectorDireccion v: pieza.getDirecciones()) {
					try {
						Casilla destino = pieza.getCasilla().add(v);
						Pieza p = posicion.getPieza(destino.getLetra(), destino.getNumero());
						while(p==null) {
							p = posicion.getPieza(destino.getLetra(), destino.getNumero());
							if(esLegal(pieza.getLetra(), pieza.getNum(), destino.getLetra(), destino.getNumero())) {
								if(p == null || (p!=null && Pieza.esBandoContrario(pieza, p))) {
									pieza.anadirMov(destino);
								}
							}
							destino = destino.add(v);
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}

				break;
			case REY:
				for(VectorDireccion v: pieza.getDirecciones()) {
					Casilla destino = pieza.getCasilla().add(v);
					try {
						if(esLegal(pieza.getLetra(), pieza.getNum(), destino.getLetra(), destino.getNumero())) {
							Pieza p = posicion.getPieza(destino.getLetra(), destino.getNumero());
							if(p == null || (p!=null && Pieza.esBandoContrario(pieza, p)))
								pieza.anadirMov(destino);
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}

				if (posicion.getEnroqueCorto(posicion.getTurno())
						&& !esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()))
						&& posicion.esVacia ((char) (pieza.getLetra() + 1), pieza.getNum())
						&& !esCasillaAtacada ((char) (pieza.getLetra() + 1),
												(pieza.getNum()))
						&& posicion.esVacia ((char) (pieza.getLetra() + 2), pieza.getNum())
						&& !esCasillaAtacada ((char) (pieza.getLetra() + 2),
												(pieza.getNum()))) {
					pieza.anadirMov ((char) (pieza.getLetra() + 2), pieza.getNum());
				}
				if (posicion.getEnroqueLargo(posicion.getTurno())
						&& !esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()))
						&& posicion.esVacia ((char) (pieza.getLetra() - 1), pieza.getNum())
						&& !esCasillaAtacada ((char) (pieza.getLetra() - 1),
												(pieza.getNum()))
						&& posicion.esVacia ((char) (pieza.getLetra() - 2), pieza.getNum())
						&& !esCasillaAtacada ((char) (pieza.getLetra() - 2),
												(pieza.getNum()))) {
					pieza.anadirMov ((char) (pieza.getLetra() - 2), pieza.getNum());
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
		VectorDireccion [] dir = new VectorDireccion[8];
		dir[0] = new VectorDireccion (1, 2);
		dir[1] = new VectorDireccion (-1, 2);
		dir[2] = new VectorDireccion (2, 1);
		dir[3] = new VectorDireccion (2, -1);
		dir[4] = new VectorDireccion (1, -2);
		dir[5] = new VectorDireccion (-1, -2);
		dir[6] = new VectorDireccion (-2, 1);
		dir[7] = new VectorDireccion (-2, -1);
		for(VectorDireccion v: dir) {
			try {
				Pieza p = posicion.getPieza((char)(letra + v.getX()), (char)(num + v.getY()));
				if (p != null) {
					if (Pieza.esBandoContrario (posicion.getTurno(), p) && p.getTipo() == Tipo.CABALLO) {
						return true;
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {}
		}
		dir[0] = new VectorDireccion (1, 0);
		dir[1] = new VectorDireccion (-1, 0);
		dir[2] = new VectorDireccion (0, 1);
		dir[3] = new VectorDireccion (0, -1);
		
		for(VectorDireccion v: dir) {
			try {
				char letDest=(char)(letra + v.getX()), numDest=(char)(num + v.getY());
				Pieza p = posicion.getPieza(letDest, numDest);
				while(p==null) {
					letDest=(char)(letDest + v.getX());
					numDest=(char)(numDest + v.getY());
					p = posicion.getPieza(letDest, numDest);
				}
				if (Pieza.esBandoContrario (posicion.getTurno(), p)
						&& (p.getTipo() == Tipo.DAMA || 
							p.getTipo() == Tipo.TORRE || 
							(num+v.getY() == numDest && letra+v.getX() == letDest && p.getTipo() == Tipo.REY))) {
					return true;
				}
			} catch(ArrayIndexOutOfBoundsException e) {}
		}
		/*dir[0] = new VectorDireccion (1, 1); // CASO DE LAS DIAGONALES. QUEDA MIRAR COMO SE HARIA PARA LOS PEONES
		dir[1] = new VectorDireccion (-1, 1);
		dir[2] = new VectorDireccion (1, -1);
		dir[3] = new VectorDireccion (-1, -1);
		for(VectorDireccion v: dir) {
			try {
				char letDest=(char)(letra + v.getX()), numDest=(char)(num + v.getY());
				Pieza p = posicion.getPieza(letDest, numDest);
				while(p==null) {
					letDest=(char)(letDest + v.getX());
					numDest=(char)(numDest + v.getY());
					p = posicion.getPieza(letDest, numDest);
				}
				if (Pieza.esBandoContrario (posicion.getTurno(), p)
						&& (p.getTipo() == Tipo.DAMA || 
							p.getTipo() == Tipo.ALFIL || 
							(num+v.getY() == numDest && letra+v.getX() == letDest && p.getTipo() == Tipo.REY))) {
					return true;
				}
			} catch(ArrayIndexOutOfBoundsException e) {}
		}*/
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
					&& (piezaObjetivo.getTipo() == Tipo.DAMA || piezaObjetivo.getTipo() == Tipo.ALFIL || (numI == posNum - 1
							&& letI == posLet - 1 && (piezaObjetivo.getTipo() == Tipo.REY || (piezaObjetivo.getTipo() == Tipo.PEON && piezaObjetivo.getBando() == Bando.BLANCO))))) {
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
					&& (piezaObjetivo.getTipo() == Tipo.DAMA || piezaObjetivo.getTipo() == Tipo.ALFIL || (numI == posNum - 1
							&& letI == posLet + 1 && (piezaObjetivo.getTipo() == Tipo.REY || (piezaObjetivo.getTipo() == Tipo.PEON && piezaObjetivo.getBando() == Bando.BLANCO))))) {
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
					&& (piezaObjetivo.getTipo() == Tipo.DAMA || piezaObjetivo.getTipo() == Tipo.ALFIL || (numI == posNum + 1
							&& letI == posLet - 1 && (piezaObjetivo.getTipo() == Tipo.REY || (piezaObjetivo.getTipo() == Tipo.PEON && piezaObjetivo.getBando() == Bando.NEGRO))))) {
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
					&& (piezaObjetivo.getTipo() == Tipo.DAMA || piezaObjetivo.getTipo() == Tipo.ALFIL || (numI == posNum + 1
							&& letI == posLet + 1 && (piezaObjetivo.getTipo() == Tipo.REY || (piezaObjetivo.getTipo() == Tipo.PEON && piezaObjetivo.getBando() == Bando.NEGRO))))) {
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
			while ((i < piezaQueMueve.getCasillasValidas().size ())
					&& (destinoLetra != piezaQueMueve.getCasillasValidas().get (i).getLetra())) {
				i++;
				// Comprobamos si el numero de la letra encontrada coincide.
			}
			if (i < piezaQueMueve.getCasillasValidas().size ()) {
				// Si se entra en el siguiente caso, es que el movimiento es
				// valido
				if (destinoNum == piezaQueMueve.getCasillasValidas().get (i).getLetra()) {
					mov = new Movimiento ();
					mov.setCasillaOrigen(new Casilla (origenLetra, origenNum));
					mov.setCasillaDestino(new Casilla (destinoLetra, destinoNum));
					mov.setNumeroMovimiento (posicion.getNumeroMovimiento());
					mov.setBando (posicion.getTurno());
					mov.setTipoPieza (piezaQueMueve.getTipo());

					// Si se come ponemos el contador a 0
					if (!posicion.esVacia (destinoLetra, destinoNum)) {
						posicion.setContadorTablas(0);
						hash.borrarTabla ();
						mov.setCasillaComer (new Casilla (destinoLetra, destinoNum));
						mov.setTipoPiezaComida (posicion.getPieza (destinoLetra,
														destinoNum).getTipo());
						posicion.borrarPieza(mov.getCasillaComer());
					}

					// Se hacen los calculos especiales si se trata de un peon
					if (piezaQueMueve.getTipo() == Tipo.PEON) {
						// Se borra la pieza correspondiente si se come al paso
						if (Math.abs (destinoLetra - origenLetra) == 1
								&& posicion.esVacia (destinoLetra, destinoNum)) {
							mov.setTipoPiezaComida (posicion.getPieza (destinoLetra,
															origenNum).getTipo());
							mov.setCasillaComer (new Casilla (destinoLetra, origenNum));
							posicion.borrarPieza(mov.getCasillaComer());
						}
						// Se establece la variable alPaso a su valor
						// correspondiente
						if (Math.abs (destinoNum - origenNum) == 2) {
							posicion.setAlPaso(origenLetra);
							mov.setAlPaso (posicion.getAlPaso());
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
									piezaQueMueve.getBando(), coronar);

							mov.setCoronacion (coronar);
						}
						posicion.setContadorTablas(0);
						hash.borrarTabla ();
					}
					// Se hacen los calculos especiales si se trata de un rey
					if (piezaQueMueve.getTipo() == Tipo.REY) {
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

					mov.setContadorTablas (posicion.getContadorTablas());
					boolean[][] enroque = new boolean[2][2];
					enroque[0][0] = posicion.getEnroqueCorto(Bando.BLANCO);
					enroque[0][1] = posicion.getEnroqueLargo(Bando.BLANCO);
					enroque[1][0] = posicion.getEnroqueCorto(Bando.NEGRO);
					enroque[1][1] = posicion.getEnroqueLargo(Bando.NEGRO);
					mov.setEnroque(enroque);
					if (posicion.getTurno() == Bando.NEGRO)
						posicion.addNumeroMovimiento();
					posicion.setTurno ();

					posicion.borrarPieza (origenLetra, origenNum);
					posicion.setPieza (piezaQueMueve, destinoLetra, destinoNum);
					posicion.addContadorTablas();
					calcularMovimientos ();

					if (esCasillaAtacada (posicion.getKingPosition(posicion.getTurno()))) {
						mov.setJaque (true);
					}
					else {
						mov.setJaque (false);
					}
					mov.setFinPartida (esFinPartida ());

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
	private Resultado esFinPartida () {
		Pieza pieza;
		Resultado devolver = null;
		boolean fin = false, fin2 = false, posibleMatInsuf = false;
		char i, j;
		// Tablas por 50 movimientos
		if (posicion.getContadorTablas() == 50) {
			fin = true;
			devolver = Resultado.TABLAS_50_MOV;
		}
		// Tablas por repeticion de posiciones
		if (hash.getRepeticiones (posicion.getClavePosicion()) == 3) {
			fin = true;
			devolver = Resultado.TABLAS_REPETICION;
		}
		// Tablas por material insuficiente
		i = 'a';
		j = '1';
		while (i <= 'h' && !fin2) {
			while (j <= '8' && !fin2) {
				pieza = posicion.getPieza(i, j);
				if (pieza != null) {
					if (posibleMatInsuf) {
						if (pieza.getTipo() != Tipo.REY) {
							fin2 = true;
						}
					}
					else {
						switch (pieza.getTipo()) {
							case PEON:
							case DAMA:
							case TORRE:
								fin2 = true;
								break;
							case ALFIL:
							case CABALLO:
								posibleMatInsuf = true;
								break;
							case REY:
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
			devolver = Resultado.TABLAS_INSUF_MATERIAL;
		}
		// Miramos si hay movimientos posibles
		i = 'a';
		j = '1';
		while (i <= 'h' && !fin) {
			while (j <= '8' && !fin) {
				pieza = posicion.getPieza(i, j);
				if ((pieza != null) && (!Pieza.esBandoContrario (posicion.getTurno(), pieza))
						&& (!pieza.getCasillasValidas().isEmpty ())) {
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
					devolver = Resultado.JAQUE_MATE_NEGRO;
				}
				else {
					devolver = Resultado.TABLAS_AHOGADO;
				}
			else
				// Blancas dan jaque mate
				if (esCasillaAtacada (posicion.getKingPosition(Bando.NEGRO))) {
					devolver = Resultado.JAQUE_MATE_BLANCO;
				}
				// Tablas por ahogado
				else {
					devolver = Resultado.TABLAS_AHOGADO;
				}
		}
		return devolver;
	}
}
