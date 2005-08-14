package com.mihail.chess;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mihail.chess.Board.Side;
import com.mihail.chess.Piece.Type;

/**
 * Esta clase se encarga de proporcionar una vista básica de la lógica, usada
 * como modelo. No proporciona ningun metodo de manejo de eventos, y enmascara
 * las funciones de la lógica interna.
 * 
 * @author Pedro Suárez Casal
 * @author Iago Porto Díaz
 */
public class Board2D extends JPanel {

	private static final long serialVersionUID = 1L;

	/*
	 * Tamaño del borde del tablero.
	 */
	private int BORDE = 30;

	/*
	 * Tamaño de la casilla.
	 */
	private int TAM;

	/*
	 * Tamaño anterior, para comparar si es necesario redimensionar.
	 * 
	 * @TODO ¿Es necesario este atributo? Queda chapucero.
	 */
	private int TAMant;

	/*
	 * Coordenada 'x' de la casilla pulsada por ultima vez [0, 8]. Sirve para
	 * indicar al metodo de dibujo que la casilla indicada no se debe dibujar si
	 * se esta arrastrando la pieza.
	 */
	protected int posX;

	/*
	 * Coordenada 'y' de la casilla pulsada por ultima vez [0, 8].
	 */
	protected int posY;

	// Espacios que hay que dejar alrededor del tablero cuando se redimensiona
	private int bordeSUP = 0;

	private int bordeLAT = 0;

	// Logica interna del tablero

	protected Position tablero;

	// Versiones ajustadas al tamaño correcto de las imagenes
	private Image[][] piezas = new Image[2][6];

	// Indica el sentido en el que se esta dibujando el tablero
	private boolean sentido = true;

	// Matriz en la que se van marcando las casillas seleccionada
	// 'S' -> Casilla seleccionada (amarillo transparente)
	// 'C' -> Indica que se comio (cuadro rojo)
	private char seleccion[][] = new char[8][8];

	// Indica si se esta arrastrando una pieza
	private boolean arrastrando = false;

	/*
	 * Posicion de la pieza que se esta arrastrando. Se usa en el metodo de
	 * dibujo para dibujar la pieza piezaArrastrada en el lugar correcto. TODO
	 * Remodelar para añadir objetos arbitrarios al tablero, entre ellos piezas
	 * que estan siendo arrastradas.
	 */
	private int posPiezaX, posPiezaY;

	/*
	 * Imagen de la pieza qu esta siendo arrastrada.
	 */
	private Image piezaArrastrada;

	/*
	 * Colores que se dibujan las casillas en caso de que el tema no lo
	 * establezca.
	 */
	private Color[] colorLiso = new Color[2];

	/*
	 * Tema que esta usando el tablero para dibujarse.
	 */

	private BoardTheme theme;

	public Board2D(BoardTheme theme) {
		this(theme, 60);
	}

	public Board2D(BoardTheme theme, int tam) {
		super();
		tablero = new Position();

		TAM = tam;

		// this.setAutoscrolls(true);
		this.theme = theme;

		redimensionar();
	}

	/**
	 * Se encarga de cambiar el sentido del tablero.
	 */

	public void rotarTablero() {
		sentido = !sentido;
		repaint();
	}

	public Dimension getPreferredSize() {
		return (new Dimension(8 * TAM + BORDE * 2, 8 * TAM + BORDE * 2));
	}

	public void paintComponent(Graphics g) {
		Piece temp;
		// Actualizamos el tamaño de la casilla en caso de que redimensionen el
		// tablero
		int TAMtotal = Math.min(getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		TAMant = TAM;
		TAM = (TAMtotal - BORDE * 2) / 8;
		if (TAMant != TAM) {
			redimensionar();
		}

		// Hints de renderizado
		// g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if(theme.getBackgroundImage()==null) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			g.drawImage(theme.getBackgroundImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		}

		if (getWidth() < getHeight()) {
			bordeSUP = (getHeight() - getWidth()) / 2;
			bordeLAT = 0;
		} else {
			bordeLAT = (getWidth() - getHeight()) / 2;
			bordeSUP = 0;
		}
		g.setClip(bordeLAT, bordeSUP, TAMtotal, TAMtotal);
		g.translate(bordeLAT, bordeSUP);

		// Borde tablero
		dibujarBorde(g);
		// Bucle para dibujar el tablero

		g.setClip(BORDE, BORDE, getWidth() - BORDE, getHeight() - BORDE);
		g.translate(BORDE, BORDE);
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				// En funcion de nuestra posicion escogemos el color para
				// casillas blancas o negras
				// if (activado) {
				int v = (i + j) % 2;
				Image textura = theme.getSquareImage(v == 0 ? Side.WHITE
						: Side.BLACK);
				if (textura != null)

					g.drawImage(textura, i * TAM, j * TAM, TAM, TAM, null);

				else {
					g.setColor(colorLiso[v]);
					g.fillRect(i * TAM, j * TAM, TAM, TAM);
				}

				// Dibujamos la casilla
				//
				// En caso de que la casilla que tratamos este seleccionada, la
				// marcamos de color verde
				int ii = i, jj = j;
				if (!sentido) {
					ii = 7 - i;
					jj = 7 - j;
				}

				// En funcion del caracter de seleccion dibujamos una cosa u
				// otra
				switch (seleccion[ii][jj]) {
				case 'S':
					g.setColor(new Color(255, 255, 0, 50));
					g.fill3DRect(i * TAM, j * TAM, TAM, TAM, true);
					break;
				case 'M':
					g2d.setStroke(new BasicStroke(2.0F, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_ROUND));
					g.setColor(new Color(220, 0, 0));
					g2d.draw(new Rectangle2D.Float(i * TAM, j * TAM, TAM - 1,
							TAM - 1));

					// g.drawRect(i * TAM, j * TAM, TAM-1, TAM-1);
					break;
				default:
				}

				// Comprobamos si existe pieza en esa casilla, y en ese caso la
				// dibujamos
				char letra, numero;
				// Se comprueba el sentido en el que dibujamos
				if (sentido) {
					numero = (char) ((7 - j) + '1');
					letra = (char) (i + 'a');
				} else {
					numero = (char) (j + '1');
					letra = (char) ((7 - i) + 'a');
				}

				if ((temp = tablero.getPieza(letra, numero)) != null) {
					if (arrastrando
							&& ((sentido && i == posX && j == posY) || (!sentido
									&& i == 7 - posX && j == 7 - posY))) {
						continue;
					}

					//g.drawImage(piezas[bandoToInt(temp.getBando())][tipoToInt(temp.getTipo())], i * TAM, j * TAM, null);
					g.drawImage(piezas[bandoToInt(temp.getSide())][tipoToInt(temp.getType())], i * TAM, j * TAM, TAM, TAM, null);
				}
			}

		}

		// Este es el caso de que haya que dibujar la pieza a arrastrar. Se
		// dibuja por encima de todo el resto del tablero
		if (piezaArrastrada != null) {
			g.translate(-bordeLAT, -bordeSUP);
			g.translate(-BORDE, -BORDE);
			g.setClip(posPiezaX - TAM / 2, posPiezaY - TAM / 2, TAM, TAM);
			if (arrastrando) {
				g.drawImage(piezaArrastrada, posPiezaX - TAM / 2, posPiezaY
						- TAM / 2, null);
			}
		}

	}

	/**
	 * @return Devuelve el tamaño del borde del tablero.
	 */

	public int getBorde() {
		return BORDE;
	}

	/**
	 * @return Devuelve el tamaño de las casilla.
	 */

	public int getTamanhoCasilla() {
		return TAM;
	}

	/**
	 * @param tam
	 *            Establece el tamaño de las casilla.
	 */

	public void setTamanhoCasilla(int tam) {
		TAM = tam;
	}

	/**
	 * Establece que una pieza se esta arrastrando para que la dibuje en la
	 * posicion correspondiente.
	 * 
	 * @param arrastrando
	 *            El valor al que queremos establecer si se esta arrastrando o
	 *            no.
	 */
	public void setArrastrando(boolean arrastrando) {
		this.arrastrando = arrastrando;
	}

	/**
	 * @param piezaArrastrada
	 *            Establece al imagen a arrastrar.
	 */
	public void setPiezaArrastrada(Image piezaArrastrada) {
		this.piezaArrastrada = piezaArrastrada;
	}

	/**
	 * @return Devuelve si la casilla indicada por (i, j) tiene algun tipo de
	 *         seleccion.
	 */

	public char getSeleccion(int i, int j) {
		return seleccion[i][j];
	}

	/**
	 * Establece la seleccion de una casilla.
	 * 
	 * @param s
	 *            Seleccion a la que establecemos la casilla.
	 */
	public void setSeleccion(char s, int i, int j) {
		this.seleccion[i][j] = s;
	}

	public Point getPosPiezaArrastrada() {
		return new Point(posPiezaX, posPiezaY);
	}

	/**
	 * @param posPiezaX
	 *            The posPiezaX to set.
	 */
	public void setPosPiezaArrastrada(int posPiezaX, int posPiezaY) {
		this.posPiezaX = posPiezaX;
		this.posPiezaY = posPiezaY;
	}

	/**
	 * @return Devuelve el borde lateral, el espacio que puede sobrar a los
	 *         lados del tablero.
	 */
	public int getBordeLateral() {
		return bordeLAT;
	}

	/**
	 * @return Devuelve el borde superior, el espacio que puede sobrar arriba y
	 *         abajo del tablero.
	 */
	public int getBordeSuperior() {
		return bordeSUP;
	}

	/**
	 * Indica el sentido en el que se esta dibujando el tablero
	 * 
	 * @return Devuelve true si las blancas estan abajo y false si las negras
	 *         estan abajo.
	 */

	public boolean isSentido() {
		return sentido;
	}

	/**
	 * Establece el sentido del tablero.
	 * 
	 * @param sentido
	 *            The sentido to set.
	 */
	public void setSentido(boolean sentido) {
		this.sentido = sentido;
	}

	/**
	 * Obtiene el color liso de las casillas de un color.
	 * 
	 * @param b
	 *            El tipo de las casillas, blancas o negras.
	 * @return El color liso.
	 */
	public Color getColorLiso(Side b) {
		return colorLiso[bandoToInt(b)];
	}

	/**
	 * Establece el color liso de las casillas de un color. Este color solo se
	 * mostrara en caso de que el tema no establezca una textura para las
	 * casillas.
	 * 
	 * @param b
	 *            El tipo de las casillas, blancas o negras.
	 * @param colorLiso
	 *            El color liso.
	 */

	public void setColorLiso(Side b, Color colorLiso) {
		this.colorLiso[bandoToInt(b)] = colorLiso;
	}

	/**
	 * Establece el tema que usa el tablero para dibujarse.
	 * 
	 * @param theme
	 *            Tema a establecer.
	 */

	public void setTema(BoardTheme theme) {
		this.theme = theme;
	}
	
	public void setPieza(Piece pieza, Square casilla) {
		tablero.setPiece(pieza, casilla);
		repintarCasilla(casilla.getFile() - 'a', casilla.getRank()-'1');
	}
	
	public void borrarPieza(Square casilla) {
		tablero.removePiece(casilla);
		repintarCasilla(casilla.getFile() - 'a', casilla.getRank()-'1');
	}
	
	public void setFEN(String pos) {
		tablero.setFEN(pos);
		repaint();
	}

	/*
	 * Funcion para repintar la casilla indicada por el punto x, y, que debe ser
	 * el centro de la casilla. TODO Modificar para que funcione con la esquina
	 * superior izquierda?
	 */

	protected void repintarCasilla(int x, int y) {
		repaint(x - TAM / 2, y - TAM / 2, TAM, TAM);
	}

	/**
	 * Metodo de utilidad para obtener de forma sencilla la casilla
	 * correspondiente a la que pertenece un pixel en la posicion (x, y).
	 * 
	 * @param x
	 *            Coordenada x
	 * @param y
	 *            Coordenada y
	 * @return Devuelve un punto con las coordenadas de la casilla
	 *         correspondiente al punto x, y. El valor devuelto estará en el
	 *         intervalo [0, 7].
	 */
	protected Point getCasilla(int x, int y) {
		int posX = (x - BORDE - bordeLAT) / TAM, posY = (y - BORDE - bordeSUP)
				/ TAM;
		if (!isSentido()) {
			posX = 7 - posX;
			posY = 7 - posY;
		}
		return new Point(posX, posY);
	}

	private void dibujarBorde(Graphics g) {
		Image borde = theme.getBorderImage();
		if (borde != null) {
			g.drawImage(borde, 0, 0, null);
		} else { // Dibujamos a mano
			g.setColor(Color.BLACK);

			g.fillRect(0, 0, Math.min(getWidth(), getHeight()), Math.min(
					getWidth(), getHeight()));
			// char num = '8';
			// char let = 'a';

			g.setColor(new Color(209, 193, 134));
			g.setFont(new Font("Arial", Font.BOLD, 12));

			int yF = 8 * TAM + BORDE * 3 / 2 + 6;
			for (int i = 0; i < 8; i++) {
				int xF = i * TAM + BORDE + TAM / 2;
				if (sentido) {
					g.drawString(new Character((char) (i + 'A')).toString(),
							xF, yF);
				} else {
					g.drawString(
							new Character((char) (7 - i + 'A')).toString(), xF,
							yF);
				}
			}
			int xF = BORDE / 2 - 3;
			for (int i = 0; i < 8; i++) {
				yF = i * TAM + BORDE + TAM / 2;
				if (sentido) {

					g.drawString(new Integer(7 - i + 1).toString(), xF, yF);

				} else {
					g.drawString(new Integer(i + 1).toString(), xF, yF);
				}
			}
		}
	}

	private int bandoToInt(Side b) {
		if (b == Side.WHITE) {
			return 0;
		}
		return 1;
	}
	
	private int tipoToInt(Type tipo) {
		switch(tipo) {
		case PAWN:
			return 0;
		case KNIGHT:
			return 1;
		case BISHOP:
			return 2;
		case ROOK:
			return 3;
		case QUEEN:
			return 4;
		case KING:
			return 5;
		}
		return -1;
	}
	
	private Type intToTipo(int i) {
		switch(i) {
		case 0:
			return Type.PAWN;
		case 1:
			return Type.KNIGHT;
		case 2:
			return Type.BISHOP;
		case 3:
			return Type.ROOK;
		case 4:
			return Type.QUEEN;
		case 5:
			return Type.KING;
		}
		return null;
	}

	/*
	 * Funcion de utilidad que se encarga de redimensionar las imagenes al
	 * tamaño adecuado del tablero
	 */

	private final void redimensionar() {
		MediaTracker media = new MediaTracker(this);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++) {
				piezas[i][j] = theme.getPieceImage(i==0?Side.WHITE:Side.BLACK, intToTipo(j))
						.getScaledInstance(TAM, TAM, Image.SCALE_FAST);
				media.addImage(piezas[i][j], 1);
			}
		}
		try {
			media.waitForID(1);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this,
					"Redimensionado de Imagenes Interrumpido:\n"
							+ e.getMessage());
		}
	}
}