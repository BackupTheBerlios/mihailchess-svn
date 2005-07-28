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

import com.mihail.chess.Logica.Bando;
import com.mihail.chess.Pieza.Tipo;

/**
 * Esta clase se encarga de proporcionar una vista básica de la lógica, usada
 * como modelo. No proporciona ningun metodo de manejo de eventos, y enmascara
 * las funciones de la lógica interna.
 * 
 * @author Pedro Suárez Casal
 * @author Iago Porto Díaz
 */
public class Tablero2D extends JPanel {
	/**
	 * Indica los diferentes tipos de casillas que el tablero puede dibujar.<br>
	 * CASILLA_LISO indica una textura formada por un unico color.<br>
	 * CASILLA_GRADIENTE indica una textura formada por dos colores que hacen un
	 * gradiente.<br>
	 * CASILLA_TEXTURA indica una textura que es una imagen.<br>
	 * 
	 */
	public static enum Textura {
		CASILLA_LISO, CASILLA_GRADIENTE, CASILLA_TEXTURA
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Tamaño del borde del tablero.
	 */
	private int BORDE = 30;

	/**
	 * Tamaño de la casilla.
	 */
	private int TAM;

	/**
	 * Tamaño anterior, para comparar si es necesario redimensionar.
	 * 
	 * @TODO ¿Es necesario este atributo? Queda chapucero.
	 */
	private int TAMant;

	/**
	 * Coordenada 'x' de la casilla pulsada por ultima vez [0, 8]. Sirve para
	 * indicar al metodo de dibujo que la casilla indicada no se debe dibujar si
	 * se esta arrastrando la pieza.
	 */
	protected int posX;

	/**
	 * Coordenada 'y' de la casilla pulsada por ultima vez [0, 8].
	 */
	protected int posY;

	// Espacios que hay que dejar alrededor del tablero cuando se redimensiona
	private int bordeSUP = 0;

	private int bordeLAT = 0;

	// Logica interna del tablero

	protected Posicion tablero;

	// Versiones ajustadas al tamaño correcto de las imagenes
	private Image[][] piezas = new Image[2][6];

	private Image[] casillas = new Image[2];

	// Imagenes de las piezas tal y como aparecen en el archivo
	private Image[][] imagenes = new Image[2][6];

	// Indica el sentido en el que se esta dibujando el tablero
	private boolean sentido = true;

	// Matriz en la que se van marcando las casillas seleccionada
	// 'S' -> Casilla seleccionada (amarillo transparente)
	// 'C' -> Indica que se comio (cuadro rojo)
	private char seleccion[][] = new char[8][8];

	// Indica si se esta arrastrando una pieza
	private boolean arrastrando = false;

	/**
	 * Posicion de la pieza que se esta arrastrando. Se usa en el metodo de
	 * dibujo para dibujar la pieza piezaArrastrada en el lugar correcto. TODO
	 * Remodelar para añadir objetos arbitrarios al tablero, entre ellos piezas
	 * que estan siendo arrastradas.
	 */
	private int posPiezaX, posPiezaY;

	/**
	 * Imagen de la pieza qu esta siendo arrastrada.
	 */
	private Image piezaArrastrada;

	/**
	 * Opciones configurables de color del tablero:
	 */
	private Textura textura = Textura.CASILLA_LISO;

	private Color[] colorLiso = new Color[2];

	private Color[] colorGradiente = new Color[2];

	private Image[] colorTextura = new Image[2];

	public Tablero2D() {
		this(60);
	}

	public Tablero2D(int tam) {
		super();
		tablero = new Posicion();

		TAM = tam;
		imagenes[0][0] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/peonB.png"))).getImage();
		imagenes[0][1] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/caballoB.png"))).getImage();
		imagenes[0][2] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/alfilB.png"))).getImage();
		imagenes[0][3] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/torreB.png"))).getImage();
		imagenes[0][4] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/damaB.png"))).getImage();
		imagenes[0][5] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/reyB.png"))).getImage();
		imagenes[1][0] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/peonN.png"))).getImage();
		imagenes[1][1] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/caballoN.png"))).getImage();
		imagenes[1][2] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/alfilN.png"))).getImage();
		imagenes[1][3] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/torreN.png"))).getImage();
		imagenes[1][4] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/damaN.png"))).getImage();
		imagenes[1][5] = (new ImageIcon(getClass().getResource(
				"/piezas/modernas/reyN.png"))).getImage();

		// this.setAutoscrolls(true);

		redimensionar();
	}

	private int bandoToInt(Bando b) {
		if (b == Bando.BLANCO) {
			return 0;
		}
		return 1;
	}

	/**
	 * Establece la imagen a usar como textura en la casilla del color indicado.
	 * 
	 * @param bando
	 *            Color de las casillas que queremos cambiar
	 * @param c
	 *            Imagen que queremos establecer
	 */
	public void setImagenTextura(int bando, Image c) {
		casillas[bando] = c;
	}

	/**
	 * 
	 */
	public void setTextura(Textura b) {
		textura = b;
	}

	/*
	 * Funcion de utilidad que se encarga de redimensionar las imagenes al
	 * tamaño adecuado del tablero
	 */

	private final void redimensionar() {
		MediaTracker media = new MediaTracker(this);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++) {
				piezas[i][j] = imagenes[i][j].getScaledInstance(TAM, TAM,
						Image.SCALE_SMOOTH);
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

	/*
	 * Funcion que se encarga de dibujar el borde del tablero
	 */

	private void dibujarBorde(Graphics g) {
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
				g
						.drawString(new Character((char) (i + 'A')).toString(),
								xF, yF);
			} else {
				g.drawString(new Character((char) (7 - i + 'A')).toString(),
						xF, yF);
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

	/*
	 * Funcion que proporciona la imagen correspondiente a una pieza definida
	 * por color y pieza.
	 */

	protected Image getImage(Bando color, Tipo pieza) {
		// if (p != null) {
		switch (pieza) {
		case PEON:
			return piezas[bandoToInt(color)][0];

		case CABALLO:
			return piezas[bandoToInt(color)][1];

		case ALFIL:
			return piezas[bandoToInt(color)][2];

		case TORRE:
			return piezas[bandoToInt(color)][3];

		case DAMA:
			return piezas[bandoToInt(color)][4];

		case REY:
			return piezas[bandoToInt(color)][5];

		default:
			return null;
		}

	}

	public void paintComponent(Graphics g) {
		Pieza temp;
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

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

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

				if (textura == Textura.CASILLA_TEXTURA)

					g.drawImage(colorTextura[v], i * TAM, j * TAM, TAM, TAM,
							null);

				else if (textura == Textura.CASILLA_LISO) {
					g.setColor(colorLiso[v]);
					g.fillRect(i * TAM, j * TAM, TAM, TAM);
				} else if (textura == Textura.CASILLA_GRADIENTE) {

					g2d.setPaint(new GradientPaint(new Point(i * TAM, j * TAM),
							colorLiso[v], new Point(i * TAM + TAM, j * TAM
									+ TAM), colorGradiente[v]));
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

					g.drawImage(getImage(temp.getBando(), temp.getTipo()), i
							* TAM, j * TAM, null);

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
	 * @param borde
	 *            Establece el tamaño del borde.
	 */

	public void setBorde(int borde) {
		BORDE = borde;
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

	public Color getColorGradiente(Bando b) {
		return colorGradiente[0];
	}

	public void setColorGradiente(Bando b, Color colorGradiente) {
		this.colorGradiente[0] = colorGradiente;
	}

	public Color getColorLiso(Bando b) {
		return colorLiso[0];
	}

	public void setColorLiso(Bando b, Color colorLiso) {
		this.colorLiso[0] = colorLiso;
	}

	public Image getColorTextura(Bando b) {
		return colorTextura[0];
	}

	public void setColorTextura(Bando b, Image colorTextura) {
		this.colorTextura[0] = colorTextura;
	}
}