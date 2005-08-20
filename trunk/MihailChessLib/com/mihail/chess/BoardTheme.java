package com.mihail.chess;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import javax.swing.ImageIcon;

import com.mihail.chess.Board.Side;
import com.mihail.chess.Piece.Type;

/**
 * Representa el tema de imagenes de piezas, texturas de las casillas, textura
 * del borde del tablero y textura de fondo del tablero. Para crear un tema
 * basta pasarle la ruta a un archivo .mcz. Un archivo en este
 * formato es un archivo comprimido Zip cuyos contenidos estan especificados
 * de la siguiente manera:
 * 
 * Todas las imagenes podran estar en formatos que soporte Java por defecto. En la 
 * version actual valen: png, jpeg y gif
 * 
 * Las imagenes de las piezas deberan estar formadas por el nombre de la pieza
 * mas la inicial del bando al que pertenecen. Por ejemplo, el peon negro seria
 * 'peonN.png', y el rey blanco 'reyB.png'. Son imagenes obligatorias.
 * 
 * Las casillas seran 'casillaB.png' y 'casillaN.png'. Estas imagenes son optativas,
 * y el tablero puede dibujarse sin ellas con colores lisos.
 * 
 * El borde del tablero 'marco.png'. Esta imagen es optativa, del mismo modo que la 
 * anterior.
 * 
 * El fondo del tablero 'fondo.png'. Esta imagen es optativa, del mismo moo que la 
 * anterior.
 * 
 * Ademas se debera añadir un archivo de texto, de nombre Metadata, en el
 * que se podra indicar cierta informacion como el autor, la fecha de creacion y el
 * nombre del tema.
 * 
 * @author wotan
 *
 */

public class BoardTheme {
	/*
	 * Imagenes del tema
	 */
	private Image[][] pieceImages = new Image[2][6];
	private Image[] casillasImages = new Image[2];
	private Image marco;
	private Image background;
	
	/*
	 * Informacion del tema
	 */
	private String autor;
	private String nombre;
	
	/**
	 * Crea un nuevo tema a partir de una ruta a el archivo ,mcz
	 * 
	 * @param path Ruta al archivo.
	 */
	
	public BoardTheme(String path) {
		File file = new File(path);
		try {
			loadZip(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Crea un tema a partir de una URL donde se encuentra el archivo .mcz
	 * Es util usar este metodo en caso de que el tema se encuentre dentro
	 * de un archivo jar. Para cargarlo cuando se da esta situacion, no habria
	 * que hacer mas que esta llamada:
	 * 
	 * new BoardTheme(getClass().getResource("/ruta/dentro/del/jar"))
	 * 
	 * @param path
	 */
	
	public BoardTheme(URL path) {
		try {
			loadZip(path.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Image getPieceImage(Side bando, Type tipo) {
		return pieceImages[bandoToInt(bando)][tipoToInt(tipo)];
	}
	
	public Image getSquareImage(Side bando) {
		return casillasImages[bandoToInt(bando)];
	}
	
	public Image getBorderImage() {
		return marco;
	}
	
	public Image getBackgroundImage() {
		return background;
	}
	
	private int bandoToInt(Side bando) {
		return bando == Side.WHITE? 0:1;
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
	
	private void readData(ZipInputStream zis, byte[] data, int size) {
		try {
			int readed = 0;
			while(readed<size) {
				readed += zis.read(data, readed, size-readed);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadZip(InputStream file) {
		try {
			ZipInputStream zipFile = new ZipInputStream(file);
			ZipEntry entry;
			while((entry=zipFile.getNextEntry())!=null) {
				String entryName = entry.getName();
				int size = new Long(entry.getSize()).intValue();
				byte [] data = new byte[size];
				System.out.println(entryName + " Size: " + size);
				readData(zipFile, data, size);
				if(entryName.equals("reyB.png")) { // Cargar todas las imagenes en funcion de los nombres
					pieceImages[0][5] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("reyN")) {
					pieceImages[1][5] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("damaB")) {
					pieceImages[0][4] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("damaN")) {
					pieceImages[1][4] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("torreB")) {
					pieceImages[0][3] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("torreN")) {
					pieceImages[1][3]= new ImageIcon(data).getImage();
				} else if (entryName.startsWith("caballoB")) {
					pieceImages[0][1] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("caballoN")) {
					pieceImages[1][1] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("alfilB")) {
					pieceImages[0][2] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("alfilN")) {
					pieceImages[1][2] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("peonB")) {
					pieceImages[0][0] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("peonN")) {
					pieceImages[1][0] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("casillaB")) {
					casillasImages[0] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("casillaN")) {
					casillasImages[1] = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("marco")) {
					marco = new ImageIcon(data).getImage();
				} else if (entryName.startsWith("fondo")) {
					background = new ImageIcon(data).getImage();
				}
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
